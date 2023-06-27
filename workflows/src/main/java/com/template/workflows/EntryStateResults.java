package com.template.workflows;

import java.util.UUID;

// Class to hold the ListChatFlow results.
// The ChatState(s) cannot be returned directly as the JsonMarshallingService can only serialize simple classes
// that the underlying Jackson serializer recognises, hence creating a DTO style object which consists only of Strings
// and a UUID. It is possible to create custom serializers for the JsonMarshallingService, but this beyond the scope
// of this simple example.
public class EntryStateResults {

    private UUID id;
    private String productId;
    private String senderName;
    private String receiver;

    public EntryStateResults() {}

    public EntryStateResults(UUID id, String productId, String senderName, String receiver) {
        this.id = id;
        this.productId = productId;
        this.senderName = senderName;
        this.receiver = receiver;
    }

    public UUID getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiver() {
        return receiver;
    }
}
