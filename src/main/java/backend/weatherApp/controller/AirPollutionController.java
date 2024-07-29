package backend.weatherApp.controller;

import backend.weatherApp.configuration.ApiKeyConfig;
import backend.weatherApp.dto.CityAndPollutionDto;
import backend.weatherApp.enums.ErrorCode;
import backend.weatherApp.exception.CustomException;
import backend.weatherApp.service.AirPollutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
/**
 * Author: Elif Ece Can
 * Date: 17.07.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/air-pollution")
public class AirPollutionController {
    private final AirPollutionService airPollutionService;

    @Operation(
            summary = "Retrieve pollution data for the past week in a specified city",
            description = "Fetch detailed pollution information for the past week in the specified city. " +
                    "This endpoint provides the severity levels of key pollutants, including carbon monoxide (CO)," +
                    " sulfur dioxide (SO2), and ozone (O3). The response includes the daily measured levels and an" +
                    " overall assessment of the air quality, enabling users to monitor pollution trends and take " +
                    "necessary precautions."
    )
    @GetMapping("/{cityName}")
    public ResponseEntity<CityAndPollutionDto> getPollutionInformation(
            @Parameter(description = "The name of the city to retrieve pollution data for", example = "London")
            @PathVariable String cityName) {
        if (!validCity(cityName))
        {
            throw new CustomException("Invalid city name: " + cityName);
        }
        LocalDate today = LocalDate.now();
        LocalDate start = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate end = start.plusDays(6);
        return ResponseEntity.ok(airPollutionService.getPollutionInfo(cityName, start, end));
    }


    @Operation(
            summary = "Retrieve pollution data for a specified date range in a city",
            description = "Fetch detailed pollution information for a user-defined date range in the specified city. " +
                    "This endpoint allows users to select start and end dates to view pollution data, providing the " +
                    "severity levels of key pollutants, including carbon monoxide (CO), sulfur dioxide (SO2), " +
                    "and ozone (O3). The response includes daily measured levels and an overall assessment of the" +
                    " air quality for the selected period, helping users monitor pollution trends and make informed decisions."
    )
    @GetMapping("/{cityName}/{startDate}/{endDate}")
    public ResponseEntity<CityAndPollutionDto> getPollutionInformation(
            @Parameter(description = "The name of the city to retrieve pollution data for", example = "London") @PathVariable String cityName,
            @Parameter(description = "The start date of the range to retrieve pollution data for, formatted as YYYY-MM-DD", example = "2024-07-01") @PathVariable String startDate,
            @Parameter(description = "The end date of the range to retrieve pollution data for, formatted as YYYY-MM-DD", example = "2024-07-07") @PathVariable String endDate) {
        if (!validCity(cityName))
        {
            throw new CustomException("Invalid city name: " + cityName);
        }
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        ErrorCode errorCode = validDates(start, end);
        switch (errorCode)
        {
            case FUTURE_DATE:
                throw new CustomException("Invalid date format: Dates in the future are not allowed");
            case END_IS_BEFORE_START:
                throw new CustomException("Invalid date format: End day: " + endDate + " is before start day: " + startDate);
            case NO_DATA_FOR_DATE:
                throw new CustomException("Invalid date format: Pollution data can be retrieved from 27 November 2020 to current day");
            default:
                return ResponseEntity.ok(airPollutionService.getPollutionInfo(cityName, start, end));
        }
    }

    @DeleteMapping("/clear-database")
    public ResponseEntity<String> deleteAllPollutionData()
    {
        return ResponseEntity.ok(airPollutionService.deleteAllPollutionData());
    }

    private boolean validCity(String cityName)
    {
        return Arrays.asList("london", "barcelona", "ankara", "tokyo", "mumbai").contains(cityName.toLowerCase());
    }

    private ErrorCode validDates(LocalDate start, LocalDate end)
    {
        final int firstDateForPollutionData = 1606435200;

        if(convertToUnixTime(start) > convertToUnixTime(LocalDate.now()) || convertToUnixTime(end) > convertToUnixTime(LocalDate.now())) {
            return ErrorCode.FUTURE_DATE;
        }

        if(convertToUnixTime(start) >= firstDateForPollutionData) {
            if(convertToUnixTime(end) >= convertToUnixTime(start)) {
                return ErrorCode.VALID;
            } else {
                return ErrorCode.END_IS_BEFORE_START;
            }
        }
        return ErrorCode.NO_DATA_FOR_DATE;
    }

    public Long convertToUnixTime(LocalDate date)
    {
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zdt = date.atStartOfDay(zoneId);
        return zdt.toEpochSecond();
    }
}
