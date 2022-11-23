package SubMate.Domain.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data @ToString @Builder
public class ReplyDTO {
        private int rno;
        private String rcontents;
        private String rwriter;
	private String rdepth;
	private String writedrno;
	private String rwriterimg;
        private String htype;
        private String hrno;
        private String rheart;
        private String rcount;
        private String mno;
        private String bno;
        private String createdDate;
}
