package com.jamboSwift.ByteThread.Utilities;

import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Client;
import com.jamboSwift.ByteThread.Models.Like;
import com.jamboSwift.ByteThread.Models.Post;

public class LikeCreatedEvent {
    private final Post post;
    private final Client client;
    private final Account account;
    private final Like savedLike;

    public Post getPost() {
        return post;
    }

    public Client getClient() {
        return client;
    }

    public Account getAccount() {
        return account;
    }

    public Like getSavedLike() {
        return savedLike;
    }

    public LikeCreatedEvent(Post post, Client client, Account account, Like savedLike) {
        this.post = post;
        this.client = client;
        this.account = account;
        this.savedLike = savedLike;
    }
}
