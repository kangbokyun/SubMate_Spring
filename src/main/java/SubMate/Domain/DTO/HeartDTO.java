package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class HeartDTO {
	private int hno;
	private String hkind;
	private String htype;
	private String mno;
	private String bno;
	private String rno;
}
