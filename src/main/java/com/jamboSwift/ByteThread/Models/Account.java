package com.jamboSwift.ByteThread.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "account",
        indexes = {
                @Index(name = "idx_account_name",columnList = "account_name")
        }
)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @MapsId //this tells hibernate to combine both pk and fk
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "account_name",unique = true)
    private String accountName;

    @CreationTimestamp //automatically fills the timestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp //automatically updates the timestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avata_url")
    private String avatarUrl;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Post> posts;

}
