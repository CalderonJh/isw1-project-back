package fpc.app.model.app;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "club", schema = "app")
public class Club {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 250, unique = true)
	@NotBlank
	private String name;

	@Column(name = "crest_image_id", length = 250)
	private String crestImageId;
}
