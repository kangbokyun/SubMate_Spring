package SubMate.Domain.DTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class NoticeDTO {
        private int nno;
        private String ntitle;
        private String ncontents;
        private List<String> nfile;
        private int nkind;
        private int mno;
}
