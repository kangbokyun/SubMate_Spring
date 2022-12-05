package SubMate.Service;

import SubMate.Domain.DTO.BoardDTO;
import SubMate.Domain.DTO.MateDTO;
import SubMate.Domain.DTO.SubWayDTO;
import SubMate.Domain.Entity.SubWayEntity;
import SubMate.Domain.Repository.SubWayRepository;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MateService {
	@Autowired
	SubWayRepository subWayRepository;

	public void SubStation() {
		List<SubWayEntity> subWayEntities = subWayRepository.findAll();
		System.out.println(subWayEntities.size());
		if(subWayEntities.size() == 0) {
			System.out.println("subWayEntities : " + subWayEntities);
			JSONParser jsonParser = new JSONParser();

			try {
				// 파일 읽기
	//			Reader reader = new FileReader("C:/Users/강보균/Desktop/SubMate_Spring/src/main/resources/Data/TrainData.json");
				Reader reader = new FileReader("C:/Users/bk940/IdeaProjects/SubMate_Spring/src/main/resources/Data/TrainData.json");
				JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

				for(int i = 0; i < jsonArray.size(); i++) {
					String array1 = jsonArray.toString().split("\\[\\{")[1];
					String array2 = array1.split("}]")[0];
					String[] array3 = array2.split("},\\{");

					// 추출 됨
					String trainLine = array3[i].split("\"line\":\"")[1].split("\",\"name")[0];
					String trainName = "";
					if(array3[i].equals("lat")) {
						trainName = array3[i].split("\"name\":\"")[1].split("\",\"lat\"")[0];
					} else {
						trainName = array3[i].split("\"name\":\"")[1].split("\"")[0];
					}
					String trainCode = "";
					if(array3[i].contains("lng")) {
						trainCode = array3[i].split("\"code\":")[1].split(",\"lng")[0];
					} else {
						trainCode = array3[i].split("\"code\":")[1].split(",\"line")[0];
					}
					String trainLng = "";
					if(array3[i].contains("lng")) {
						trainLng = array3[i].split("\"lng\":")[1].split(",\"line")[0];
					}
					String trainLat = "";
					if(array3[i].contains("lat")) {
						trainLat = array3[i].split("lat\":")[1];
					}

					SubWayEntity subWayEntity = SubWayEntity.builder()
						.sline(trainLine).scode(trainCode).slat(trainLat).slng(trainLng).sname(trainName).build();
					subWayRepository.save(subWayEntity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//	public List<SubWayDTO> SubWayKind() {
//		List<SubWayEntity> subWayEntities = subWayRepository.findAll();
//		List<SubWayDTO> subWayDTOS = new ArrayList<>();
//		if(subWayEntities.size() != 0) {
//			for(SubWayEntity subWayEntity : subWayEntities) {
//				SubWayDTO subWayDTO = new SubWayDTO();
//				subWayDTO.setSline(subWayEntity.getSline());
//				if(subWayEntity.getSline().equals())
//			}
//		}
//	}

	public void SearchStation(MateDTO mateDTO) {
		List<SubWayEntity> subWayEntities = subWayRepository.findAll();
		List<SubWayDTO> subWayStartLine = new ArrayList<>();
		List<SubWayDTO> subWayEndLine = new ArrayList<>();
		if(mateDTO.getMatestartstation() == null) { mateDTO.setMatestartstation("01호선");
		} else if(mateDTO.getMateendstation() == null) { mateDTO.setMateendstation("01호선");
		} else if(mateDTO.getMatestartstation() == null && mateDTO.getMateendstation() == null) {
			mateDTO.setMatestartstation("01호선"); mateDTO.setMateendstation("01호선");
		}
		if(subWayEntities.size() != 0) {
			for(SubWayEntity subWayEntity : subWayEntities) {
				if(mateDTO.getMatestartstation().equals(subWayEntity.getSline())) {
					SubWayDTO subWayDTO = new SubWayDTO();
					subWayDTO.setSno(Integer.toString(subWayEntity.getSno()));
					subWayDTO.setSline(subWayEntity.getSline());
					subWayDTO.setSlng(subWayEntity.getSlng());
					subWayDTO.setSlat(subWayEntity.getSlat());
					subWayDTO.setSname(subWayEntity.getSname());
					subWayDTO.setScode(subWayEntity.getScode());

					subWayStartLine.add(subWayDTO);
				}
				if(mateDTO.getMateendstation().equals(subWayEntity.getSline())) {
					SubWayDTO subWayDTO = new SubWayDTO();
					subWayDTO.setSno(Integer.toString(subWayEntity.getSno()));
					subWayDTO.setSline(subWayEntity.getSline());
					subWayDTO.setSlng(subWayEntity.getSlng());
					subWayDTO.setSlat(subWayEntity.getSlat());
					subWayDTO.setSname(subWayEntity.getSname());
					subWayDTO.setScode(subWayEntity.getScode());

					subWayEndLine.add(subWayDTO);
				}
			}

			Comparator<SubWayDTO> listSort = new Comparator<SubWayDTO>() {
				@Override
				public int compare(SubWayDTO o1, SubWayDTO o2) {
					double a = Double.parseDouble(o1.getSlng());
					double b = Double.parseDouble(o2.getSlng());

					if(a > b) { return -1; } else {	return 1;	 }
				}
			};
			Collections.sort(subWayStartLine, listSort);
			Collections.sort(subWayEndLine, listSort);

			for(int i = 0; i < subWayStartLine.size(); i++) {
				for(int j = 0 ; j < subWayEndLine.size(); j++) {
					if(subWayStartLine.get(i).getSname().equals(subWayEndLine.get(j).getSname())) {
						System.out.println("같다 : " + subWayStartLine.get(i).getSname());
					}
				}
			}
		}
	}
}
