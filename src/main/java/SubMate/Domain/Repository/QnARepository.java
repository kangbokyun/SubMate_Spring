package SubMate.Domain.Repository;

import SubMate.Domain.Entity.QnAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnAEntity, Integer> {
}
