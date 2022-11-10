package SubMate.Domain.DTO;

import SubMate.Config.Auth.Role;
import lombok.*;

import java.time.LocalDateTime;

@Data @ToString @Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO  {
	private int mno;
	private String mid;
	private String mpw;
	private String mname;
	private String mnickname;
	private String mbirth;
	private String mgender;
	private String mphone;
	private String maddress;
	private Role mrole;
	private LocalDateTime createddate;
	private String mplatform;
	private String mager;
	private String mbti;
	private String mhobby;
	private String token;
}
