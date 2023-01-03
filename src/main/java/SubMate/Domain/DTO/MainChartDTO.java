package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class MainChartDTO {
	private int chartno;
	private int charttendinous;
	private int chartqna;
	private int chartreport;
	private String chartdate;
}
