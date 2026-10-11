package com.jamboSwift.ByteThread.Services;

import com.jamboSwift.ByteThread.Models.Tag;
import com.jamboSwift.ByteThread.Repo.TagsRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TagsService {
    private TagsRepo tagsRepo;

    public List<String> getTags(){
        List<Tag> tags=tagsRepo.findAll();
        List<String> tagsName=new ArrayList<>();
        String tagName;

        for (Tag tag : tags){
            tagName=tag.getName();
            tagsName.add(tagName);
        }

        return tagsName;
    }
}
