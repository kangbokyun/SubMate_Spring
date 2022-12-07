package SubMate.Controller;

import SubMate.Domain.DTO.MateDTO;
import SubMate.Domain.DTO.MemberDTO;
import SubMate.Service.MateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MateController {
        @Autowired
        MateService mateService;

        @GetMapping("/Setting/Mate")
        public void CheckSubWay() {
                System.out.println("Init Setting Mate");
                mateService.SubStation();
        }

        @PostMapping("Setting/Mate/Station") // 세팅_메이트 역 설정
        public ResponseEntity<?> SearchStation(@RequestBody MateDTO mateDTO) {
                System.out.println("mateDTO : " + mateDTO);
                boolean result = mateService.SearchStation(mateDTO);
                if(result) {
                        return ResponseEntity.ok().body(HttpStatus.OK);
                } else {
                        return ResponseEntity.ok().body(HttpStatus.OK);
                }
        }

        @PostMapping("/Mate/Users")
        public ResponseEntity<?> MateData(@RequestParam("mno") int mno) {
                System.out.println(mno);
                List<MemberDTO> memberDTOS = mateService.MateData(mno);
                return ResponseEntity.ok().body(memberDTOS);
        }
}
