package SubMate.Domain.DTO;

import lombok.*;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Data @ToString @Builder
public class BoardDTO {
	private int bno; // 게시판 프라이머리 키
	private String btitle; // 글 제목
	private String bcontents; // 글 내용
	private String bwriter; // 글 작성자
	private String bechotimer; // 글 에코 설정 시간
	private String bimg; // 글 이미지
	private String bview; // 글 조회수
	private String becho; // 글 에코 설정 유무
	private String checkreply; // 글에 댓글 유무
	private String heart; // 글 하트 유무
	private String htype; // 하트의 종류(게시글, 댓글, 대댓글 등)
	private String hrno; // 하트 번호
	private String pmdivision; // 페이징 처리를 위한 PC인지 모바일인지 체크
	private String createdDate; // 글 작성시간
	private String mno; // 글 작성자 번호
	private String writerimg;
}
