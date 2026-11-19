package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.Enums.NotificationStatus;
import com.jamboSwift.ByteThread.Enums.NotificationSubject;
import com.jamboSwift.ByteThread.Models.*;
import com.jamboSwift.ByteThread.Repo.FollowRepo;
import com.jamboSwift.ByteThread.Repo.NotificationRepo;
import com.jamboSwift.ByteThread.Utilities.LikeCreatedEvent;
import com.jamboSwift.ByteThread.Utilities.PostCreatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor

public class NotificationService {

    private final FollowRepo followRepo;
    private final NotificationRepo notificationRepo;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNewPostNotifications(PostCreatedEvent event){

        Post savedPost = event.getPost();
        Client client = event.getClient();
        Account account = event.getAuthor();

        /*// 1. Return a plain List from your repository instead of Optional<List<...>>
        List<Follow> followers = followRepo.findByFollowedAccount(account);

        if (followers != null && !followers.isEmpty()) {
            List<Notification> notificationsToSave = new ArrayList<>();

            String senderName = client.getFirstName() + " " + client.getSecondName()
                    + " (" + account.getAccountName() + ") posted a new thread. Click to view.";

            for (Follow follow : followers) {
                Account followingAccount = follow.getFollowingAccount();

                Notification notification = Notification.builder()
                        .account(followingAccount)
                        .actor(account)
                        .message(senderName)
                        .post(savedPost)
                        .subject(NotificationSubject.NEW_POST)
                        .status(NotificationStatus.UNREAD)
                        .build();

                notificationsToSave.add(notification);
            }

            // 2. Batch save all notifications in one single database call
            notificationRepo.saveAll(notificationsToSave);
            System.out.println("\n*** Successfully saved " + notificationsToSave.size() + " notifications asynchronously! (for new post)***\n");
        }*/

        String senderName = client.getFirstName() + " " + client.getSecondName()
                + " (" + account.getAccountName() + ") posted a new thread. Click to view.";

        int page = 0;
        int size = 1000; // Process 1,000 followers per batch
        boolean hasMore = true;
        Long notificationCounter=0L;

        while (hasMore) {
            Pageable pageable = PageRequest.of(page, size);
            Slice<Follow> followerSlice = followRepo.findByFollowedAccount(account, pageable);

            List<Follow> followers = followerSlice.getContent();
            if (followers.isEmpty()) {
                break;
            }

            List<Notification> notificationsToSave = new ArrayList<>();
            for (Follow follow : followers) {
                Notification notification = Notification.builder()
                        .account(follow.getFollowingAccount())
                        .actor(account)
                        .message(senderName)
                        .post(savedPost)
                        .subject(NotificationSubject.NEW_POST)
                        .status(NotificationStatus.UNREAD)
                        .build();

                notificationsToSave.add(notification);
            }

            // Save this chunk safely without memory spikes
            notificationRepo.saveAll(notificationsToSave);

            // Move to the next chunk
            hasMore = followerSlice.hasNext();
            page++;
            notificationCounter++;

            System.out.println("Processing chunk page: " + page + ", fetched " + followers.size() + " followers.");
        }
        System.out.println("\n*** Successfully saved " + notificationCounter + " notifications asynchronously! (for new post)***\n");
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createNewLikeNotification(LikeCreatedEvent event){

        Post post = event.getPost();
        Client client = event.getClient();
        Account account = event.getAccount();
        Like savedLike=event.getSavedLike();

        int page = 0;
        int size = 1000; // Process 1,000 followers per batch
        boolean hasMore = true;
        Long notificationCounter=0L;

        String senderName = client.getFirstName()+" "+client.getSecondName()+"("+account.getAccountName()+")"+" have liked you post("+post.getTitle()+").";

        while (hasMore) {
            Pageable pageable = PageRequest.of(page, size);
            Slice<Follow> followerSlice = followRepo.findByFollowedAccount(account, pageable);

            List<Follow> followers = followerSlice.getContent();
            if (followers.isEmpty()) {
                break;
            }

            List<Notification> notificationsToSave = new ArrayList<>();
            for (Follow follow : followers) {
                Account followingAccount = follow.getFollowingAccount();

                Notification notification = Notification.builder()
                        .account(followingAccount)
                        .actor(account)
                        .message(senderName)
                        .likes(savedLike)
                        .subject(NotificationSubject.NEW_POST)
                        .status(NotificationStatus.UNREAD)
                        .build();

                notificationsToSave.add(notification);
            }

            // Save this chunk safely without memory spikes
            notificationRepo.saveAll(notificationsToSave);

            // Move to the next chunk
            hasMore = followerSlice.hasNext();
            page++;
            notificationCounter++;
        }

        System.out.println("\n*** Successfully saved " + notificationCounter + " notifications asynchronously! (for new like)***\n");

        /*// 1. Return a plain List from your repository instead of Optional<List<...>>
        List<Follow> followers = followRepo.findByFollowedAccount(account);

        if (followers != null && !followers.isEmpty()) {
            List<Notification> notificationsToSave = new ArrayList<>();

            String senderName = client.getFirstName()+" "+client.getSecondName()+"("+account.getAccountName()+")"+" have liked you post("+post.getTitle()+").";

            for (Follow follow : followers) {
                Account followingAccount = follow.getFollowingAccount();

                Notification notification = Notification.builder()
                        .account(followingAccount)
                        .actor(account)
                        .message(senderName)
                        .likes(savedLike)
                        .subject(NotificationSubject.NEW_POST)
                        .status(NotificationStatus.UNREAD)
                        .build();

                notificationsToSave.add(notification);
            }

            // 2. Batch save all notifications in one single database call
            notificationRepo.saveAll(notificationsToSave);
            System.out.println("\n*** Successfully saved " + notificationsToSave.size() + " notifications asynchronously! (for new like) ***\n");
        }*/
    }
}
