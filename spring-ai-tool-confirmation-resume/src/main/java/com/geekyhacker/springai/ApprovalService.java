package com.geekyhacker.springai;

import java.util.List;
import java.util.Optional;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallConfirmationRequiredException;
import org.springframework.ai.model.tool.ToolCallDecision;
import org.springframework.ai.model.tool.ToolCallDecisions;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;

public class ApprovalService {

    private final ToolCallingManager toolCallingManager;
    private final ParkedTurnStore store;
    private final ToolCallingChatOptions options;

    public ApprovalService(ToolCallingManager toolCallingManager, ParkedTurnStore store, PaymentTools tools) {
        this.toolCallingManager = toolCallingManager;
        this.store = store;
        ToolCallback[] callbacks = ToolCallbacks.from(tools);
        this.options = ToolCallingChatOptions.builder().toolCallbacks(callbacks).build();
    }

    /**
     * Runs the (model) reply. If a tool needs confirmation, the turn is parked instead.
     */
    public TurnResult submit(String sessionId, String userMessage, AssistantMessage reply) {
        try {
            ToolExecutionResult result = toolCallingManager.executeToolCalls(
                    promptFor(userMessage, reply), responseOf(reply));
            return new TurnResult(false, result.conversationHistory());
        } catch (ToolCallConfirmationRequiredException e) {
            store.park(sessionId, userMessage, reply.getToolCalls());
            return new TurnResult(true, List.of());
        }
    }

    public Optional<ParkedTurn> pending(String sessionId) {
        return store.find(sessionId).filter(t -> ParkedTurnStore.PARKED.equals(t.status()));
    }

    public TurnResult resume(String sessionId, boolean approved, String denyMessage) {
        ParkedTurn turn = store.find(sessionId)
                .orElseThrow(() -> new IllegalStateException("No parked turn for " + sessionId));

        // Claim first, so a repeated approval can never run the tool twice
        String newStatus = approved ? ParkedTurnStore.APPROVED : ParkedTurnStore.DENIED;
        if (!store.claim(sessionId, newStatus)) {
            throw new IllegalStateException("Turn " + sessionId + " was already resolved");
        }

        AssistantMessage reply = AssistantMessage.builder()
                .content("")
                .toolCalls(turn.toolCalls())
                .build();

        // Every call in the reply needs a decision, flagged or not
        ToolCallDecision[] decisions = turn.toolCalls().stream()
                .map(call -> approved
                        ? ToolCallDecision.allow(call.id())
                        : ToolCallDecision.deny(call.id(), denyMessage))
                .toArray(ToolCallDecision[]::new);

        ToolExecutionResult result = toolCallingManager.executeToolCalls(
                promptFor(turn.userMessage(), reply), responseOf(reply), ToolCallDecisions.of(decisions));
        return new TurnResult(false, result.conversationHistory());
    }

    private Prompt promptFor(String userMessage, AssistantMessage reply) {
        List<Message> conversation = List.of(new UserMessage(userMessage), reply);
        return new Prompt(conversation, options);
    }

    private ChatResponse responseOf(AssistantMessage reply) {
        return ChatResponse.builder().generations(List.of(new Generation(reply))).build();
    }
}
