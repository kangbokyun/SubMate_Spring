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
				Reader reader = new FileReader("C:/Users/강보균/Desktop/SubMate_Spring/src/main/resources/Data/TrainData.json");
//				Reader reader = new FileReader("C:/Users/bk940/IdeaProjects/SubMate_Spring/src/main/resources/Data/TrainData.json");
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
		List<String> overLapStartStation = new ArrayList<>();
		List<String> overLapEndStation = new ArrayList<>();

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
					double a = Double.parseDouble(o1.getSlng()) + Double.parseDouble(o1.getSlat());
					double b = Double.parseDouble(o2.getSlng()) + Double.parseDouble(o2.getSlat());

					if(a > b) { return -1; } else {        return 1;       }
				}
			};
			Collections.sort(subWayStartLine, listSort);
			Collections.sort(subWayEndLine, listSort);

			for(int i = 0; i < subWayStartLine.size(); i++) {
				for(int j = 0 ; j < subWayEndLine.size(); j++) {
					if(subWayStartLine.get(i).getSname().equals(subWayEndLine.get(j).getSname())) { // 환승역 찾기
//                                        System.out.println("같다 : " + subWayStartLine.get(i).getSname());
						overLapStartStation.add(subWayStartLine.get(i).getSname());
					}
				}
			}
			// 중복되는 환승역을 찾음 / 환승역 <-> 시작역, 환승역 <-> 도착역 length 구해서 더하고 비교하기
			int startCnt = 0;
			int endCnt = 0;
			for(int i = 0; i < subWayStartLine.size(); i++) {
				if(subWayStartLine.get(i).getSname().equals(mateDTO.getMatestartstationname())) {
					startCnt = i;
				}
			}
			for(int i = 0; i < subWayEndLine.size(); i++) {
				if(subWayEndLine.get(i).getSname().equals(mateDTO.getMateendstationname())) {
					endCnt = i;
				}
			}
			String[] startArr = new String[overLapStartStation.size()];
			String[] endArr = new String[overLapStartStation.size()];
			for(int i = 0; i < subWayStartLine.size(); i++) { // 시작역부터 환승역까지의 역 갯수 카운트
				for(int j = 0; j < overLapStartStation.size(); j++) {
					if (subWayStartLine.get(i).getSname().equals(overLapStartStation.get(j))) {
						startArr[j] = (startCnt - i) + "_" + subWayStartLine.get(i).getSname();
					}
				}
			}
			for(int i = 0; i < subWayEndLine.size(); i++) { // 도착역부터 환승역까지의 역 갯수 카운트
				for(int j = 0; j < overLapStartStation.size(); j++) {
					if(subWayEndLine.get(i).getSname().equals(overLapStartStation.get(j))) {
						endArr[j] = (endCnt - i) + "_" + subWayEndLine.get(i).getSname();
					}
				}
			}
			for(int i = 0; i < overLapStartStation.size(); i++) { // 시작/도착 역부터 환승역까지의 역 갯수별 정렬
				for(int j = 0; j < overLapStartStation.size(); j++) {
					if(Integer.parseInt(startArr[i].split("_")[0]) < Integer.parseInt(startArr[j].split("_")[0])) {
						String temp = startArr[i];
						startArr[i] = startArr[j];
						startArr[j] = temp;
					}
					if(Integer.parseInt(endArr[i].split("_")[0]) < Integer.parseInt(endArr[j].split("_")[0])) {
						String temp = endArr[i];
						endArr[i] = endArr[j];
						endArr[j] = temp;
					}
				}
			}
			List<Integer> wayCnt = new ArrayList<>();
			for(int i = 0 ; i < overLapStartStation.size(); i++) { // 최단경로 환승역
				wayCnt.add(Integer.parseInt(startArr[i].split("_")[0]) + Integer.parseInt(endArr[i].split("_")[0]));
			}
			int startStation = 0;
			int startTransferStation = 0;
			int endStation = 0;
			int endTransferStation = 0;
			if(wayCnt.get(0) < wayCnt.get(1)) { // 출발역부터 환승역까지
//                          System.out.println(subWayStartLine + "\n");
				for(int i = 0; i < subWayStartLine.size(); i++) {
					if(subWayStartLine.get(i).getSname().equals(mateDTO.getMatestartstationname())) { startStation = i; } // 출발역
					if(subWayStartLine.get(i).getSname().equals(startArr[0].split("_")[1])) { startTransferStation = i; } // 출발환승역
				}
				for(int i = 0; i < subWayEndLine.size(); i++) {
					if(subWayEndLine.get(i).getSname().equals(mateDTO.getMateendstationname())) { endStation = i; } // 도착역
					if(subWayEndLine.get(i).getSname().equals(endArr[0].split("_")[1])) { endTransferStation = i; } // 도착환승역
				}
				for(int i = startStation; i >= startStation - (startStation - startTransferStation); i--) { // 출발역부터 환승역까지
					System.out.println("subWayStartLine.get(i).getSname() : " + subWayStartLine.get(i).getSname());
				}
				for(int i = endTransferStation - 1; i >= endTransferStation - (endTransferStation - endStation); i--) { // 환승역 다음 역부터 도착역까지
					System.out.println("subWayEndLine.get(i).getSname() : " + subWayEndLine.get(i).getSname());
				}
			}
		}
	}
}
