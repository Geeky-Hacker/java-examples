package com.geekyhacker.springai;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.ai.chat.messages.AssistantMessage.ToolCall;

/**
 * Keeps parked turns in a database, so any process that can reach it can resume them.
 */
public class ParkedTurnStore {

    public static final String PARKED = "PARKED";
    public static final String APPROVED = "APPROVED";
    public static final String DENIED = "DENIED";

    private final String jdbcUrl;

    public ParkedTurnStore(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS parked_turn ("
                    + "session_id VARCHAR(64) PRIMARY KEY, "
                    + "user_message VARCHAR(2000) NOT NULL, "
                    + "status VARCHAR(16) NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS parked_tool_call ("
                    + "session_id VARCHAR(64) NOT NULL, "
                    + "idx INT NOT NULL, "
                    + "call_id VARCHAR(128) NOT NULL, "
                    + "call_type VARCHAR(32) NOT NULL, "
                    + "name VARCHAR(128) NOT NULL, "
                    + "arguments VARCHAR(4000) NOT NULL, "
                    + "PRIMARY KEY (session_id, idx))");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot initialise the schema", e);
        }
    }

    public void park(String sessionId, String userMessage, List<ToolCall> toolCalls) {
        try (Connection c = connect()) {
            c.setAutoCommit(false);
            try (PreparedStatement turn = c.prepareStatement(
                    "INSERT INTO parked_turn (session_id, user_message, status) VALUES (?, ?, ?)");
                 PreparedStatement call = c.prepareStatement(
                         "INSERT INTO parked_tool_call (session_id, idx, call_id, call_type, name, arguments) "
                                 + "VALUES (?, ?, ?, ?, ?, ?)")) {
                turn.setString(1, sessionId);
                turn.setString(2, userMessage);
                turn.setString(3, PARKED);
                turn.executeUpdate();
                for (int i = 0; i < toolCalls.size(); i++) {
                    ToolCall tc = toolCalls.get(i);
                    call.setString(1, sessionId);
                    call.setInt(2, i);
                    call.setString(3, tc.id());
                    call.setString(4, tc.type());
                    call.setString(5, tc.name());
                    call.setString(6, tc.arguments());
                    call.executeUpdate();
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot park turn " + sessionId, e);
        }
    }

    public Optional<ParkedTurn> find(String sessionId) {
        try (Connection c = connect();
             PreparedStatement turn = c.prepareStatement(
                     "SELECT user_message, status FROM parked_turn WHERE session_id = ?");
             PreparedStatement calls = c.prepareStatement(
                     "SELECT call_id, call_type, name, arguments FROM parked_tool_call "
                             + "WHERE session_id = ? ORDER BY idx")) {
            turn.setString(1, sessionId);
            try (ResultSet rs = turn.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                String userMessage = rs.getString(1);
                String status = rs.getString(2);
                calls.setString(1, sessionId);
                List<ToolCall> toolCalls = new ArrayList<>();
                try (ResultSet crs = calls.executeQuery()) {
                    while (crs.next()) {
                        toolCalls.add(new ToolCall(crs.getString(1), crs.getString(2),
                                crs.getString(3), crs.getString(4)));
                    }
                }
                return Optional.of(new ParkedTurn(sessionId, userMessage, status, toolCalls));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot read turn " + sessionId, e);
        }
    }

    /**
     * Atomically moves a turn from PARKED to the given status. Only one caller can win.
     */
    public boolean claim(String sessionId, String newStatus) {
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE parked_turn SET status = ? WHERE session_id = ? AND status = ?")) {
            ps.setString(1, newStatus);
            ps.setString(2, sessionId);
            ps.setString(3, PARKED);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update turn " + sessionId, e);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }
}
