package com.template.workflows;

import com.r3.developers.csdetemplate.utxoexample.workflows.FinalizeChatSubFlow;
import com.template.contracts.EntryContract;
import com.template.states.EntryState;
import net.corda.v5.application.flows.ClientRequestBody;
import net.corda.v5.application.flows.ClientStartableFlow;
import net.corda.v5.application.flows.CordaInject;
import net.corda.v5.application.flows.FlowEngine;
import net.corda.v5.application.marshalling.JsonMarshallingService;
import net.corda.v5.application.membership.MemberLookup;
import net.corda.v5.base.annotations.Suspendable;
import net.corda.v5.base.exceptions.CordaRuntimeException;
import net.corda.v5.base.types.MemberX500Name;
import net.corda.v5.ledger.common.NotaryLookup;
import net.corda.v5.ledger.utxo.UtxoLedgerService;
import net.corda.v5.ledger.utxo.transaction.UtxoSignedTransaction;
import net.corda.v5.ledger.utxo.transaction.UtxoTransactionBuilder;
import net.corda.v5.membership.MemberInfo;
import net.corda.v5.membership.NotaryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class CreateNewEntryFlow implements ClientStartableFlow {

    private final static Logger log = LoggerFactory.getLogger(CreateNewEntryFlow.class);

    @CordaInject
    public JsonMarshallingService jsonMarshallingService;

    @CordaInject
    public MemberLookup memberLookup;

    // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
    @CordaInject
    public UtxoLedgerService ledgerService;

    @CordaInject
    public NotaryLookup notaryLookup;

    // FlowEngine service is required to run SubFlows.
    @CordaInject
    public FlowEngine flowEngine;


    @Suspendable
    @Override
    public String call( ClientRequestBody requestBody) {

        log.info("CreateNewEntryFlow.call() called");

        try {
            // Obtain the deserialized input arguments to the flow from the requestBody.
            CreateNewEntryFlowArgs flowArgs = requestBody.getRequestBodyAs(jsonMarshallingService, CreateNewEntryFlowArgs.class);

            // Get MemberInfos for the Vnode running the flow and the otherMember.
            MemberInfo myInfo = memberLookup.myInfo();
            MemberInfo otherMember = requireNonNull(
                    memberLookup.lookup(MemberX500Name.parse(flowArgs.getReceiver())),
                    "MemberLookup can't find otherMember specified in flow arguments."
            );

            // Create the EntryState from the input arguments and member information.
            EntryState entryState = new EntryState(
                    UUID.randomUUID(),
                    flowArgs.getProductId(),
                    myInfo.getName(),
                    flowArgs.getReceiver(),
                    flowArgs.getDate(),
                    Arrays.asList(myInfo.getLedgerKeys().get(0), otherMember.getLedgerKeys().get(0))
            );

            // Obtain the Notary name and public key.
            NotaryInfo notary = notaryLookup.getNotaryServices().iterator().next();

            // Use UTXOTransactionBuilder to build up the draft transaction.
            UtxoTransactionBuilder txBuilder = ledgerService.createTransactionBuilder()
                    .setNotary(notary.getName())
                    .setTimeWindowBetween(Instant.now(), Instant.now().plusMillis(Duration.ofDays(1).toMillis()))
                    .addOutputState(entryState)
                    .addCommand(new EntryContract.Create())
                    .addSignatories(entryState.getParticipants());

            // Convert the transaction builder to a UTXOSignedTransaction. Verifies the content of the
            // UtxoTransactionBuilder and signs the transaction with any required signatories that belong to
            // the current node.
            UtxoSignedTransaction signedTransaction = txBuilder.toSignedTransaction();

            // Call FinalizeChatSubFlow which will finalise the transaction.
            // If successful the flow will return a String of the created transaction id,
            // if not successful it will return an error message.
            return flowEngine.subFlow(new FinalizeEntryFlow(signedTransaction, otherMember.getName()));
        }
        // Catch any exceptions, log them and rethrow the exception.
        catch (Exception e) {
            log.warn("Failed to process Roxana's flow for request body " + requestBody + " because: " + e.getMessage());
            throw new CordaRuntimeException(e.getMessage());
        }
    }
}

/*
RequestBody for triggering the flow via REST:
{
    "clientRequestId": "create-1",
    "flowClassName": "com.template.workflows.CreateNewEntryFlow",
    "requestBody": {
        "productId":"decasept",
        "receiver":"CN=Distribuitor1, OU=Test Dept, O=Roxana, L=Bucuresti, C=RO",
        "date": "12/06/2023"
        }
}
 */
