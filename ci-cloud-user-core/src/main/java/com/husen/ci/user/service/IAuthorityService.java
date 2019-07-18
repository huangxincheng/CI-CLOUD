package com.husen.ci.user.service;

import com.husen.ci.user.entity.CustomerDTO;
import com.husen.ci.user.entity.MenuDTO;
import com.husen.ci.user.entity.RoleDTO;

/***
 @Author:MrHuang
 @Date: 2019/7/17 17:33
 @DESC: TODO
 @VERSION: 1.0
 ***/
public interface IAuthorityService {

    CustomerDTO addCustomer(CustomerDTO customerDTO);

    void updateCustomerInfo(CustomerDTO customerDTO);

    void updateCustomerRole(CustomerDTO customerDTO);

    CustomerDTO findCustomeById(String id);

    RoleDTO addRole(RoleDTO roleDTO);

    void updateRoleInfo(RoleDTO roleDTO);

    void updateRoleMenu(RoleDTO roleDTO);

    RoleDTO findRoleById(String id);

    MenuDTO addMenu(MenuDTO menuDTO);

    void updateMenuInfo(MenuDTO menuDTO);

    MenuDTO findMenuById(String id);
}
