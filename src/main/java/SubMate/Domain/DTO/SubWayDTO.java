package SubMate.Domain.DTO;

import lombok.*;

@Data @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubWayDTO {
        private String sno;
        private String sline;
        private String scode;
        private String sname;
        private String slng;
        private String slat;
}
