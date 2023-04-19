package SubMate.Domain.Repository;

import SubMate.Domain.Entity.SubWayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubWayRepository extends JpaRepository<SubWayEntity, Integer> {
	public List<SubWayEntity> findBySlineOrderByScodeAsc(String sline);
}
