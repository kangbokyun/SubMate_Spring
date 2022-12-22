package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "tendinous")
public class TendinousEntity extends BaseTimeEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int tno;
        private String tcontents;
        private String tselectcontentkind;
        private String tselecttendinouskind;
        // 1 : 열람 | 2 : 보류 | 3 : 처리
        private int tstatus;

        @ManyToOne @JoinColumn(name = "mno")
        MemberEntity memberEntity;
}
