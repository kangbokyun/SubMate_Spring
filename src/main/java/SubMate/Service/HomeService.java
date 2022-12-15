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
                List<RankDTO> rankDTOS = new ArrayList<>();

                ArrayList<String> integers = new ArrayList<>();

                for(int i = 0; i < heartEntities.size(); i++) {
                        for(int j = 0; j < heartEntities.size(); j++) {
                                if(heartEntities.get(i).getUserno() != null &&
                                        heartEntities.get(j).getUserno() != null &&
                                        heartEntities.get(i).getUserno().equals(heartEntities.get(j).getUserno())) {
                                        integers.add(heartEntities.get(j).getUserno());
                                        break;
                                }
                        }
                }

                System.out.println("integers : " + integers);
                ArrayList<Integer> temp = new ArrayList<>();
                for(int i = 0 ; i < integers.size(); i++) {
                        int cnt = 1;
                        for(int j = i + 1; j < integers.size(); j++) {
                                if(integers.get(i) == integers.get(j)) {
                                        System.out.println(i + " : " + j);
                                        cnt++;
                                        integers.remove(j);
                                        System.out.println("integers.get(i) : " + integers.get(i));
                                        System.out.println("integers.get(j) : " + integers.get(j));
                                }
                        }
                        temp.add(cnt);
                }
                System.out.println("temp : " + temp);

                return null;
        }
}
