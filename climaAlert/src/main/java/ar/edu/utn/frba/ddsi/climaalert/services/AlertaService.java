package ar.edu.utn.frba.ddsi.climaalert.services;

import ar.edu.utn.frba.ddsi.climaalert.domain.Clima;
import ar.edu.utn.frba.ddsi.climaalert.domain.CondicionHumedadAlta;
import ar.edu.utn.frba.ddsi.climaalert.domain.CondicionTemperaturaAlta;
import ar.edu.utn.frba.ddsi.climaalert.domain.ICondicion;
import ar.edu.utn.frba.ddsi.climaalert.repositories.ClimaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AlertaService {
  private final ClimaRepository climaRepository;
  private final EmailService emailService;
  private final MensajeFactory mensajeFactory;
  private Clima climaActual;
  private final List<ICondicion> condiciones;
  private boolean estadoAlerta = false;


  public AlertaService(ClimaRepository climaRepository, EmailService emailService, MensajeFactory mensajeFactory) {
    this.climaRepository = climaRepository;
    this.emailService = emailService;
    this.mensajeFactory = mensajeFactory;
    this.condiciones = new ArrayList<>();
    this.condiciones.add(new CondicionTemperaturaAlta(35));
    this.condiciones.add(new CondicionHumedadAlta(60));
  }

  public boolean hayAlerta(Clima clima) {
    for (ICondicion condicion : condiciones) {
      if (!condicion.esNormal(clima)) {
        return true;
      }
    }
    return false;
  }


  @Scheduled(fixedDelay =60000) // 60 * 1000
  public void revisarClima(){
    Clima ultimoClima = this.climaRepository.getUltimoRegistro();

    if (ultimoClima == null) {
      return;
    }
      this.climaActual = ultimoClima;
      setEstadoAlerta(hayAlerta(this.climaActual));
  }

  void setEstadoAlerta(boolean estadoAlerta) {
    if (estadoAlerta != this.estadoAlerta) {
      this.estadoAlerta = estadoAlerta;
      enviarAlerta();
    }
  }

  void enviarAlerta(){
    String asunto = estadoAlerta
        ? "Alerta: condiciones climáticas fuera de rango"
        : "Alerta finalizada: condiciones climáticas normalizadas";
    String mensaje = mensajeFactory.generarHtml(climaActual);


    emailService.enviar(asunto,mensaje);
    log.info(asunto);
    log.info(mensajeFactory.generarTexto(climaActual));

  }
}
