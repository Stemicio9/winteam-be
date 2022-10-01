package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.SubscriptionDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "subscriptions")
public class Subscription {

    @Id
    private String id;
    private String name;
    private int numAnnunci;
    private int numberOfDays;
    private double price;
    private boolean searchEnabled;
    private String imageLink;


    public SubscriptionDTO toDTO(){
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setId(this.id);
        subscriptionDTO.setName(this.name);
        subscriptionDTO.setNumAnnunci(this.numAnnunci);
        subscriptionDTO.setNumberOfDays(this.numberOfDays);
        subscriptionDTO.setPrice(this.price);
        subscriptionDTO.setSearchEnabled(this.searchEnabled);
        subscriptionDTO.setImageLink(this.imageLink);
        return subscriptionDTO;
    }
}
