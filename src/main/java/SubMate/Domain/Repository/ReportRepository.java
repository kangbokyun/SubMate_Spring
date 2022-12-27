package SubMate.Domain.Repository;

import SubMate.Domain.Entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
        public List<ReportEntity> findByMemberEntity_Mno(int mno);
}
