package com.recruitment.platform.config;

import com.recruitment.platform.config.properties.GeminiProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

/**
 * WebClient bean dedicated to the AI (Gemini) module.
 *
 * A single named/qualified WebClient - rather than relying on Spring Boot's
 * auto-configured default - keeps Gemini-specific timeout/base-URL settings
 * from leaking into or colliding with any other outbound HTTP client the app
 * might need later (e.g. a future cloud storage SDK with its own client).
 */
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final GeminiProperties geminiProperties;

    @Bean(name = "geminiWebClient")
    public WebClient geminiWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) geminiProperties.timeoutMs())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(geminiProperties.timeoutMs(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(geminiProperties.timeoutMs(), TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(geminiProperties.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}