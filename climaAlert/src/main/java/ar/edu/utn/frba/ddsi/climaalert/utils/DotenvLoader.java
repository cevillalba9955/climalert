package ar.edu.utn.frba.ddsi.climaalert.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DotenvLoader {

  private DotenvLoader() {
  }

  public static void load() {
    Path envFile = Path.of(".env");
    if (!Files.exists(envFile)) {
      return;
    }

    try {
      List<String> lines = Files.readAllLines(envFile);
      for (String line : lines) {
        String trimmed = line.trim();
        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
          continue;
        }

        int separatorIndex = trimmed.indexOf('=');
        if (separatorIndex < 0) {
          continue;
        }

        String key = trimmed.substring(0, separatorIndex).trim();
        String value = trimmed.substring(separatorIndex + 1).trim();
        if (System.getProperty(key) == null) {
          System.setProperty(key, value);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("No se pudo leer el archivo .env", e);
    }
  }
}
