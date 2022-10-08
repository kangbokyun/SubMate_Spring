package SubMate.Controller;

import SubMate.Config.Auth.Role;
import SubMate.Config.DTO.ResponseDTO;
import SubMate.Config.Filter.TokenProvider;
import SubMate.Domain.DTO.MemberDTO;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/Auth")
public class MemberController {
	@Autowired
	MemberService memberService;

	@Autowired
	TokenProvider tokenProvider;

	// 회원가입
	@PostMapping("/SignUp")
	public boolean SignUp(@RequestBody MemberDTO member) {
		boolean result = memberService.SignUp(member);
		if (result) {
			return true;
		} else {
			return false;
		}
	}

	// 로그인
	@PostMapping("/Login")
	public ResponseEntity<?> Login(@RequestBody MemberDTO memberDTO) {
		MemberEntity user = memberService.Login(memberDTO.getMid(), memberDTO.getMpw());

		try {
			// user 정보 받아오고 롤이 NOTUSER가 아니면 토큰 생성
			if (user != null && user.getRole() != Role.NOTUSER) {
				String jwtToken = tokenProvider.create(user);
				MemberDTO memberDTO1 = MemberDTO.builder()
					.mno(user.getMno()).mid(user.getMid())
					.mname(user.getMname()).maddress(user.getMaddress())
					.mnickname(user.getMnickname()).mphone(user.getMphone())
					.token(jwtToken).mrole(user.getRole()).createddate(user.getCreateDate())
					.build();
				return ResponseEntity.ok().body(memberDTO1);
			} else {
				ResponseDTO responseDTO = ResponseDTO.builder()
						.error("NOTUSER")
						.build();
				return ResponseEntity.badRequest().body(responseDTO);
			}
		} catch (Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Login Failed")
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}