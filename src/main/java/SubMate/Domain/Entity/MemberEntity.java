package SubMate.Domain.Entity;

import SubMate.Config.Auth.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity @Table(name = "member")
@Getter @Setter @ToString @Builder
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
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
}
