package com.husen.ci;

import com.husen.ci.user.entity.CustomerDTO;
import com.husen.ci.user.service.IAuthorityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/***
 @Author:MrHuang
 @Date: 2019/7/17 17:42
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAuthority {

    @Autowired
    private IAuthorityService authorityService;

    @Test
    public void testAdd() {
        CustomerDTO dto = new CustomerDTO().setCname("Husen").setStatus(1);
        authorityService.addCustomer(dto);
        System.out.println(dto);
    }

    @Test
    public void testUpdateInfo() {
        CustomerDTO customerDTO = new CustomerDTO().setCid("5d2ef13f29144334c882a5bd").setCname("Husen2").setStatus(2);
        authorityService.updateCustomerInfo(customerDTO);
        System.out.println(customerDTO);
    }

    @Test
    public void testUpdateRole() {
        CustomerDTO customerDTO = new CustomerDTO().setCid("5d2ef13f29144334c882a5bd").setBindRoleIds(Arrays.asList("213123123dasda"));
        boolean b = authorityService.updateCustomerRole(customerDTO);
        System.out.println(b);
    }
}
