package ru.otus.example.weatherdemo.services;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.example.weatherdemo.apiclients.OpenWeatherApiClient;
import ru.otus.example.weatherdemo.models.Weather;

import java.lang.reflect.Type;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenWeatherService implements WeatherService {

    private final OpenWeatherApiClient apiClient;

    @Value("${app.openweather-api-key}")
    private String apiKey;

    @Value("${app.city-name}")
    private String cityName;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Weather.class, new JsonDeserializer<Weather>(){
        @Override
        public Weather deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject main = jsonElement.getAsJsonObject().getAsJsonObject("main");
            return new Weather("OpenWeatherMap", cityName, main.get("temp").getAsString());
        }
    }).create();

    private final RestTemplate restTemplate;


    @Override
    public List<Weather> gWeather() {
        //String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName +"&units=metric&lang=ru&appid=" + apiKey;
        //String weatherString = restTemplate.getForObject(url, String.class);
        String weatherString = apiClient.gWeather(apiKey, cityName);
        return List.of(gson.fromJson(weatherString, Weather.class));
    }
}
