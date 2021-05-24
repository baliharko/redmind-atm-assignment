package com.baliharko.redmindatm;

import com.baliharko.redmindatm.model.Bill;
import com.baliharko.redmindatm.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
@RequiredArgsConstructor
public class RedmindAtmApplication implements CommandLineRunner {

    private final BillRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(RedmindAtmApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        repository.deleteAll();
        repository.saveAll(Arrays.asList(
                new Bill(1000),
                new Bill(1000),
                new Bill(500),
                new Bill(500),
                new Bill(500),
                new Bill(100),
                new Bill(100),
                new Bill(100),
                new Bill(100),
                new Bill(100)
        ));
    }
}
