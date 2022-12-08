package SubMate.Domain.Entity;

import SubMate.Config.Auth.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "member")
@ToString(exclude = {"mateEntity", "profileEntity"})
public class MemberEntity extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mno; // 고유번호
	@Column
	private String mid; // 이메일형식 아이디
	@Column
	private String mpw;
	@Column
	private String mname; // 이름
	@Column
	private String mnickname; // 웹앱 내부에서 사용할 닉네임
	@Column
	private String mphone;
	@Column
	private String maddress;
	@Column
	private String mbirth;
	@Column
	private String mgender;
	@Column
	private String mplatform;
	@Column
	private String mager;
	@Column
	private String mhobby;
	@Column
	private String mbti;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	@Column
	private String profileimg;

	// 회원 한 명당 여러개의 게시글 작성
	@OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
	private List<BoardEntity> boardEntityList = new ArrayList<>();

	// 회원 한 명당 여러개의 댓글, 대댓글 작성
	@OneToMany(mappedBy = "memberReplyEntity", cascade = CascadeType.ALL)
	private List<ReplyEntity> replyEntityList = new ArrayList<>();

	// 한 명당 한개의 Mate Setting
	@OneToOne(mappedBy =  "memberEntity", cascade = CascadeType.ALL)
	private MateEntity mateEntity = new MateEntity();

	// 한 명당 한 개의 Profile Setting
	@OneToOne(mappedBy = "memberEntity", cascade = CascadeType.ALL)
	private ProfileEntity profileEntity = new ProfileEntity();
}
