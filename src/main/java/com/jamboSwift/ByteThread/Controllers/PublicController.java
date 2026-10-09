package com.jamboSwift.ByteThread.Controllers;

import com.jamboSwift.ByteThread.DTO.ClientRegisterRequest;
import com.jamboSwift.ByteThread.DTO.ClientRegisterResponce;
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
@RequestMapping("api/jamboSwift/public")
@AllArgsConstructor
public class PublicController {
    private ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<ClientRegisterResponce> register(@Valid @RequestBody ClientRegisterRequest clientRegisterRequest){
        //System.out.println("email from controller : "+clientRegisterRequest.email()+clientRegisterRequest.password());
        ClientRegisterResponce clientRegisterResponce=clientService.registerUser(clientRegisterRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-App-Version","1.0")
                .body(clientRegisterResponce);
    }
}
