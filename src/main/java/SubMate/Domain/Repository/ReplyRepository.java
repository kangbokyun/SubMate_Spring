package SubMate.Domain.Repository;

import SubMate.Domain.Entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {
}
