package backend.weatherApp.controller;

import backend.weatherApp.configuration.ApiKeyConfig;
import backend.weatherApp.dto.LocationDto;
import backend.weatherApp.service.AirPollutionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
/**
 * @author: Elif Ece Can
 * @date: 19.09.2024
 */
@WebMvcTest(controllers = LocationController.class)
@ContextConfiguration(classes = LocationController.class)
public class LocationControllerTests {


    @MockBean
    private ApiKeyConfig apiKeyConfig;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirPollutionService airPollutionService;

    @Test
    public void testGetCityInfo_ValidCity() throws Exception {
        String cityName = "London";
        LocationDto mockLocationDto = new LocationDto();
        mockLocationDto.setName("London");
        mockLocationDto.setLat(51.5074);
        mockLocationDto.setLon(-0.1278);

        when(airPollutionService.getCityGeocoding(anyString())).thenReturn(mockLocationDto);

        mockMvc.perform(get("/api/city/{cityName}", cityName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\":\"London\",\"lat\":51.5074,\"lon\":-0.1278}"));

        Mockito.verify(airPollutionService, Mockito.times(1)).getCityGeocoding(cityName);
    }
    @Test
    public void testGetCityInfo_ValidCity_ExtraSpaces() throws Exception {
        String cityName = "  London  ";
        LocationDto mockLocationDto = new LocationDto();
        mockLocationDto.setName("London");
        mockLocationDto.setLat(51.5074);
        mockLocationDto.setLon(-0.1278);

        when(airPollutionService.getCityGeocoding(anyString())).thenReturn(mockLocationDto);

        mockMvc.perform(get("/api/city/{cityName}", cityName.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\":\"London\",\"lat\":51.5074,\"lon\":-0.1278}"));

        Mockito.verify(airPollutionService, Mockito.times(1)).getCityGeocoding(cityName.trim());
    }

}
