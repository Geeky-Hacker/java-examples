package com.geekyhacker.springai;

import java.util.List;

import org.springframework.ai.chat.messages.AssistantMessage;

public record ParkedTurn(String sessionId, String userMessage, String status,
                         List<AssistantMessage.ToolCall> toolCalls) {
}
