package com.template.states;

import com.r3.developers.csdetemplate.utxoexample.contracts.ChatContract;
import com.template.contracts.EntryContract;
import net.corda.v5.base.annotations.ConstructorForDeserialization;
import net.corda.v5.base.types.MemberX500Name;
import net.corda.v5.ledger.utxo.BelongsToContract;
import net.corda.v5.ledger.utxo.ContractState;

import java.security.PublicKey;
import java.util.List;
import java.util.UUID;

@BelongsToContract(EntryContract.class)
public class EntryState implements ContractState {

    private UUID id;
    private String productId;
    private MemberX500Name sender;
    private String receiver;
    private String date;
    public List<PublicKey> participants;

    // Allows serialisation and to use a specified UUID.
    @ConstructorForDeserialization
    public EntryState(UUID id,
                      String productId,
                      MemberX500Name sender,
                      String receiver,
                      String date,
                      List<PublicKey> participants) {
        this.id = id;
        this.productId = productId;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.participants = participants;
    }

    public UUID getId() {
        return id;
    }
    public String getProductId() {
        return productId;
    }
    public MemberX500Name getSender() {
        return sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getDate() {
        return date;
    }

    public List<PublicKey> getParticipants() {
        return participants;
    }
}
