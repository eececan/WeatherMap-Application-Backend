package backend.weatherApp.service;

import static org.mockito.Mockito.*;
import backend.weatherApp.configuration.ApiKeyConfig;
import backend.weatherApp.dto.LocationDto;
import backend.weatherApp.repository.ILocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author: Elif Ece Can
 * @date: 19.09.2024
 */
@ExtendWith(MockitoExtension.class)
public class AirPollutionServiceTests {

    @InjectMocks
    private AirPollutionService airPollutionService;

    @Mock
    private ApiKeyConfig apiKeyConfig;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ILocationRepository locationRepository;

    @Test
    public void testGeocoding() {
        String cityName = "London";

        LocationDto mockLocationDto = new LocationDto();
        mockLocationDto.setName("London");
        mockLocationDto.setLat(51.5074);
        mockLocationDto.setLon(-0.1278);

        LocationDto[] mockLocationDtos = new LocationDto[] { mockLocationDto };
        ResponseEntity<LocationDto[]> responseEntity = ResponseEntity.ok(mockLocationDtos);

        when(restTemplate.getForEntity(anyString(), eq(LocationDto[].class)))
                .thenReturn(responseEntity);

        LocationDto result = airPollutionService.getCityGeocoding(cityName);

        assertEquals(mockLocationDto, result);
        verify(locationRepository, times(1)).save(argThat(location ->
                "London".equals(location.getCityName()) &&
                        51.5074 == location.getLatitude() &&
                        -0.1278 == location.getLongitude()
        ));
    }
}
