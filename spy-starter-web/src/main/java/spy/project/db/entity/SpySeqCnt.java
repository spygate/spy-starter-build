package spy.project.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "spy_seq_cnt")
@Getter
@Setter
public class SpySeqCnt {

    @Id
    @Column(name = "cnt_key_seq")
    private String cntKeySeq;//主键

    @Column(name = "cnt_seq_cod")
    private String cntSeqCod;//流水代码

    @Column(name = "cnt_sed_val")
    private String cntSedVal;//种子

    @Column(name = "cnt_seq_fmt")
    private String cntSeqFmt;//格式 SDS：种子+日期+流水

    @Column(name = "cnt_ttl_len")
    private Integer cntTtlLen;//长度

    @Column(name = "cnt_rtv_buf")
    private Integer cntRtvBuf;//缓存个数

    @Column(name = "cnt_seq_dat")
    private Integer cntSeqDat;//计数器日期

    @Column(name = "cnt_seq_cnt")
    private Long cntSeqCnt;//计数器序号

    @Column(name = "cnt_rcd_sts")
    private String cntRcdSts;//状态

    @Version
    @Column(name = "cnt_rcd_ver")
    private Integer cntRcdVer;//版本号

}
