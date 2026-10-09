package com.jamboSwift.ByteThread.Controllers;

import com.jamboSwift.ByteThread.DTO.*;
import com.jamboSwift.ByteThread.Services.CategoriesService;
import com.jamboSwift.ByteThread.Services.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/jamboSwift")
@AllArgsConstructor
public class ClientController {
    private ClientService clientService;
    private CategoriesService categoriesService;

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



}
