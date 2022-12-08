package SubMate.Domain.Repository;

import SubMate.Domain.Entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
        public ProfileEntity findByMemberEntity_mno(int mno);
}
