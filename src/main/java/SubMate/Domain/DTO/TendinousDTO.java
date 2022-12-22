package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class TendinousDTO {
        private int tno;
        private String tcontents;
        private String tselectcontentkind;
        private String tselecttendinouskind;
        private String tstatus;
        private int mno;
}
