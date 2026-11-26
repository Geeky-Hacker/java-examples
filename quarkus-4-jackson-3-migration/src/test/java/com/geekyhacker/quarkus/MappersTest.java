package com.geekyhacker.quarkus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

class MappersTest {

  @Test
  void trailingTokensCanBeRejectedOrAllowed() {
    JsonMapper strict = Mappers.create(b -> b.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS));
    JsonMapper lenient = Mappers.create(b -> b.disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS));
    String json = "{\"name\":\"api\",\"retries\":3} {}";

    assertThrows(JacksonException.class, () -> strict.readValue(json, Settings.class));
    assertEquals(3, lenient.readValue(json, Settings.class).retries());
  }

  @Test
  void nullForPrimitiveCanBeRejectedOrAllowed() {
    JsonMapper strict = Mappers.create(b -> b.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES));
    JsonMapper lenient = Mappers.create(b -> b.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES));
    String json = "{\"name\":\"api\",\"retries\":null}";

    assertThrows(JacksonException.class, () -> strict.readValue(json, Settings.class));
    assertEquals(0, lenient.readValue(json, Settings.class).retries());
  }

  @Test
  void durationsCanBeWrittenAsIsoStrings() {
    Settings settings = new Settings("api", 3, Duration.ofSeconds(30));
    JsonMapper iso = Mappers.create(b -> b.disable(DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS));
    JsonMapper numeric = Mappers.create(b -> b.enable(DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS));

    assertTrue(iso.writeValueAsString(settings).contains("\"PT30S\""));
    assertFalse(numeric.writeValueAsString(settings).contains("PT30S"));
  }
}
