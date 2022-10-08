package SubMate.Config.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> { /*T는 Type의 약자*/
    private String error;
    private List<T> data;
    private List<List<T>> datalist;
    private Optional<T> optiondata;
    private boolean check;
}
