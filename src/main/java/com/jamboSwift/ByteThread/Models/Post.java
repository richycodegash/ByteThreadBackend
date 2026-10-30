package com.jamboSwift.ByteThread.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "post")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "slug",nullable = false,unique = true)
    private String slug;

    //@Lob
    @Column(name = "content",nullable = false,columnDefinition = "TEXT")
    private String content;

    @Column(name = "excerpt")
    private String excerpt;

    @Column(name="image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @CreationTimestamp //automatically fills the timestamp
    @Column(name = "posted_on", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp //automatically updates the timestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToMany
    @JoinTable(
            name = "post_categories",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    /*@OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> commentList =new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Like> likesList =new ArrayList<>();*/

    @Column(nullable = false)
    @ColumnDefault("0")
    private long likesCount = 0;

    @Column(nullable = false)
    @ColumnDefault("0")
    private long commentsCount = 0;
}
