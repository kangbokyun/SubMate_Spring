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
	@Column
        private String tcontents;
	@Column
        private String tselectcontentkind;
	@Column
        private String tselecttendinouskind;
        // 1 : 열람 | 2 : 보류 | 3 : 처리
	@Column
        private int tstatus;
	@Column
	private String tanswer;

        @ManyToOne @JoinColumn(name = "mno")
        MemberEntity memberEntity;
}
