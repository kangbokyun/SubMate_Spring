package SubMate.Service;

import SubMate.Domain.DTO.*;
import SubMate.Domain.Entity.HeartEntity;
import SubMate.Domain.Entity.MateEntity;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Entity.RankEntity;
import SubMate.Domain.Repository.HeartRepository;
import SubMate.Domain.Repository.MateRepository;
import SubMate.Domain.Repository.MemberRepository;
import SubMate.Domain.Repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
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
                return rankDTOS;
        }
}
