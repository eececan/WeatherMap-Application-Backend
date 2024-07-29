package backend.weatherApp.dto;

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
public class LocationDto
{
    private String name;
    private double lat;
    private double lon;
}
