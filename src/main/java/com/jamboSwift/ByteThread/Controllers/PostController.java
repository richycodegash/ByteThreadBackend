package com.jamboSwift.ByteThread.Controllers;

import com.jamboSwift.ByteThread.DTO.PostListsResponce;
import com.jamboSwift.ByteThread.DTO.PostRequest;
import com.jamboSwift.ByteThread.DTO.PostResponce;
import com.jamboSwift.ByteThread.Services.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/jamboSwift")
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<PostResponce> post(@Valid @RequestBody PostRequest request){
        System.out.println("\n***Inside post controller***\n");
        PostResponce responce= postService.postThemShit(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responce);

    }

    @GetMapping("/post")
    public ResponseEntity<Page<PostListsResponce>> getPost(
            @RequestParam String accountName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            ){
        Page<PostListsResponce> responces=postService.getPost(accountName, category, keyword, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responces);

    }

    @GetMapping("/post/{postTitle}")
    public ResponseEntity<Page<PostListsResponce>> getPostBySlug(
            @RequestParam String accountName,
            @PathVariable String postTitle,
            @PageableDefault(size = 10,sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<PostListsResponce> responces =postService.getPostWithSlug(accountName, postTitle, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responces);
    }

    @GetMapping("/{accountName}/post")
    public ResponseEntity<Page<PostListsResponce>> getByAccount(
            @PathVariable String accountName,
            @PageableDefault(size = 10,sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable
    ){
        System.out.println("\n***RECEIVED ACCOUNT NAME: '" + accountName + "'");
        Page<PostListsResponce> responces =postService.getPostByUser(accountName, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responces);
    }
}
