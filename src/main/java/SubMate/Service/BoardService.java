package SubMate.Service;

import SubMate.Domain.DTO.BoardDTO;
import SubMate.Domain.Entity.BoardEntity;
import SubMate.Domain.Repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class BoardService {
	@Autowired
	BoardRepository boardRepository;

	// 글등록(이미지 1개 등록 가능)
	public boolean BoardWrite(BoardDTO boardDTO, MultipartFile file) {
		try {
			if (boardDTO != null) {
				if(file != null) {
					if (!boardDTO.getBimg().equals("") || boardDTO.getBimg() != null) {
						String uuidFile = "";
						if (!file.getOriginalFilename().equals("")) {
							UUID uuid = UUID.randomUUID();
							uuidFile = uuid.toString() + "_" + file.getOriginalFilename().replace("_", "-");

							String filePath = "C:/Users/bk940/OneDrive/바탕 화면/SubMate_React/src/BoardImg";
	//						String filePath = "C:/Users/bk940/IdeaProjects/SubMate_Spring/src/main/resources/Upload";
	//						String filePath = "C:/Users/강보균/Desktop/SubMate_Spring/src/main/resources/Upload";
							String fileDir = filePath + "/" + uuidFile;
							boardDTO.setBimg(fileDir);

							file.transferTo(new File(fileDir));
							System.out.println("Save File On DeskTop Folder");
						} else {
							uuidFile = "null";
						}
					} else {
						boardDTO.setBimg("NoImg");
					}
					// 에코 체크 값에 따라 시간 설정
				}

				BoardEntity boardEntity = BoardEntity.builder()
					.btitle(boardDTO.getBtitle())
					.bcontents(boardDTO.getBcontents())
					.bview("0").becho(boardDTO.getBecho())
					.bimg(boardDTO.getBimg()).build();
				boardRepository.save(boardEntity);

				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	// 모든 게시글 가져오기
	public List<BoardDTO> BoardList() {
		List<BoardEntity> boardList = boardRepository.findAll();
		List<BoardDTO> boardDTOS = new ArrayList<>();
		for(BoardEntity entity : boardList) {
			BoardDTO boardDTO = new BoardDTO();
			boardDTO.setBno(entity.getBno());
			boardDTO.setBtitle(entity.getBtitle());
			boardDTO.setBcontents(entity.getBcontents());
			boardDTO.setBview(entity.getBview());
			boardDTO.setBecho(entity.getBecho());
			if(entity.getBimg() != null) {
				boardDTO.setBimg(entity.getBimg().split("/BoardImg/")[1]);
			} else {
				boardDTO.setBimg("null");
			}
			boardDTOS.add(boardDTO);
		}

		// 게시글 내림차순
		Comparator<BoardDTO> listSort = new Comparator<BoardDTO>() {
			@Override
			public int compare(BoardDTO o1, BoardDTO o2) {
				int a = o1.getBno();
				int b = o2.getBno();

				if(a > b) { return -1; } else {	return 1;	 }
			}
		};
		Collections.sort(boardDTOS, listSort);

		return boardDTOS;
	}
}
