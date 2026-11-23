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
import com.jamboSwift.ByteThread.Utilities.TimeConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        String zone =request.zoneId().trim();

        Account sender=accountRepo.findByAccountName(senderAccountName).orElseThrow(
                ()-> new ClientNotFoundException("client conflict (your account name is invalid).")
        );

        Account receiver=accountRepo.findByAccountName(receiverAccountName).orElseThrow(
                ()-> new ClientNotFoundException("client conflict (receiver account name is invalid.")
        );

        Post post=postRepo.getReferenceById(request.postId());
        post.setSharesCount(Math.max(0, post.getSharesCount() + 1));

        ShareInstance shareInstance = ShareInstance.builder()
                .post(post)
                .whoShared(sender)
                .receivingAccount(receiver)
                .shareStatus(SharingStatus.UNRECEIVED)
                .build();

        shareRepo.save(shareInstance);

        System.out.println("\n*** share instance id is : rgx"+shareInstance.getId()+"v");
        LocalDateTime creationDate= TimeConverter.toLocalDateTime(Instant.now(),zone);
        String formatedDate = DateTimeFormatter.ofPattern("d, MMM yyyy").format(creationDate);
        String formatedTime= DateTimeFormatter.ofPattern("HH:mm a").format(creationDate);

        Notification notifyReceiver = Notification.builder()
                .status(NotificationStatus.UNREAD)
                .subject(NotificationSubject.POST_SHARED)
                .post(post)
                .message(sender.getAccountName() + " has shared a post to you")
                .actor(sender)
                .account(receiver)
                .shareInstance(shareInstance)
                .build();

        Notification notification = Notification.builder()
                .status(NotificationStatus.UNREAD)
                .account(sender)
                .actor(sender)
                .message("the sharing process completed successfully on "+ formatedDate+" at "+formatedTime)
                .post(post)
                .subject(NotificationSubject.POST_SHARED_NOTIFICATION)
                .build();

        notificationRepo.saveAll(List.of(notifyReceiver,notification));
        postRepo.save(post);
    }
}
