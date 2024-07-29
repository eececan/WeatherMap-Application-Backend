package backend.weatherApp.dto;

import backend.weatherApp.model.Pollution;
import lombok.*;
import java.util.List;
/**
 * @author Elif Ece Can
 * @date 17.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityAndPollutionDto
{
    private String city;
    private List<Pollution> results;
}
