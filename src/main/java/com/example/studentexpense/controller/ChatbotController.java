package com.example.studentexpense.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    @Value("${gemini.api-key}")
    private String apiKey;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

    // In-memory map to store last topic per user (you can replace this with a DB later)
    private final Map<String, String> userContext = new HashMap<>();

    private static final Set<String> ALLOWED_TOPICS = Set.of(
            "expense", "expenses", "budget", "budgets", "summary", "summarization",
            "estimation", "chart", "charts", "bar chart", "pie chart", "food", "travel",
            "clothing", "insights", "analysis", "spending", "savings"
    );

    private static final Set<String> GREETINGS = Set.of(
            "hi", "hello", "hey", "good morning", "good afternoon", "good evening"
    );

    private static final Set<String> AFFIRMATIVE_RESPONSES = Set.of(
            "yes", "yeah", "yup", "sure", "ok", "okay", "of course", "why not"
    );

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.getOrDefault("message", "").trim();
        String lowerMsg = userMessage.toLowerCase();
        String userId = payload.getOrDefault("userId", "default");
        String reply;

        // ✅ 1. Greetings
        if (GREETINGS.contains(lowerMsg)) {
            reply = "👋 Hello! I’m your Expense Assistant. I can help you understand your expenses, budgets, charts, and savings summaries.";
            return Map.of("reply", reply);
        }

        // ✅ 2. Continue conversation if user says “yes” or similar
        if (AFFIRMATIVE_RESPONSES.contains(lowerMsg)) {
            String lastTopic = userContext.getOrDefault(userId, "");
            if (!lastTopic.isEmpty()) {
                lowerMsg = lastTopic; // Continue the same topic
            } else {
                return Map.of("reply", "Could you please tell me which expense or budget topic you’d like to continue with?");
            }
        }

        // ✅ 3. Check relevance
        boolean isRelevant = ALLOWED_TOPICS.stream().anyMatch(lowerMsg::contains);
        if (!isRelevant) {
            return Map.of("reply", "I’m sorry, I can only answer questions about your expenses, budgets, or insights shown on this page.");
        }

        // ✅ 4. Remember last relevant topic for the user
        userContext.put(userId, lowerMsg);

        try {
            RestTemplate restTemplate = new RestTemplate();

            String systemPrompt = """
                    You are an intelligent Expense Assistant for a Student Expense Tracker website.
                    You can ONLY respond about:
                    - Food, Travel, and Clothing expenses
                    - Budget estimation and summarization
                    - Bar chart and pie chart insights
                    - Total expenditure, total savings, and spending insights
                    - Helping users understand how to use the expense tracker

                    When the user says "yes" or similar, continue the last conversation context.
                    Be concise, friendly, and use natural English.
                    If the question is not related to expenses, budgets, or charts, respond:
                    "I’m sorry, I can only answer questions about your expenses, budgets, or insights shown on this page."
                    """;

            Map<String, Object> content = Map.of(
                    "parts", List.of(Map.of("text", systemPrompt + "\nUser: " + userMessage))
            );
            Map<String, Object> requestBody = Map.of("contents", List.of(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    GEMINI_API_URL + apiKey,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");
                reply = (String) parts.get(0).get("text");
            } else {
                reply = "⚠️ No response from Gemini API.";
            }

        } catch (Exception e) {
            reply = "⚠️ Error: Could not connect to Gemini API. " + e.getMessage();
        }

        return Map.of("reply", reply);
    }
}
