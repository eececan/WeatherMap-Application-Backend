package backend.weatherApp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
/**
 * @author Elif Ece Can
 * @date 17.07.2024
 */
@Data
@Entity
@Table(name="pollution")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

@Schema(description = "Details about the Air Pollution")
public class Pollution
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="city")
    @Schema(
            description = "The name of the city where pollution is being measured",
            name = "city",
            type = "string",
            example = "London")
    private String city;

    @Column(name="carbonmonoxide")
    @Schema(
            description = "The pollution severity based on the measured CO (carbon monoxide) rate.",
            name = "co",
            type = "string",
            example = "GOOD")
    private String co;

    @Column(name="sulphurdioxide")
    @Schema(
            description = "The pollution severity based on the measured S02 (sulphur dioxide) rate.",
            name = "so2",
            type = "string",
            example = "SATISFACTORY")
    private String so2;

    @Column(name="ozone")
    @Schema(
            description = "The pollution severity based on the measured O3 (ozone) rate.",
            name = "o3",
            type = "string",
            example = "POOR")
    private String o3;

    @Column(name="date")
    @Schema(
            description = "The pollution severity based on the measured CO (carbon monoxide) rate.",
            name = "date",
            type = "LocalDate",
            example = "GOOD")
    private LocalDate date;

    public Pollution(String city, String co, String so2, String o3, LocalDate date) {
        this.city = city;
        this.co = co;
        this.so2 = so2;
        this.o3 = o3;
        this.date = date;
    }
}
