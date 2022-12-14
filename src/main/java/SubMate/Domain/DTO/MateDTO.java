package SubMate.Domain.DTO;

import lombok.*;

@Data @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class MateDTO {
        private int mateno;
        private String mategwst;
        private String mategwet;
        private String matelwst;
        private String matelwet;
        private String matetline;
        private String matestartstation;
        private String mateendstation;
        private String matestartstationname;
        private String mateendstationname;
        private int mno;
}
