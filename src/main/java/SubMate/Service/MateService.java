package SubMate.Service;

import SubMate.Domain.DTO.HeartDTO;
import SubMate.Domain.Entity.HeartEntity;
import SubMate.Domain.Repository.HeartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MateService {
        @Autowired
        HeartRepository heartRepository;

        // 유저 하트
        public boolean UserHeart(HeartDTO heartDTO) {
                if(heartDTO != null) {
                        System.out.println("UserHeart : " + heartDTO);
                        if(heartDTO.getHkind().equals("1")) {
                                HeartEntity heartEntity = HeartEntity.builder()
                                        .hkind(heartDTO.getHkind()).htype(heartDTO.getHtype())
                                        .mno(heartDTO.getMno()).userno(heartDTO.getUserno())
                                        .build();
                                heartRepository.save(heartEntity);
                                return true;
                        } else {
                                List<HeartEntity> heartEntities = heartRepository.findAll();
                                if(heartEntities != null) {
                                        for(HeartEntity heartEntity : heartEntities) {
                                                if(heartEntity.getUserno() != null
                                                        && heartEntity.getMno().equals(heartDTO.getMno())
                                                        && heartEntity.getUserno().equals(heartDTO.getUserno())) {
                                                        heartRepository.delete(heartEntity);
                                                }
                                        }
                                }
                                return false;
                        }
                }
                return false;
        }
        // 하트 리스트
        public List<HeartDTO> UserHeartList() {
                List<HeartEntity> heartEntities = heartRepository.findAll();
                List<HeartDTO> heartDTOS = new ArrayList<>();
                for(HeartEntity heartEntity : heartEntities) {
                        if(heartEntity.getUserno() != null) {
                                HeartDTO heartDTO = HeartDTO.builder()
                                        .hno(heartEntity.getHno()).userno(heartEntity.getUserno()).htype(heartEntity.getHtype()).hkind(heartEntity.getHkind())
                                        .build();
                                heartDTOS.add(heartDTO);
                        }
                }
                return heartDTOS;
        }
}
