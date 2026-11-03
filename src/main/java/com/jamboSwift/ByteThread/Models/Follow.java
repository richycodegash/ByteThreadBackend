package com.jamboSwift.ByteThread.Models;

import com.jamboSwift.ByteThread.Enums.FollowStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "follows",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"followed_account","following_account"})
        }
)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne // ✅ Changed from OneToOne to ManyToOne
    @JoinColumn(name = "followed_account", nullable = false)
    private Account followedAccount;

    @ManyToOne // ✅ Changed from OneToOne to ManyToOne
    @JoinColumn(name = "following_account", nullable = false)
    private Account followingAccount;

    @CreationTimestamp //automatically fills the timestamp
    @Column(name = "followed_on", nullable = false, updatable = false)
    private Instant followedOn;

    @UpdateTimestamp //automatically updates the timestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "follow_status")
    @Enumerated(EnumType.STRING)
    private FollowStatus status;
}
