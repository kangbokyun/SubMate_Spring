package SubMate.Domain.Repository;

import SubMate.Domain.Entity.MateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateRepository extends JpaRepository<MateEntity, Integer> {
}
