package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.Repo.AccountRepo;
import com.jamboSwift.ByteThread.Repo.ClientRepo;
import com.jamboSwift.ByteThread.Repo.PostRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RandomGeneratorHelper {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final ClientRepo clientRepo;
    private final AccountRepo accountRepo;
    private final PostRepo postRepo;

    public String generateAccountName(String firstName,String secondName){
        String reference;
        String randomCharacters;
        int randomNumbers;
        do {
            randomCharacters= UUID.randomUUID().toString().substring(0, 3).toUpperCase();
            randomNumbers=34+ SECURE_RANDOM.nextInt(100);
            reference=firstName+secondName+"@"+randomCharacters+randomNumbers;
        }while (accountRepo.existsByAccountName(reference));
        return reference.trim().toLowerCase();
    }

    public String generateRandomSlug(String slug){
        String randomCharacters;
        String newSlug;

        do {
            randomCharacters=UUID.randomUUID().toString().substring(0,3).toLowerCase();
            newSlug=slug+randomCharacters.trim().replace(" ","-");
        }while (postRepo.existsBySlug(newSlug));

        return newSlug;
    }
}
