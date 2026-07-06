package ar.edu.utn.frba.ddsi.climaalert;

import ar.edu.utn.frba.ddsi.climaalert.utils.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClimaAlertApplication {

  public static void main(String[] args) {
    DotenvLoader.load();
    SpringApplication.run(ClimaAlertApplication.class, args);
  }

}
