package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.DTO.CommentReplyObject;
import com.jamboSwift.ByteThread.DTO.CommentResponce;
import com.jamboSwift.ByteThread.DTO.PostCommentRequest;
import com.jamboSwift.ByteThread.Enums.CommentType;
import com.jamboSwift.ByteThread.Exceptions.ClientNotFoundException;
import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Client;
import com.jamboSwift.ByteThread.Models.Comment;
import com.jamboSwift.ByteThread.Models.Post;
import com.jamboSwift.ByteThread.Repo.ClientRepo;
import com.jamboSwift.ByteThread.Repo.CommentRepo;
import com.jamboSwift.ByteThread.Repo.PostRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {
    private final ClientRepo clientRepo;
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;

    @Transactional
    public void postComment(PostCommentRequest request){
        String email=request.email().trim().toLowerCase();

        Client client=clientRepo.findByEmail(email).orElseThrow(
                ()-> new ClientNotFoundException("Invalid credentials")
        );

        Account account=client.getAccount();

        Post post=postRepo.findById(request.postId()).orElseThrow(
                ()->new ClientNotFoundException("Invalid post")
        );

        Comment parentComment = null;

        if (request.parentCommentId()!=null){
            parentComment=commentRepo.findById(request.parentCommentId()).orElseThrow(
                    ()-> new ClientNotFoundException("Invalid parent comment.")
            );
        }

        Comment comment=Comment.builder()
                .content(request.content())
                .post(post)
                .account(account)
                .commentType(request.parentCommentId()==null? CommentType.PARENT_COMMENT : CommentType.CHILD_COMMENT)
                .build();

        if (parentComment!=null){
            comment.setParentComment(parentComment);
        }

        commentRepo.save(comment);


    }

    @Transactional
    public Slice<CommentResponce> getComment(Pageable pageable, UUID postId){
        Slice<Comment> comments=commentRepo.findByPost_IdAndCommentType(pageable,postId,CommentType.PARENT_COMMENT);
        System.out.println("**** inside get post service.");

        return comments.map(CommentResponce::fromComment);

    }

    public Slice<CommentReplyObject> getReplies(Pageable pageable,UUID commentId){
        Slice<Comment> replies=commentRepo.findByParentComment_Id(pageable, commentId);

        return replies.map(CommentReplyObject::fromReply);
    }
}
