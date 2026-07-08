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

  @Scheduled(fixedDelay = 300000) // 5 * 60 * 1000
  public void consultarClima(){
    WeatherResponseDto response =  this.weatherApiClient.consultar();

    Clima clima = Clima.builder()
        .lastUpdateEpoch(response.getLastUpdateEpoch())
        .fechaHora(LocalDateTime.parse(response.getFechaHora(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
        .temperatura(response.getTemperatura())
        .humedad(response.getHumedad())
        .condicion(response.getCondicion())
        .velocidadViento(response.getVelocidadViento())
        .direccionViento(response.getDireccionViento())
        .precipitacion(response.getPrecipitacion())
        .nubosidad(response.getNubosidad())
        .sensacionTermica(response.getSensacionTermica())
        .puntoRocio(response.getPuntoRocio())
        .visibilidad(response.getVisibilidad())
        .radiacionUV(response.getRadiacionUV())
        .lluvia(response.getLluvia())
        .probabilidadLluvia(response.getProbabilidadLluvia())
        .nieve(response.getNieve())
        .probabilidadNieve(response.getProbabilidadNieve())
        .build();
    this.climaRepository.agregarRegistro(clima);

    log.info("fecha = {} : temperatura= {} C, humedad= {}%"
        ,clima.getFechaHora()
        ,clima.getTemperatura()
        ,clima.getHumedad());
  }
}
