package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString @Builder @Data
@Entity@Table(name = "mate")
public class MateEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mateno;
}
