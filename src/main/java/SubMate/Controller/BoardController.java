package SubMate.Controller;

import SubMate.Domain.DTO.BoardDTO;
import SubMate.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BoardController {
	@Autowired
	BoardService boardService;

	@PostMapping("/Board/BoardWrite")
	public ResponseEntity<?> BoardWrite(@RequestParam("btitle") String btitle, @RequestParam("bcontents") String bcontents, @RequestParam("becho") String becho, @RequestParam("bimg") MultipartFile file) {
		System.out.println("btitle : " + btitle);
		System.out.println("bcontents : " + bcontents);
		System.out.println("becho : " + becho);
		System.out.println("file.getOriginalFilename() : " + file.getOriginalFilename());
		BoardDTO boardDTO = BoardDTO.builder().btitle(btitle).bcontents(bcontents).becho(becho).bimg(file.getName()).build();
		System.out.println("boardDTO : " + boardDTO);
//		boolean result = boardService.BoardWrite(boardDTO, file);
//		if(result) {
//			return ResponseEntity.ok().body(true);
//		} else {
			return ResponseEntity.ok().body(false);
//		}
	}
}