package SubMate.Domain.Repository;

import SubMate.Domain.Entity.ChatHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository extends JpaRepository<ChatHistoryEntity, Integer> {
}
