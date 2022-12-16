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
//                for(int i = 0; i < heartEntities.size(); i++) {
//                        if(heartEntities.get(i).getUserno() != null) {
//                                for(int j = 0; j < heartEntities.size(); j++) {
//                                        if(heartDTOS.size() == 0) {
//                                                HeartDTO heartDTO = HeartDTO.builder().mno(heartEntities.get(i).getMno()).userno(heartEntities.get(i).getUserno()).build();
//                                                heartDTOS.add(heartDTO);
//                                        } else if(heartDTOS.size() != 0) {
//                                                if(heartDTOS.size() < j) {
//                                                        break;
//                                                }
//                                                System.out.println("heartDTOS.get(j).getUserNo() : " + heartDTOS.get(j).getUserno());
//                                        }
//                                }
//                        }
//                }

                for(int i = 0; i < heartEntities.size(); i++) {
                        if(heartEntities.get(i).getUserno() != null) {
                                HeartDTO heartDTO = HeartDTO.builder().mno(heartEntities.get(i).getMno()).userno(heartEntities.get(i).getUserno()).build();
                                tempDTOS.add(heartDTO);
                        }
                }

                for(int i = 0; i < tempDTOS.size(); i++) {
                        if(heartDTOS.size() == 0) {
                                heartDTOS.add(tempDTOS.get(i));
                        } else {
                                for(int j = 0; j <= heartDTOS.size(); j++) {
                                        if(!heartDTOS.get(j).getUserno().equals(tempDTOS.get(i))) {
                                                System.out.println("j : " + j);
                                                System.out.println("heartDTOS.size() : " + heartDTOS.size());
                                                heartDTOS.add(tempDTOS.get(i));
                                                break;
                                        }
                                }
                        }
                }
                System.out.println("heartDTOS: " + heartDTOS);
                return null;
        }
}
