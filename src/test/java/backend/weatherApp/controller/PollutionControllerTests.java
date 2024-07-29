package backend.weatherApp.controller;

import backend.weatherApp.configuration.ApiKeyConfig;
import backend.weatherApp.dto.CityAndPollutionDto;
import backend.weatherApp.exception.CustomException;
import backend.weatherApp.service.AirPollutionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
/**
 * @author: Elif Ece Can
 * @date: 19.09.2024
 */
@WebMvcTest(controllers = AirPollutionController.class)
@ContextConfiguration(classes = AirPollutionController.class)
public class PollutionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiKeyConfig apiKeyConfig;

    @MockBean
    private AirPollutionService airPollutionService;

    @Test
    public void testGetPollutionInformation_ValidCity() throws Exception
    {
        String cityName = "London";
        CityAndPollutionDto mockCityAndPollutionDto = new CityAndPollutionDto();
        mockCityAndPollutionDto.setCity(cityName);
        mockCityAndPollutionDto.setResults(Collections.emptyList());

        when(airPollutionService.getPollutionInfo(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockCityAndPollutionDto);

        mockMvc.perform(get("/api/air-pollution/{cityName}", cityName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"city\":\"London\",\"results\":[]}"));

        Mockito.verify(airPollutionService, Mockito.times(1))
                .getPollutionInfo(anyString(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    public void testGetPollutionInformation_ValidCityWithValidDates() throws Exception {
        String cityName = "London";
        String startDate = "2024-07-01";
        String endDate = "2024-07-08";

        CityAndPollutionDto mockCityAndPollutionDto = new CityAndPollutionDto();
        mockCityAndPollutionDto.setCity(cityName);
        mockCityAndPollutionDto.setResults(Collections.emptyList());

        when(airPollutionService.getPollutionInfo(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockCityAndPollutionDto);

        mockMvc.perform(get("/api/air-pollution/{cityName}/{startDate}/{endDate}", cityName, startDate, endDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"city\":\"London\",\"results\":[]}"));

        Mockito.verify(airPollutionService, Mockito.times(1))
                .getPollutionInfo(anyString(), any(LocalDate.class), any(LocalDate.class));
    }


}
