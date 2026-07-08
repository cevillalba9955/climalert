package ar.edu.utn.frba.ddsi.climaalert.repositories;

import ar.edu.utn.frba.ddsi.climaalert.domain.Clima;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class ClimaRepository {
  private final Map<Long, Clima> registros = new ConcurrentHashMap<>();
  @Getter
  private volatile Clima ultimoRegistro;

  public void agregarRegistro(Clima clima) {
    // Map no agrega registro repetido ( mismo getLastUpdateEpoch)
    registros.put(clima.getLastUpdateEpoch(), clima);
    this.ultimoRegistro = clima;
  }

}
