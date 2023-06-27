package com.template.workflows;

import com.template.states.EntryState;
import net.corda.v5.application.flows.ClientRequestBody;
import net.corda.v5.application.flows.ClientStartableFlow;
import net.corda.v5.application.flows.CordaInject;
import net.corda.v5.application.marshalling.JsonMarshallingService;
import net.corda.v5.base.annotations.Suspendable;
import net.corda.v5.ledger.utxo.StateAndRef;
import net.corda.v5.ledger.utxo.UtxoLedgerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class GetEntriesFlow implements ClientStartableFlow {

    private final static Logger log = LoggerFactory.getLogger(GetEntriesFlow.class);

    @CordaInject
    public JsonMarshallingService jsonMarshallingService;

    // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
    @CordaInject
    public UtxoLedgerService utxoLedgerService;

    @Suspendable
    @Override
    public String call(ClientRequestBody requestBody) {

        log.info("GetEntriesFlow.call() called");
        GetEntriesFlowArgs flowArgs = requestBody.getRequestBodyAs(jsonMarshallingService, GetEntriesFlowArgs.class);

        // Queries the VNode's vault for unconsumed states and converts the result to a serializable DTO.
        List<StateAndRef<EntryState>> states = utxoLedgerService.findUnconsumedStatesByType(EntryState.class);
        List<EntryStateResults> results = states.stream()
                .filter(state -> state.getState().getContractState().getSender().getCommonName().equals(flowArgs.getSenderName()))
                .map( stateAndRef ->
                new EntryStateResults(
                        stateAndRef.getState().getContractState().getId(),
                        stateAndRef.getState().getContractState().getProductId(),
                        stateAndRef.getState().getContractState().getSender().getCommonName(),
                        stateAndRef.getState().getContractState().getDate()
                )
        ).collect(Collectors.toList());

        // Uses the JsonMarshallingService's format() function to serialize the DTO to Json.
        return jsonMarshallingService.format(results);
    }
}

/*
RequestBody for triggering the flow via REST:
{
    "clientRequestId": "get-1",
    "flowClassName": "com.template.workflows.GetEntriesFlow",
    "requestBody": {
        "senderName":"Producator1"
        }
}
*/