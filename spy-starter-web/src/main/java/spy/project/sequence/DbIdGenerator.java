package spy.project.sequence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spy.project.db.dao.SeqRepository;
import spy.project.db.entity.SpySeqCnt;
import spy.project.exceptions.FrameException;
import spy.project.exceptions.codes.ErrorCode;
import spy.project.utils.DateUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class DbIdGenerator implements Sequence {

    private static Map<String, Lock> lockMap = new HashMap<>();
    private static Map<String, DbSequence> sequenceMap = new HashMap<>();

    private Lock sLock = new ReentrantLock();

    @Autowired
    private SeqRepository seqRepository;

    @Override
    public String getNext(String wSeqCod, String wSedVal) {
        String key = wSeqCod + wSedVal;
        if(!lockMap.containsKey(key)) {
            try {
                sLock.lock();
                if(!lockMap.containsKey(key)) {
                    DbSequence seq = new DbSequence();
                    int wCurDay = DateUtils.getCurrent().formatIntValue("yyyyMMdd");
                    sequenceMap.put(key, seq);
                    ((DbIdGenerator)AopContext.currentProxy()).loadSeq(wSeqCod, wSedVal, wCurDay);
                    Lock lock = new ReentrantLock();
                    lockMap.put(key,lock);
                }

            } finally {
                sLock.unlock();
            }
        }

        String xSeqNbr = "";
        try {
            lockMap.get(key).lock();
            xSeqNbr = this.getSeqNumber(wSeqCod,wSedVal);
        } finally {
            lockMap.get(key).unlock();
        }
        return xSeqNbr;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadSeq(String seqCode, String seedVal, int wCurDay) {
        DbSequence seq = sequenceMap.get(seqCode + seedVal);

        // 取数据库定义for update
        SpySeqCnt seqDef = seqRepository.findByCntSeqCodAndCntSedVal(seqCode, seedVal);

        if(seqDef == null) {
            log.error("流水号定义不存在：{} - {}", seqCode, seedVal);
            throw new FrameException(ErrorCode.ERR0001);
        }

        int wSerialLength = seq.calSerialLength(seqDef.getCntSeqFmt(), seedVal, seqDef.getCntTtlLen());

        long wCurSerial = 1;
        if(!seq.getSwitchFlag(seqDef.getCntSeqFmt(), wCurDay, seqDef.getCntSeqDat())) {
            wCurSerial = seqDef.getCntSeqCnt();
        }

        seq.totalLength = seqDef.getCntTtlLen();
        seq.seed = seqDef.getCntSedVal();
        seq.format = seqDef.getCntSeqFmt();
        seq.cacheNumber = seqDef.getCntRtvBuf();
        seq.curDate = wCurDay;
        seq.curSerialNumber = wCurSerial;
        seq.nextSerialNumber = wCurSerial + seqDef.getCntRtvBuf();
        seq.serialLength = wSerialLength;

        //修改定义
        seqDef.setCntSeqCnt(seq.nextSerialNumber);
        seqDef.setCntSeqDat(wCurDay);
        //修改
        seqRepository.save(seqDef);
    }

    public String getSeqNumber(String seqCode, String seedVal) {
        try {
            String key = seqCode + seedVal;
            DbSequence seq = sequenceMap.get(key);
            int wCurDay = DateUtils.getCurrent().formatIntValue("yyyyMMdd");
            boolean flag = seq.getSwitchFlag(seq.format, wCurDay, seq.curDate);

            if((seq.curSerialNumber == seq.nextSerialNumber) || flag) {
                ((DbIdGenerator) AopContext.currentProxy()).loadSeq(seqCode, seedVal, wCurDay);
            }

            StringBuilder sn = new StringBuilder();
            int len = seq.serialLength;
            long seqNumber = seq.curSerialNumber;

            String format = "%0" + len + "d";
            sn.append(seedVal.trim())
                    .append(seq.getDatePart(seq.format, seq.curDate))
                    .append(String.format(format, seqNumber));
            seq.curSerialNumber = seq.curSerialNumber + 1;
            return sn.toString();

        } catch (Exception e) {
            throw new FrameException(ErrorCode.ERR0001);
        }
    }


    public class DbSequence {

        public int totalLength;     //总长度
        public String seed;         //种子
        public String format;       //格式
        public int cacheNumber;     //一次加载个数
        public int curDate;
        public long curSerialNumber;  //当前流水
        public long nextSerialNumber; //下次预加载流水
        public int serialLength;    //流水长度

        // 计算序号长度
        private int calSerialLength(String xSeqFmt, String wSedVal, int wSeqLen) {
            int wSerialLength = 0;
            switch (xSeqFmt) {
                case "SDS":
                    wSerialLength = wSeqLen - wSedVal.trim().length() - 8;
                    break;
                case "SYS":
                    wSerialLength = wSeqLen - wSedVal.trim().length() - 2;
                case "SS":
                    wSerialLength = wSeqLen - wSedVal.trim().length();
                    break;
                default:
                    throw new FrameException(ErrorCode.ERR0002);
            }
            return wSerialLength;
        }


        //判断是否需要日期切换
        private boolean getSwitchFlag(String xSeqFmt, int wCurDay, int wSeqDay) {
            switch (xSeqFmt) {
                case "SDS":
                    if(wCurDay == wSeqDay) {
                        return false;
                    }
                    break;
                case "SYS":
                    String aYear = String.valueOf(wCurDay).substring(0,4);
                    String bYear = String.valueOf(wSeqDay).substring(0,4);
                    if(aYear.equals(bYear)) {
                        return false;
                    }
                    break;
                case "SS":
                    return false;
                default:
                    throw new FrameException(ErrorCode.ERR0002);
            }
            return true;
        }

        //获取日期部分
        private String getDatePart(String xSeqFmt, int wCurDay) {
            String datePart = "";
            switch (xSeqFmt) {
                case "SDS":
                    datePart = String.valueOf(wCurDay).substring(0,8);
                    break;
                case "SYS":
                    datePart = String.valueOf(wCurDay).substring(2,4);
                    break;
                case "SS":
                    break;
                default:
                    throw new FrameException(ErrorCode.ERR0002);
            }
            return datePart;
        }

    }


}
