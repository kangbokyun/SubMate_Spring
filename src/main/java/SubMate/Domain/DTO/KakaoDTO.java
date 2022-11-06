package SubMate.Domain.DTO;

import SubMate.Domain.Entity.BaseTimeEntity;
import lombok.*;

@Data @ToString @Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoDTO extends BaseTimeEntity {
	private int kakaono;
	private String kakaoid;
	private String kakaoage;
	private String kakaobirth;
	private String kakaonickname;
	private String gender;
	private String accesstoken;
	private String profileimg;
	private String thumbnail;
	private String platform;
}
