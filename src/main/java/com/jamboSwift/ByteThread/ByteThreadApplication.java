package com.jamboSwift.ByteThread;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ByteThreadApplication {

	public static void main(String[] args) {
        // 1. Load .env file explicitly before Spring starts
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String pass = dotenv.get("DB_PASSWORD");
        System.out.println("******DEBUG PASS: [" + pass + "] Length: " + (pass != null ? pass.length() : 0));

        // 2. Set environment variables into System properties
        dotenv.entries().forEach(entry -> {
            // .trim() strips \r, \n, and trailing spaces
            String cleanValue = entry.getValue().trim();
            System.setProperty(entry.getKey(), cleanValue);
        });
        //System.out.println("\n\n******DB_URL = " + System.getenv("DB_URL"));

		SpringApplication.run(ByteThreadApplication.class, args);
	}
}
