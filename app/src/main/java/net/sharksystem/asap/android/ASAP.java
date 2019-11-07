package net.sharksystem.asap.android;

import java.util.UUID;

/**
 * Be sure to have the AASPEngine in one of your library directories!
 */
public class ASAP {
    public static final String UNKNOWN_USER = "anon";

    public static final String USER = "user";
    public static final String FOLDER = "folder";
    public static final String RECIPIENT = "recipient";
    public static final String RECIPIENTS = "recipients";

    public static final String ASAP_CHUNK_RECEIVED_ACTION = "net.sharksystem.asap.received";

    public static final String URI = "ASAP_MESSAGE_URI";
    public static final String MESSAGE_CONTENT = "ASAP_MESSAGE_CONTENT";
    public static final String FORMAT = "ASAP_FORMAT";

    public static final String ERA = "ASAP_ERA";
    public static final int PORT_NUMBER = 7777;

    public static final String ONLINE_EXCHANGE = "ASAP_ONLINE_EXCHANGE";
    public static final boolean ONLINE_EXCHANGE_DEFAULT = true;
    public static final String MAX_EXECUTION_TIME = "ASAP_MAX_EXECUTION_TIME";
}
