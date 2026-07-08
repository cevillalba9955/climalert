package ar.edu.utn.frba.ddsi.climaalert.services;

import ar.edu.utn.frba.ddsi.climaalert.domain.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
public class EmailService {
  Queue<Mail> mailsPendientes = new LinkedList<>();
  private final String emisor;
  private final List<String> destinos;
  private final SendGrid sendGrid;

  @Autowired
  public EmailService(
      @Value("${email.emisor}") String emisor,
      @Value("#{'${email.destinatarios}'.split(',')}") List<String> destinos,
      SendGrid sendGrid)
  {
    this.emisor = emisor;
    this.destinos = destinos;
    this.sendGrid = sendGrid;
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

  private void send(Mail mail) throws IOException {
    Email from = new Email(mail.getOrigen());
    Email to = new Email(mail.getDestino());
    Content content = new Content("text/html", mail.getMensaje());
    com.sendgrid.helpers.mail.Mail sendGridMail =
        new com.sendgrid.helpers.mail.Mail(from, mail.getAsunto(), to, content);

    Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(sendGridMail.build());

    Response response = sendGrid.api(request);
    if (response.getStatusCode() >= 300) {
      throw new IllegalStateException(
          "SendGrid respondió " + response.getStatusCode() + ": " + response.getBody());
    }

    log.info("Mail enviado... {} ", mail.getDestino());
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


