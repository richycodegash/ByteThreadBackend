package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.DTO.PostLikeRequest;
import com.jamboSwift.ByteThread.Enums.NotificationStatus;
import com.jamboSwift.ByteThread.Enums.NotificationSubject;
import com.jamboSwift.ByteThread.Exceptions.ClientNotFoundException;
import com.jamboSwift.ByteThread.Exceptions.InvalidTransaction;
import com.jamboSwift.ByteThread.Models.*;
import com.jamboSwift.ByteThread.Repo.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepo likeRepo;
    private final ClientRepo clientRepo;
    private final PostRepo postRepo;
    private final AccountRepo accountRepo;
    private final FollowRepo followRepo;
    private final NotificationRepo notificationRepo;

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

        Like savedLike=likeRepo.save(like);
        postRepo.save(post);
        accountRepo.save(account);

        Optional<List<Follow>> allAccountsFollowingMe=followRepo.findByFollowedAccount(account);
        System.out.println("Follow count : "+ (long) allAccountsFollowingMe.get().size());
        allAccountsFollowingMe.get().forEach(
                follow -> {
                    Account followingAccount=follow.getFollowingAccount();
                    Account followedAccount=follow.getFollowedAccount();
                    System.out.println("\n***account name is : "+account.getAccountName()+",followed account name is "+followedAccount.getAccountName());
                    if (account.equals(follow.getFollowedAccount())){
                        Notification notification =Notification.builder()
                                .account(followingAccount)
                                .actor(account)
                                .message(client.getFirstName()+" "+client.getSecondName()+"("+account.getAccountName()+")"+" have liked you post("+post.getTitle()+").")
                                .likes(savedLike)
                                .subject(NotificationSubject.NEW_LIKE)
                                .status(NotificationStatus.UNREAD)
                                .build();

                        notificationRepo.save(notification);
                    }
                }
        );

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
