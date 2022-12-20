package SubMate.Service;

import SubMate.Domain.DTO.*;
import SubMate.Domain.Entity.*;
import SubMate.Domain.Repository.*;
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
//                        if(sureMap.size() > 5) {
//                                break;
//                        }
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
                System.out.println("rankDTOS : " + rankDTOS);
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

                        try {
                                String apiurl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=HECNY5QN6CQD7qu%2BwHHkPELwmRonryzFFC%2F19jU5jFMu9pT34DW66NlYrxVUnw%2BwBtTQvt7RxBbPo3RR9Y9pQQ%3D%3D&pageNo=1&numOfRows=1000&dataType=JSON&base_date=20221220&base_time=0500&nx=55&ny=127";
//                                String apiurl = "https://openapi.gg.go.kr/AnimalSale?Key=d33e0915e37c453abb4d9a94d8f265ed&Type=json&pIndex=1&pSize=1000";

                                URL url = new URL(apiurl);
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                                String result = bufferedReader.readLine();

                                System.out.println("result : " + result);
                                JSONParser jsonParser = new JSONParser();
                                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                        } catch (Exception e) {
                                System.out.println("e.getMessage() : " + e.getMessage());
                        }

                        return issueDTOS;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return null;
                }
        }

        public void Weather() {
                try {
                        String apiurl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?" +
                                "serviceKey=HECNY5QN6CQD7qu%2BwHHkPELwmRonryzFFC%2F19jU5jFMu9pT34DW66NlYrxVUnw%2BwBtTQvt7RxBbPo3RR9Y9pQQ%3D%3D&" +
                                "pageNo=1&" +
                                "numOfRows=1000&" +
                                "dataType=JSON&" +
                                "base_date=20221220&" +
                                "base_time=0500&" +
                                "nx=55&" +
                                "ny=127";
                        String apiKey = "HECNY5QN6CQD7qu%2BwHHkPELwmRonryzFFC%2F19jU5jFMu9pT34DW66NlYrxVUnw%2BwBtTQvt7RxBbPo3RR9Y9pQQ%3D%3D";

                        URL url = new URL(apiurl);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                        String result = bufferedReader.readLine();

                        System.out.println("result : " + result);
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }
}
