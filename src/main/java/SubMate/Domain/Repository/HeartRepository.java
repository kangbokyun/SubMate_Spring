package SubMate.Domain.Repository;

import SubMate.Domain.Entity.HeartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<HeartEntity, Integer> {
}
