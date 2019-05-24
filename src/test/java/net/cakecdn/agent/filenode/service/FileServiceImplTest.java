package net.cakecdn.agent.filenode.service;

import net.cakecdn.agent.filenode.CakeAgentFileNodeApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FileServiceImplTest extends CakeAgentFileNodeApplicationTests {

    @Autowired
    private FileServiceImpl uploadService;

    @Test
    public void fileExists() {
        boolean exists = uploadService.fileExists(123456L, "/", "IGXN201803027_00900.jpg");
        System.out.println(exists);
    }
}