package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class PagingDTO {
    private int pageno; // 페이지 개수
}
