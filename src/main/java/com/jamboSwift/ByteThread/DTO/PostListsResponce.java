package com.jamboSwift.ByteThread.DTO;

import com.jamboSwift.ByteThread.Models.Category;
import com.jamboSwift.ByteThread.Models.Post;
import com.jamboSwift.ByteThread.Utilities.TimeConverter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
public record PostListsResponce(
        String title,
        String slug,
        String content,
        String excerpt,
        String authorName,
        Set<String> categoryName,
        LocalDateTime createdAt
) {
    public static PostListsResponce fromPost(Post post){
        Set<String> categories=new HashSet<>();
        for (Category category:post.getCategories()){
            String categoryName=category.getName();
            categories.add(categoryName);
        }

        LocalDateTime creationDate= TimeConverter.toLocalDateTime(post.getCreatedAt(),"Africa/Nairobi");
        return PostListsResponce.builder()
                .title(post.getTitle())
                .authorName(post.getAccount().getAccountName())
                .categoryName(categories)
                .content(post.getContent())
                .excerpt(post.getExcerpt())
                .createdAt(creationDate)
                .slug(post.getSlug())
                .build();
    }
}
