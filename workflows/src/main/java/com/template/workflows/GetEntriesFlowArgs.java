package com.template.workflows;

// A class to hold the deserialized arguments required to start the flow.
public class GetEntriesFlowArgs {

    private String senderName;
    public GetEntriesFlowArgs() {}

    public GetEntriesFlowArgs(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
