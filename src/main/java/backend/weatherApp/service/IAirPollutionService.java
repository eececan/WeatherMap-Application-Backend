package backend.weatherApp.service;

import backend.weatherApp.dto.LocationDto;

public interface IAirPollutionService
{
    LocationDto getCityGeocoding(String cityName);
    String deleteAllPollutionData();
}
