package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.DTO.CategoryCreationRequest;
import com.jamboSwift.ByteThread.DTO.CategoryCreationResponce;
import com.jamboSwift.ByteThread.Enums.Roles;
import com.jamboSwift.ByteThread.Exceptions.CategoryExists;
import com.jamboSwift.ByteThread.Exceptions.ClientNotFoundException;
import com.jamboSwift.ByteThread.Exceptions.InvalidCredantials;
import com.jamboSwift.ByteThread.Models.Category;
import com.jamboSwift.ByteThread.Models.Client;
import com.jamboSwift.ByteThread.Repo.CategoryRepo;
import com.jamboSwift.ByteThread.Repo.ClientRepo;
import com.jamboSwift.ByteThread.Utilities.HelperClass;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoriesService {
    private final ClientRepo clientRepo;
    private final HelperClass helperClass;
    private final CategoryRepo categoryRepo;

    public CategoryCreationResponce createCategory(CategoryCreationRequest request){
        String normalizedEmail = request.email().trim().toLowerCase();
        String categoryName= request.categoryName().trim().toLowerCase();
        String description=request.description().trim().toLowerCase();

        Client client=clientRepo.findByEmail(normalizedEmail).orElseThrow(
                ()-> new ClientNotFoundException("Account does not exist")
        );

        Roles clientRole=client.getRole();
        if (!clientRole.equals(Roles.ADMIN)){
            throw new InvalidCredantials("You are not allowed to use this endpoint.");
        }

        String categorySlug=helperClass.slugify(categoryName);

        if (categoryRepo.existsBySlug(categorySlug)){
            throw new CategoryExists("Category exist.Try another one.");
        }

        Category newCategory=Category.builder()
                .name(categoryName)
                .slug(categorySlug)
                .description(description)
                .build();

        categoryRepo.save(newCategory);

        String clientName=client.getFirstName()+" "+client.getSecondName();

        return CategoryCreationResponce.fromCategory(categoryName, clientName);
    }
}
