package backend.weatherApp.configuration;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
/**
 * @author Elif Ece Can
 * @date 19.07.2024
 */
@Component
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyConfig {

    @Value("${X-API-KEY}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}