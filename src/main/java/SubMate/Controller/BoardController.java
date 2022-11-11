package SubMate.Controller;

import SubMate.Domain.DTO.BoardDTO;
import SubMate.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BoardController {
    @Autowired
    BoardService boardService;

//    @PostMapping("/Board/BoardWrite")
//    public boolean BoardWrite(@RequestBody BoardDTO boardDTO, @RequestParam("bimg")MultipartFile file) {
//        System.out.println("boardDTO : " + boardDTO);
//        System.out.println("file : " + file);
//        boolean result = boardService.BoardWrite(boardDTO, file);
//        if(result) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
