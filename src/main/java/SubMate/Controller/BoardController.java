package SubMate.Controller;

import SubMate.Domain.DTO.BoardDTO;
import SubMate.Domain.DTO.ReplyDTO;
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

	@PostMapping("/Board/BoardList")
	public ResponseEntity<?> BoardList() {
		List<BoardDTO> boardDTOS = boardService.BoardList();
		if(boardDTOS != null) {
			return ResponseEntity.ok().body(boardDTOS);
		} else {
			boardDTOS = null;
			return ResponseEntity.ok().body(boardDTOS);
		}
	}

	@PostMapping("/Board/ReplyWrite")
	public ResponseEntity<?> ReplyWrite(@RequestBody ReplyDTO replyDTO) {
		boolean result = boardService.ReplyWrite(replyDTO);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/Board/ReplyList")
	public ResponseEntity<?> ReplyList(@RequestBody String bno) {
		List<ReplyDTO> replyDTOS = boardService.ReplyList(bno);
		return ResponseEntity.ok().body(replyDTOS);
	}
}