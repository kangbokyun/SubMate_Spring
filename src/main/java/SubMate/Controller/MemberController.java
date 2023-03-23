package SubMate.Controller;

import SubMate.Config.Auth.Role;
import SubMate.Config.DTO.ResponseDTO;
import SubMate.Config.Filter.TokenProvider;
import SubMate.Domain.DTO.BoardDTO;
import SubMate.Domain.DTO.KakaoDTO;
import SubMate.Domain.DTO.MemberDTO;
import SubMate.Domain.DTO.ProfileTalkDTO;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
	public ResponseEntity<?> SignUp(
					@RequestParam("mid") String mid, @RequestParam("mpw") String mpw, @RequestParam("mname") String mname, @RequestParam("mnickname") String mnickname,
					@RequestParam("mphone") String mphone, @RequestParam("mbirth") String mbirth, @RequestParam("maddress")
					String maddress, @RequestParam("mgender") String mgender, @RequestParam("mbti") String mbti, @RequestParam("profileimg") MultipartFile profileimg) {

		MemberDTO memberDTO = MemberDTO.builder()
			.mnickname(mnickname).mname(mname).maddress(maddress).mphone(mphone).mid(mid)
			.mbirth(mbirth).mbti(mbti).mpw(mpw).mgender(mgender).profileimg(profileimg.getName())
			.build();
		boolean result = memberService.SignUp(memberDTO, profileimg);
		if (result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/SignUpNoImg")
	public ResponseEntity<?> SignUpNoImg(@RequestBody MemberDTO memberDTO) {
		System.out.println("memberDTO : " + memberDTO);
		boolean result = memberService.SignUp(memberDTO, null);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}

	// 로그인
	@PostMapping("/Login")
	public ResponseEntity<?> Login(@RequestBody MemberDTO memberDTO) {
		MemberEntity user = memberService.Login(memberDTO.getMid(), memberDTO.getMpw());
		try {
			System.out.println("1");
			// user 정보 받아오고 롤이 NOTUSER가 아니면 토큰 생성
			if (user != null && user.getRole() != Role.NOTUSER) {
			System.out.println("2");
				String jwtToken = tokenProvider.create(user);
				MemberDTO memberDTO1 = MemberDTO.builder().mno(user.getMno()).mid(user.getMid())
					.mname(user.getMname()).maddress(user.getMaddress()).profileimg(user.getProfileimg())
					.mnickname(user.getMnickname()).mphone(user.getMphone()).mplatform(user.getMplatform())
					.mager(user.getMager()).mbirth(user.getMbirth()).mhobby(user.getMhobby()).mbti(user.getMbti())
					.token(jwtToken).mrole(user.getRole()).createddate(user.getCreateDate()).mbti(user.getMbti()).mgender(user.getMgender())
					.build();
			System.out.println("3");
				return ResponseEntity.ok().body(memberDTO1);
			} else if(user == null) {
			System.out.println("4");
//				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@user : " + user);
				ResponseDTO responseDTO = ResponseDTO.builder().error("Login Failed").build();
				return ResponseEntity.badRequest().body(responseDTO);
			} else {
			System.out.println("5");
				ResponseDTO responseDTO = ResponseDTO.builder().error("NOTUSER").build();
				return ResponseEntity.badRequest().body(responseDTO);
			}
		} catch (Exception e) {
			System.out.println("6");
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
			memberService.SignUp(memberDTO, null);

			return ResponseEntity.ok().body(memberDTO);
		}
	}
}