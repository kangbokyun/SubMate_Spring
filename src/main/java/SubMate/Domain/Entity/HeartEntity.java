package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "heart")
@Data @ToString @Builder
public class HeartEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int hno;
	@Column // 하트 눌렀는지 아닌지
	private String hkind;
	// htype
	// 1 => 게시글 하트, 2=> 댓글 하트, 3 => 대댓글 하트, 4 => 유저 하트
	@Column
	private String htype;
	@Column
	private String mno; // 하트를 누른 사람
	@Column
	private String bno;
	@Column
	private String rno;
	@Column
	private String userno; // 하트를 받은 사람
}
