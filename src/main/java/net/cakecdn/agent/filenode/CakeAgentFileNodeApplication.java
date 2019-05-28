package net.cakecdn.agent.filenode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableFeignClients
public class CakeAgentFileNodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CakeAgentFileNodeApplication.class, args);
    }

}
