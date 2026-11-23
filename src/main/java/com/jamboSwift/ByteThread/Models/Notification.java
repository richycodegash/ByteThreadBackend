package com.jamboSwift.ByteThread.Models;

import com.jamboSwift.ByteThread.Enums.NotificationStatus;
import com.jamboSwift.ByteThread.Enums.NotificationSubject;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_status",columnList = "notification_status"),
                @Index(name = "idx_subject",columnList = "notification_subject"),
                @Index(name = "idx_notification_account",columnList = "account"),
                @Index(name = "idx_createdAt",columnList = "created_at"),
                @Index(name = "idx_share",columnList = "share")
        }
)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account",nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_account_id")
    private Account actor; // The user who triggered the notification (e.g., liked/commented)

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "share")
    private ShareInstance shareInstance;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_status",nullable = false)
    private NotificationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "likes")
    private Like likes;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_subject",nullable = false)
    private NotificationSubject subject;

    @CreationTimestamp //automatically fills the timestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp //automatically updates the timestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
