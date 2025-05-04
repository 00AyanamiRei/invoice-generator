package com.makara.invoicegenerator;

import com.makara.invoicegenerator.models.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories("com.makara.invoicegenerator.models.dao")
@EntityScan("com.makara.invoicegenerator.models.entity")
public class InvoiceGeneratorApplication implements CommandLineRunner {

    @Autowired
    IUploadFileService uploadService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(InvoiceGeneratorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        uploadService.deleteAll();
        uploadService.init();

        String password = "password";

        for (int i = 0; i < 2; i++) {
            String bcryptPassword = passwordEncoder.encode(password);
            System.out.println(bcryptPassword);
        }
    }
}