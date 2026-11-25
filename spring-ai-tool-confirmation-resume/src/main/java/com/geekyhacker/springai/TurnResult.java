package com.geekyhacker.springai;

import java.util.List;

import org.springframework.ai.chat.messages.Message;

public record TurnResult(boolean parked, List<Message> history) {
}
