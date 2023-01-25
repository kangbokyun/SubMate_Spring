package SubMate.Service;

import SubMate.Domain.DTO.*;
import SubMate.Domain.Entity.*;
import SubMate.Domain.Repository.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HomeService {
        @Autowired
        MemberRepository memberRepository;
        @Autowired
        MateRepository mateRepository;
        @Autowired
        HeartRepository heartRepository;
        @Autowired
        RankRepository rankRepository;
        // 랭커 찾기
        public List<RankDTO> SearchRanker(int mno) {
                MateEntity targetMateEntity = mateRepository.findByMemberEntity_Mno(mno);
                List<HeartEntity> heartEntities = heartRepository.findAll();
                List<MateEntity> mateEntities = mateRepository.findAll();

                // 하트 개수를 구한다.
                Map<Integer, Integer> sureMap = new HashMap<>();
                for(int i = 0; i < heartEntities.size(); i++) {
                        MateEntity tempMate = mateRepository.findByMemberEntity_Mno(Integer.parseInt(heartEntities.get(i).getMno()));
                        if(heartEntities.get(i).getUserno() != null && targetMateEntity.getMatestartstation().equals(tempMate.getMatestartstation())) {
                                HeartDTO heartDTO = HeartDTO.builder().userno(heartEntities.get(i).getUserno()).mno(heartEntities.get(i).getMno()).build();
                                if(sureMap.size() != 0 && sureMap.containsKey(Integer.parseInt(heartDTO.getUserno()))) {
                                        sureMap.put(Integer.parseInt(heartDTO.getUserno()), sureMap.get(Integer.parseInt(heartDTO.getUserno())) + 1);
                                } else {
                                        sureMap.put(Integer.parseInt(heartDTO.getUserno()), 1);
                                }
                        }
                }

                int n = 0;
                List<RankDTO> rankDTOS = new ArrayList<>();
                for(Integer key : sureMap.keySet()) {
                        Optional<MemberEntity> memberEntity = memberRepository.findById(key);
                        MateEntity mateEntity = mateRepository.findByMemberEntity_Mno(key);
                        RankDTO rankDTO = RankDTO.builder()
                                .heartcount((Integer)sureMap.values().toArray()[n])
                                .mno(memberEntity.get().getMno())
                                .profileImg(memberEntity.get().getProfileimg().split("MemberImg/")[1])
                                .rankerager(memberEntity.get().getMgender())
                                .rankerhobby(memberEntity.get().getMhobby())
                                .rankermbti(memberEntity.get().getMbti())
                                .rankline(mateEntity.getMatestartstation())
                                .rankrankernickname(memberEntity.get().getMnickname())
                                .build();
                        rankDTOS.add(rankDTO);
                        n++;
                        if(n > 5 || n == sureMap.size()) {
                                break;
                        }
                }

                Comparator<RankDTO> listSort = new Comparator<RankDTO>() {
                        @Override
                        public int compare(RankDTO o1, RankDTO o2) {
                                int a = o1.getHeartcount();
                                int b = o2.getHeartcount();

                                if(a > b) { return -1; } else {	return 1;	 }
                        }
                };
                Collections.sort(rankDTOS, listSort);

                List<RankEntity> rankEntities = rankRepository.findAll();
                if(rankEntities.size() == 0) {
                        for (int i = 0; i < rankDTOS.size(); i++) {
                                Optional<MemberEntity> memberEntity = memberRepository.findById(rankDTOS.get(i).getMno());

                                RankEntity rankEntity = RankEntity.builder()
                                        .heartcount(rankDTOS.get(i).getHeartcount())
                                        .rankermbti(rankDTOS.get(i).getRankermbti())
                                        .memberEntity(memberEntity.get())
                                        .rankerager(rankDTOS.get(i).getRankerager())
                                        .rankerhobby(rankDTOS.get(i).getRankerhobby())
                                        .rankerline(rankDTOS.get(i).getRankline())
                                        .rankernickname(rankDTOS.get(i).getRankrankernickname())
                                        .rankerprofileimg(rankDTOS.get(i).getProfileImg())
                                        .build();
                                rankRepository.save(rankEntity);
                        }
                }
                return rankDTOS;
        }

        public List<IssueDTO> HomeIssue() {
                List<IssueDTO> issueDTOS = new ArrayList<>();
                try {
                        Document document = Jsoup.connect("https://search.naver.com/search.naver?where=news&ie=utf8&sm=nws_hty&query=지하철").get();
                        Elements news = document.getElementsByClass("list_news");
                        Elements title = news.select("li>div>div>a");
                        for (int i = 0; i < title.size(); i++) {
                                IssueDTO issueDTO = IssueDTO.builder().issueLink(title.get(i).attr("href")).issueTitle(title.get(i).attr("title")).issueNo(i + 1).build();
                                if(title.get(i).attr("title").contains("\"")) {
                                        issueDTO.setIssueTitle(title.get(i).attr("title").replace("\"", ""));
                                }
                                issueDTOS.add(issueDTO);
                                if(i > 5) {
                                        break;
                                }
                        }
                        return issueDTOS;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return null;
                }
        }

        public WeatherDTO Weather() {
                try {
                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HHmm");
                        System.out.println("simpleDateFormat1.format(date) : " + simpleDateFormat1.format(date));
                        String baseTime = null;
                        if(Integer.parseInt(simpleDateFormat1.format(date)) < 200) {
                                baseTime = "0200";
                        } else if(Integer.parseInt(simpleDateFormat1.format(date)) < 500 && Integer.parseInt(simpleDateFormat1.format(date)) > 200) {
                                baseTime = "0200";
                        } else if(Integer.parseInt(simpleDateFormat1.format(date)) < 800 && Integer.parseInt(simpleDateFormat1.format(date)) > 500) {
                                baseTime = "0500";
                        } else if(Integer.parseInt(simpleDateFormat1.format(date)) < 1100 && Integer.parseInt(simpleDateFormat1.format(date)) > 800) {
                                baseTime = "0800";
                        } else if(Integer.parseInt(simpleDateFormat1.format(date)) < 1400 && Integer.parseInt(simpleDateFormat1.format(date)) > 1100) {
                                baseTime = "1100";
                        } else if(Integer.parseInt(simpleDateFormat1.format(date)) < 1700 && Integer.parseInt(simpleDateFormat1.format(date)) > 1400) {
                                baseTime = "1400";
                        } else if(Integer.parseInt(simpleDateFormat1.format(date)) < 2100 && Integer.parseInt(simpleDateFormat1.format(date)) > 1700) {
                                baseTime = "1700";
                        } else {
                                baseTime = "2300";
                        }
//                                String apiurl = "https://openapi.gg.go.kr/AnimalSale?Key=d33e0915e37c453abb4d9a94d8f265ed&Type=json&pIndex=1&pSize=1000";
                        String apiurl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
                                + "?serviceKey=HECNY5QN6CQD7qu%2BwHHkPELwmRonryzFFC%2F19jU5jFMu9pT34DW66NlYrxVUnw%2BwBtTQvt7RxBbPo3RR9Y9pQQ%3D%3D"
                                + "&dataType=JSON"            // JSON, XML
                                + "&numOfRows=10"             // 페이지 ROWS
                                + "&pageNo=1"                 // 페이지 번호
                                + "&base_date=" + simpleDateFormat.format(date)       // 발표일자
//                                 동네예보 --
//                                  baseTime = 0200, 0500, 0800, 1100, 1400, 1700, 2100
//                                  API 제공시간 = baseTime+10
//                                  API 제공시간인 0210분 이전시간이면 전날 23시로 세팅
                                + "&base_time=" + baseTime           // 발표시각 // 총 몇개인지 조사해야됨
                                + "&nx=58"                    // 예보지점 X 좌표
                                + "&ny=121";

                        URL url = new URL(apiurl);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                        String result = bufferedReader.readLine();

                        JSONParser jsonParser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                        JSONObject jsonObject1 = (JSONObject) jsonObject.get("response");
                        JSONObject jsonObject2 = (JSONObject) jsonObject1.get("body");
                        JSONObject jsonObject3 = (JSONObject) jsonObject2.get("items");
                        JSONArray jsonArray = (JSONArray) jsonObject3.get("item");

                        WeatherDTO weatherDTO = new WeatherDTO();
                        for(int i = 0; i < jsonArray.size(); i++) {
                                if(jsonArray.get(i).toString().contains("TMP")) {
                                        weatherDTO.setTMP(jsonArray.get(i).toString().split("Value\":\"")[1].split("\",\"")[0]);
                                }
                                if(jsonArray.get(i).toString().contains("PTY")) {
                                        switch (jsonArray.get(i).toString().split("Value\":\"")[1].split("\",\"")[0]) {
                                                case "1": weatherDTO.setPTY("비"); break;
                                                case "2": weatherDTO.setPTY("진눈개비"); break;
                                                case "3": weatherDTO.setPTY("눈"); break;
                                                case "4": weatherDTO.setPTY("소나기"); break;
                                                case "5": weatherDTO.setPTY("빗방울"); break;
                                                case "6": weatherDTO.setPTY("빗방울/눈날림"); break;
                                                case "7": weatherDTO.setPTY("눈날림"); break;
                                                default: weatherDTO.setPTY("null"); break;
                                        }
                                }
                                if(jsonArray.get(i).toString().contains("SKY")) {
                                        switch (jsonArray.get(i).toString().split("Value\":\"")[1].split("\",\"")[0]) {
                                                case "1": weatherDTO.setSKY("맑음"); break;
                                                case "3": weatherDTO.setSKY("구름많음"); break;
                                                case "4": weatherDTO.setSKY("흐림"); break;
                                                default: weatherDTO.setSKY("null"); break;
                                        }
                                }
                                if(jsonArray.get(i).toString().contains("POP")) {
                                        weatherDTO.setPOP(jsonArray.get(i).toString().split("Value\":\"")[1].split("\",\"")[0] + "%");
                                }
                        }
//                        System.out.println("weatherDTO : " + weatherDTO);
                        return weatherDTO;
                } catch (Exception e) {
                        System.out.println("e.getMessage() : " + e.getMessage());
                        return null;
                }
        }
}
