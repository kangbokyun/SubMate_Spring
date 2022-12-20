package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class IssueDTO {
        private int issueNo;
        private String issueTitle;
        private String issueLink;
}
