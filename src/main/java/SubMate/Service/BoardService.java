package SubMate.Service;

import SubMate.Domain.DTO.BoardDTO;
import SubMate.Domain.DTO.HeartDTO;
import SubMate.Domain.DTO.ReplyDTO;
import SubMate.Domain.Entity.BoardEntity;
import SubMate.Domain.Entity.HeartEntity;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Entity.ReplyEntity;
import SubMate.Domain.Repository.BoardRepository;
import SubMate.Domain.Repository.HeartRepository;
import SubMate.Domain.Repository.MemberRepository;
import SubMate.Domain.Repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BoardService {
	@Autowired
	BoardRepository boardRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ReplyRepository replyRepository;
	@Autowired
	HeartRepository heartRepository;

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

//							String filePath = "C:/Users/bk940/OneDrive/바탕 화면/SubMate_React/src/BoardImg";
							String filePath = "C:/Users/강보균/Desktop/SubMate_React/src/BoardImg";
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
				}

				MemberEntity memberEntity = memberRepository.findById(Integer.parseInt(boardDTO.getMno())).get();
				BoardEntity boardEntity = BoardEntity.builder()
					.btitle(boardDTO.getBtitle())
					.bcontents(boardDTO.getBcontents())
					.bview("0").becho(boardDTO.getBecho())
					.bechotimer(boardDTO.getBechotimer())
					.bimg(boardDTO.getBimg()).bwriter(boardDTO.getBwriter())
					.memberEntity(memberEntity).build();
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
			if(entity.getBechotimer() != null) { // 에코 타이머가 설정됐을 때
				try {
					// entity.getBechotimer() : 30m
					// entity.getCreateDate().toString() : 2022-11-15T16:11:36.648321
					String echoDate = entity.getCreateDate().toString().replace("T", " ").replace(".", "_").split("_")[0];
					System.out.println("echoDate : " + echoDate);

					Date date = new Date();
					Calendar calendar = Calendar.getInstance();
					Calendar nowTime = Calendar.getInstance();
					nowTime.setTime(new Date());
					SimpleDateFormat formatEcho = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					BoardDTO boardDTO = new BoardDTO();
					boardDTO.setBno(entity.getBno());
					boardDTO.setBtitle(entity.getBtitle());
					boardDTO.setBcontents(entity.getBcontents());
					boardDTO.setBwriter(entity.getBwriter());
					boardDTO.setBview(entity.getBview());
					boardDTO.setBecho(entity.getBecho());
					boardDTO.setBechotimer(entity.getBechotimer());

					List<HeartEntity> heartEntities = heartRepository.findAll();
					for(HeartEntity heartEntity : heartEntities) {
						if(heartEntity.getMno().equals(entity.getMemberEntity().getMno() + "") &&
							heartEntity.getBno().equals(entity.getBno() + "")) {
							boardDTO.setHeart("1");
							boardDTO.setHrno(heartEntity.getRno());
						}
					}

					if(entity.getBechotimer().endsWith("h")) {
						calendar.setTime(formatEcho.parse(echoDate));
						String temp = entity.getBechotimer();
						calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(temp.replace("h", "")));

						long sec = (calendar.getTimeInMillis() - nowTime.getTimeInMillis()) / 1000;
						int minute = Integer.parseInt(String.valueOf(sec)) / 60;
						int hour = minute / 60 ;
						int day = hour / 24;
						System.out.println("sec : " + sec + "\nminute : " + minute + "\nhour : " + hour + "\nday : " + day);
						if(sec <= 59) {
							boardDTO.setCreatedDate(sec + "초 남음");
						} else if(day == 0 && hour == 0) {
							boardDTO.setCreatedDate(minute + "분 남음");
						} else if(day == 0 && hour > 0) {
							boardDTO.setCreatedDate(hour + "시간 남음");
						} else {
							boardDTO.setCreatedDate(day + "일 남음");
						}


						if(formatEcho.format(calendar.getTime()).compareTo(formatEcho.format(date)) <= 0) { // 아직 안지났으면 1, 같으면 0, 지났으면 -1
							System.out.println("Echo H Cut");
							boardRepository.delete(entity);
							continue;
						}
					} else {
						calendar.setTime(formatEcho.parse(echoDate));
						String temp = entity.getBechotimer();
						calendar.add(Calendar.MINUTE, Integer.parseInt(temp.replace("m", "")));

						long sec = (calendar.getTimeInMillis() - nowTime.getTimeInMillis()) / 1000;
						int minute = Integer.parseInt(String.valueOf(sec)) / 60;
						int hour = minute / 60 ;
						int day = hour / 24;
						if(sec <= 59) {
							boardDTO.setCreatedDate(sec + "초 남음");
						} else if(day == 0 && hour == 0) {
							boardDTO.setCreatedDate(minute + "분 남음");
						} else if(day == 0 && hour > 0) {
							boardDTO.setCreatedDate(hour + "시간 남음");
						} else {
							boardDTO.setCreatedDate(day + "일 남음");
						}

						if( formatEcho.format(calendar.getTime()).compareTo(formatEcho.format(date)) <= 0) {
							System.out.println("Echo M Cut");
							boardRepository.delete(entity);
							continue;
						}
					}

					List<ReplyEntity> replyEntityList =  replyRepository.findAll();
					for(ReplyEntity replyEntity : replyEntityList) {
						if(replyEntity.getBoardReplyEntity().getBno() == boardDTO.getBno()) {
							boardDTO.setCheckreply("1");
							break;
						} else {
							boardDTO.setCheckreply("0");
						}
					}

					if(entity.getBimg() != null) {
						boardDTO.setBimg(entity.getBimg().split("/BoardImg/")[1]);
					} else {
						boardDTO.setBimg("null");
					}
					boardDTOS.add(boardDTO);
				} catch(Exception e) {
					System.out.println("e.Msg : " + e.getMessage());
				}
			} else { // 에코타이머가 설정 안됐을 때
				BoardDTO boardDTO = new BoardDTO();
				boardDTO.setBno(entity.getBno());
				boardDTO.setBtitle(entity.getBtitle());
				boardDTO.setBcontents(entity.getBcontents());
				boardDTO.setBwriter(entity.getBwriter());
				boardDTO.setBview(entity.getBview());
				boardDTO.setBecho(entity.getBecho());
				boardDTO.setBechotimer(entity.getBechotimer());
				boardDTO.setHtype("1");

				List<HeartEntity> heartEntities = heartRepository.findAll();
				for(HeartEntity heartEntity : heartEntities) {
					if(heartEntity.getMno().equals(entity.getMemberEntity().getMno() + "") &&
						heartEntity.getBno().equals(entity.getBno() + "") && (heartEntity.getRno() == null || heartEntity.getRno().equals("null"))) {
						boardDTO.setHeart("1");
						boardDTO.setHrno(heartEntity.getRno());
					} else {
						boardDTO.setHeart("0");
					}
				}

				try {
					// 시간계산
					Calendar calendar = Calendar.getInstance();
					Calendar calDate = Calendar.getInstance();
					calendar.setTime(new Date());

					String splitDate = entity.getCreateDate().toString().replace("T", " ").replace(".", "_").split("_")[0]; // 글 작성일
					Date getDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(splitDate);
					SimpleDateFormat sevenDaysAgo = new SimpleDateFormat("yy-MM-dd");
					calDate.setTime(getDate);

					long sec = (calendar.getTimeInMillis() - calDate.getTimeInMillis()) / 1000;
					int minute = Integer.parseInt(String.valueOf(sec)) / 60;
					int hour = Integer.parseInt(String.valueOf(sec)) / ( 60 * 60 );
					int day = Integer.parseInt(String.valueOf(sec)) / ( 24 * 60 * 60 );

					if(day < 7) {
						if(sec <= 59) {
							boardDTO.setCreatedDate(sec + "초 전");
						} else if(sec >= 60 && minute <= 59) {
							boardDTO.setCreatedDate(minute + "분 전");
						} else if(minute >= 60 && hour <= 23) {
							boardDTO.setCreatedDate(hour + "시간 전");
						} else  {
							boardDTO.setCreatedDate(day + "일 전");
						}
					} else {
						boardDTO.setCreatedDate(sevenDaysAgo.format(getDate));
					}

				} catch (ParseException e) {
					System.out.println(e.getMessage());
				}

				List<ReplyEntity> replyEntityList =  replyRepository.findAll();
				for(ReplyEntity replyEntity : replyEntityList) {
					if(replyEntity.getBoardReplyEntity().getBno() == boardDTO.getBno()) {
						boardDTO.setCheckreply("1");
						break;
					} else {
						boardDTO.setCheckreply("0");
					}
				}

				if(entity.getBimg() != null) {
					boardDTO.setBimg(entity.getBimg().split("/BoardImg/")[1]);
				} else {
					boardDTO.setBimg("null");
				}
				boardDTOS.add(boardDTO);
			}
		}

		// 게시글 내림차순(가장 최신 글이 제일 위로)
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

	// 댓글 등록하기
	public boolean ReplyWrite(ReplyDTO replyDTO) {
		if(replyDTO != null) {
			BoardEntity boardEntity = boardRepository.findById(Integer.parseInt(replyDTO.getBno())).get();
			MemberEntity memberEntity = memberRepository.findById(Integer.parseInt(replyDTO.getMno())).get();
			if(replyDTO.getRdepth().equals("1")) {
				ReplyEntity replyEntity = ReplyEntity.builder()
					.rcontents(replyDTO.getRcontents())
					.rwriter(replyDTO.getRwriter())
					.rdepth(replyDTO.getRdepth())
					.rwriterimg(replyDTO.getRwriterimg())
					.boardReplyEntity(boardEntity)
					.memberReplyEntity(memberEntity)
					.build();
				replyRepository.save(replyEntity);
			} else {
				ReplyEntity replyEntity = ReplyEntity.builder()
					.rcontents(replyDTO.getRcontents())
					.rwriter(replyDTO.getRwriter())
					.rdepth(replyDTO.getRdepth())
					.rwriterimg(replyDTO.getRwriterimg())
					.writedrno(replyDTO.getWritedrno())
					.boardReplyEntity(boardEntity)
					.memberReplyEntity(memberEntity)
					.build();
				replyRepository.save(replyEntity);
			}
			return true;
		} else {
			return false;
		}
	}
	// 댓글 리스트
	public List<ReplyDTO> ReplyList(String bno) {
		List<ReplyEntity> replyEntityList = replyRepository.findAll();
		List<ReplyDTO> replyDTOS = new ArrayList<>();
		if(replyEntityList != null) {
			BoardEntity boardEntity = boardRepository.findById(Integer.parseInt(bno)).get();
			int countReply = 0;
			for(ReplyEntity replyEntity : replyEntityList) {
				if(Integer.parseInt(bno) == replyEntity.getBoardReplyEntity().getBno()) {
					ReplyDTO replyDTO = ReplyDTO.builder()
						.rno(replyEntity.getRno())
						.rcontents(replyEntity.getRcontents())
						.rwriter(replyEntity.getRwriter())
						.rdepth(replyEntity.getRdepth())
						.writedrno(replyEntity.getWritedrno())
						.mno(Integer.toString(replyEntity.getMemberReplyEntity().getMno()))
						.bno(Integer.toString(replyEntity.getBoardReplyEntity().getBno()))
						.rwriterimg(replyEntity.getRwriterimg().split("MemberImg/")[1])
						.build();

					if(replyDTO.getRdepth().equals("1")) {
						replyDTO.setHtype("2");
					} else if(replyDTO.getRdepth().equals("2")) {
						replyDTO.setHtype("3");
					}

					List<HeartEntity> heartEntities = heartRepository.findAll();
					for(HeartEntity heartEntity : heartEntities) {
						if((heartEntity.getRno() != null || !heartEntity.getRno().equals("null")) && heartEntity.getBno().equals(replyDTO.getBno()) && heartEntity.getMno().equals(replyDTO.getMno()) && heartEntity.getRno().equals(Integer.toString(replyDTO.getRno()))) {
							replyDTO.setHrno( heartEntity.getRno());
							replyDTO.setRheart("1");
							System.out.println("여기 걸린 reply : " + replyDTO);
						}
					}

					for(int i = 0; i < replyEntityList.size(); i++) {
						if(replyEntityList.get(i).getWritedrno() != null && Integer.toString(replyDTO.getRno()).equals(replyEntityList.get(i).getWritedrno())) {
							countReply++;
						}
					}
					replyDTO.setRcount(Integer.toString(countReply));

					try {
						// 시간계산
						Calendar calendar = Calendar.getInstance();
						Calendar calDate = Calendar.getInstance();
						calendar.setTime(new Date());

						String splitDate = replyEntity.getCreateDate().toString().replace("T", " ").replace(".", "_").split("_")[0]; // 글 작성일
						Date getDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(splitDate);
						SimpleDateFormat sevenDaysAgo = new SimpleDateFormat("yy-MM-dd");
						calDate.setTime(getDate);

						long sec = (calendar.getTimeInMillis() - calDate.getTimeInMillis()) / 1000;
						int minute = Integer.parseInt(String.valueOf(sec)) / 60;
						int hour = Integer.parseInt(String.valueOf(sec)) / ( 60 * 60 );
						int day = Integer.parseInt(String.valueOf(sec)) / ( 24 * 60 * 60 );

						if(day < 7) {
							if(sec <= 59) {
								replyDTO.setCreatedDate(sec + "초 전");
							} else if(sec >= 60 && minute <= 59) {
								replyDTO.setCreatedDate(minute + "분 전");
							} else if(minute >= 60 && hour <= 23) {
								replyDTO.setCreatedDate(hour + "시간 전");
							} else  {
								replyDTO.setCreatedDate(day + "일 전");
							}
						} else {
							replyDTO.setCreatedDate(sevenDaysAgo.format(getDate));
						}

					} catch (ParseException e) {
						System.out.println(e.getMessage());
					}

					replyDTOS.add(replyDTO);
				}
			}
			return replyDTOS;
		}
		return null;
	}

	// 조회수 증가
	public BoardEntity ViewUpdate(String bno, String bview) {
		BoardEntity boardEntity = boardRepository.findById(Integer.parseInt(bno)).get();
		if(bno != null && bview != null) {
			boardEntity.setBview(Integer.toString(Integer.parseInt(boardEntity.getBview()) + 1));
			boardRepository.save(boardEntity);
			return boardEntity;
		} else {
			return boardEntity;
		}
	}

	// 하트
	public HeartDTO BoardHeart(HeartDTO heartDTO) {
		if(heartDTO.getHkind().equals("1")) { // 하트 안누름
			System.out.println("하트 OFF");
			List<HeartEntity> heartEntity = heartRepository.findAll();
			for(HeartEntity heartEntity1 : heartEntity) {
				if(heartEntity1.getBno().equals(heartDTO.getBno()) && heartEntity1.getHkind().equals("0")
				&& heartEntity1.getHtype().equals(heartDTO.getHtype()) && heartEntity1.getMno().equals(heartDTO.getMno())) {
					heartRepository.delete(heartEntity1);
				}
			}
		} else { // 하트 누름
			System.out.println("하트 ON");
			System.out.println("heartDTO.getHkind() : " + heartDTO.getHkind());
			if(heartDTO.getHkind().equals("undefined") || heartDTO.getHkind() == null) {
				heartDTO.setHkind("0");
			}
			HeartEntity heartEntity = HeartEntity.builder()
				.hkind(heartDTO.getHkind()).mno(heartDTO.getMno())
				.bno(heartDTO.getBno()).htype(heartDTO.getHtype()).build();
			if(heartDTO.getRno() != null) {
				heartEntity.setRno(heartDTO.getRno());
			}
			heartRepository.save(heartEntity);
		}
		return heartDTO;
	}

	// 하트 리스트
	public List<HeartDTO> HeartList() {
		List<HeartEntity> heartEntityList = heartRepository.findAll();
		List<HeartDTO> heartDTOS = new ArrayList<>();
		for(HeartEntity heartEntity : heartEntityList) {
			HeartDTO heartDTO = new HeartDTO();
			heartDTO.setHno(heartEntity.getHno());
			heartDTO.setHkind(heartEntity.getHkind());
			heartDTO.setHtype(heartEntity.getHtype());
			heartDTO.setBno(heartEntity.getBno());
			heartDTO.setRno(heartEntity.getRno());
			heartDTO.setMno(heartEntity.getMno());
			heartDTOS.add(heartDTO);
		}
		return heartDTOS;
	}
}
