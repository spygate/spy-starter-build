package spy.project.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import spy.project.db.entity.SpySeqCnt;

import javax.persistence.LockModeType;

@Repository
public interface SeqRepository extends JpaRepository<SpySeqCnt, String> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    SpySeqCnt findByCntSeqCodAndCntSedVal(String cntSeqCod, String cntSedVal);

}
