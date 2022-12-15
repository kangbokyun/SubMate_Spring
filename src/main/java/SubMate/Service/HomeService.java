package SubMate.Service;

import SubMate.Domain.DTO.RankDTO;
import SubMate.Domain.Entity.MateEntity;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Repository.MateRepository;
import SubMate.Domain.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {
        @Autowired
        MemberRepository memberRepository;
        @Autowired
        MateRepository mateRepository;
        // 랭커 찾기
        public List<RankDTO> SearchRanker(int mno) {
                List<MemberEntity> memberEntities = memberRepository.findAll();
                List<MateEntity> mateEntities = mateRepository.findAll();
                for(MemberEntity memberEntity : memberEntities) {
                        for(MateEntity mateEntity : mateEntities) {
                                if(memberEntity.getMno() == mateEntity.getMemberEntity().getMno()) {
                                        System.out.println("------------------------같음-----------------------");
                                        System.out.println("memberEntity : " + memberEntity);
                                        System.out.println("mateEntity : " + mateEntity);
                                        System.out.println("-----------------------------------------------------");
                                } else {
                                        System.out.println("------------------------다름-----------------------");
                                        System.out.println("memberEntity : " + memberEntity);
                                        System.out.println("mateEntity : " + mateEntity);
                                        System.out.println("-----------------------------------------------------");
                                }
                        }
                }
                return null;
        }
}
