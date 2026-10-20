package com.jamboSwift.ByteThread.Controllers;

import com.jamboSwift.ByteThread.DTO.ClientRegisterRequest;
import com.jamboSwift.ByteThread.DTO.ClientRegisterResponce;
import com.jamboSwift.ByteThread.Services.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/jamboSwift/public")
@RequiredArgsConstructor
public class PublicController {
    private ClientService clientService;
    private int counter=1;

    @PostMapping("/register")
    public ResponseEntity<ClientRegisterResponce> register(@Valid @RequestBody ClientRegisterRequest clientRegisterRequest){
        //System.out.println("email from controller : "+clientRegisterRequest.email()+clientRegisterRequest.password());
        ClientRegisterResponce clientRegisterResponce=clientService.registerUser(clientRegisterRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-App-Version","1.0")
                .body(clientRegisterResponce);
    }

    @GetMapping("/greet")
    public ResponseEntity<String> howIsMe(){
        return ResponseEntity.ok(counter++ +". I am alive🎉🎉");
    }
}
