package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class IssueDTO {
        private String issueTitle;
        private String issueLink;
}
