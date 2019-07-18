package com.husen.ci;

import com.husen.ci.framework.net.bean.HttpResult;
import com.husen.ci.user.dao.CustomerDao;
import com.husen.ci.user.entity.CustomerDTO;
import com.husen.ci.user.service.IAuthorityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testAdd() {
        CustomerDTO dto = new CustomerDTO().setCname("Husen").setStatus(1);
        dto = authorityService.addCustomer(dto);
        System.out.println(dto);
    }

    @Test
    public void testUpdateInfo() {
        CustomerDTO customerDTO = new CustomerDTO().setCid("5d2ef13f29144334c882a5bd").setCname("Husen2").setStatus(2);
        authorityService.updateCustomerInfo(customerDTO);
    }

    @Test
    public void testUpdateRole() {
        CustomerDTO customerDTO = new CustomerDTO().setCid("5d2ef13f29144334c882a5bd").setBindRoleIds(Arrays.asList("213123123dasda","cacsacw"));
        authorityService.updateCustomerRole(customerDTO);
    }

    @Test
    public void findCustomeById() {
        CustomerDTO customeById = authorityService.findCustomeById("5d2ef13f29144334c882a5bd");
        System.out.println(customeById);
    }

    @Test
    public void customerDao() {
        /**
         * all 必须要全部包含
         */
        List<CustomerDTO> bindRoleIds = customerDao.findByQuery(
                Query.query(Criteria.where("bindRoleIds").all("213123123dasda","cacsacw"))
        );
        System.out.println(bindRoleIds);

        /**
         * in 有一个即可
         */
        List<CustomerDTO> bindRoleIds2 = customerDao.findByQuery(
                Query.query(Criteria.where("bindRoleIds").in("213123123dasda","cacsacw"))
        );
        System.out.println(bindRoleIds2);

        customerDao.insert(new CustomerDTO().setBindResult(
                Arrays.asList(new HttpResult().setCode(200).setContent("OK"),
                        new HttpResult().setCode(404).setContent("404"))
        ));

        List<CustomerDTO> byQuery = customerDao.findByQuery(Query.query(Criteria.where("bindResult.code").is(200)));
        System.out.println(byQuery);
    }
}
