package SubMate.Service;

import SubMate.Config.Auth.Role;
import SubMate.Domain.DTO.MemberDTO;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// 회원가입
	public boolean SignUp(MemberDTO memberDTO) {
		MemberEntity memberEntityCheck = memberRepository.findByMid(memberDTO.getMid());
		if(memberEntityCheck != null) {
			return false;
		} else {
			if(memberDTO.getMplatform().equals("Kakao")) {
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
						.mplatform("kakaoPlatform")
						.role(Role.USER)
						.build();
				memberRepository.save(memberEntity);
			} else if(memberDTO.getMplatform() == "null" || memberDTO.getMplatform() == "null" || memberDTO.getMplatform() == "") {
				// 나이대
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
				String ager = "";
				if(Integer.parseInt(memberDTO.getMbirth().substring(0, 1)) != 0) {
					ager = "19" + memberDTO.getMbirth().substring(0, 2);
					ager = Integer.toString((Integer.parseInt(sdf.format(date)) - Integer.parseInt(ager) + 1));
					memberDTO.setMager(ager.substring(0, 1) + "0 ~ " + ager.substring(0, 1) + "9");
				} else {
					ager = "20" + memberDTO.getMbirth().substring(0, 2);
					ager = Integer.toString((Integer.parseInt(sdf.format(date)) - Integer.parseInt(ager) + 1));
					memberDTO.setMager(ager.substring(0, 1) + "0 ~ " + ager.substring(0, 1) + "9");
				};

				MemberEntity memberEntity = MemberEntity.builder()
					.mid(memberDTO.getMid())
					.mpw(passwordEncoder.encode(memberDTO.getMpw()))
					.mname(memberDTO.getMname())
					.mnickname(memberDTO.getMnickname())
					.mphone(memberDTO.getMphone())
					.maddress(memberDTO.getMaddress())
					.mbirth(memberDTO.getMbirth().substring(2, 4))
					.mgender(memberDTO.getMgender())
					.mager(memberDTO.getMager())
					.mplatform("SubMate")
					.role(Role.USER)
					.build();
				memberRepository.save(memberEntity);
			}
			return true;
		}
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
	public boolean KakaoLogin(MemberDTO memberDTO) {
		MemberEntity memberEntity = memberRepository.findByMid(memberDTO.getMid());
		if(memberEntity == null) {
			return false;
		} else {
			return true;
		}
	}
}
