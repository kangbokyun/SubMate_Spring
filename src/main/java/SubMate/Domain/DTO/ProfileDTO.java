package SubMate.Domain.DTO;

import lombok.*;

import javax.persistence.Column;

@Getter @Setter @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
        int pno;
        String pintro; // 소개말
        String plike1; // 좋아요1
        String plike2; // 좋아요2
        String plike3; // 좋아요3
        String punlike1; // 싫어요1
        String punlike2; // 싫어요2
        String punlike3; // 싫어요3
        String phobby1; // 취미1
        String phobby2; // 취미2
        String phobby3; // 취미3
        int mno;
}
