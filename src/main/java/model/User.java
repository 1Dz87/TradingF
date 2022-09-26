package model;

import java.util.*;

public class User {

    private UUID id;

    private String login;

    private String passwordHash;

    private List<Wallet> walletList;

    private List<User> friends;

    public User(UUID id, String login, String passwordHash) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.walletList = new ArrayList<>();
        this.friends = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<Wallet> getWalletList() {
        if (this.walletList == null) {
            this.walletList = new ArrayList<>();
        }
        return walletList;
    }

    public void addWallet(Wallet wallet) {
        if (this.walletList == null) {
            this.walletList = new ArrayList<>();
        }
        Optional.ofNullable(wallet)
                .ifPresent(wal -> this.walletList.add(wal));
    }

    public List<User> getFriends() {
        if (this.friends == null) {
            this.friends = new ArrayList<>();
        }
        return friends;
    }

    public void addFriend(User user) {
        if (this.friends == null) {
            this.friends = new ArrayList<>();
        }
        this.friends.add(user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, passwordHash, walletList, friends);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return this.id.equals(((User) obj).getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", walletList=" + walletList +
                ", friends=" + friends +
                '}';
    }
}
