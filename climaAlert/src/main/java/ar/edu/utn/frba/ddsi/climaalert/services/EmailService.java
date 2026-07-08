package ar.edu.utn.frba.ddsi.climaalert.services;

import ar.edu.utn.frba.ddsi.climaalert.domain.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
public class EmailService {
  Queue<Mail> mailsPendientes = new LinkedList<>();
  private final String emisor;
  private final List<String> destinos;

  @Autowired
  public EmailService(
      @Value("${email.emisor}") String emisor,
      @Value("#{'${email.destinatarios}'.split(',')}") List<String> destinos)
  {
    this.emisor = emisor;
    this.destinos = destinos;
  }

  public void enviar(String asunto, String mensaje) {

    for(String destino: destinos) {

      Mail mail = Mail.builder()
          .origen(emisor)
          .destino(destino)
          .asunto(asunto)
          .mensaje(mensaje)
          .build();
      mailsPendientes.add(mail);
    }
  }

  private void send(Mail mail) {
    // implementar
    log.info(mail.toString());
  }
  
  @Scheduled(cron = "* */5 * * * *")
  void enviarPendientes() {
    if (mailsPendientes == null) {
      return;
    }

    int cantidadPendientes = mailsPendientes.size();
    for (int i = 0; i < cantidadPendientes; i++) {
      Mail mail = mailsPendientes.poll();
      if (mail == null) {
        break;
      }
      try {
        send(mail);
      } catch (Exception e) {
        log.warn("Error al enviar mail, se reintentará en la próxima ejecución: {}", mail, e);
        mailsPendientes.offer(mail);
      }
    }
  }
}


