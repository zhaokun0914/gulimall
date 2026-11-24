package com.atguigu.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallThirdPartyApplicationTests {

    @Autowired
    OSS ossClient;

    @Test
    public void uploadTest() {
        ossClient.putObject("gulimall-xka", "test.jpg", Paths.get("/Users/kun/Downloads/鼠标默认值.png").toFile());
        ossClient.shutdown();
        System.out.println("上传完成");
    }

    @Test
    public void contextLoads() {
    }

}
