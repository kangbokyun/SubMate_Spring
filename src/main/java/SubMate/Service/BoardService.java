package SubMate.Service;

import SubMate.Domain.DTO.BoardDTO;
import SubMate.Domain.Entity.BoardEntity;
import SubMate.Domain.Repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    BoardRepository boardRepository;

    public boolean BoardWrite(BoardDTO boardDTO, MultipartFile file) {
        if(boardDTO != null) {
		if(!boardDTO.getBimg().equals("") || boardDTO.getBimg() != null ) {
		    String uuidFile = "";
		    if(!file.getOriginalFilename().equals("")) {
			    UUID uuid = UUID.randomUUID();
			    uuidFile = uuid.toString() + "_" + file.getOriginalFilename().replace("_", "-");

//			    String filePath = "C:/Users/bk940/IdeaProjects/SubMate_Spring/src/main/resources/Upload" + boardDTO.getMno();
			    String filePath = "C:/Users/강보균/Desktop/SubMate_Spring/src/main/resources/Upload" + boardDTO.getMno();
			    String fileDir = filePath + "/" + uuidFile;
			    boardDTO.setBimg(fileDir);
		    } else {
			    uuidFile = "null";
		    }
	    }

            BoardEntity boardEntity = BoardEntity.builder()
                    .btitle(boardDTO.getBtitle())
                    .bcontents(boardDTO.getBcontents())
                    .bview("0").becho(boardDTO.getBecho())
                    .bimg(boardDTO.getBimg())
                    .build();
            boardRepository.save(boardEntity);

            return true;
        } else {
            return false;
        }
    }
}
