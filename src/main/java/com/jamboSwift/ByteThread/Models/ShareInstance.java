package com.jamboSwift.ByteThread.Models;

import com.jamboSwift.ByteThread.Enums.SharingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "share_instance",
        indexes = {
                @Index(name = "idx_share_status",columnList = "share_status")
        }
)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareInstance {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(nullable = false)
        private Post post;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "receiving_account",nullable = false)
        private Account receivingAccount;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "who_shared")
        private Account whoShared; // The user who triggered the sharing

        @Column(name = "share_status",nullable = false)
        @Enumerated(EnumType.STRING)
        private SharingStatus shareStatus=SharingStatus.PENDING;

        @CreationTimestamp //automatically fills the timestamp
        @Column(name = "shared_on", nullable = false, updatable = false)
        private Instant sharedOn;

        @UpdateTimestamp //automatically updates the timestamp
        @Column(name = "updated_on")
        private Instant updatedOn;
}
