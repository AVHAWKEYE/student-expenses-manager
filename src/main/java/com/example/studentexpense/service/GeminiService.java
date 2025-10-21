package com.example.studentexpense.service;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeminiService {
    private final String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY");
    private final String GEMINI_ENDPOINT = "https://api.generativeai.googleapis.com/v1/models/gemini-1.5-mini:generateContent"; // example; adjust as needed
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String generateReply(String prompt) throws Exception {
        if (GEMINI_API_KEY == null || GEMINI_API_KEY.isBlank()) {
            return "[Gemini API key not set on server (GEMINI_API_KEY environment variable)]";
        }
        // Build a simple request body following the Gemini REST quickstart pattern.
        Map<String, Object> body = Map.of(
            "prompt", Map.of("text", prompt),
            "maxOutputTokens", 256
        );
        String json = mapper.writeValueAsString(body);
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(GEMINI_ENDPOINT))
            .timeout(Duration.ofSeconds(20))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + GEMINI_API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
            // best-effort parse: attempt to extract text from typical Gemini response
            String bodyStr = resp.body();
            try {
                Map m = mapper.readValue(bodyStr, Map.class);
                // The exact path may vary by API version. We'll try a few expected keys.
                if (m.containsKey("candidates")) {
                    Object cand = ((java.util.List)m.get("candidates")).get(0);
                    if (cand instanceof Map && ((Map)cand).containsKey("output")) {
                        Object out = ((Map)cand).get("output");
                        if (out instanceof Map && ((Map)out).containsKey("text")) {
                            return ((Map)out).get("text").toString();
                        }
                    }
                }
                if (m.containsKey("output")) {
                    Object out = m.get("output");
                    if (out instanceof Map && ((Map)out).containsKey("content")) {
                        return ((Map)out).get("content").toString();
                    }
                }
                // fallback: return the full body
                return bodyStr;
            } catch (Exception ex) {
                return resp.body();
            }
        } else {
            return String.format("[Gemini API error %d] %s", resp.statusCode(), resp.body());
        }
    }
}
