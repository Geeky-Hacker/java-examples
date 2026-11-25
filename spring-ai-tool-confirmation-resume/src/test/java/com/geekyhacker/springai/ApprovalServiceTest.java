package com.geekyhacker.springai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.AssistantMessage.ToolCall;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.model.tool.ToolCallingManager;

class ApprovalServiceTest {

    private static ApprovalService newService(String url, PaymentTools tools) {
        return new ApprovalService(ToolCallingManager.builder().build(), new ParkedTurnStore(url), tools);
    }

    private static AssistantMessage paymentReply() {
        ToolCall call = new ToolCall("call_1", "function", "collectPayment",
                "{\"accountId\":\"MORT-1042\",\"amount\":1850.0}");
        return AssistantMessage.builder().content("").toolCalls(List.of(call)).build();
    }

    private static AssistantMessage lookupReply() {
        ToolCall call = new ToolCall("call_2", "function", "paymentFailureReason",
                "{\"accountId\":\"MORT-1042\"}");
        return AssistantMessage.builder().content("").toolCalls(List.of(call)).build();
    }

    private static String lastToolResult(List<Message> history) {
        ToolResponseMessage last = (ToolResponseMessage) history.get(history.size() - 1);
        return last.getResponses().get(0).responseData();
    }

    @Test
    void approvalAfterRestartRunsPaymentExactlyOnce(@TempDir Path dir) {
        String url = "jdbc:h2:file:" + dir.resolve("parked").toAbsolutePath();

        PaymentTools before = new PaymentTools();
        TurnResult parked = newService(url, before).submit("s-1", "Collect my payment", paymentReply());
        assertTrue(parked.parked());
        assertTrue(before.collected().isEmpty());

        // "restart": nothing is shared except the database file
        PaymentTools after = new PaymentTools();
        ApprovalService restarted = newService(url, after);
        ParkedTurn pending = restarted.pending("s-1").orElseThrow();
        assertEquals("collectPayment", pending.toolCalls().get(0).name());
        assertTrue(after.collected().isEmpty());

        TurnResult resumed = restarted.resume("s-1", true, null);
        assertFalse(resumed.parked());
        assertEquals(List.of("MORT-1042:1850.0"), after.collected());
        assertTrue(lastToolResult(resumed.history()).contains("Collected"));

        assertThrows(IllegalStateException.class, () -> restarted.resume("s-1", true, null));
        assertEquals(1, after.collected().size());
    }

    @Test
    void denialNeverRunsThePaymentAndTellsTheModel(@TempDir Path dir) {
        String url = "jdbc:h2:file:" + dir.resolve("parked").toAbsolutePath();
        PaymentTools tools = new PaymentTools();
        ApprovalService service = newService(url, tools);

        assertTrue(service.submit("s-2", "Collect my payment", paymentReply()).parked());
        TurnResult resumed = service.resume("s-2", false, "The supervisor said no.");

        assertTrue(tools.collected().isEmpty());
        assertTrue(lastToolResult(resumed.history()).contains("The supervisor said no."));
    }

    @Test
    void unmarkedToolRunsWithoutParking(@TempDir Path dir) {
        String url = "jdbc:h2:file:" + dir.resolve("parked").toAbsolutePath();
        ApprovalService service = newService(url, new PaymentTools());

        TurnResult result = service.submit("s-3", "Why did my payment fail?", lookupReply());

        assertFalse(result.parked());
        assertTrue(lastToolResult(result.history()).contains("insufficient funds"));
        assertTrue(service.pending("s-3").isEmpty());
    }
}
