package ar.edu.utn.frba.ddsi.climaalert.repositories;

import ar.edu.utn.frba.ddsi.climaalert.domain.Clima;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ClimaRepository {
  private final Map<Long, Clima> registros = new ConcurrentHashMap<>();

  public void agregarRegistro(Clima clima) {
    registros.put(clima.getLastUpdateEpoch(), clima);
  }
}
