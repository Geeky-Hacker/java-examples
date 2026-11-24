package com.geekyhacker.springintegration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;

public final class GadgetSupport {

    private GadgetSupport() {
    }

    public static byte[] serializedGadget() {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(new HarmlessGadget());
            out.flush();
            return bytes.toByteArray();
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Plays the attacker: writes the gadget straight into INT_MESSAGE, in the default region.
     */
    public static UUID insertGadgetRow(JdbcTemplate jdbc) {
        UUID id = UUID.randomUUID();
        jdbc.update("INSERT INTO INT_MESSAGE (MESSAGE_ID, REGION, CREATED_DATE, MESSAGE_CONTENT) VALUES (?, ?, ?, ?)",
                id.toString(), "DEFAULT", Timestamp.from(Instant.now()), serializedGadget());
        return id;
    }
}
