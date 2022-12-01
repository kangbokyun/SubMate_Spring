package SubMate.Service;

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
		if(subWayEntities.size() != 0) {
			for(SubWayEntity subWayEntity : subWayEntities) {
				if(subWayEntity.getSname().equals(mateDTO.getMateendstation())) {
					System.out.println("subWayEntity : " + subWayEntity);

				}
			}
		}
	}
}
