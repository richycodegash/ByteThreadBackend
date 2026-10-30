package com.jamboSwift.ByteThread.Controllers;

import com.jamboSwift.ByteThread.DTO.PostLikeRequest;
import com.jamboSwift.ByteThread.Services.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/jamboSwift")
@AllArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<String> postLike(@RequestBody PostLikeRequest request){
        likeService.PostLike(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("You are all set 👍👍");
    }

    @DeleteMapping("/like")
    public ResponseEntity<Void> dislike(@RequestBody PostLikeRequest request){
        likeService.dislike(request);

        return ResponseEntity
                .noContent()
                .build();
    }
}
