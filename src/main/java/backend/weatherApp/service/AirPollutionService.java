package backend.weatherApp.service;

import backend.weatherApp.configuration.ApiKeyConfig;
import backend.weatherApp.dto.CityAndPollutionDto;
import backend.weatherApp.dto.LocationDto;
import backend.weatherApp.dto.PollutionDateAndComponentsDto;
import backend.weatherApp.dto.PollutionListDto;
import backend.weatherApp.enums.PollutionSeverity;
import backend.weatherApp.model.Location;
import backend.weatherApp.model.Pollution;
import backend.weatherApp.repository.ILocationRepository;
import backend.weatherApp.repository.IPollutionRepository;
import lombok.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.*;
import java.util.*;
/**
 * @author Elif Ece Can
 * @date 17.07.2024
 */
@Service
@RequiredArgsConstructor
public class AirPollutionService implements IAirPollutionService
{
    private final RestTemplate restTemplate;
    private final ILocationRepository locationRepository;
    private final IPollutionRepository pollutionRepository;
    private final ApiKeyConfig apiKeyConfig;
    private static final Logger logger = LoggerFactory.getLogger(AirPollutionService.class);
    private static final String AIR_POLLUTION_URL = "http://api.openweathermap.org/data/2.5/air_pollution/history?";
    private static final String GEO_URL = "http://api.openweathermap.org/geo/1.0/direct?q=";
    private static final ZoneId SYSTEM_DEFAULT_ZONE_ID = ZoneId.of("UTC");

    private PollutionSeverity pollutantLevelCO (Double concentrationCO)
    {
        concentrationCO /= 1000;
        if (concentrationCO <= 1) {
            return PollutionSeverity.GOOD;
        } else if (concentrationCO <= 2) {
            return PollutionSeverity.SATISFACTORY;
        } else if (concentrationCO <= 10) {
            return PollutionSeverity.MODERATE;
        } else if (concentrationCO <= 17) {
            return PollutionSeverity.POOR;
        } else if (concentrationCO <= 34) {
            return PollutionSeverity.SEVERE;
        } else {
            return PollutionSeverity.HAZARDOUS;
        }
    }
    private PollutionSeverity pollutantLevelS02 (Double concentrationS02)
    {
        if (concentrationS02 <= 40) {
            return PollutionSeverity.GOOD;
        } else if (concentrationS02 <= 80) {
            return PollutionSeverity.SATISFACTORY;
        } else if (concentrationS02 <= 380) {
            return PollutionSeverity.MODERATE;
        } else if (concentrationS02 <= 800) {
            return PollutionSeverity.POOR;
        } else if (concentrationS02 <= 1600) {
            return PollutionSeverity.SEVERE;
        } else {
            return PollutionSeverity.HAZARDOUS;
        }
    }
    private PollutionSeverity pollutantLevelO3 (Double concentrationO3)
    {
        if (concentrationO3 <= 50) {
            return PollutionSeverity.GOOD;
        } else if (concentrationO3 <= 100) {
            return PollutionSeverity.SATISFACTORY;
        } else if (concentrationO3 <= 168) {
            return PollutionSeverity.MODERATE;
        } else if (concentrationO3 <= 208) {
            return PollutionSeverity.POOR;
        } else if (concentrationO3 <= 748) {
            return PollutionSeverity.SEVERE;
        } else {
            return PollutionSeverity.HAZARDOUS;
        }
    }
    private Map<String, PollutionSeverity> getPollutantValues(Double[] p)
    {
        Map <String, PollutionSeverity> values = new HashMap<>();
        values.put("co",pollutantLevelCO(p[0]));
        values.put("so2",pollutantLevelS02(p[1]));
        values.put("o3",pollutantLevelO3(p[2]));
        return values;
    }
    public LocationDto getCityGeocoding(String cityName) {
        String url = GEO_URL + cityName.replaceAll("\\s", "") + "&limit=1&appid=" + apiKeyConfig.getApiKey();
        LocationDto[] locationData = restTemplate.getForEntity(url, LocationDto[].class).getBody();
        if (locationData != null) {
            Location location = new Location(locationData[0].getName(), locationData[0].getLat(), locationData[0].getLon());
            locationRepository.save(location);
            return locationData[0];
        } else {
            logger.error("Location data is not found for the city: {}", cityName);
            return null;
        }
    }
    public Long convertToUnixTime(LocalDate date)
    {
        ZonedDateTime zdt = date.atStartOfDay(SYSTEM_DEFAULT_ZONE_ID);
        return zdt.toEpochSecond();
    }
    private static Double[] getGasRates(List<PollutionDateAndComponentsDto> valuesList)
    {
        double co2Rate = valuesList.stream()
                .mapToDouble(p -> p.getComponents().getCo()).average().orElse(0.0);
        double so2Rate = valuesList.stream()
                .mapToDouble(p -> p.getComponents().getSo2()).average().orElse(0.0);
        double o3Rate = valuesList.stream()
                .mapToDouble(p -> p.getComponents().getO3()).average().orElse(0.0);
        return new Double[]{co2Rate, so2Rate, o3Rate};
    }
    public CityAndPollutionDto sort(CityAndPollutionDto dto)
    {
        if (dto.getResults()!= null)
        {
            Collections.sort(dto.getResults(), Comparator.comparing(Pollution::getDate));
        }
        return dto;
    }
    public CityAndPollutionDto getPollutionInfo(String cityName, LocalDate start, LocalDate end)
    {
        LocationDto locationDto = getCityGeocoding(cityName);
        Location location = new Location(locationDto.getName(), locationDto.getLat(), locationDto.getLon());
        String url = AIR_POLLUTION_URL + "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() +
                "&start=" + convertToUnixTime(start) + "&end=" + convertToUnixTime(end) + "&appid=" + apiKeyConfig.getApiKey();

        ResponseEntity<PollutionListDto> response = restTemplate.getForEntity(url, PollutionListDto.class);
        PollutionListDto resultDto = response.getBody();
        if (resultDto == null) { return null; }

        Map<LocalDate, List<PollutionDateAndComponentsDto>> groupedData = new HashMap<>();
        for (PollutionDateAndComponentsDto pollutionData : resultDto.getList())
        {
            LocalDate date = Instant.ofEpochSecond(pollutionData.getDt())
                    .atZone(SYSTEM_DEFAULT_ZONE_ID)
                    .toLocalDate();
            if (!groupedData.containsKey(date)) {
                groupedData.put(date, new ArrayList<>());
            }
            groupedData.get(date).add(pollutionData);
        }

        List<Pollution> allPollutionsList = new ArrayList<>();
        List<PollutionComponentAndSeverity> pollutionComponentsList = new ArrayList<>();

        for (Map.Entry<LocalDate, List<PollutionDateAndComponentsDto>> element : groupedData.entrySet())
        {
            LocalDate date = element.getKey();
            List<Pollution> pollutionsInDatabase = pollutionRepository.findByCityAndDate(cityName, date);
            if (pollutionsInDatabase != null && !pollutionsInDatabase.isEmpty())
            {
                logger.info("Data is retrieved from the database.");
                allPollutionsList.addAll(pollutionsInDatabase);
                continue;
            }
            List<PollutionDateAndComponentsDto> valuesList = element.getValue();
            Double[] averageGasRates = getGasRates(valuesList);
            PollutionComponentAndSeverity componentAndSeverity = new PollutionComponentAndSeverity(date, getPollutantValues(averageGasRates));

            Pollution currentObj = new Pollution(locationDto.getName(), componentAndSeverity.getSeverityMap().get("co").name(),
                    componentAndSeverity.getSeverityMap().get("so2").name(),
                    componentAndSeverity.getSeverityMap().get("o3").name(),
                    Instant.ofEpochSecond(valuesList.get(0).getDt()).atZone(SYSTEM_DEFAULT_ZONE_ID).toLocalDate()
            );
            pollutionComponentsList.add(componentAndSeverity);
            allPollutionsList.add(currentObj);
            logger.info("Data is retrieved from the API.");
        }
        pollutionRepository.saveAll(allPollutionsList);
        CityAndPollutionDto result = new CityAndPollutionDto(cityName, allPollutionsList);
        return sort(result);
    }

    public String deleteAllPollutionData()
    {
        pollutionRepository.deleteAll();
        return "All existing pollution data is deleted.";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class PollutionComponentAndSeverity
    {
        private LocalDate date;
        private Map<String, PollutionSeverity> severityMap;
    }
}