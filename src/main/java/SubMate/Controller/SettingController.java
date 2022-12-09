package SubMate.Controller;

import SubMate.Domain.DTO.MateDTO;
import SubMate.Domain.DTO.MemberDTO;
import SubMate.Domain.DTO.ProfileDTO;
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
}
