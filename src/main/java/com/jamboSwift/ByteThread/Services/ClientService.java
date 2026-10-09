package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.DTO.*;
import com.jamboSwift.ByteThread.Enums.ClientStatus;
import com.jamboSwift.ByteThread.Enums.Roles;
import com.jamboSwift.ByteThread.Exceptions.AccountExists;
import com.jamboSwift.ByteThread.Exceptions.ClientAlreadyRegistered;
import com.jamboSwift.ByteThread.Exceptions.ClientNotFoundException;
import com.jamboSwift.ByteThread.Exceptions.InvalidCredantials;
import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Client;
import com.jamboSwift.ByteThread.Repo.AccountRepo;
import com.jamboSwift.ByteThread.Repo.ClientRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepo clientRepo;
    private final RandomGeneratorHelper randomGeneratorHelper;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepo accountRepo;

    @Transactional
    public ClientRegisterResponce registerUser(ClientRegisterRequest clientRegisterRequest){
        String normalizedEmail = clientRegisterRequest.email().trim().toLowerCase();
        boolean wasUserRegistered=clientRepo.existsByEmail(normalizedEmail);

        if (wasUserRegistered){
            throw new ClientAlreadyRegistered("An account with this email address already exists");
        }

        String accountName= randomGeneratorHelper.generateAccountName(clientRegisterRequest.firstName(),clientRegisterRequest.secondName());
        String hashedPassword= passwordEncoder.encode(clientRegisterRequest.password());
        Client ourClient=Client.builder()
                .email(normalizedEmail)
                .role(Roles.USER)
                .password(hashedPassword)
                .firstName(clientRegisterRequest.firstName())
                .secondName(clientRegisterRequest.secondName())
                .status(ClientStatus.ACTIVE)
                .phoneNumber(clientRegisterRequest.phoneNumber())
                .build();

        Account account=Account.builder()
                .client(ourClient)
                .bio("null")
                .accountName(accountName)
                .avatarUrl("null")
                .build();

        ourClient.setAccount(account);

        Client savedClient=clientRepo.save(ourClient);

        return ClientRegisterResponce.fromClient(savedClient);
    }

    @Transactional
    public AccountChangeResponce changeAccountName(ChangeAccountNameRequest request){
        String normalizedEmail = request.email().trim().toLowerCase();
        String password= request.password();
        String accountName= request.accountName();

        /*
        get the client
         */
        Client client=clientRepo.findByEmail(normalizedEmail).orElseThrow(
                ()->new ClientNotFoundException("Account does not exist")
        );

        if (!passwordEncoder.matches(password,client.getPassword())){
            throw new InvalidCredantials("Invalid email or password");
        }

        if (accountRepo.existsByAccountName(accountName)){
            throw new AccountExists("Account name already taken.Please try again");
        }

        Account account = client.getAccount();
        account.setAccountName(accountName);


        return AccountChangeResponce.fromAccount(account);


    }

    @Transactional
    public  ChangePasswordResponce changePassword(ChangePassworRequest request){
        String normalizedEmail = request.email().trim().toLowerCase();

        Client client=clientRepo.findByEmail(normalizedEmail).orElseThrow(
                ()-> new ClientNotFoundException("Account does not exist")
        );

        if (!passwordEncoder.matches(request.oldPassword(), client.getPassword())){
            throw new InvalidCredantials("Invalid email or password");
        }

        client.setPassword(passwordEncoder.encode(request.newPassword()));

        return ChangePasswordResponce.buildResponce();

    }

}
