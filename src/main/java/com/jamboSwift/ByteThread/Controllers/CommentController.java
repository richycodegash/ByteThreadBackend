package com.jamboSwift.ByteThread.Controllers;

import com.jamboSwift.ByteThread.DTO.CommentReplyObject;
import com.jamboSwift.ByteThread.DTO.CommentResponce;
import com.jamboSwift.ByteThread.DTO.PostCommentRequest;
import com.jamboSwift.ByteThread.Services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/jamboSwift")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<String> postComment(@RequestBody PostCommentRequest request){
        commentService.postComment(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("You are all set 👍👍");

    }

    @GetMapping("/comment")
    public ResponseEntity<Slice<CommentResponce>> getComment(
            @PageableDefault(sort = "createdAt",direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam("post") UUID postId
            ){
        Slice<CommentResponce> responce=commentService.getComment(pageable, postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responce);

    }

    @GetMapping("/{parentId}/comment")
    public ResponseEntity<Slice<CommentReplyObject>> getReplies(
            @PathVariable UUID parentId,
            @PageableDefault(sort = "createdAt",direction = Sort.Direction.ASC)Pageable pageable

    ){
        Slice<CommentReplyObject> responce=commentService.getReplies(pageable,parentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responce);
    }
}
