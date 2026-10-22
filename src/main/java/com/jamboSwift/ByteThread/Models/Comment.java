package com.jamboSwift.ByteThread.Models;

import com.jamboSwift.ByteThread.Enums.CommentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "comment")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "content",columnDefinition = "TEXT")
    private String content;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "account_id",nullable = false)
    private Account account;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.ALL)
    private List<Comment> replies=new ArrayList<>();

    @CreationTimestamp //automatically fills the timestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp //automatically updates the timestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "comment_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentType commentType;

}
