package SubMate.Controller;

import SubMate.Domain.DTO.*;
import SubMate.Service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SettingController {
        @Autowired
        SettingService settingService;

        @GetMapping("/Setting/Mate")
        public void CheckSubWay() {
                System.out.println("Init Setting Mate");
                settingService.SubStation();
        }

        @PostMapping("/Setting/Mate/Station") // 세팅_메이트 역 설정
        public ResponseEntity<?> SearchStation(@RequestBody MateDTO mateDTO) {
                System.out.println("mateDTO : " + mateDTO);
                boolean result = settingService.SearchStation(mateDTO);
                if(result) {
                        return ResponseEntity.ok().body(HttpStatus.OK);
                } else {
                        return ResponseEntity.ok().body(HttpStatus.OK);
                }
        }

        @PostMapping("/Mate/Users")
        public ResponseEntity<?> MateData(@RequestParam("mno") int mno) {
                System.out.println("/Mate/Users/mno" + mno);
                List<MemberDTO> memberDTOS = settingService.MateData(mno);
                return ResponseEntity.ok().body(memberDTOS);
        }

        @PostMapping("/Setting/Profile")
        public ResponseEntity<?> ProfileSetting(@RequestBody ProfileDTO profileDTO) {
                System.out.println("/Setting/Profile/profileDTO : " + profileDTO);
                boolean result = settingService.ProfileSetting(profileDTO);
                if(result) {
                        return ResponseEntity.ok().body(HttpStatus.OK);
                } else {
                        return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
                }
        }

        @PostMapping("/Mate/Profile")
        public ResponseEntity<?> UserProfile(@RequestParam("mno") int mno) {
                System.out.println("/Mate/Profile/mno : " + mno);
                List<ProfileDTO> profileDTOS = settingService.UserProfile(mno);
                return ResponseEntity.ok().body(profileDTOS);
        }

        @PostMapping("/Set/Customer/QnA")
        public ResponseEntity<?> QnA(@RequestBody QnADTO qnADTO) {
                boolean result = settingService.QnA(qnADTO);
                if(result) {
                        return ResponseEntity.ok().body(HttpStatus.OK);
                } else {
                        return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
                }
        }

        @PostMapping("/Set/Customer/Tendinous")
        public ResponseEntity<?> Tendinous(@RequestBody TendinousDTO tendinousDTO) {
                System.out.println("/Set/Customer/Tendinous/TendinousDTO" + tendinousDTO);
                boolean result = settingService.Tendinous(tendinousDTO);
                if(result) {
                        return ResponseEntity.ok().body(HttpStatus.OK);
                } else {
                        return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
                }
        }

	@PostMapping("/Profile/LineTalk")
	public ResponseEntity<?> LineTalk(@RequestBody  ProfileTalkDTO profileTalkDTO) {
		System.out.println("profileTalkDTO : " + profileTalkDTO);
		boolean result = settingService.LineTalk(profileTalkDTO);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/Profile/TalkList")
	public ResponseEntity<?> TalkList(@RequestParam("mno") String mno) {
		List<ProfileTalkDTO> profileTalkDTOS = settingService.TalkList(Integer.parseInt(mno));
		return ResponseEntity.ok().body(profileTalkDTOS);
	}

    @PostMapping("/SendedTendinous")
    public ResponseEntity<?> SendedTendinous() {
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
