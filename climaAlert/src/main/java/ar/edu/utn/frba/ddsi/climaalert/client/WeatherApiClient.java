package ar.edu.utn.frba.ddsi.climaalert.client;

import ar.edu.utn.frba.ddsi.climaalert.client.dto.WeatherResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;

@Slf4j
@Service
public class WeatherApiClient {
  private final RestTemplate restTemplate;
  private final String apiKey;

  @Autowired
  public WeatherApiClient(RestTemplate restTemplate, @Value("${weatherapi.key}") String apiKey) {
    this.restTemplate = restTemplate;
    this.apiKey = apiKey;
  }

  public WeatherResponseDto consultar() {
    String url = UriComponentsBuilder
        .fromUriString("https://api.weatherapi.com/v1/current.json")
        .queryParam("key", apiKey)
        .queryParam("q", "ezeiza")
        .queryParam("lang", "es")
        .toUriString();

    JsonNode json = restTemplate.getForObject(url, JsonNode.class);

    //TODO: verificar json valido sino lanzar excepción

    WeatherResponseDto response = new WeatherResponseDto(
        json.get("current").get("last_updated_epoch").asLong(),
        json.get("current").get("last_updated").asString(),
        json.get("current").get("temp_c").asDouble(),
        json.get("current").get("humidity").asInt()
    );
    return response;
  }
}
