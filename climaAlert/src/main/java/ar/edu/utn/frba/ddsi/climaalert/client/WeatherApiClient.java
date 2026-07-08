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

    JsonNode current = json.get("current");

    WeatherResponseDto response = new WeatherResponseDto(
        current.get("last_updated_epoch").asLong(),
        current.get("last_updated").asString(),
        current.get("temp_c").asDouble(),
        current.get("humidity").asDouble(),
        current.get("condition").get("text").asString(),
        current.get("wind_kph").asDouble(),
        current.get("wind_degree").asDouble(),
        current.get("precip_mm").asDouble(),
        current.get("cloud").asDouble(),
        current.get("feelslike_c").asDouble(),
        current.get("dewpoint_c").asDouble(),
        current.get("vis_km").asDouble(),
        current.get("uv").asDouble(),
        current.get("will_it_rain").asDouble(),
        current.get("chance_of_rain").asDouble(),
        current.get("will_it_snow").asDouble(),
        current.get("chance_of_snow").asDouble()
    );
    return response;
  }
}
