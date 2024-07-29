package backend.weatherApp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
/**
 * @author Elif Ece Can
 * @date 19.07.2024
 */
@Configuration
public class RestTemplateConfiguration
{
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder)
    {
        return builder.build();
    }
}