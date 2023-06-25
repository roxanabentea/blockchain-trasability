package com.template.workflows;

// A class to hold the deserialized arguments required to start the flow.
public class GetHistoryFlowArgs {

    private String productId;
    public GetHistoryFlowArgs() {}

    public GetHistoryFlowArgs(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
