package com.jamboSwift.ByteThread.Utilities;

import org.springframework.stereotype.Service;

@Service
public class HelperClass {
    public String slugify(String name){
        return name.trim().toLowerCase().replace(" ","-");
    }
}
