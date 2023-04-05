package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class WritedDTO {
    private int wno;
    private String wname;
    private String wdate;
    private int rno;
    private String rcontents;
    private String hno;
    private String htypem; // 메이트에서 받은 하트
    private String htypeb; // 게시글에서 받은 하트
    private String htyper; // 댓글에서 받은 하트
	private String htyperr; // 대댓글에서 받은 하트
    private String mno;
}
