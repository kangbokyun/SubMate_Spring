package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "profiletalk")
public class ProfileTalkEntity extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ptno;
	@Column
	private String ptcontents;
	@Column
	private int writedmno;
	@Column
	private String ptwriter;

	@ManyToOne @JoinColumn(name = "mno")
	MemberEntity memberEntity;
}
