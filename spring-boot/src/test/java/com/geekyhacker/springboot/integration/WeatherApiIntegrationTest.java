package com.geekyhacker.springboot.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WeatherApiIntegrationTest {

  @Autowired
  private WeatherApi weatherApi;

  @Test
  void testGetWeatherApiEndpointAddress() {
    String result = weatherApi.getApiEndpoint();

    assertEquals("https://dummyjson.com/test", result);
  }
}