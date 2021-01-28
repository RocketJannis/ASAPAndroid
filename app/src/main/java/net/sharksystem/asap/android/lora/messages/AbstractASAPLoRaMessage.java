package net.sharksystem.asap.android.lora.messages;

import android.util.Log;

import net.sharksystem.asap.android.lora.exceptions.ASAPLoRaMessageException;

public abstract class AbstractASAPLoRaMessage {
    private static final String CLASS_LOG_TAG = "AbstractASAPLoRaMessage";

    public String getPayload() throws ASAPLoRaMessageException {
        throw new ASAPLoRaMessageException("Trying to call getPayload() on non-outgoing ASAP Message. This should never happen.");
    }

    public static AbstractASAPLoRaMessage createASAPLoRaMessage(String rawMessage) throws ASAPLoRaMessageException {
        Log.i(CLASS_LOG_TAG, "Raw LoRa Message Received!");
        Log.i(CLASS_LOG_TAG, "Creating AbstractASAPLoRaMessage from String: " + rawMessage);
        // rawMessage is of format: <COMMAND (5 Char)>:<Payload> or <COMMAND (5 Char)>@<Address (4 Char)>:<Payload>
        if (rawMessage.equals("") || rawMessage.length() < 6)
            throw new ASAPLoRaMessageException("Invalid Message Format: " + rawMessage);
        if (!(
                rawMessage.substring(5, 6).equals(":") ||
                        (rawMessage.substring(5, 6).equals("@") && rawMessage.substring(10, 11).equals(":"))
        ))
            throw new ASAPLoRaMessageException("Invalid Message Format: " + rawMessage);
        String messageType = rawMessage.substring(0, 5);
        String messagePayload = rawMessage.substring(6);
        switch (messageType) {
            case "DVDCR":
                return new DeviceDiscoveredASAPLoRaMessage(messagePayload);
            case "MSSGE":
                String messageAddress = messagePayload.substring(0, 4);
                String message = messagePayload.substring(5);
                return new ASAPLoRaMessage(messageAddress, message);
        }
        throw new ASAPLoRaMessageException("Recieved invalid Message Type: " + rawMessage);
    }
}