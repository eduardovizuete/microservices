package com.codearqui.serviceorder.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfiguration {

    private static final String BACKEND_IN = "microInventory";

    @Bean
    CircuitBreaker servicesCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker(BACKEND_IN);
    }

    @Bean
    Retry servicesRetry(RetryRegistry registry) {
        return registry.retry(BACKEND_IN);
    }

    @Bean
    RateLimiter servicesRateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter(BACKEND_IN);
    }

    @Bean
    TimeLimiter serviceTimeLimiter(TimeLimiterRegistry registry) {
        return registry.timeLimiter(BACKEND_IN);
    }

}
