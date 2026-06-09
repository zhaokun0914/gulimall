package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {

    @Autowired
    BrandService service;

    @Test
    public void contextLoads() {

//        BrandEntity brand = new BrandEntity();
//        brand.setName("华为");
//        service.save(brand);

        BrandEntity byId = service.getById(15);
        System.out.println(byId);

        byId.setDescript("华为为");
        service.updateById(byId);

    }

}
