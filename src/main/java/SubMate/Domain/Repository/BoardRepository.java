package SubMate.Domain.Repository;

import SubMate.Domain.Entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    public List<BoardEntity> findTop12ByOrderByBnoDesc();
    public List<BoardEntity> findByBnoBetweenOrderByBnoDesc(int smallbno, int bigbno);
}
