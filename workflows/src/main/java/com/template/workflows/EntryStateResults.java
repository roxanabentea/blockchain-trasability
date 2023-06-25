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
    private String receiverName;
    private String date;

    public EntryStateResults() {}

    public EntryStateResults(UUID id, String productId, String receiverName, String date) {
        this.id = id;
        this.productId = productId;
        this.receiverName = receiverName;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getDate() {
        return date;
    }
}
