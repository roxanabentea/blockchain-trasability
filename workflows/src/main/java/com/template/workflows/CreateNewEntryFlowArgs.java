package com.template.workflows;

// A class to hold the deserialized arguments required to start the flow.
public class CreateNewEntryFlowArgs {

    // Serialisation service requires a default constructor
    public CreateNewEntryFlowArgs() {}

    private String productId;
    private String receiver;
    private String date;

    public CreateNewEntryFlowArgs(String productId, String receiver, String date) {
        this.productId = productId;
        this.receiver = receiver;
        this.date = date;
    }

    public String getProductId() {
        return productId;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getDate() {
        return date;
    }
}