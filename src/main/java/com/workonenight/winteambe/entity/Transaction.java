package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import com.workonenight.winteambe.entity.interfaces.Transferrable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction extends DataTransferObject implements Transferrable {

    private String userId;
    private Subscription subscription;
    private double price;
    private LocalDateTime purchaseDate;

    @Override
    public DataTransferObject asDTO() {
        return this;
    }

    @Override
    public DataTransferObject createBaseDTO() {
        return new Transaction();
    }
}
