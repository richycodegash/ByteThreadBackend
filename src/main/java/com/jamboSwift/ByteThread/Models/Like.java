package com.jamboSwift.ByteThread.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"post_id", "account_id"})
        }
)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "account_id",nullable = false,unique = true)
    private Account account;

    @CreationTimestamp //automatically fills the timestamp
    @Column(name = "posted_on", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp //automatically updates the timestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
