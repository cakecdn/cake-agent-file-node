package net.cakecdn.agent.filenode.service;

import net.cakecdn.agent.filenode.CakeAgentFileNodeApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FileServiceImplTest extends CakeAgentFileNodeApplicationTests {

    @Autowired
    private FileServiceImpl fileService;

    @Test
    public void fileExists() {
        boolean exists = fileService.fileExists(123456L, "/", "IGXN201803027_00900.jpg");
        System.out.println(exists);
    }

    @Test
    public void mkdir() {
        fileService.mkdir(4L,"/","/kkk");
    }

    @Test
    public void delete() {
        boolean bool = fileService.delete(4L,"/","/kkk");
        System.out.println(bool);
    }
}