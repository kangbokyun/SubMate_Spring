package SubMate.Domain.Repository;

import SubMate.Domain.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
	public MemberEntity findByMid(String username);
}
