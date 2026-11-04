package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.DTO.*;
import com.jamboSwift.ByteThread.Enums.ClientStatus;
import com.jamboSwift.ByteThread.Enums.FollowStatus;
import com.jamboSwift.ByteThread.Enums.Roles;
import com.jamboSwift.ByteThread.Exceptions.*;
import com.jamboSwift.ByteThread.Models.Account;
import com.jamboSwift.ByteThread.Models.Client;
import com.jamboSwift.ByteThread.Models.Follow;
import com.jamboSwift.ByteThread.Repo.AccountRepo;
import com.jamboSwift.ByteThread.Repo.ClientRepo;
import com.jamboSwift.ByteThread.Repo.FollowRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepo clientRepo;
    private final RandomGeneratorHelper randomGeneratorHelper;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepo accountRepo;
    private final FollowRepo followRepo;

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

    @Transactional
    public void followSomeone(FollowRequest request){

        String normalizedEmail=request.email().trim().toLowerCase();
        String followedAccountname=request.accountToFollow().trim().toLowerCase();

        Client client=clientRepo.findByEmail(normalizedEmail).orElseThrow(
                ()->new ClientNotFoundException("following account does not exist")
        );

        Account followingAccount = client.getAccount();

        Account followedAccount=accountRepo.findByAccountName(followedAccountname).orElseThrow(
                ()->new ClientNotFoundException("Followed account does not exist")
        );

        if (followRepo.existsByFollowedAccountAndFollowingAccountAndStatus(followedAccount,followingAccount,FollowStatus.FOLLOWING))
            throw new InvalidTransaction("You had already followed this account");

        if (followedAccount.equals(followingAccount))
            throw new InvalidTransaction("You cannot follow yourself");

        //Follow oldFollowObject=followRepo.findByFollowedAccountAndFollowingAccount(followedAccount,followingAccount).or();

        /*Follow follow=Follow.builder()
                .followedAccount(followedAccount)
                .followingAccount(followingAccount)
                .status(FollowStatus.FOLLOWING)
                .build();*/

        Follow follow=followRepo.findByFollowedAccountAndFollowingAccount(followedAccount,followingAccount).orElseGet(
                ()->followRepo.save(
                        Follow.builder()
                                .followedAccount(followedAccount)
                                .followingAccount(followingAccount)
                                .build()
                )
        );

        follow.setStatus(FollowStatus.FOLLOWING);



        followingAccount.setFollowingCountCount(Math.max(0,followingAccount.getFollowingCountCount()+1));
        followedAccount.setFollowersCountCount(Math.max(0,followedAccount.getFollowersCountCount()+1));


        followRepo.save(follow);
        accountRepo.saveAll(List.of(followedAccount,followingAccount));


    }

    @Transactional
    public void unfollowSomeone(FollowRequest request){
        String normalizedEmail=request.email().trim().toLowerCase();
        String followedAccountname=request.accountToFollow().trim().toLowerCase();

        Client client=clientRepo.findByEmail(normalizedEmail).orElseThrow(
                ()->new ClientNotFoundException("following account does not exist")
        );

        Account followingAccount = client.getAccount();

        Account followedAccount=accountRepo.findByAccountName(followedAccountname).orElseThrow(
                ()->new ClientNotFoundException("Followed account does not exist")
        );

        if (followedAccount.equals(followingAccount))
            throw new InvalidTransaction("You cannot unfollow yourself");

        Follow follow = followRepo.findByFollowedAccountAndFollowingAccount(followedAccount,followingAccount).orElseThrow(
                ()->new ClientNotFoundException("You never followed this account")
        );

        if (follow.getStatus().equals(FollowStatus.NOT_FOLLOWING)){
            throw new InvalidTransaction("You had already unfollowed this account");
        }else {
            follow.setStatus(FollowStatus.NOT_FOLLOWING);
            followRepo.save(follow);
        }

        followingAccount.setFollowingCountCount(Math.max(0, followingAccount.getFollowingCountCount()-1));
        followedAccount.setFollowersCountCount(Math.max(0, followedAccount.getFollowersCountCount())-1);

        accountRepo.saveAll(List.of(followedAccount,followingAccount));


    }

    @Transactional
    public AccountDetails getAccountDetails(String email){
        String normalizedEmail=email.trim().toLowerCase();

        Client client=clientRepo.findByEmail(normalizedEmail).orElseThrow(
                ()->new ClientNotFoundException("following account does not exist")
        );

        Account account = client.getAccount();

        return AccountDetails.fromAccount(account);
    }

}
