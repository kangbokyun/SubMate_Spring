package SubMate.Controller;

import SubMate.Domain.DTO.HeartDTO;
import SubMate.Service.MateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MateController {
        @Autowired
        MateService mateService;

        @PostMapping("/Mate/UserHeart") // 유저 하트 클릭 여부
        public ResponseEntity<?> UserHeart(HeartDTO heartDTO) {
                System.out.println("heartDTO : " + heartDTO);
                boolean result = mateService.UserHeart(heartDTO);
                if(result) {
                        return ResponseEntity.ok().body(HttpStatus.OK);
                } else {
                        return ResponseEntity.ok().body(HttpStatus.OK);
                }
        }

        @PostMapping("/Mate/UserHeartList")
        public ResponseEntity<?> UserHeartList() {
                System.out.println("/Mate/UserHeartList Init");
                List<HeartDTO> heartDTOS = mateService.UserHeartList();
                return ResponseEntity.ok().body(heartDTOS);
        }
}
