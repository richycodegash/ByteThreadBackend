package com.jamboSwift.ByteThread.Utilities;

import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Client;
import com.jamboSwift.ByteThread.Models.Post;

public class PostCreatedEvent {
    private final Post post;
    private final Client client;
    private final Account author;

    public PostCreatedEvent(Post post, Client client, Account author) {
        this.post = post;
        this.client = client;
        this.author = author;
    }

    public Post getPost() { return post; }
    public Client getClient() { return client; }
    public Account getAuthor() { return author; }
}
