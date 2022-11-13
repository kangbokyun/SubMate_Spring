package SubMate.Controller;

import SubMate.Domain.DTO.BoardDTO;
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

	@PostMapping("/Board/BoardWrite")
	public ResponseEntity<?> BoardWrite(@RequestParam("btitle") String btitle, @RequestParam("bcontents") String bcontents,
					    @RequestParam("becho") String becho, @RequestParam("bimg") MultipartFile file, @RequestParam("mno") String mno) {
		BoardDTO boardDTO = BoardDTO.builder().btitle(btitle).bcontents(bcontents).becho(becho).bimg(file.getName())
			.mno(mno).build();
		System.out.println("boardDTO : " + boardDTO);
		boolean result = boardService.BoardWrite(boardDTO, file);
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
}