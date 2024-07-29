package backend.weatherApp.controller;

import backend.weatherApp.configuration.ApiKeyConfig;
import backend.weatherApp.dto.LocationDto;
import backend.weatherApp.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backend.weatherApp.service.AirPollutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
/**
 * @author Elif Ece Can
 * @date 17.07.2024
 */
@RestController
@RequestMapping("api/city")
@RequiredArgsConstructor
public class LocationController
{
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    private final AirPollutionService airPollutionService;
    private final ApiKeyConfig apiKeyConfig;

    @GetMapping("/{cityName}")
    public ResponseEntity<LocationDto> getCityInformation(
            @PathVariable String cityName)
    {
        if (validCity(cityName))
        {
            logger.info("Fetching information for city: {} with API key: {}", cityName, apiKeyConfig.getApiKey());
            return ResponseEntity.ok(airPollutionService.getCityGeocoding(cityName));
        } else
        {
            throw new CustomException("Invalid city name: " + cityName);
        }
    }
    private boolean validCity(String cityName)
    {
        return Arrays.asList("london", "barcelona", "ankara", "tokyo", "mumbai").contains(cityName.toLowerCase());
    }
}
