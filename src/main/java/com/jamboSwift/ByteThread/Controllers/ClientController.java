package com.jamboSwift.ByteThread.Controllers;

import com.jamboSwift.ByteThread.DTO.*;
import com.jamboSwift.ByteThread.Services.CategoriesService;
import com.jamboSwift.ByteThread.Services.ClientService;
import com.jamboSwift.ByteThread.Services.TagsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/jamboSwift")
@AllArgsConstructor
public class ClientController {
    private ClientService clientService;
    private CategoriesService categoriesService;
    private TagsService tagsService;

    @PostMapping("/account-name")
    public ResponseEntity<AccountChangeResponce> changeAccountName(@Valid @RequestBody ChangeAccountNameRequest request){
        AccountChangeResponce responce=clientService.changeAccountName(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responce);


    }

    @PostMapping("/password")
    public ResponseEntity<ChangePasswordResponce> changePassword(@Valid @RequestBody ChangePassworRequest request){
        ChangePasswordResponce responce=clientService.changePassword(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responce);

    }

    @PostMapping("/category")
    public ResponseEntity<CategoryCreationResponce> createCategory(@Valid @RequestBody CategoryCreationRequest request){
        CategoryCreationResponce responce=categoriesService.createCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responce);

    }

    @GetMapping("/category")
    public ResponseEntity<List<String>> getCategories(){
        List<String> categories=categoriesService.getcategories();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categories);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<String>> getTags(){
        List<String> tags=tagsService.getTags();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tags);
    }

    @PostMapping("/follow")
    public ResponseEntity<String> followAccount(@Valid @RequestBody FollowRequest followRequest){
        clientService.followSomeone(followRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("You are all set 👍👍");
    }

    @PutMapping("/follow")
    public ResponseEntity<String> unfollowAccount(@Valid @RequestBody FollowRequest followRequest){
        clientService.unfollowSomeone(followRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("You are all set 👍👍");
    }

    @GetMapping("/account")
    public ResponseEntity<AccountDetails> getAccountDits(@RequestParam String email){
        AccountDetails details=clientService.getAccountDetails(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(details);

    }

    @PostMapping("/account-bio")
    public ResponseEntity<String> updateBio(@Valid @RequestBody UpdateBioRequest request){
        clientService.updateBio(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("You are all set 👍👍");
    }

    @PostMapping("/account-profile-pic")
    public ResponseEntity<String> updateProfilePic(
            @RequestPart("request") UpdateProfilePicRequest request,
            @RequestParam(value = "file",required = false) MultipartFile multipartFile
    ){
        clientService.updateProfilePic(multipartFile,request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("You are all set 👍👍");
    }



}
