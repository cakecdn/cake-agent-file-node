package net.cakecdn.agent.filenode.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Okeyja
 * @version 2019/05/22 022 20:22
 */
@RestController
public class FileController {

    @RequestMapping("/test")
    public String test() {
        return "\"OK\"";
    }
}
