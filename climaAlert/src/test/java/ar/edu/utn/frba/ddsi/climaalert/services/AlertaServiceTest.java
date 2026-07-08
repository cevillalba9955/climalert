package ar.edu.utn.frba.ddsi.climaalert.services;

import ar.edu.utn.frba.ddsi.climaalert.domain.Clima;
import ar.edu.utn.frba.ddsi.climaalert.repositories.ClimaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertaServiceTest {

  @Mock
  private ClimaRepository climaRepository;

  @Mock
  private EmailService emailService;

  private AlertaService alertaService;

  private static Clima clima(long epoch, double temperatura, double humedad) {
    return Clima.builder()
        .lastUpdateEpoch(epoch)
        .fechaHora(LocalDateTime.now())
        .temperatura(temperatura)
        .humedad(humedad)
        .build();
  }

  // 10 registros consecutivos: alternan entre alerta y no alerta para ejercitar
  // la lógica de "solo avisar cuando cambia el estado" de revisarClima().
  private static final List<Clima> CLIMAS = List.of(
      clima(1, 20, 40),  // sin alerta - inicializa climaActual, no dispara
      clima(2, 25, 45),  // sin alerta - sin cambio de estado
      clima(3, 40, 50),  // alerta por temperatura - cambia de estado -> dispara
      clima(4, 40, 55),  // alerta - sin cambio de estado
      clima(5, 30, 50),  // sin alerta - cambia de estado -> dispara
      clima(6, 28, 48),  // sin alerta - sin cambio de estado
      clima(7, 25, 75),  // alerta por humedad - cambia de estado -> dispara
      clima(8, 30, 90),  // alerta - sin cambio de estado
      clima(9, 22, 30),  // sin alerta - cambia de estado -> dispara
      clima(10, 24, 35)  // sin alerta - sin cambio de estado
  );

  @BeforeEach
  void setUp() {
    alertaService = spy(new AlertaService(climaRepository, emailService));
  }

  @Test
  void seEnviaAlertaSoloCuandoCambiaElEstadoDeAlerta() {
    when(climaRepository.getUltimoRegistro()).thenReturn(
        CLIMAS.get(0), CLIMAS.get(1), CLIMAS.get(2), CLIMAS.get(3), CLIMAS.get(4),
        CLIMAS.get(5), CLIMAS.get(6), CLIMAS.get(7), CLIMAS.get(8), CLIMAS.get(9)
    );

    for (int i = 0; i < CLIMAS.size(); i++) {
      alertaService.revisarClima();
    }

    // enviarAlerta() no recibe el Clima como argumento (lee el estado interno del service),
    // así que la secuencia de estados se verifica a través de setEstadoAlerta(boolean).
    ArgumentCaptor<Boolean> estadoCaptor = ArgumentCaptor.forClass(Boolean.class);
    verify(alertaService, times(CLIMAS.size())).setEstadoAlerta(estadoCaptor.capture());
    assertThat(estadoCaptor.getAllValues())
        .containsExactly(false, false, true, true, false, false, true, true, false, false);

    verify(alertaService, times(4)).enviarAlerta();
  }

  @Test
  void noSeEnviaAlertaCuandoNoHayRegistros() {
    when(climaRepository.getUltimoRegistro()).thenReturn(null);

    alertaService.revisarClima();

    verify(alertaService, never()).enviarAlerta();
  }
}
