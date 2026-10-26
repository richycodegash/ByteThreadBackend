package com.jamboSwift.ByteThread.Repo;

import com.jamboSwift.ByteThread.Enums.CommentType;
import com.jamboSwift.ByteThread.Models.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CommentRepo extends JpaRepository<Comment, UUID> {
    Slice<Comment> findByPost_IdAndCommentType(Pageable pageable, UUID postId, CommentType commentType);

    Slice<Comment> findByParentComment_Id(Pageable pageable,UUID parentCommentId);

    @Query("SELECT c.post.id, COUNT(c) FROM Comment c WHERE c.post.id IN :postIds AND c.commentType = :commentType GROUP BY c.post.id")
    List<Object[]> countCommentsByPostIds(
            @Param("postIds") List<UUID> postIds,
            @Param("commentType") CommentType commentType
    );

    long countByPostId(UUID postId);
}
