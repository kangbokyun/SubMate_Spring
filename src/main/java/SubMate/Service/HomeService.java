package SubMate.Service;

import SubMate.Domain.DTO.BoardDTO;
import SubMate.Domain.DTO.HeartDTO;
import SubMate.Domain.DTO.MateDTO;
import SubMate.Domain.DTO.RankDTO;
import SubMate.Domain.Entity.HeartEntity;
import SubMate.Domain.Entity.MateEntity;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Repository.HeartRepository;
import SubMate.Domain.Repository.MateRepository;
import SubMate.Domain.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class HomeService {
        @Autowired
        MemberRepository memberRepository;
        @Autowired
        MateRepository mateRepository;
        @Autowired
        HeartRepository heartRepository;
        // 랭커 찾기
        public List<RankDTO> SearchRanker(int mno) {
                MateEntity targetMateEntity = mateRepository.findByMemberEntity_Mno(mno);
                List<MateEntity> mateEntities = mateRepository.findAll();
                List<MateDTO> mateDTOS = new ArrayList<>();
                List<HeartEntity> heartEntities = heartRepository.findAll();
                List<HeartDTO> heartDTOS = new ArrayList<>();
                List<HeartDTO> tempDTOS = new ArrayList<>();
                List<RankDTO> rankDTOS = new ArrayList<>();

                // 하트 개수를 구한다.
                Map<Integer, Integer> sureMap = new HashMap<>();
                for(int i = 0; i < heartEntities.size(); i++) {
                        if(heartEntities.get(i).getUserno() != null) {
                                HeartDTO heartDTO = HeartDTO.builder().userno(heartEntities.get(i).getUserno()).mno(heartEntities.get(i).getMno()).build();
                                if(sureMap.size() != 0 && sureMap.containsKey(Integer.parseInt(heartDTO.getUserno()))) {
                                        sureMap.put(Integer.parseInt(heartDTO.getUserno()), sureMap.get(Integer.parseInt(heartDTO.getUserno())) + 1);
                                } else {
                                        sureMap.put(Integer.parseInt(heartDTO.getUserno()), 1);
                                }
                        }
                        if(sureMap.size() <= 5) {
                                break;
                        }
                }

                System.out.println("sureMap.get(i) : " + sureMap);
                return null;
        }
}
