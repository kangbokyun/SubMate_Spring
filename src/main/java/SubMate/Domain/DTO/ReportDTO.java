package SubMate.Domain.DTO;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Access;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class ReportDTO {
        private int reportno; // PK
        private int reportmno; // 신고자
        // 1 => 유저 2 => 게시글
        private int reportkind; // 게시글, 유저 등의 구분
        private int reportbno;
        private int reportrno;
        private int reportuserno; // 신고 받은 사람
        private int reportcheck;
        private int reportclickvalue;
        // 결과 1 => 조치 2 => 보류 3 => 반려
        private int reportresult;
}
