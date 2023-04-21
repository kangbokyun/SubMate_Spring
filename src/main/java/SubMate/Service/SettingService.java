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

//	public boolean SubStation() {
//		List<SubWayEntity> subWayEntities = subWayRepository.findAll();
//		if(subWayEntities.size() == 0) {
//			System.out.println("subWayEntities : " + subWayEntities);
//			JSONParser jsonParser = new JSONParser();
//
//			try {
//				// 파일 읽기
////				Reader reader = new FileReader("C:/Users/강보균/Desktop/SubMate_Spring/src/main/resources/Data/TrainData.json");
//				Reader reader = new FileReader("C:/Users/bk940/IdeaProjects/SubMate_Spring/src/main/resources/Data/TrainData.json");
//				JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
//
//				for(int i = 0; i < jsonArray.size(); i++) {
//					String array1 = jsonArray.toString().split("\\[\\{")[1];
//					String array2 = array1.split("}]")[0];
//					String[] array3 = array2.split("},\\{");
//
//					// 추출 됨
//					String trainLine = array3[i].split("\"line\":\"")[1].split("\",\"name")[0];
//					String trainName = "";
//					if(array3[i].equals("lat")) {
//						trainName = array3[i].split("\"name\":\"")[1].split("\",\"lat\"")[0];
//					} else {
//						trainName = array3[i].split("\"name\":\"")[1].split("\"")[0];
//					}
//					String trainCode = "";
//					if(array3[i].contains("lng")) {
//						trainCode = array3[i].split("\"code\":")[1].split(",\"lng")[0];
//					} else {
//						trainCode = array3[i].split("\"code\":")[1].split(",\"line")[0];
//					}
//					String trainLng = "";
//					if(array3[i].contains("lng")) {
//						trainLng = array3[i].split("\"lng\":")[1].split(",\"line")[0];
//					}
//					String trainLat = "";
//					if(array3[i].contains("lat")) {
//						trainLat = array3[i].split("lat\":")[1];
//					}
//
//					SubWayEntity subWayEntity = SubWayEntity.builder()
//						.sline(trainLine).scode(trainCode).slat(trainLat).slng(trainLng).sname(trainName).build();
//					subWayRepository.save(subWayEntity);
//				}
//				return true;
//			} catch (Exception e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//		return true;
//	}

	public boolean SearchStation(MateDTO mateDTO) { // 지하철 역 경로찾기
		System.out.println("mateDTO : " + mateDTO);
		if(mateDTO.getMateendstation().startsWith("0")) { mateDTO.setMateendstation(mateDTO.getMateendstation().substring(1)); }
		if(mateDTO.getMatestartstation().startsWith("0")) { mateDTO.setMatestartstation(mateDTO.getMatestartstation().substring(1)); }

		if(!mateDTO.getMatestartstation().equals(mateDTO.getMateendstation())) { // 환승역이 존재
			int cntStartStation = 0;/* 동일 노선 배열 중 출발 역이 몇번째인지 */ int cntEndStation = 0; /* 동일 노선 배열 중 도착 역이 몇번째인지 */
			int startPlus = 0; /* 출발역과 환승역까지의 거리 */ int endPlus = 0; /* 도착역과 환승역까지의 거리 */
			Map<Integer, String> startTempMap = new HashMap<>();
			Map<Integer, String> endTempMap = new HashMap<>();
			List<SubWayEntity> startSubWayEntities = subWayRepository.findBySlineOrderByScodeAsc(mateDTO.getMatestartstation());
			List<SubWayEntity> endSubWayEntities = subWayRepository.findBySlineOrderByScodeAsc(mateDTO.getMateendstation());

			// 받아온 역(출발/도착)의 배열 내 순서 가져오기
			for(int i = 0; i < startSubWayEntities.size(); i++) {
				for(int j = 0; j < endSubWayEntities.size(); j++) {
					startSubWayEntities.get(i).setSname(startSubWayEntities.get(i).getSname().split("\\(")[0]);
					endSubWayEntities.get(j).setSname(endSubWayEntities.get(j).getSname().split("\\(")[0]);
					if(startSubWayEntities.get(i).getSname().equals(mateDTO.getMatestartstationname())) { cntStartStation = i; }
					if(endSubWayEntities.get(j).getSname().equals(mateDTO.getMateendstationname())) { cntEndStation = j; }
				}
			}
			// 받아온 역의 역 별 노선에 겹치는 역 찾기(몽가 문제가 있음)
			for(int i = 0; i < startSubWayEntities.size(); i++) {
				for(int j = 0; j < endSubWayEntities.size(); j++) {
					if(startSubWayEntities.get(i).getSname().equals(endSubWayEntities.get(j).getSname())) {
						 endPlus = cntEndStation - j;
						System.out.println("Same >>>>>>>>>>>>>>>\n" + startSubWayEntities.get(i).getSname() + " ::: " + endSubWayEntities.get(j).getSname() + "\n<<<<<<<<<<<<<<<<<<<<");
						if(startSubWayEntities.get(i).getScode().contains("-") && Integer.parseInt(startSubWayEntities.get(i).getScode().split("-")[1]) > 20) {
							startPlus = cntStartStation - i - 20;
							if(startPlus < 0) { startPlus = startPlus * (-1); }
							startTempMap.put(startPlus, startSubWayEntities.get(i).getSname());
							System.out.println("1 : " + startPlus);
						} else {
							startPlus = cntStartStation - i;
							if(startPlus < 0) { startPlus = startPlus * (-1); }
							startTempMap.put(startPlus, startSubWayEntities.get(i).getSname());
							System.out.println("2 : " + startPlus);
						}

						if(endPlus < 0) { endPlus = endPlus * (-1); }

						endTempMap.put(endPlus, endSubWayEntities.get(j).getSname());
						System.out.println("3 : " + endPlus);
					}
				}
			}

			// 겹치는 역 중 가장 가까운 역 찾기
			Map<Integer, String> transferLine = new HashMap<>();
			List<Integer> sortingStartMap = new ArrayList<>(startTempMap.keySet());
			List<Integer> sortingEndMap = new ArrayList<>(endTempMap.keySet());
			sortingStartMap.sort((s1, s2) -> s1.compareTo(s2));
			sortingEndMap.sort((s1, s2) -> s1.compareTo(s2));
			for(int i = 0; i < sortingStartMap.size(); i++) {
				transferLine.put(sortingStartMap.get(i) + sortingEndMap.get(i), startTempMap.get(sortingStartMap.get(i)));
			}
			List<Integer> transferLineMap = new ArrayList<>(transferLine.keySet());
			transferLineMap.sort((s1, s2) -> s1.compareTo(s2));
			System.out.println("startTempMap : " + startTempMap + " : " + "endTempMap : " +  endTempMap);
			System.out.println("환승역" + transferLine.get(transferLineMap.get(0)));

			// 출발역과 환승역 사이의 역
			int startStationI = 0; /* 출발역 */ int transferStationI = 0; /* 환승역 */
			for(int i = 0; i < startSubWayEntities.size(); i++) {
				if(mateDTO.getMatestartstationname().equals(startSubWayEntities.get(i).getSname())) { startStationI = i; }
				if(transferLine.get(transferLineMap.get(0)).equals(startSubWayEntities.get(i).getSname())) { transferStationI = i; }
			}
			for(int i = 0; i < startSubWayEntities.size(); i++) {
				if(startStationI != 0 && transferStationI != 0) {
					if(startStationI > transferStationI) {
						if(i >= transferStationI && i <= startStationI) {
							if(startSubWayEntities.get(transferStationI).getScode().contains("-") && Integer.parseInt(startSubWayEntities.get(transferStationI).getScode().split("-")[1]) > 20) {
								if(startSubWayEntities.get(i).getSline().equals("1호선") && startSubWayEntities.get(i).getScode().contains("-") && Integer.parseInt(startSubWayEntities.get(i).getScode().split("-")[1]) > 20) {
									System.out.println("@@5 " + startSubWayEntities.get(i).getSname());
								} else {
									System.out.println("@@6 " + startSubWayEntities.get(i).getSname());
								}
							} else {
								if(startSubWayEntities.get(i).getSline().equals("1호선") && startSubWayEntities.get(i).getScode().contains("-") && Integer.parseInt(startSubWayEntities.get(i).getScode().split("-")[1]) > 20) {
									System.out.println("@@7 " + startSubWayEntities.get(i).getSname());
								} else {
									System.out.println("@@8 " + startSubWayEntities.get(i).getSname());
								}
							}
						}
					} else {
						if(i <= transferStationI && i >= startStationI) {
							if(startSubWayEntities.get(transferStationI).getScode().contains("-") && Integer.parseInt(startSubWayEntities.get(transferStationI).getScode().split("-")[1]) > 20) {
								if(startSubWayEntities.get(i).getSline().equals("1호선") && startSubWayEntities.get(i).getScode().contains("-") && Integer.parseInt(startSubWayEntities.get(i).getScode().split("-")[1]) > 20) {
									System.out.println("@@1 " + startSubWayEntities.get(i).getSname());
								} else {
									System.out.println("@@3 " + startSubWayEntities.get(i).getSname());
								}
							} else {
								if(startSubWayEntities.get(i).getSline().equals("1호선") && startSubWayEntities.get(i).getScode().contains("-") && Integer.parseInt(startSubWayEntities.get(i).getScode().split("-")[1]) > 20) {
									System.out.println("@@2 " + startSubWayEntities.get(i).getSname());
								} else {
									System.out.println("@@4 " + startSubWayEntities.get(i).getSname());
								}
							}
						}
					}
				}
			}

			// 환승역과 도착역 사이의 역
			int endStationI = 0; /* 도착역 */ int transferStationII = 0; /* 환승역 */
			for(int i = 0; i < endSubWayEntities.size(); i++) {
				if(mateDTO.getMateendstationname().equals(endSubWayEntities.get(i).getSname())) { endStationI = i; }
				if(transferLine.get(transferLineMap.get(0)).equals(endSubWayEntities.get(i).getSname())) { transferStationII = i; }
			}
			for(int i = 0; i < endSubWayEntities.size(); i++) {
				if(endStationI != 0 && transferStationII != 0) {
					if(endStationI > transferStationII) {
						if(i >= transferStationII && i <= endStationI) {
							if(endSubWayEntities.get(i).getSline().equals("1호선")) {
								if(endSubWayEntities.get(transferStationII).getScode().contains("-")) {
									int linePoint = endSubWayEntities.get(i).getScode().length() - endSubWayEntities.get(i).getScode().replace("-", "").length();
									int endPoint = endSubWayEntities.get(endStationI).getScode().length() - endSubWayEntities.get(endStationI).getScode().replace("-", "").length();
									if(endPoint + 1 == linePoint && endPoint == 0) {
										System.out.println(">> " + endSubWayEntities.get(i).getSname() + "\n>> " + endPoint + " : " + linePoint);
									} else if (endPoint == linePoint && endPoint == 1) {
										System.out.println(">>> " + endSubWayEntities.get(i).getSname() + "\n>>> " + endPoint + " : " + linePoint);
									} else if (endPoint - 1 == linePoint || endPoint == 2) {
										System.out.println(">>>> " + endSubWayEntities.get(i).getSname() + "\n>>>> " + endPoint + " : " + linePoint);
									}
								}
							} else {
								System.out.println(">>>>>1 " + endSubWayEntities.get(i).getSname());
							}
						}
					} else {
						if(i <= transferStationII && i >= endStationI) {
							// 도착역이 1호선이면서 환승역 코드에 '-'가 포함되어 있을 때(분리되는 노선)
							if(endSubWayEntities.get(i).getSline().equals(mateDTO.getMateendstation()) && mateDTO.getMateendstation().equals("1호선") && endSubWayEntities.get(transferStationII).getScode().contains("-")) {
								// 환승역 '-' 역코드가 20이 넘어가는지 확인(20을 기준으로 선로가 두개로 갈라짐)
								if(Integer.parseInt(endSubWayEntities.get(transferStationII).getScode().split("-")[1]) > 20) {
									// 조건에 맞는 값의 역코드에 '-'이 포함되어 있고, '-' 이후 역코드가 20이 넘거나, '-'이 역코드에 없는 경우
									// (ex. 환승역 금정, 도착역 구로 = 금정 -> 가산디지털단지(여기까지는 '-'이 붙은 역코드를 가짐) -> 구로('-'이 역코드에 없음)를 가야하기 때문)
									if(endSubWayEntities.get(i).getScode().contains("-") && Integer.parseInt(endSubWayEntities.get(i).getScode().split("-")[1]) > 20 || !endSubWayEntities.get(i).getScode().contains("-")) {
										int linePoint = endSubWayEntities.get(i).getScode().length() - endSubWayEntities.get(i).getScode().replace("-", "").length();
										int endPoint = endSubWayEntities.get(endStationI).getScode().length() - endSubWayEntities.get(endStationI).getScode().replace("-", "").length();
										if(endPoint + 1 == linePoint && endPoint == 0) {
											System.out.println(">> " + endSubWayEntities.get(i).getSname() + "\n>> " + endPoint + " : " + linePoint);
										} else if (endPoint == linePoint && endPoint == 1) {
											System.out.println(">>> " + endSubWayEntities.get(i).getSname() + "\n>>> " + endPoint + " : " + linePoint);
										} else if (endPoint - 1 == linePoint || endPoint == 2) {
											System.out.println(">>>> " + endSubWayEntities.get(i).getSname() + "\n>>>> " + endPoint + " : " + linePoint);
										}
									}
								}
							} else {
								System.out.println(endSubWayEntities.get(i).getSname() + " <<");
							}
						}
					}
				}
			}

//			System.out.println("Same >>>>>>>>>>>>>>>\n" + cntStartStationLine + "\n<<<<<<<<<<<<<<<<<<<<");
//			System.out.println("Same >>>>>>>>>>>>>>>\n" + cntEndStationLine + "\n<<<<<<<<<<<<<<<<<<<<");
//			System.out.println(cntStartStation + " : " + cntEndStation);
		} else { // 환승역 없음

		}

		return true;
	}

	// OpenAPI 1 ~ 8 호선 역사 좌표
	public boolean getTrainInfo() {
		List<SubWayEntity> subWayEntities = subWayRepository.findAll();
		if(subWayEntities.size() == 0) {
			String[][] nullData = new String[][]{
				// sname, slat, slng
				{"도봉", "37.6795", "127.0456"}, {"보산", "37.6795", "127.0455"}, {"광운대", "37.6238", "127.0618"}, {"반월", "37.3123", "126.9037"},
				{"까치울", "37.5062", "126.8113"}, {"부천종합운동장", "37.5055", "126.7974"}, {"신중동", "37.5031", "126.0776"}, {"부천시청", "37.5047", "126.7636"},
				{"상동", "37.5058", "126.7532"}, {"삼산체육관", "37.5065", "126.0742"}, {"굴포천", "37.0507", "126.7313"}, {"부평구청", "37.5074", "126.7211"},
				{"산곡", "37.5086", "126.7035"}, {"석남", "37.5002", "126.6758"}, {"수내", "37.3785", "127.1143"}, {"구룡", "37.4869", "127.0595"},
				{"상현", "37.2978", "127.0693"}, {"춘의", "37.5037", "126.0787"}
			};

			try {
				List<SubWayDTO> subWayDTOS = new ArrayList<>();
				String[][] trainAPIData = new String[][]{
					/* 1호선 */{"15041300", "90c8cf16-7bc9-42a4-a219-9a54f47768ed"}, /* 2호선 */{"15041301", "3ecd8bc2-34ea-4860-a788-bf2578754ad9"},
					/* 3호선 */{"15041302", "e654fca8-d6d5-4977-bf0d-a4ebea52d5b6"}, /* 4호선 */{"15041303", "c49053c3-6900-46c9-9615-cf5cc51c0dcc"},
					/* 5호선 */{"15041304", "8717c1fd-0d93-465e-93fb-f34dda9612d5"}, /* 6호선 */{"15041305", "e405b333-40e8-44f3-b918-d0fd7e7dd7b4"},
					/* 7호선 */{"15041306", "25014d49-e302-4157-a95d-b09a383a4774"}, /* 8호선 */{"15041334", "d7bacdad-8a49-4d47-8d96-b3a226db4efc"},
					/* 9호선 */{"15041335", "515ee279-c88f-47cc-9f94-a4dac970894c"}, /* 인천1호선 */{"15041338", "a7d7eb6c-dd76-44ad-8132-a9c41880a9b0"},
					/* 인천2호선 */{"15041339", "f9119403-9bad-4a3e-94e0-dfaddfd48f36"}, /* 수인분당 */{"15041336", "97b7dfab-9734-4b79-8b18-e5ef46f2cc12"},
					/* 신분당선 */{"15041337", "256d7380-e813-4155-a286-bdcf306f111c"}
				};
				for (int a = 0; a < trainAPIData.length; a++) {
					String otherLines = "https://api.odcloud.kr/api/" + trainAPIData[a][0] + "/v1/uddi:" + trainAPIData[a][1] + "?page=1&perPage=100&serviceKey=HECNY5QN6CQD7qu%2BwHHkPELwmRonryzFFC%2F19jU5jFMu9pT34DW66NlYrxVUnw%2BwBtTQvt7RxBbPo3RR9Y9pQQ%3D%3D";
					URL url = new URL(otherLines);
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
					String resultBuffer = bufferedReader.readLine();

					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObject = (JSONObject) jsonParser.parse(resultBuffer);
					JSONArray dataArray = (JSONArray) jsonObject.get("data");

					jsonObject = (JSONObject) jsonParser.parse(resultBuffer);
					dataArray = (JSONArray) jsonObject.get("data");
					subWayDTOS.add(new SubWayDTO("", "2호선", "0234-04", "까치산", "126.8467", "37.5318")); // 공공데이터에 아예 없어서 추가
					subWayDTOS.add(new SubWayDTO("", "3호선", "0309", "원흥", "126.8732", "37.6506")); // 공공데이터에 아예 없어서 추가
					for (int i = 0; i < dataArray.size(); i++) {
						SubWayDTO subWayDTO = new SubWayDTO();
						subWayDTO.setSname(dataArray.get(i).toString().split("역명\":\"")[1].split("\",\"")[0]); // 역명
						if (subWayDTO.getSname().equals("잠실새내")) {
							subWayDTO.setSname("신천");
						}
						if (dataArray.get(i).toString().split("경도\":")[1].split(",\"")[0].equals("null")) {
							for (int j = 0; j < subWayDTOS.size(); j++) {
								if (subWayDTOS.get(j).getSname().equals(subWayDTO.getSname())) {
									subWayDTO.setSlng(subWayDTOS.get(j).getSlng());
									break;
								} else {
									for (int k = 0; k < nullData.length; k++) {
										if (subWayDTO.getSname().contains(nullData[k][0])) { subWayDTO.setSlng(nullData[k][2]); break;
										} else { subWayDTO.setSlng("0"); }
									}
								}
							}
						} else {
							subWayDTO.setSlng(dataArray.get(i).toString().split("경도\":\"")[1].split("\",\"")[0]); // 경도
						}
						if (dataArray.get(i).toString().split("위도\":")[1].split(",\"")[0].equals("null")) {
							for (int j = 0; j < subWayDTOS.size(); j++) {
								if (subWayDTOS.get(j).getSname().equals(subWayDTO.getSname())) {
									subWayDTO.setSlat(subWayDTOS.get(j).getSlat());
									break;
								} else {
									for (int k = 0; k < nullData.length; k++) {
										if (subWayDTO.getSname().contains(nullData[k][0])) { subWayDTO.setSlat(nullData[k][1]); break;
										} else { subWayDTO.setSlat("0"); }
									}
								}
							}
						} else {
							subWayDTO.setSlat(dataArray.get(i).toString().split("위도\":\"")[1].split("\",\"")[0]); // 위도
						}
						subWayDTO.setSline(dataArray.get(i).toString().split("선명\":\"")[1].split("\"}")[0]); // 호선
						if (subWayDTO.getSline().equals("1호선")) {
							for (int n = 0; n < SubWay.line1.length; n++) { if (subWayDTO.getSname().contains(SubWay.line1[n][0])) { subWayDTO.setScode(SubWay.line1[n][1]); } }
							for (int n = 0; n < SubWay.line1_1.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line1_1[n][0])) { subWayDTO.setScode(SubWay.line1_1[n][1]); } }
							for (int n = 0; n < SubWay.line1_2.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line1_2[n][0])) { subWayDTO.setScode(SubWay.line1_2[n][1]); 	} }
						}
						if (subWayDTO.getSline().equals("2호선")) {
							for (int n = 0; n < SubWay.line2.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line2[n][0])) { subWayDTO.setScode(SubWay.line2[n][1]); } }
							for (int n = 0; n < SubWay.line2_1.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line2_1[n][0])) { subWayDTO.setScode(SubWay.line2_1[n][1]); } }
							for (int n = 0; n < SubWay.line2_2.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line2_2[n][0])) { subWayDTO.setScode(SubWay.line2_2[n][1]); } }
						}
						if (subWayDTO.getSline().equals("3호선")) {
							for (int n = 0; n < SubWay.line3.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line3[n][0])) { subWayDTO.setScode(SubWay.line3[n][1]); } }
						}
						if (subWayDTO.getSline().equals("4호선")) {
							for (int n = 0; n < SubWay.line4.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line4[n][0])) { subWayDTO.setScode(SubWay.line4[n][1]); } }
						}
						if (subWayDTO.getSline().equals("5호선")) {
							for (int n = 0; n < SubWay.line5.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line5[n][0])) { subWayDTO.setScode(SubWay.line5[n][1]); } }
							for (int n = 0; n < SubWay.line5_1.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line5_1[n][0])) { subWayDTO.setScode(SubWay.line5_1[n][1]); } }
							for (int n = 0; n < SubWay.line5_2.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line5_2[n][0])) { subWayDTO.setScode(SubWay.line5_2[n][1]); } }
						}
						if (subWayDTO.getSline().equals("6호선")) {
							for (int n = 0; n < SubWay.line6.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line6[n][0])) { subWayDTO.setScode(SubWay.line6[n][1]); } }
							for (int n = 0; n < SubWay.line6_1.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line6_1[n][0])) { subWayDTO.setScode(SubWay.line6_1[n][1]); } }
						}
						if (subWayDTO.getSline().equals("7호선")) {
							for (int n = 0; n < SubWay.line7.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line7[n][0])) { subWayDTO.setScode(SubWay.line7[n][1]); } }
						}
						if (subWayDTO.getSline().equals("8호선")) {
							for (int n = 0; n < SubWay.line8.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line8[n][0])) { subWayDTO.setScode(SubWay.line8[n][1]); } }
						}
						if (subWayDTO.getSline().equals("9호선")) {
							for (int n = 0; n < SubWay.line9.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.line9[n][0])) { subWayDTO.setScode(SubWay.line9[n][1]); } }
						}
						if (subWayDTO.getSline().equals("수인분당")) {
							for (int n = 0; n < SubWay.suinBundang.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.suinBundang[n][0])) { subWayDTO.setScode(SubWay.suinBundang[n][1]); } }
						}
						if (subWayDTO.getSline().equals("신분당선")) {
							for (int n = 0; n < SubWay.sinBundang.length; n++) { if (subWayDTO.getSname().split("\\(")[0].equals(SubWay.sinBundang[n][0])) { subWayDTO.setScode(SubWay.sinBundang[n][1]); } }
						}
						subWayDTOS.add(subWayDTO);
						if (a == 8 && i == 28) { break; }
					}
				}

				for (int i = 0; i < subWayDTOS.size(); i++) {
					SubWayEntity subWayEntity = SubWayEntity.builder()
						.scode(subWayDTOS.get(i).getScode()).slat(subWayDTOS.get(i).getSlat()).sline(subWayDTOS.get(i).getSline()).slng(subWayDTOS.get(i).getSlng()).sname(subWayDTOS.get(i).getSname()).build();
					subWayRepository.save(subWayEntity);
				}
				return true;
			} catch (Exception e) {
				System.out.println("getTrainInfo_Exception : " + e.getMessage());
				return false;
			}
		}
		return true;
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

