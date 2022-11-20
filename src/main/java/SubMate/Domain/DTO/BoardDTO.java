package SubMate.Domain.DTO;

import lombok.*;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Data @ToString @Builder
public class BoardDTO {
	private int bno;
	private String btitle;
	private String bcontents;
	private String bwriter;
	private String bechotimer;
	private String bimg;
	private String bview;
	private String becho;
	private String checkreply;
	private String maddress;
	private String heart;
	private String createdDate;
	private String mno;
}
