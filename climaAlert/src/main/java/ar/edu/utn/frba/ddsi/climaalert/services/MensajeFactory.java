package ar.edu.utn.frba.ddsi.climaalert.services;

import ar.edu.utn.frba.ddsi.climaalert.domain.Clima;
import org.springframework.stereotype.Component;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MensajeFactory {

  public String generarHtml(Clima clima) {
    Map<String, String> datos = datos(clima);

    StringBuilder html = new StringBuilder();
    html.append("<table style=\"border-collapse: collapse; font-family: sans-serif;\">");
    datos.forEach((etiqueta, valor) -> html
        .append("<tr>")
        .append("<td style=\"border: 1px solid #ccc; padding: 4px 8px; font-weight: bold;\">")
        .append(etiqueta)
        .append("</td>")
        .append("<td style=\"border: 1px solid #ccc; padding: 4px 8px;\">")
        .append(valor)
        .append("</td>")
        .append("</tr>"));
    html.append("</table>");

    return html.toString();
  }

  public String generarTexto(Clima clima) {
    Map<String, String> datos = datos(clima);

    StringBuilder texto = new StringBuilder();
    datos.forEach((etiqueta, valor) -> texto
        .append(etiqueta)
        .append(": ")
        .append(valor)
        .append(System.lineSeparator()));

    return texto.toString();
  }

  private Map<String, String> datos(Clima clima) {
    Map<String, String> datos = new LinkedHashMap<>();
    datos.put("Fecha", String.valueOf(clima.getFechaHora()));
    datos.put("Temperatura", String.format("%.1f°C", clima.getTemperatura()));
    datos.put("Humedad", String.format("%.1f%%", clima.getHumedad()));
    datos.put("Condición", clima.getCondicion());
    datos.put("Velocidad del viento", String.format("%.1f km/h", clima.getVelocidadViento()));
    datos.put("Dirección del viento", String.format("%.0f°", clima.getDireccionViento()));
    datos.put("Precipitación", String.format("%.1f mm", clima.getPrecipitacion()));
    datos.put("Nubosidad", String.format("%.1f%%", clima.getNubosidad()));
    datos.put("Sensación térmica", String.format("%.1f°C", clima.getSensacionTermica()));
    datos.put("Punto de rocío", String.format("%.1f°C", clima.getPuntoRocio()));
    datos.put("Visibilidad", String.format("%.1f km", clima.getVisibilidad()));
    datos.put("Radiación UV", String.format("%.1f", clima.getRadiacionUV()));
    datos.put("Lluvia", String.format("%.0f", clima.getLluvia()));
    datos.put("Probabilidad de lluvia", String.format("%.0f%%", clima.getProbabilidadLluvia()));
    datos.put("Nieve", String.format("%.0f", clima.getNieve()));
    datos.put("Probabilidad de nieve", String.format("%.0f%%", clima.getProbabilidadNieve()));
    return datos;
  }
}
