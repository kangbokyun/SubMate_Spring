package SubMate.Service;

import SubMate.Config.Auth.Role;
import SubMate.Domain.DTO.MemberDTO;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// 회원가입
	public boolean SignUp(MemberDTO memberDTO, MultipartFile file) {
		MemberEntity memberEntityCheck = memberRepository.findByMid(memberDTO.getMid());
		if(memberDTO.getMplatform() == null) {
			memberDTO.setMplatform("SubMate");
		}
		if (memberEntityCheck == null || memberEntityCheck.equals("null")) {
			if(file != null) {
				if (!memberDTO.getProfileimg().equals("") || memberDTO.getProfileimg() != null) {
					String uuidFile = "";
					if (!file.getOriginalFilename().equals("")) {
						UUID uuid = UUID.randomUUID();
						uuidFile = uuid.toString() + "_" + file.getOriginalFilename().replace("_", "-");

//						String filePath = "C:/Users/bk940/OneDrive/바탕 화면/SubMate_React/src/MemberImg";
						String filePath = "C:/Users/강보균/Desktop/SubMate_React/src/MemberImg";
						String fileDir = filePath + "/" + uuidFile;
						memberDTO.setProfileimg(fileDir);

						try {
							file.transferTo(new File(fileDir));
							System.out.println("Save File On DeskTop Folder");
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					} else {
						uuidFile = "null";
					}
				}
			}else {
				String uuidFile = "";
				UUID uuid = UUID.randomUUID();
				uuidFile = uuid.toString() + "/User_No_Img.png";

//				String filePath = "C:/Users/bk940/OneDrive/바탕 화면/SubMate_React/src/MemberImg";
				String filePath = "C:/Users/강보균/Desktop/SubMate_React/src/MemberImg";
				String fileDir = filePath + "/" + uuidFile;
				memberDTO.setProfileimg(fileDir);

				try {
					file.transferTo(new File(fileDir));
					System.out.println("Save File On DeskTop Folder");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			if (memberDTO.getMplatform().equals("Kakao")) {
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
			} else if (memberDTO.getMplatform().equals("SubMate")) {
				// 나이대
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
				String ager = "";
				if (Integer.parseInt(memberDTO.getMbirth().substring(0, 1)) != 0) {
					ager = "19" + memberDTO.getMbirth().substring(0, 2);
					ager = Integer.toString((Integer.parseInt(sdf.format(date)) - Integer.parseInt(ager) + 1));
					memberDTO.setMager(ager.substring(0, 1) + "0 ~ " + ager.substring(0, 1) + "9");
				} else {
					ager = "20" + memberDTO.getMbirth().substring(0, 2);
					ager = Integer.toString((Integer.parseInt(sdf.format(date)) - Integer.parseInt(ager) + 1));
					memberDTO.setMager(ager.substring(0, 1) + "0 ~ " + ager.substring(0, 1) + "9");
				}

				MemberEntity memberEntity = MemberEntity.builder()
					.mid(memberDTO.getMid())
					.mpw(passwordEncoder.encode(memberDTO.getMpw()))
					.mname(memberDTO.getMname())
					.mnickname(memberDTO.getMnickname())
					.mphone(memberDTO.getMphone())
					.maddress(memberDTO.getMaddress())
					.mbirth(memberDTO.getMbirth().substring(2, 6))
					.mgender(memberDTO.getMgender())
					.mager(memberDTO.getMager())
					.mplatform(memberDTO.getMplatform())
					.profileimg(memberDTO.getProfileimg())
					.role(Role.USER)
					.build();
				memberRepository.save(memberEntity);
			}
			return true;
		} else {
			return false;
		}
	}

	// 로그인
	public MemberEntity Login(String mid, String mpw) {
		MemberEntity memberEntity = memberRepository.findByMid(mid);

		// passwordEncoder의 matches 메소드를 통해 패스워드 일치유무 확인
		if (memberEntity != null && passwordEncoder.matches(mpw, memberEntity.getMpw())) {
			System.out.println("패스워드가 일치합니다.");
//			if(memberEntity.getProfileimg() != null) {
//				memberEntity.setProfileimg(memberEntity.getProfileimg().split("/MemberImg/")[1]);
//			}
			return memberEntity;
		}
		return null;
	}

	// 카카오 로그인
	public boolean KakaoLogin(MemberDTO memberDTO) {
		MemberEntity memberEntity = memberRepository.findByMid(memberDTO.getMid());
		if (memberEntity == null) {
			return false;
		} else {
			return true;
		}
	}
}
