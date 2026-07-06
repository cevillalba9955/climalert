package ar.edu.utn.frba.ddsi.climaalert.services;

import ar.edu.utn.frba.ddsi.climaalert.client.WeatherApiClient;
import ar.edu.utn.frba.ddsi.climaalert.client.dto.WeatherResponseDto;
import ar.edu.utn.frba.ddsi.climaalert.domain.Clima;
import ar.edu.utn.frba.ddsi.climaalert.repositories.ClimaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class ClimaService {
  private final ClimaRepository climaRepository;

  private final WeatherApiClient weatherApiClient;
  @Autowired
  public ClimaService(WeatherApiClient weatherApiClient, ClimaRepository climaRepository) {
    this.weatherApiClient = weatherApiClient;
    this.climaRepository = climaRepository;
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void consultarClima(){
    log.info("Consultando el clima");
    WeatherResponseDto response =  this.weatherApiClient.consultar();

    Clima clima = Clima.builder()
        .lastUpdateEpoch(response.getLastUpdateEpoch())
        .humedad(response.getHumedad())
        .temperatura(response.getTemperatura())
        .fechaHora(LocalDateTime.parse(response.getFechaHora(), DateTimeFormatter.ISO_DATE_TIME))
        .build();
    this.climaRepository.agregarRegistro(clima);

  }
}
