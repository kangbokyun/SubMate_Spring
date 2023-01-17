package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class ChatHistoryDTO {
    private int chno;
    private String chsendername;
    private String chsenderno;
    private String createddate;
    private String chsgender;
}
