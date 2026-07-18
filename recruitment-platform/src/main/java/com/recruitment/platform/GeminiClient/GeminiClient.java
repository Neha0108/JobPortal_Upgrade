package com.recruitment.platform.GeminiClient;

import com.recruitment.platform.common.exception.AiAnalysisException;
import com.recruitment.platform.config.properties.GeminiProperties;
import com.recruitment.platform.dto.GeminiContent;
import com.recruitment.platform.dto.GeminiPart;
import com.recruitment.platform.dto.GeminiRequest;
import com.recruitment.platform.dto.GeminiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.Base64;
import java.util.List;

/**
 * Thin wrapper around the Gemini generateContent endpoint. Deliberately
 * synchronous (.block()) despite using WebClient internally - the rest of
 * this application is a blocking servlet stack (Spring MVC), and every
 * caller of this client already runs on a request thread or an @Async
 * worker thread, so there's no reactive pipeline upstream to preserve.
 *
 * NOT annotated with @RequiredArgsConstructor: the WebClient bean is
 * disambiguated with @Qualifier, and Lombok does not reliably propagate
 * field-level @Qualifier onto a generated constructor parameter - an
 * explicit constructor avoids a subtle, hard-to-diagnose wiring bug.
 */
@Component
@Slf4j
public class GeminiClient {

    private final WebClient geminiWebClient;
    private final GeminiProperties geminiProperties;

    public GeminiClient(@Qualifier("geminiWebClient") WebClient geminiWebClient, GeminiProperties geminiProperties) {
        this.geminiWebClient = geminiWebClient;
        this.geminiProperties = geminiProperties;
    }

    /**
     * Sends a text prompt plus an optional file (resume) to Gemini and returns
     * the raw text of its first response candidate. Callers are responsible
     * for instructing Gemini (via the prompt) to return valid JSON and for
     * parsing that JSON themselves - this method only handles the transport.
     */
    public String generate(String prompt, byte[] fileBytes, String mimeType) {
        List<GeminiPart> parts = fileBytes != null
                ? List.of(GeminiPart.ofText(prompt), GeminiPart.ofFile(mimeType, Base64.getEncoder().encodeToString(fileBytes)))
                : List.of(GeminiPart.ofText(prompt));

        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiContent(parts)),
                new GeminiRequest.GenerationConfig("application/json"));

        try {
            GeminiResponse response = geminiWebClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/{model}:generateContent")
                            .queryParam("key", geminiProperties.apiKey())
                            .build(geminiProperties.model()))
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .timeout(Duration.ofMillis(geminiProperties.timeoutMs()))
                    .block();

            if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
                throw new AiAnalysisException("Gemini returned an empty response.");
            }

            return response.candidates().get(0).content().parts().get(0).text();

        } catch (WebClientResponseException e) {
            log.error("Gemini API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new AiAnalysisException("Gemini API request failed with status " + e.getStatusCode(), e);
        } catch (Exception e) {
            if (e instanceof AiAnalysisException) throw e;
            log.error("Unexpected error calling Gemini API", e);
            throw new AiAnalysisException("Failed to reach the AI analysis service.", e);
        }
    }
}
