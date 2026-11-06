package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.DTO.PostLikeRequest;
import com.jamboSwift.ByteThread.Exceptions.ClientNotFoundException;
import com.jamboSwift.ByteThread.Exceptions.InvalidTransaction;
import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Client;
import com.jamboSwift.ByteThread.Models.Like;
import com.jamboSwift.ByteThread.Models.Post;
import com.jamboSwift.ByteThread.Repo.AccountRepo;
import com.jamboSwift.ByteThread.Repo.ClientRepo;
import com.jamboSwift.ByteThread.Repo.LikeRepo;
import com.jamboSwift.ByteThread.Repo.PostRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepo likeRepo;
    private final ClientRepo clientRepo;
    private final PostRepo postRepo;
    private final AccountRepo accountRepo;

    @Transactional
    public void PostLike(PostLikeRequest request){
        String normalisedEmail=request.email().trim().toLowerCase();

        Client client=clientRepo.findByEmail(normalisedEmail).orElseThrow(
                ()-> new ClientNotFoundException("Invalid credentials")
        );

        Account account=client.getAccount();

        if (account == null) {
            throw new InvalidTransaction("User account is not properly initialized or linked.");
        }

        Post post=postRepo.findById(request.postId()).orElseThrow(
                ()->new ClientNotFoundException("Invalid post")
        );

        if (likeRepo.existsByAccountAndPost(account,post)){
            throw new InvalidTransaction("You had already liked");
        }

        post.setLikesCount(post.getLikesCount() + 1);
        account.setLikeCount(account.getLikeCount() + 1);

        Like like=Like.builder()
                .account(account)
                .post(post)
                .build();

        likeRepo.save(like);
        postRepo.save(post);
        accountRepo.save(account);
    }

    @Transactional
    public void dislike(PostLikeRequest request){
        String normalisedEmail=request.email().trim().toLowerCase();

        Client client=clientRepo.findByEmail(normalisedEmail).orElseThrow(
                ()-> new ClientNotFoundException("Invalid credentials")
        );

        Account account=client.getAccount();

        /*if (!likeRepo.existsByAccount(account)){
            throw new InvalidTransaction("You never liked the post");
        }*/

        Post post=postRepo.findById(request.postId()).orElseThrow(
                ()->new ClientNotFoundException("Invalid post")
        );

        Like like=likeRepo.findByAccountAndPost(account,post).orElseThrow(
                ()-> new InvalidTransaction("You disliked / never liked the post")
        );

        likeRepo.delete(like);

        if (post.getLikesCount()>0)
            post.setLikesCount(post.getLikesCount()-1);
        account.setLikeCount(Math.max(0,account.getLikeCount() - 1));

        postRepo.save(post);
        accountRepo.save(account);


    }
}
