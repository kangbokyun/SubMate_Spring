package SubMate.Controller;

import SubMate.Domain.DTO.BoardDTO;
import SubMate.Domain.DTO.HeartDTO;
import SubMate.Domain.DTO.ReplyDTO;
import SubMate.Domain.DTO.ReportDTO;
import SubMate.Domain.Entity.BoardEntity;
import SubMate.Domain.Entity.HeartEntity;
import SubMate.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class BoardController {
	@Autowired
	BoardService boardService;

	@PostMapping("/Board/BoardWrite") // 이미지가 있는 글 등록
	public ResponseEntity<?> BoardWrite(@RequestParam("btitle") String btitle, @RequestParam("bcontents") String bcontents,
					    @RequestParam("becho") String becho, @RequestParam("bimg") MultipartFile file, @RequestParam("mno") String mno,
					    @RequestParam("bechotimer") String bechotimer, @RequestParam("bwriter") String bwriter) {
		System.out.println("BoardWriteImg Init");
		BoardDTO boardDTO = BoardDTO.builder().btitle(btitle).bcontents(bcontents).becho(becho).bimg(file.getName())
			.mno(mno).bechotimer(bechotimer).bwriter(bwriter).build();
		if(boardDTO.getBechotimer() == null || boardDTO.getBechotimer().equals("0")) {
			boardDTO.setBechotimer(null);
		}
//		System.out.println("boardDTO : " + boardDTO);
		boolean result = boardService.BoardWrite(boardDTO, file);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/Board/BoardWriteNoImg") // 이미지가 없는 글 등록
	public ResponseEntity<?> BoardWriteNoImg(@RequestBody BoardDTO boardDTO) {
		System.out.println("BoardWriteNoImgBoardDTO : " + boardDTO);
		if(boardDTO.getBecho() == null) {
			boardDTO.setBecho("1");
		}
		boolean result = boardService.BoardWrite(boardDTO, null);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/Board/BoardList") // 글 목록
	public ResponseEntity<?> BoardList(@RequestParam("mno") int mno, @RequestParam("page") int page, @RequestParam("lastno") int lastno) {
		System.out.println("mno : " + mno + " page : " + page + " lastno : " + lastno);
//		List<BoardDTO> boardDTOS = boardService.BoardList(mno, page, lastno);
		List<BoardDTO> boardDTOS = boardService.IsBoardList(mno, page, lastno);
		if(boardDTOS != null) {
			return ResponseEntity.ok().body(boardDTOS);
		} else {
			boardDTOS = null;
			return ResponseEntity.ok().body(boardDTOS);
		}
	}

	@PostMapping("/Board/ReplyWrite") // 댓글 쓰기
	public ResponseEntity<?> ReplyWrite(@RequestBody ReplyDTO replyDTO) {
		boolean result = boardService.ReplyWrite(replyDTO);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/Board/ReplyList") // 댓글/대댓글 목록
	public ResponseEntity<?> ReplyList(@RequestBody String bno) {
		List<ReplyDTO> replyDTOS = boardService.ReplyList(bno);
		return ResponseEntity.ok().body(replyDTOS);
	}

	@PostMapping("/Board/ViewUpdate") // 조회수 증가
	public ResponseEntity<?> ViewUpdate(@RequestParam("bno") String bno, @RequestParam("bview") String bview) {
		System.out.println("ViewUpdateBoardDTO : " + bno + " / " + bview);
		BoardEntity boardEntity = boardService.ViewUpdate(bno, bview);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@PostMapping("/Board/Heart")
	public ResponseEntity<?> HeartUpdate(@RequestParam("hkind") String hkind, @RequestParam("htype")
		String htype, @RequestParam("bno") String bno, @RequestParam("mno") String mno, @RequestParam("rno") String rno) {
		HeartDTO heartDTO = HeartDTO.builder().hkind(hkind).bno(bno).mno(mno).htype(htype).build();
		System.out.println("heartDTO.getHkind() : " + heartDTO.getHkind());
		if(rno != null) {
			heartDTO.setRno(rno);
		}
		System.out.println("heartDTO : " + heartDTO);
		heartDTO = boardService.BoardHeart(heartDTO);
		return ResponseEntity.ok().body(heartDTO);
	}

	@PostMapping("/Board/HeartList")
	public ResponseEntity<?> HeartList() {
		System.out.println("HeartList Init");
		List<HeartDTO> heartDTOS = boardService.HeartList();
		return ResponseEntity.ok().body(heartDTOS);
	}

	@PostMapping("/Board/Report")
	public ResponseEntity<?> BoardReport(
					     @RequestParam("reportbno") int reportbno,
					     @RequestParam("reportkind") int reportkind,
					     @RequestParam("mno") int mno,
					     @RequestParam("reportclickvalue") int clickvalue,
					     @RequestParam("reportvalue") String reportvalue,
					     @RequestParam("reportcontents") String reportcontents) {
		System.out.println("/Board/Report/Init");
		System.out.println("reportvalue : " + reportvalue);
		System.out.println("reportcontents : " + reportcontents);
//		if(reportvalue == null) {
//			System.out.println("123123123");
//		}
		ReportDTO reportDTO = ReportDTO.builder()
			.reportbno(reportbno).reportkind(reportkind).reportmno(mno).reportclickvalue(clickvalue)
			.reportvalue(reportvalue).reportcontents(reportcontents)
			.build();
		boolean result = boardService.BoardReport(reportDTO);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}
}