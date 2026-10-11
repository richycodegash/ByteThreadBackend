package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.DTO.*;
import com.jamboSwift.ByteThread.Enums.Roles;
import com.jamboSwift.ByteThread.Exceptions.ClientNotFoundException;
import com.jamboSwift.ByteThread.Exceptions.InvalidCredantials;
import com.jamboSwift.ByteThread.Exceptions.UnfoundCategory;
import com.jamboSwift.ByteThread.Models.*;
import com.jamboSwift.ByteThread.Repo.*;
import com.jamboSwift.ByteThread.Utilities.HelperClass;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;

@Service
@AllArgsConstructor
public class PostService {
    private final ClientRepo clientRepo;
    private final PostRepo postRepo;
    private final CategoryRepo categoryRepo;
    private final TagsRepo tagsRepo;
    private final HelperClass helperClass;
    private final RandomGeneratorHelper randomGeneratorHelper;
    private final AccountRepo accountRepo;

    @Transactional
    public PostResponce postThemShit(PostRequest request){
        System.out.println("\n**inside post service***\n");
        String normalizedEmail = request.email().trim().toLowerCase();
        String excerpt=request.excerpt();
        Set<String> tags=request.tags();
        Set<String> categories=request.categories();
        String title=request.title().trim().toLowerCase().replace("."," ");
        String theSlug=title.trim().toLowerCase().replace(" ","-");

        System.out.println("\nFetching the client");
        Client client=clientRepo.findByEmail(normalizedEmail).orElseThrow(
                ()->new ClientNotFoundException("Account does not exist")
        );

        System.out.println("\nFetching the account");
        Account account=client.getAccount();

        if (!Objects.equals(account.getAccountName(), request.accountName())){
            throw new InvalidCredantials("Invalid account");
        }

        if (excerpt == null || excerpt.isEmpty()){
            excerpt="JamboSwift";
        }

        /*
            this block here checks for categories
         */
        Set<Category> foundCategories=new HashSet<>();
        for (String name:categories){
            System.out.println("\n***in category loop");
            String slug=helperClass.slugify(name);
            Category category1=categoryRepo.findBySlug(slug).orElseThrow(
                    ()->new UnfoundCategory("No such category found [ "+name+" ].Please chose another relevant category.")
            );
            foundCategories.add(category1);
        }

        /*
            this block here checks and adds new tags to foundTag set and the database (if not found)
         */
        Set<Tag> foundTags=new HashSet<>();
        for (String name:tags){
            System.out.println("\n***in tags loop");
            String perfectName=name.trim().toLowerCase();
            String slug=helperClass.slugify(perfectName);
            Tag tag=tagsRepo.findBySlug(slug).orElseGet(
                    ()->tagsRepo.save(
                            Tag.builder()
                                    .name(perfectName)
                                    .slug(slug)
                                    .build()
                    )
            );
            foundTags.add(tag);
        }

        if (postRepo.existsBySlug(theSlug)){
            theSlug= randomGeneratorHelper.generateRandomSlug(theSlug);
        }



        Post post=Post.builder()
                .title(title)
                .slug(theSlug)
                .account(account)
                .excerpt(excerpt)
                .content(request.content())
                .categories(foundCategories)
                .tags(foundTags)
                .build();

        postRepo.save(post);

        return PostResponce.fromSlug(theSlug);
    }

    public Page<PostListsResponce> getPost(String accountName,String category,
                                           String keyword,Pageable pageable){
        if (!accountRepo.existsByAccountName(accountName)){
            throw new ClientNotFoundException("client conflict.");
        }

        String cleanCategory = (category == null || category.isBlank()) ? null : category.trim();
        String pattern = (keyword == null || keyword.isBlank())
                ? "%"
                : "%" + keyword.trim().toLowerCase() + "%";


        Page<Post> posts=postRepo.findPost(cleanCategory,pattern,pageable);

        return posts.map(PostListsResponce::fromPost);
    }

    public Page<PostListsResponce> getPostWithSlug(String accountName,String postTitle,Pageable pageable){
        if (!accountRepo.existsByAccountName(accountName)){
            throw new ClientNotFoundException("client conflict.");
        }

        String slug=postTitle.trim().toLowerCase();

        Page<Post> post=postRepo.findByTitleContainingIgnoreCase(slug,pageable);


        return post.map(PostListsResponce::fromPost);

    }

    public Page<PostListsResponce> getPostByUser(String accountName,Pageable pageable){
        if (!accountRepo.existsByAccountName(accountName)){
            throw new ClientNotFoundException("client conflict.");
        }

        String accName=accountName.trim().toLowerCase();

        Page<Post> posts=postRepo.findByAccount(accName, pageable);

        return posts.map(PostListsResponce::fromPost);

    }

    public PostListsResponce udpadePost(UpdatePostRequest request){
        String email=request.email().trim().toLowerCase();
        String accountName=request.accountName().trim().toLowerCase();
        UUID id=request.postId();
        String content= (request.content().isPresent()&& !request.content().get().isEmpty()) ? request.content().get().trim() : "null";
        String excerpt=(request.excerpt().isPresent() && !request.excerpt().get().isEmpty()) ? request.excerpt().get().trim() : "null";

        Client client=clientRepo.findByEmail(email).orElseThrow(
                ()->new InvalidCredantials("Invalid credentials.")
        );

        Account account=client.getAccount();

        if (!Objects.equals(account.getAccountName(), accountName)){
            throw new InvalidCredantials("Invalid credentials.You are not the owner of this post.");
        }

        Post post=postRepo.findById(id).orElseThrow(
                ()->new ClientNotFoundException("Invalid post.")
        );

        if (post.getAccount().getId()!=account.getId()){
            throw new InvalidCredantials("This post does not belong to your account.");
        }

        if (!content.equals("null")){
            post.setContent(content);
        }

        if (!excerpt.equals("null")){
            post.setExcerpt(excerpt);
        }

        postRepo.save(post);

        return PostListsResponce.fromPost(post);
    }

    public void deletePost(DeletePostRequest request){
        String email=request.email().trim().toLowerCase();
        UUID id=request.postId();
        String accountName=request.accountName().trim().toLowerCase();

        Client client=clientRepo.findByEmail(email).orElseThrow(
                ()->new InvalidCredantials("Invalid credentials.")
        );

        Account account=client.getAccount();

        if (!Objects.equals(account.getAccountName(), accountName)){
            throw new InvalidCredantials("Invalid credentials.You are not the owner of this post.");
        }

        Post post=postRepo.findById(id).orElseThrow(
                ()->new ClientNotFoundException("Post not found.")
        );

        if (post.getAccount().getId()!=account.getId()){
            throw new InvalidCredantials("This post does not belong to your account.");
        }

        postRepo.delete(post);


    }

    public Page<PostListsResponce> getPostByTag(String tagSlug,String accountName,Pageable pageable){
        if (!accountRepo.existsByAccountName(accountName)){
            throw new ClientNotFoundException("client conflict.");
        }
        Tag tag=tagsRepo.findBySlug(tagSlug).orElseThrow(
                ()->new ClientNotFoundException("Tag does not exist.")
        );

        Page<Post> posts=postRepo.findByTags_Slug(tag.getSlug(),pageable);

        return posts.map(PostListsResponce::fromPost);
    }

}
