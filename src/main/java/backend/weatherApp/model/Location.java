package backend.weatherApp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
/**
 * @author Elif Ece Can
 * @date 17.07.2024
 */

@Data
@Entity
@Table(name="location")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "Details about the Location of the Air Pollution")
public class Location
{
    @Id
    @Column(name="city_name")
    @Schema(
            description = "The name of the city for which the user wants to find the coordinates",
            name = "cityName",
            type = "string",
            example = "London")
    private String cityName;

    @Schema(
            description = "The latitude of the city",
            name = "latitude",
            type = "double",
            example = "51.5098")
    @Column(name="latitude")
    private double latitude;

    @Schema(
            description = "The longitude of the city",
            name = "longitude",
            type = "double",
            example = "-0.1180")
    @Column(name="longitude")
    private double longitude;
}
