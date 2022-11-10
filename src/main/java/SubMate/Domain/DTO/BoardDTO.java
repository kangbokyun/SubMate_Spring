package SubMate.Domain.DTO;

import lombok.*;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Data @ToString @Builder
public class BoardDTO {
	private String bno;
	private String btitle;
	private String bcontents;
	private String bimg;
	private String becho;
}
