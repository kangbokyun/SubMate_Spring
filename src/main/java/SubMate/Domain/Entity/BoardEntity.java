package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data @Builder @ToString
@Entity @Table(name = "board")
public class BoardEntity extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bno;
	@Column
	private String btitle;
	@Column
	private String bcontents;
	@Column
	private String bimg;
	@Column
	private String bview;
	@Column
	private String becho;

	@ManyToOne @JoinColumn(name = "mno")
	private MemberEntity memberEntity;
}
