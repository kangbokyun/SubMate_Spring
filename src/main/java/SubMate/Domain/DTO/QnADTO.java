package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class QnADTO {
        private int qnano;
        private String qnatitle;
        private String qnacontents;
        private int qnamno;
        private String qnastatus;
}
