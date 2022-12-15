package SubMate.Domain.DTO;

import lombok.*;

@Getter @Setter @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankDTO {
        private int rankno;
        private String rankline;
        private String rankrankerno;
        private String rankrankernickname;
        private int mno;
        private String profileImg;
}
