package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.DTO.PostLikeRequest;
import com.jamboSwift.ByteThread.Exceptions.ClientNotFoundException;
import com.jamboSwift.ByteThread.Exceptions.InvalidTransaction;
import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Client;
import com.jamboSwift.ByteThread.Models.Like;
import com.jamboSwift.ByteThread.Models.Post;
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

    @Transactional
    public void PostLike(PostLikeRequest request){
        String normalisedEmail=request.email().trim().toLowerCase();

        Client client=clientRepo.findByEmail(normalisedEmail).orElseThrow(
                ()-> new ClientNotFoundException("Invalid credentials")
        );

        Account account=client.getAccount();

        if (likeRepo.existsByAccount(account)){
            throw new InvalidTransaction("You had already liked");
        }

        Post post=postRepo.findById(request.postId()).orElseThrow(
                ()->new ClientNotFoundException("Invalid post")
        );

        post.setLikesCount(post.getLikesCount() + 1);

        Like like=Like.builder()
                .account(account)
                .post(post)
                .build();

        likeRepo.save(like);
        postRepo.save(post);
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

        Like like=likeRepo.findByAccount(account).orElseThrow(
                ()-> new InvalidTransaction("You never liked the post")
        );

        Post post=postRepo.findById(request.postId()).orElseThrow(
                ()->new ClientNotFoundException("Invalid post")
        );

        likeRepo.delete(like);
        post.setLikesCount(post.getLikesCount()-1);

        postRepo.save(post);


    }
}
