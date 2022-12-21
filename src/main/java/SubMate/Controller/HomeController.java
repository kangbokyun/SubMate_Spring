package SubMate.Controller;

import SubMate.Domain.DTO.IssueDTO;
import SubMate.Domain.DTO.RankDTO;
import SubMate.Domain.DTO.WeatherDTO;
import SubMate.Service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {
        @Autowired
        HomeService homeService;

        @PostMapping("/Home/Rank")
        public ResponseEntity<?> SearchRank(@RequestParam("mno") int mno) {
                System.out.println("/Home/Rank/mno : " + mno);
                List<RankDTO> rankDTOS = homeService.SearchRanker(mno);
                return ResponseEntity.ok().body(rankDTOS);
        }

        @PostMapping("/Home/Issue")
        public ResponseEntity<?> HomeIssue() {
                List<IssueDTO> issueDTOS = homeService.HomeIssue();
//                System.out.println("Home/Issue/IssueDTO : " + issueDTOS);
                return ResponseEntity.ok().body(issueDTOS);
        }

        @PostMapping("/Home/Weather")
        public ResponseEntity<?> HomeWeather() {
                WeatherDTO weatherDTO = homeService.Weather();
                return ResponseEntity.ok().body(weatherDTO);
        }
}
