package com.workonenight.winteambe;

import com.workonenight.winteambe.common.ResourceRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableMongoRepositories(repositoryBaseClass = ResourceRepositoryImpl.class)
public class WinteamBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinteamBeApplication.class, args);
    }



}
