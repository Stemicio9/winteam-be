package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.Subscription;
import lombok.Data;

@Data
public class SubscriptionDTO {

    private String id;
    private String name;
    private int numAnnunci;
    private int numberOfDays;
    private double price;
    private boolean searchEnabled;
    private boolean createAdvertisementEnabled;
    private String imageLink;

    public Subscription toEntity(){
        Subscription subscription = new Subscription();
        subscription.setId(this.id);
        subscription.setName(this.name);
        subscription.setNumAnnunci(this.numAnnunci);
        subscription.setNumberOfDays(this.numberOfDays);
        subscription.setPrice(this.price);
        subscription.setSearchEnabled(this.searchEnabled);
        subscription.setCreateAdvertisementEnabled(this.createAdvertisementEnabled);
        subscription.setImageLink(this.imageLink);
        return subscription;
    }
}
