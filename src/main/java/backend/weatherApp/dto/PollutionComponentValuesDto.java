package backend.weatherApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
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
public class PollutionComponentValuesDto {
    private double co;
    private double so2;
    private double o3;
}