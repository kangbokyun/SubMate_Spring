package SubMate.Domain.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString @Builder
public class WeatherDTO {
        private String TMP; // 기온
        private String POP; // 강수확률
        // 강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
        // 여기서 비/눈은 비와 눈이 섞여 오는 것을 의미 (진눈개비)
        private String PTY; // 강수형태(눈 / 비)
        // 하늘상태(SKY) 코드 : 맑음(1), 구름많음(3), 흐림(4)
        private String SKY; // 하늘 상태
}
