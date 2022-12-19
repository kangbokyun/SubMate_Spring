package SubMate.Domain.DTO;

import lombok.*;

import javax.persistence.Column;

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
        private String rankerager; // 랭커 나이대
        private String rankerhobby; // 랭커 취미
        private String rankermbti; // 랭커 성향
        private int heartcount;; // 랭커 하트 수
        private String createdDate;
}
