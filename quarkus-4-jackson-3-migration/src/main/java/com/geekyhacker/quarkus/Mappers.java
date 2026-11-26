package com.geekyhacker.quarkus;

import tools.jackson.databind.json.JsonMapper;

/**
 * Builds immutable Jackson 3 mappers, in the same builder style as Quarkus 4.0's
 * JsonMapperBuilderCustomizer.
 */
public final class Mappers {

  @FunctionalInterface
  public interface Customizer {
    void customize(JsonMapper.Builder builder);
  }

  private Mappers() {
  }

  public static JsonMapper create(Customizer... customizers) {
    JsonMapper.Builder builder = JsonMapper.builder();
    for (Customizer customizer : customizers) {
      customizer.customize(builder);
    }
    return builder.build();
  }
}
