package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class ProfileTalkDTO {
	private int ptno;
	private String ptcontents;
	private int writedmno;
	private String ptwriter;
	private int mno;
}
