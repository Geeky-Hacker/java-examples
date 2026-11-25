package com.geekyhacker.springai;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.ai.tool.annotation.Tool;

public class PaymentTools {

    private final List<String> collected = new CopyOnWriteArrayList<>();

    @Tool(description = "Explain why the last mortgage payment failed")
    public String paymentFailureReason(String accountId) {
        return "Payment for " + accountId + " failed: insufficient funds on the 1st";
    }

    @Tool(description = "Collect the overdue mortgage payment from the customer's account",
            requiresConfirmation = true)
    public String collectPayment(String accountId, double amount) {
        collected.add(accountId + ":" + amount);
        return "Collected " + amount + " from " + accountId;
    }

    public List<String> collected() {
        return List.copyOf(collected);
    }
}
