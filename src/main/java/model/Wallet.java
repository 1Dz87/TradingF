package model;

import java.util.UUID;

public class Wallet {

    private UUID id;

    private Currency currency;

    private Float amount;

    private User user;

    public Wallet(UUID id, Currency currency, Float amount, User user) {
        this.id = id;
        this.currency = currency;
        this.amount = amount;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public Float addAmount(Float amount) {
        this.amount += amount;
        return this.amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wallet) {
            return this.id.equals(((Wallet) obj).getId());
        }
        return super.equals(obj);
    }
}
