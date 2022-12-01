package SubMate.Controller;

import SubMate.Domain.DTO.MateDTO;
import SubMate.Service.MateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MateController {
        @Autowired
        MateService mateService;

        @GetMapping("/Setting/Mate")
        public void CheckSubWay() {
                System.out.println("Init Setting Mate");
                mateService.SubStation();
        }

        @PostMapping("/Setting/Mate/SubWayKind")
        public ResponseEntity<?> SubWayKind() {
                return ResponseEntity.ok().body(HttpStatus.OK);
        }

        @PostMapping("Setting/Mate/Station")
        public ResponseEntity<?> SearchStation(@RequestBody MateDTO mateDTO) {
                System.out.println("mateDTO : " + mateDTO);
                mateService.SearchStation(mateDTO);
                return ResponseEntity.ok().body(HttpStatus.OK);
        }
}
