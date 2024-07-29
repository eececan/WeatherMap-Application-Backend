package backend.weatherApp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
/**
 * @author: Elif Ece Can
 * @date: 22.09.2024
 */
@Configuration
public class OpenApiConfiguration
{
    @Bean
    public OpenAPI customOpenAPI()
    {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas("Pollution", createPollutionSchema()))
                .info(new Info().title("Weather App API Documentation")
                        .description("This RESTful application, built with Spring Boot, provides a " +
                                "comprehensive solution for retrieving and managing air pollution data. It leverages " +
                                "the OpenWeatherMap public API to fetch pollution information based on city name and " +
                                "specified date ranges. The application processes and stores this data in a database, " +
                                "offering users a robust interface to query and analyze air quality trends over time. " +
                                "The API documentation includes detailed schemas and endpoints for accessing and " +
                                "managing pollution data."));
    }
    private Schema<?> createPollutionSchema()
    {
        return new Schema<>()
                .type("object")
                .addProperties("id", new Schema<>().type("integer").format("int64").description("The unique ID of the pollution record"))
                .addProperties("city", new Schema<>().type("string").description("The name of the city where pollution is being measured").example("London"))
                .addProperties("co", new Schema<>().type("string").description("The pollution severity based on the measured CO (carbon monoxide) rate").example("GOOD"))
                .addProperties("so2", new Schema<>().type("string").description("The pollution severity based on the measured SO2 (sulfur dioxide) rate").example("SATISFACTORY"))
                .addProperties("o3", new Schema<>().type("string").description("The pollution severity based on the measured O3 (ozone) rate").example("POOR"))
                .addProperties("date", new Schema<>().type("string").format("date").description("The date when the pollution was measured").example("2024-07-17"));
    }
}
