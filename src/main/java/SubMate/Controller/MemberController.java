package SubMate.Controller;

import SubMate.Config.Auth.Role;
import SubMate.Config.DTO.ResponseDTO;
import SubMate.Config.Filter.TokenProvider;
import SubMate.Domain.DTO.KakaoDTO;
import SubMate.Domain.DTO.MemberDTO;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.http.HttpResponse;

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
		System.out.println("member : " + member);
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
				ResponseDTO responseDTO = ResponseDTO.builder().error("NOTUSER").build();
				return ResponseEntity.badRequest().body(responseDTO);
			}
		} catch (Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder().error("Login Failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	@PostMapping("/KakaoLogin")
	public ResponseEntity<?> KakaoLoginTest(@RequestBody String kakaoData) {
		String jsonData0 = kakaoData.replace("[{\"", " ");
		String jsonData1 = jsonData0.replace("\"}]", " ");
		String jsonData2 = jsonData1.replace("\":\"", " ");
		String jsonData3 = jsonData2.replace("\",\"", "\n");
		String[] socialMember = jsonData3.split("\n"); String ktoken = socialMember[0].split(" ")[1];
		String kager = socialMember[1].split(" ")[1]; String kbirth = socialMember[2].split(" ")[1];
		String kid = socialMember[3].split(" ")[1]; String kgender = socialMember[4].split(" ")[1];
		String knickname = socialMember[5].split(" ")[1]; String kprofileimg = socialMember[6].split(" ")[1];
		String kthumbnail = socialMember[7].split(" ")[1]; String kplatform = socialMember[8].split(" ")[1];

		MemberDTO memberDTO = MemberDTO.builder()
			.mid(kid).mnickname(knickname).mager(kager).mbirth(kbirth)
			.mgender(kgender).mplatform(kplatform).build();

		// 커밋 주석
		boolean result = memberService.KakaoLogin(memberDTO);
		if(result) {
			System.out.println("기존 가입된 카카오 아이디");
			return ResponseEntity.ok().body(memberDTO);
		} else {
			System.out.println("새로 가입할 카카오 아이디");
			memberService.SignUp(memberDTO);

			return ResponseEntity.ok().body(memberDTO);
		}
	}
}