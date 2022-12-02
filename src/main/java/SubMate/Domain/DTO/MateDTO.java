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
        private String matestartstaion;
        private String mateendstation;
        private String matestartstaionname;
        private String mateendstaionname;
}
