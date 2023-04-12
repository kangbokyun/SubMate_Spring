package SubMate.Service;

import SubMate.Domain.DTO.*;
import SubMate.Domain.Entity.*;
import SubMate.Domain.Repository.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.*;

@Service
public class SettingService {
	@Autowired
	SubWayRepository subWayRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	MateRepository mateRepository;
	@Autowired
	ProfileRepository profileRepository;
	@Autowired
	HeartRepository heartRepository;
	@Autowired
	QnARepository qnARepository;
	@Autowired
	TendinousRepository tendinousRepository;
	@Autowired
	ProfileTalkRepository profileTalkRepository;

	public boolean SubStation() {
		List<SubWayEntity> subWayEntities = subWayRepository.findAll();
		if(subWayEntities.size() == 0) {
			System.out.println("subWayEntities : " + subWayEntities);
			JSONParser jsonParser = new JSONParser();

			try {
				// 파일 읽기
//				Reader reader = new FileReader("C:/Users/강보균/Desktop/SubMate_Spring/src/main/resources/Data/TrainData.json");
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
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean SearchStation(MateDTO mateDTO) { // 지하철 역 경로 찾는 로직 및 데이터 수정해야 됨
		List<SubWayEntity> subWayEntities = subWayRepository.findAll();
		List<SubWayDTO> subWayStartLine = new ArrayList<>();
		List<SubWayDTO> subWayEndLine = new ArrayList<>();
		List<String> overLapStartStation = new ArrayList<>();
		String line = "";

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
				for(int i = 0; i < subWayStartLine.size(); i++) {
					if(subWayStartLine.get(i).getSname().equals(mateDTO.getMatestartstationname())) { startStation = i; } // 출발역
					if(subWayStartLine.get(i).getSname().equals(startArr[0].split("_")[1])) { startTransferStation = i; } // 출발환승역
				}
				for(int i = 0; i < subWayEndLine.size(); i++) {
					if(subWayEndLine.get(i).getSname().equals(mateDTO.getMateendstationname())) { endStation = i; } // 도착역
					if(subWayEndLine.get(i).getSname().equals(endArr[0].split("_")[1])) { endTransferStation = i; } // 도착환승역
				}
				for(int i = startStation; i >= startStation - (startStation - startTransferStation); i--) { // 출발역부터 환승역까지
//					System.out.println("subWayStartLine.get(i).getSname() : " + subWayStartLine.get(i).getSname());
					line += subWayStartLine.get(i).getSname() + ", ";
				}
				for(int i = endTransferStation - 1; i >= endTransferStation - (endTransferStation - endStation); i--) { // 환승역 다음 역부터 도착역까지
//					System.out.println("subWayEndLine.get(i).getSname() : " + subWayEndLine.get(i).getSname());
					line += subWayEndLine.get(i).getSname() + ", ";
				}
				System.out.println("line : " + line);
			}

			List<MateEntity> mateEntityCheck = mateRepository.findAll();
			boolean flag = true;
			if(mateEntityCheck.size() != 0) {
				for(MateEntity mateEntity : mateEntityCheck) {
					if(mateDTO.getMno() == mateEntity.getMemberEntity().getMno()) {
						flag = true;
						break;
					} else {
						flag = false;
					}
				}
			} else {
				flag = false;
			}
			if(flag) { // 설정한 값이 이미 있음
				MateEntity mateEntity = mateRepository.findByMemberEntity_Mno(mateDTO.getMno());
				mateEntity.setMategwst(mateDTO.getMategwst());
				mateEntity.setMategwet(mateDTO.getMategwet());
				mateEntity.setMatelwst(mateDTO.getMatelwst());
				mateEntity.setMatelwet(mateDTO.getMatelwet());
				mateEntity.setMatestartstation(mateDTO.getMatestartstation());
				mateEntity.setMatestartstationname(mateDTO.getMatestartstationname());
				mateEntity.setMateendstation(mateDTO.getMateendstation());
				mateEntity.setMateendstationname(mateDTO.getMateendstationname());
				mateEntity.setMatetline(line);
				mateRepository.save(mateEntity);
				return true;
			} else {
				MemberEntity memberEntity = memberRepository.findById(mateDTO.getMno()).get();
				MateEntity mateEntity = MateEntity.builder()
					.mategwst(mateDTO.getMategwst())
					.mategwet(mateDTO.getMategwet())
					.matelwst(mateDTO.getMatelwst())
					.matelwet(mateDTO.getMatelwet())
					.matestartstation(mateDTO.getMatestartstation())
					.mateendstation(mateDTO.getMateendstation())
					.matestartstationname(mateDTO.getMatestartstationname())
					.mateendstationname(mateDTO.getMateendstationname())
					.memberEntity(memberEntity).matetline(line)
					.build();
				mateRepository.save(mateEntity);
				return true;
			}
		}
		return false;
	}

	// OpenAPI 1 ~ 8 호선 역사 좌표
	public void getTrainInfo() {
//		String[] line1 = {"당고개", "상계", "노원", "창동", "쌍문", "수유", "미아", "미아삼거리", "길음", "성신여대입구", "한성대입구", "혜화", "동대문", "동대문역사문화공원", "충무로", "명동", "회현", "서울역", "숙대입구", "삼각지", "신용산", "이촌", "동작", "이수","사당", "남태령", "경마공원", "과천", "정부과천청사", "인덕원", "평촌", "범계", "금정", "산본", "수리산", "대야미", "반월", "상록수", "한대앞", "중앙", "고잔", "공단", "안산", "신길온천", "정왕", "오이도"};

		String[] line2 = {"시청", "을지로입구", "을지로3가", "을지로4가", "동대문역사문화공원", "신당", "상왕십리", "왕십리", "한양대", "뚝섬", "성수", "건대입구", "구의", "강변", "잠실나루", "잠실", "신천", "종합운동장", "삼성", "선릉", "역삼", "강남", "교대", "서초", "방배", "사당", "낙성대", "서울대입구", "봉천", "신림", "신대방", "구로디지털단지", "대림", "신도림", "문래", "영등포구청", "당산" , "합정", "홍대입구", "신촌", "이대", "아현", "충정로", "시청"};
		String[] line3 = {"대화", "주엽", "정발산", "마두", "백석", "대곡", "화정", "원당", "삼송", "지축", "구파발", "연신내", "불광", "녹번", "홍제", "무악재", "독립문", "경복궁", "안국", "종로3가", "을지로3가", "충무로", "동대입구", "약수", "금호", "옥수", "압구정", "신사", "잠원", "고속터미널", "교대", "남부터미널", "양재", "매봉", "도곡", "대치", "학여울", "대청", "일원", "수서", "가락시장", "경찰병원", "오금"};
		String[] line4 = {"당고개", "상계", "노원", "창동", "쌍문", "수유", "미아", "미아삼거리", "길음", "성신여대입구", "한성대입구", "혜화", "동대문", "동대문역사문화공원", "충무로", "명동", "회현", "서울역", "숙대입구", "삼각지", "신용산", "이촌", "동작", "이수","사당", "남태령", "경마공원", "과천", "정부과천청사", "인덕원", "평촌", "범계", "금정", "산본", "수리산", "대야미", "반월", "상록수", "한대앞", "중앙", "고잔", "공단", "안산", "신길온천", "정왕", "오이도"};
		String[] line6 = {"응암", "역촌", "불광", "독바위", "연신내", "구산", "응암", "새절", "증산", "디지털미디어시티", "월드컵경기장", "마포구청", "망원", "합정", "상수", "광흥창", "대흥", "공덕", "효창공원앞", "삼각지", "녹사평", "이태원", "한강진", "버티고개", "약수", "청구", "신당", "동묘앞", "창신", "보문", "안암", "고려대", "월곡", "상월곡", "돌곶이", "석계", "태릉입구", "화랑대", "봉화산"};
		String[] line7 = {"장암", "도봉산", "수락산", "마들", "노원", "중계", "하계", "공릉", "태릉입구", "먹골", "중화", "상봉", "면목", "사가정", "용마산", "중곡", "군자", "어린이대공원", "건대입구", "뚝섬유원지", "청담", "강남구청", "학동", "논현", "반포", "고속터미널", "내방", "이수", "남성", "숭실대입구", "상도", "장승배기", "신대방삼거리", "보라매", "신풍", "대림", "남구로", "가산디지털단지", "철산", "광명사거리", "천왕", "온수", "까치울", "부천종합운동장", "춘의", "신중동", "부천시청", "상동", "삼산체육관", "굴포천", "부평구청"};
		String[] line8 = {"암사", "천호", "강동구청", "몽촌토성", "잠실", "석촌", "송파", "가락시장", "문정", "장지", "복정", "산성", "남한산성입구", "단대오거리", "신흥", "수진", "모란"};
		String[] line9 = {"개화", "김포공항", "공항시장", "신방화", "양천향교", "가양", "증미", "등촌", "염창", "신목동", "선유도", "당산", "국회의사당", "여의도", "샛강", "노량진", "노들", "흑석", "동작", "구반포", "신반포", "고속터미널", "사평", "신논현", "언주", "선정릉", "삼성중앙", "봉은사", "종합운동장"};
		String[] newBundang = {"강남", "양재", "양재시민의숲", "청계산입구", "판교", "정자", "미금", "동천", "수지구청", "성복", "상현", "광교중앙", "광교"};

		청량리 왕십리 서울숲 압구정로데오 강남구청 선정릉 선릉 한티 도곡 구룡 개포동 대모산입구 복정 가천대 태평 모란 야탑 이매 서현 수내 정자 미금 오리 죽전 보정 구성 신갈 기흥 상갈 청명 영통 망포 매탄권선 수원시청 매교 수원 고색 오목천 어천 야목 사리 한대앞 중앙</b>-<b>고잔</b>-<b>초지</b>-<b>안산</b>-<b>신길온천</b>-<b>정왕</b><b>-오이도</b>-달월-월곶-소래포구-인천논현-호구포-남동인더스파크-<b>원인재</b>-연수-송도-인하대-숭의-신포-<b>인천</b></p>

		List<Map<String, String>> line_4 = new ArrayList<>();
		try {
			List<SubWayDTO> subWayDTOS = new ArrayList<>();
			String[][] trainAPIData = new String[][] {
					/* 1호선 */{"15041300", "90c8cf16-7bc9-42a4-a219-9a54f47768ed"}, /* 2호선 */{"15041301", "3ecd8bc2-34ea-4860-a788-bf2578754ad9"},
					/* 3호선 */{"15041302", "e654fca8-d6d5-4977-bf0d-a4ebea52d5b6"}, /* 4호선 */{"15041303", "c49053c3-6900-46c9-9615-cf5cc51c0dcc"},
					/* 5호선 */{"15041304", "8717c1fd-0d93-465e-93fb-f34dda9612d5"}, /* 6호선 */{"15041305", "e405b333-40e8-44f3-b918-d0fd7e7dd7b4"},
					/* 7호선 */{"15041306", "25014d49-e302-4157-a95d-b09a383a4774"}, /* 8호선 */{"15041334", "d7bacdad-8a49-4d47-8d96-b3a226db4efc"},
					/* 9호선 */{"15041335", "515ee279-c88f-47cc-9f94-a4dac970894c"}, /* 인천1호선 */{"15041338", "a7d7eb6c-dd76-44ad-8132-a9c41880a9b0"},
					/* 인천2호선 */{"15041339", "f9119403-9bad-4a3e-94e0-dfaddfd48f36"}, /* 수인분당 */{"15041336", "97b7dfab-9734-4b79-8b18-e5ef46f2cc12"},
					/* 신분당선 */{"15041337", "256d7380-e813-4155-a286-bdcf306f111c"}
			};
			for(int a = 0; a < trainAPIData.length; a++) {
				System.out.println("trainAPIData : " + trainAPIData.length + " : " + a);
				String otherLines = "https://api.odcloud.kr/api/" + trainAPIData[a][0] + "/v1/uddi:" + trainAPIData[a][1] + "?page=1&perPage=100&serviceKey=HECNY5QN6CQD7qu%2BwHHkPELwmRonryzFFC%2F19jU5jFMu9pT34DW66NlYrxVUnw%2BwBtTQvt7RxBbPo3RR9Y9pQQ%3D%3D";
				URL url = new URL(otherLines);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
				String resultBuffer = bufferedReader.readLine();

				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(resultBuffer);
				JSONArray dataArray = (JSONArray) jsonObject.get("data");

				jsonObject = (JSONObject) jsonParser.parse(resultBuffer);
				dataArray = (JSONArray) jsonObject.get("data");
				for(int i = 0; i < dataArray.size(); i++) {
					SubWayDTO subWayDTO = new SubWayDTO();
					subWayDTO.setSname(dataArray.get(i).toString().split("역명\":\"")[1].split("\",\"")[0]); // 역명
					if(dataArray.get(i).toString().split("경도\":")[1].split(",\"")[0].equals("null")) {
						for(int j = 0; j < subWayDTOS.size(); j++) {
							if(subWayDTOS.get(j).getSname().equals(subWayDTO.getSname())) { subWayDTO.setSlng(subWayDTOS.get(j).getSlng()); break;
							} else { subWayDTO.setSlng("0"); }
						}
					} else {
						subWayDTO.setSlng(dataArray.get(i).toString().split("경도\":\"")[1].split("\",\"")[0]); // 경도
					}
					if(dataArray.get(i).toString().split("위도\":")[1].split(",\"")[0].equals("null")) {
						for(int j = 0; j < subWayDTOS.size(); j++) {
							if(subWayDTOS.get(j).getSname().equals(subWayDTO.getSname())) { subWayDTO.setSlat(subWayDTOS.get(j).getSlat()); break;
							} else { subWayDTO.setSlat("0"); }
						}
					} else {
						subWayDTO.setSlat(dataArray.get(i).toString().split("위도\":\"")[1].split("\",\"")[0]); // 위도
					}
					subWayDTO.setSline(dataArray.get(i).toString().split("선명\":\"")[1].split("\"}")[0]); // 호선
					subWayDTOS.add(subWayDTO);
					if(a == 8 && i == 28) { break; }
				}
			}

			System.out.println("ㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜ\n" + subWayDTOS + "\nㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗ");
			System.out.println("ㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜ\n" + subWayDTOS.size() + "\nㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗ");
		} catch(Exception e) {
			System.out.println("getTrainInfo_Exception : " + e.getMessage());
		}
	}

	public List<MemberDTO> MateData(int mno) {
		List<MateEntity> mateEntities = mateRepository.findAll();
		List<MemberDTO> memberDTOS = new ArrayList<>();

		MateEntity mateEntity = mateRepository.findByMemberEntity_Mno(mno);
		if(mateEntity == null) {
			return memberDTOS;
		}
		String[] oneLine = mateEntity.getMatetline().split(", ");

		List<HeartEntity> heartEntities = heartRepository.findAll();
		List<HeartDTO> heartDTOS = new ArrayList<>();
		for(HeartEntity heartEntity : heartEntities) {
			if(heartEntity.getUserno() != null) {
				HeartDTO heartDTO = HeartDTO.builder()
					.hno(heartEntity.getHno()).hkind(heartEntity.getHkind()).htype(heartEntity.getHtype())
					.userno(heartEntity.getUserno()).mno(heartEntity.getMno()).build();
				heartDTOS.add(heartDTO);
			}
		}

		for(MateEntity mate : mateEntities) {
			int heartCnt = 0;
			int overLabCnt = 0;
			if(mate.getMateno() != mateEntity.getMateno()) {
				MemberDTO memberDTO = new MemberDTO();
				for(int i = 0; i < oneLine.length; i++) {
					if(mate.getMatetline().contains(oneLine[i])) {
						// oneLine( 초지 )이 포함된 MateLine :
						// 초지, 고잔, 중앙, 한대앞, 상록수, 반월, 대야미, 수리산, 산본, 금정, 명학,
						// 안양, 관악, 석수, 오류동, 금천구청, 개봉, 독산, 가산디지털단지,
						// System.out.println("oneLine( " + oneLine[i] + " )이 포함된 MateLine : " + mate.getMatetline());
						overLabCnt++;
						if(overLabCnt > 2) {
							MemberEntity entity = memberRepository.findById(mate.getMemberEntity().getMno()).get();
							if(heartDTOS.size() != 0) {
								for(int j = 0; j < heartDTOS.size(); j++) {
									if(heartDTOS.get(j).getUserno() != null && Integer.parseInt(heartDTOS.get(j).getUserno()) == entity.getMno()) {
										heartCnt ++;
									}
								}
								for(int j = 0; j < heartDTOS.size(); j++) {
									if(Integer.parseInt(heartDTOS.get(j).getMno()) == mno && Integer.parseInt(heartDTOS.get(j).getUserno()) == entity.getMno()) {
										memberDTO.setHeartclicker("true");
										break;
									} else {
										memberDTO.setHeartclicker("false");
									}
								}
								memberDTO.setUserheartcnt(heartCnt + "");
								for (int j = 0; j < heartDTOS.size(); j++) {
									if (!memberDTO.getHeartclicker().equals("false") && Integer.parseInt(heartDTOS.get(j).getUserno()) == entity.getMno()) {
										memberDTO.setUserheart(heartDTOS.get(j).getHkind());
										break;
									} else {
										memberDTO.setUserheart(Integer.toString(Integer.parseInt(heartDTOS.get(j).getHkind()) + 1));
									}
								}
							} else {
								memberDTO.setUserheart("0");
							}
							memberDTO.setMno(entity.getMno());
							memberDTO.setMgender(entity.getMgender());
							memberDTO.setMager(entity.getMager());
							memberDTO.setMbirth(entity.getMbirth());
							memberDTO.setProfileimg(entity.getProfileimg().split("/MemberImg")[1]);
							memberDTO.setMnickname(entity.getMnickname());
							memberDTO.setMbti(entity.getMbti());
							memberDTO.setMname(entity.getMname());
							memberDTO.setMhobby(entity.getMhobby());
							memberDTO.setMphone(entity.getMphone());
							memberDTO.setPsetting(entity.getPsetting());
							memberDTOS.add(memberDTO);
							break;
						}
					}
				}
			}
		}

		return memberDTOS;
	}

	public boolean ProfileSetting(ProfileDTO profileDTO) {
		if(profileDTO != null) {
			MemberEntity memberEntity = memberRepository.findById(profileDTO.getMno()).get();
			ProfileEntity profileEntity = ProfileEntity.builder()
				.pintro(profileDTO.getPintro()).memberEntity(memberEntity)
				.plike1(profileDTO.getPlike1()).plike2(profileDTO.getPlike2()).plike3(profileDTO.getPlike3())
				.punlike1(profileDTO.getPunlike1()).punlike2(profileDTO.getPunlike2()).punlike3(profileDTO.getPunlike3())
				.phobby1(profileDTO.getPhobby1()).phobby2(profileDTO.getPhobby2()).phobby3(profileDTO.getPhobby3())
				.build();
			profileRepository.save(profileEntity);

			if(memberEntity.getMhobby() == null) {
				memberEntity.setMhobby(profileDTO.getPhobby1());
				List<ProfileEntity> profileEntities = profileRepository.findAll();
				for(ProfileEntity profileEntity1 : profileEntities) {
					if(profileEntity1.getMemberEntity().getMno() == memberEntity.getMno()) {
						memberEntity.setPsetting(Integer.toString(profileEntity1.getPno()));
					}
				}
				memberRepository.save(memberEntity);
			}

			return true;
		} else {
			return false;
		}
	}

	public List<ProfileDTO> UserProfile(int mno) {
		List<ProfileDTO> profileDTOS = new ArrayList<>();
//		ProfileEntity profileEntity = profileRepository.findByMemberEntity_mno(mno);
		List<ProfileEntity> profileEntities = profileRepository.findAll();
		for(ProfileEntity profileEntity : profileEntities) {
			if(profileEntity != null) {
				ProfileDTO profileDTO = new ProfileDTO();
				profileDTO.setMno(profileEntity.getMemberEntity().getMno());
				profileDTO.setPintro(profileEntity.getPintro());
				profileDTO.setPhobby1(profileEntity.getPhobby1());
				profileDTO.setPhobby2(profileEntity.getPhobby2());
				profileDTO.setPhobby3(profileEntity.getPhobby3());
				profileDTO.setPlike1(profileEntity.getPlike1());
				profileDTO.setPlike2(profileEntity.getPlike2());
				profileDTO.setPlike3(profileEntity.getPlike3());
				profileDTO.setPunlike1(profileEntity.getPunlike1());
				profileDTO.setPunlike2(profileEntity.getPunlike2());
				profileDTO.setPunlike3(profileEntity.getPunlike3());
				profileDTO.setPno(profileEntity.getPno());
				profileDTO.setChecknull("NOTNULL");
				profileDTOS.add(profileDTO);
			} else {
				ProfileDTO profileDTO = new ProfileDTO();
				profileDTO.setChecknull("NULL");
				profileDTOS.add(profileDTO);
			}
		}
		return profileDTOS;
	}

	public boolean QnA(QnADTO qnADTO) {
		if(qnADTO != null) {
			MemberEntity memberEntity = memberRepository.findById(qnADTO.getQnamno()).get();
			QnAEntity qnAEntity = QnAEntity.builder()
				.qnatitle(qnADTO.getQnatitle()).qnacontents(qnADTO.getQnacontents()).memberEntity(memberEntity)
				.build();
			qnARepository.save(qnAEntity);
		}
		return true;
	}

	public boolean Tendinous(TendinousDTO tendinousDTO) {
		if(tendinousDTO != null) {
			System.out.println("tendinousDTO.getMno() : " + tendinousDTO.getMno());
			Optional<MemberEntity> memberEntity = memberRepository.findById(tendinousDTO.getMno());
			System.out.println("memberEntity : " + memberEntity);
			TendinousEntity tendinousEntity = TendinousEntity.builder()
				.tcontents(tendinousDTO.getTcontents())
				.tselectcontentkind(tendinousDTO.getTselectcontentkind())
				.tselecttendinouskind(tendinousDTO.getTselecttendinouskind())
				.memberEntity(memberEntity.get()).build();
			tendinousRepository.save(tendinousEntity);
			return true;
		} else {
			return false;
		}
	}

	public boolean LineTalk(ProfileTalkDTO profileTalkDTO) {
		if(profileTalkDTO != null) {
			MemberEntity memberEntity = memberRepository.findById(profileTalkDTO.getMno()).get();
			ProfileTalkEntity profileTalkEntity = ProfileTalkEntity.builder()
				.ptcontents(profileTalkDTO.getPtcontents())
				.ptwriter(profileTalkDTO.getPtwriter())
				.writedmno(profileTalkDTO.getWritedmno())
				.memberEntity(memberEntity)
				.build();
			profileTalkRepository.save(profileTalkEntity);
			return true;
		} else {
			return false;
		}
	}

	public List<ProfileTalkDTO> TalkList(int mno) {
		List<ProfileTalkEntity> profileTalkEntities = profileTalkRepository.findAll();
		List<ProfileTalkDTO> profileTalkDTOS = new ArrayList<>();
		for(ProfileTalkEntity profileTalkEntity : profileTalkEntities) {
			if(mno == profileTalkEntity.getMemberEntity().getMno()) {
				ProfileTalkDTO profileTalkDTO = ProfileTalkDTO.builder()
					.mno(profileTalkEntity.getMemberEntity().getMno())
					.ptcontents(profileTalkEntity.getPtcontents())
					.ptno(profileTalkEntity.getPtno())
					.ptwriter(profileTalkEntity.getPtwriter())
					.writedmno(profileTalkEntity.getWritedmno())
					.build();
				profileTalkDTOS.add(profileTalkDTO);
			}
		}
		Comparator<ProfileTalkDTO> listSort = new Comparator<ProfileTalkDTO>() {
			@Override
			public int compare(ProfileTalkDTO o1, ProfileTalkDTO o2) {
				int a = o1.getPtno();
				int b = o2.getPtno();

				if(a > b) { return -1; } else { return 1; }
			}
		};
		Collections.sort(profileTalkDTOS, listSort);
		return profileTalkDTOS;
	}

	public List<TendinousDTO> SendedTendinous(int mno) {
		List<TendinousEntity> tendinousEntities = tendinousRepository.findAll();
		List<TendinousDTO> tendinousDTOS = new ArrayList<>();
		for(TendinousEntity tendinousEntity : tendinousEntities) {
			if(tendinousEntity.getMemberEntity().getMno() == mno) {
				TendinousDTO tendinousDTO = TendinousDTO.builder()
						.mno(tendinousEntity.getMemberEntity().getMno())
						.tno(tendinousEntity.getTno())
						.tcontents(tendinousEntity.getTcontents())
						.tstatus(Integer.toString(tendinousEntity.getTstatus()))
						.tselectcontentkind(tendinousEntity.getTselectcontentkind())
						.tselecttendinouskind(tendinousEntity.getTselecttendinouskind())
						.build();
				tendinousDTOS.add(tendinousDTO);
			}
		}
		return tendinousDTOS;
	}
}

