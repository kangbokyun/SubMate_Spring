package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class WritedDTO {
    private int wno;
    private String wname;
    private String wdate;
    private String mno;
}
