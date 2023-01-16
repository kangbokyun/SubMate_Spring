package SubMate.Domain.Repository;

import SubMate.Domain.Entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Integer> {
}
