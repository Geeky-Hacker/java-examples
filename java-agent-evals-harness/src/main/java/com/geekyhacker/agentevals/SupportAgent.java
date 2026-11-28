package com.geekyhacker.agentevals;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Deterministic stand-in for an LLM-backed agent. It stops at the first matching
 * intent, so requests with two intents only trigger one tool.
 */
public class SupportAgent {

    private static final Pattern ORDER_ID = Pattern.compile("order (\\d+)");

    public AgentResult run(String input) {
        String text = input.toLowerCase();
        Matcher order = ORDER_ID.matcher(text);

        if (order.find()) {
            return new AgentResult("Order " + order.group(1) + " is shipped.",
                    List.of("lookup_order"));
        }
        if (text.contains("refund")) {
            return new AgentResult("I opened a refund ticket for you.",
                    List.of("create_refund_ticket"));
        }
        if (text.contains("password")) {
            return new AgentResult("I sent you a password reset link.",
                    List.of("send_reset_link"));
        }
        return new AgentResult("Sorry, I can't help with that.", List.of());
    }
}
