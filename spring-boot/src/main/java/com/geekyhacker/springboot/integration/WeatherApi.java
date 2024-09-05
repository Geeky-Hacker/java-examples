package com.geekyhacker.springboot.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeatherApi {

  private static final Logger logger = LoggerFactory.getLogger(WeatherApi.class);

  @Value("${api.endpoint}")
  private String apiEndpoint;

  @Value("${api.key}")
  private String apiKey;

  public double getCurrentTemperature() {
    logger.info("Getting the current weather temperature from {} endpoint with key {}", apiEndpoint, apiKey);
    return -1;
  }

  public String getApiEndpoint() {
    return apiEndpoint;
  }

  public String getApiKey() {
    return apiKey;
  }
}
