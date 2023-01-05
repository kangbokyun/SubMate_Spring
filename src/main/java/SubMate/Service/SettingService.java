package SubMate.Service;

import SubMate.Domain.DTO.*;
import SubMate.Domain.Entity.*;
import SubMate.Domain.Repository.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.Reader;
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

	public void SubStation() {
		List<SubWayEntity> subWayEntities = subWayRepository.findAll();
		System.out.println(subWayEntities.size());
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean SearchStation(MateDTO mateDTO) { // 지하철 역 경로 찾는 로직 수정해야 됨
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
			System.out.println(mateEntityCheck.size());
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
}

