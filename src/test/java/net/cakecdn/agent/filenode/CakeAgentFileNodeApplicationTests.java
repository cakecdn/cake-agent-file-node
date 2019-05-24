package net.cakecdn.agent.filenode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CakeAgentFileNodeApplicationTests {

    @Value("${cake.node.fileDir}")
    private String fileDir;

    @Test
    public void contextLoads() {
    }

    @Test
    public void fileDir() {
        System.out.println(fileDir);
    }

}
