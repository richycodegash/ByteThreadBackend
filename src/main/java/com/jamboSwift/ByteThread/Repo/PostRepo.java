package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Models.Post;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PostRepo extends JpaRepository<Post, UUID> {
    boolean existsBySlug(String slug);

    @Query("""
        SELECT p FROM Post p
        WHERE (CAST(:category AS string) IS NULL
               OR EXISTS (SELECT c FROM p.categories c WHERE c.name = :category))
          AND (LOWER(p.title) LIKE :pattern OR LOWER(p.content) LIKE :pattern)
        """)
    Page<Post> findPost(@Param("category") String category,
                        @Param("pattern") String pattern, @NonNull Pageable pageable);

    Page<Post> findBySlug(String slug, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCase(String postTitle,Pageable pageable);

    @EntityGraph(attributePaths = {"account"})
    @Query("""
    SELECT p FROM Post p
    WHERE LOWER(TRIM(p.account.accountName)) = LOWER(TRIM(:accountName))
""")
    Page<Post> findByAccount(@Param("accountName") String accountName,Pageable pageable);
}
