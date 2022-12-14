package SubMate.Domain.Repository;

import SubMate.Domain.Entity.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<RankEntity, Integer> {
}
