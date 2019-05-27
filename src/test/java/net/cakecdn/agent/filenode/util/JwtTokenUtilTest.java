package net.cakecdn.agent.filenode.util;

import net.cakecdn.agent.filenode.CakeAgentFileNodeApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.*;

public class JwtTokenUtilTest extends CakeAgentFileNodeApplicationTests {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    public void getUidFromToken() {
        Map<String, Object> subject = jwtTokenUtil.getSubFromToken("eyJhbGciOiJIUzUxMiJ9." +
                "eyJleHAiOjE1NjE1NDkzNjEsInN1YiI6eyJ1aWQiOjQsImF1dGgiOlsiUk9MRV9VU0VSIl0s" +
                "ImNyZWF0ZWQiOjE1NTg5NTczNjExNTMsImRpc2FibGVkIjpmYWxzZSwiYXZhdGFyIjoiIyIs" +
                "InVzZXJuYW1lIjoib2tleWphIn19.SQQVj2n3Ce1_X2R9YAGbnwrdZMAHp61P0beOrLwje32" +
                "4M66v-0g9WE52brw35uY37d0j72QggTo2NcD63Sw8IQ"
        );
        System.out.println(subject);
    }
}