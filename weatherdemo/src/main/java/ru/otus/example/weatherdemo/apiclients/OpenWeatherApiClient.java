package ru.otus.example.weatherdemo.apiclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "open-weather-api-client", url = "http://api.openweathermap.org:80/data/2.5/")
public interface OpenWeatherApiClient {
    @GetMapping("/weather?q={cityName}&units=metric&lang=ru&appid={apiKey}")
    String gWeather(@PathVariable("apiKey") String apiKey, @PathVariable("cityName") String cityName);
}
