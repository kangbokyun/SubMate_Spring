package SubMate.Domain.Repository;

import SubMate.Domain.Entity.TendinousEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TendinousRepository extends JpaRepository<TendinousEntity, Integer> {
}
