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

    boolean addCustomer(CustomerDTO customerDTO);

    boolean updateCustomerInfo(CustomerDTO customerDTO);

    boolean updateCustomerRole(CustomerDTO customerDTO);

    CustomerDTO findCustomeById(String id);

    boolean addRole(RoleDTO roleDTO);

    boolean updateRoleInfo(RoleDTO roleDTO);

    boolean updateRoleMenu(RoleDTO roleDTO);

    RoleDTO findRoleById(String id);

    boolean addMenu(MenuDTO menuDTO);

    boolean updateMenuInfo(MenuDTO menuDTO);

    MenuDTO findMenuById(String id);
}
