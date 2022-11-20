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
	@Column // 좋아요가 됐는지 아닌지
	private String hkind;
	// htype
	// 1 => 게시글 좋아요, 2=> 댓글 좋아요, 3 => 대댓글 좋아요
	@Column
	private String htype;
	@Column
	private String mno;
	@Column
	private String bno;
	@Column
	private String rno;
}
