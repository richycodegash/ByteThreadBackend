package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.DTO.ShareRequest;
import com.jamboSwift.ByteThread.Enums.NotificationStatus;
import com.jamboSwift.ByteThread.Enums.NotificationSubject;
import com.jamboSwift.ByteThread.Enums.SharingStatus;
import com.jamboSwift.ByteThread.Exceptions.ClientNotFoundException;
import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Notification;
import com.jamboSwift.ByteThread.Models.Post;
import com.jamboSwift.ByteThread.Models.ShareInstance;
import com.jamboSwift.ByteThread.Repo.AccountRepo;
import com.jamboSwift.ByteThread.Repo.NotificationRepo;
import com.jamboSwift.ByteThread.Repo.PostRepo;
import com.jamboSwift.ByteThread.Repo.ShareRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class ShareService {
    private final ShareRepo shareRepo;
    private final AccountRepo accountRepo;
    private final PostRepo postRepo;
    private final NotificationRepo notificationRepo;

    @Transactional
    public void shareThePost(ShareRequest request){
        String senderAccountName= request.senderAccount();
        String receiverAccountName= request.receiverAccount();

        Account sender=accountRepo.findByAccountName(senderAccountName).orElseThrow(
                ()-> new ClientNotFoundException("client conflict (your account name is invalid).")
        );

        Account receiver=accountRepo.findByAccountName(receiverAccountName).orElseThrow(
                ()-> new ClientNotFoundException("client conflict (receiver account name is invalid.")
        );

        Post post=postRepo.getReferenceById(request.postId());

        ShareInstance shareInstance = ShareInstance.builder()
                .post(post)
                .whoShared(sender)
                .receivingAccount(receiver)
                .shareStatus(SharingStatus.UNRECEIVED)
                .build();

        shareRepo.save(shareInstance);

        Notification notifyReceiver = Notification.builder()
                .status(NotificationStatus.UNREAD)
                .subject(NotificationSubject.POST_SHARED)
                .post(post)
                .message(sender.getAccountName() + " has shared a post to you")
                .actor(sender)
                .account(receiver)
                .build();

        Notification notification = Notification.builder()
                .status(NotificationStatus.UNREAD)
                .account(sender)
                .actor(sender)
                .message("the sharing process completed successfully at "+ Instant.now())
                .post(post)
                .subject(NotificationSubject.POST_SHARED)
                .build();

        notificationRepo.saveAll(List.of(notifyReceiver,notification));
    }
}
