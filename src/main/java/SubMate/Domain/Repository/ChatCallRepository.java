package SubMate.Domain.Repository;

import SubMate.Domain.Entity.ChatCallEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatCallRepository extends JpaRepository<ChatCallEntity, Integer> {
}
