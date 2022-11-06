package SubMate.Service;

import SubMate.Config.Auth.Role;
import SubMate.Domain.DTO.MemberDTO;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// 회원가입
	public boolean SignUp(MemberDTO memberDTO) {
		System.out.println("memberDTO: " + memberDTO);
		if(memberDTO.getMplatform().equals("Kakao")) {
			memberDTO.setMpw("kakaoPlatform");
		}
		MemberEntity memberEntity = MemberEntity.builder()
			.mid(memberDTO.getMid())
			.mpw(passwordEncoder.encode(memberDTO.getMpw()))
			.mname(memberDTO.getMname())
			.mnickname(memberDTO.getMnickname())
			.mphone(memberDTO.getMphone())
			.maddress(memberDTO.getMaddress())
			.mbirth(memberDTO.getMbirth())
			.mgender(memberDTO.getMgender())
			.mager(memberDTO.getMager())
			.mplatform(memberDTO.getMplatform())
			.role(Role.USER)
			.build();
		memberRepository.save(memberEntity);
		return true;
	}

	// 로그인
	public MemberEntity Login(String mid, String mpw) {
		MemberEntity memberEntity = memberRepository.findByMid(mid);

		// passwordEncoder의 matches 메소드를 통해 패스워드 일치유무 확인
		if(memberEntity != null && passwordEncoder.matches(mpw, memberEntity.getMpw())) {
			System.out.println("패스워드가 일치합니다.");
			return memberEntity;
		}
		return null;
	}

	// 카카오 로그인
	public boolean KakaoLogin(String kakaoData) {
		String jsonData0 = kakaoData.replace("[{\"", " ");
		String jsonData1 = jsonData0.replace("\"}]", " ");
		String jsonData2 = jsonData1.replace("\":\"", " ");
		String jsonData3 = jsonData2.replace("\",\"", "\n");
		String[] socialMember = jsonData3.split("\n");
		String kid = socialMember[3].split(" ")[1];
		MemberEntity memberEntity = memberRepository.findByMid(kid);
		if(memberEntity == null) {
			return false;
		} else {
			return true;
		}
	}
}
