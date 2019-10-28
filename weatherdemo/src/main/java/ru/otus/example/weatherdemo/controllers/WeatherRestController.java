package ru.otus.example.weatherdemo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.example.weatherdemo.models.Weather;
import ru.otus.example.weatherdemo.services.WeatherService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WeatherRestController {

    //private final List<WeatherService> weatherServices;

    private final WeatherService weatherService;

    public WeatherRestController(@Qualifier("weatherAggregationService") WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("api/weather")
    public List<Weather> getWeather() {
        return weatherService.gWeather();
/*
        List<Weather> weatherList = new ArrayList<>();
        weatherServices.forEach(ws -> weatherList.addAll(ws.gWeather()));
        return weatherList;
*/
    }

}
