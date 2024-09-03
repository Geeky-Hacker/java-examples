package com.geekyhacker.springboot.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WeatherApiTest {

  @InjectMocks
  private WeatherApi weatherApi;

  @BeforeEach
  void initialize() {
    ReflectionTestUtils.setField(weatherApi, "apiEndpoint", "https://weather-api.com");
  }

  @Test
  void testGetWeatherApiEndpointAddress() {
    String result = weatherApi.getApiEndpoint();

    assertEquals("https://weather-api.com", result);
  }
}