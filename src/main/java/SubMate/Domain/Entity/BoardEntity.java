package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
	private String bwriter;
	@Column
	private String bechotimer;
	@Column
	private String bimg;
	@Column
	private String bview;
	@Column
	private String becho;

	@ManyToOne @JoinColumn(name = "mno")
	private MemberEntity memberEntity;

	@OneToMany(mappedBy = "boardReplyEntity", cascade = CascadeType.ALL)
	private List<ReplyEntity> replyEntityList = new ArrayList<>();
}
