package com.workonenight.winteambe.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    private String userId;
    private Subscription subscription;
    private double price;
    private LocalDateTime purchaseDate;

}
