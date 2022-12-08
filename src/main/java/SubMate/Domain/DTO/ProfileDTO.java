package SubMate.Domain.DTO;

import lombok.*;

import javax.persistence.Column;

@Getter @Setter @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
        private int pno;
	private String pintro; // 소개말
	private String plike1; // 좋아요1
	private String plike2; // 좋아요2
	private String plike3; // 좋아요3
	private String punlike1; // 싫어요1
	private String punlike2; // 싫어요2
	private String punlike3; // 싫어요3
	private String phobby1; // 취미1
	private String phobby2; // 취미2
	private String phobby3; // 취미3
	private String checknull;
	private int mno;
}
