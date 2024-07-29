package backend.weatherApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDate;
/**
 * @author Elif Ece Can
 * @date 17.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollutionDto
{
    private Long id;
    private String city;
    private String co;
    private String so2;
    private String o3;
    private LocalDate date;
}
