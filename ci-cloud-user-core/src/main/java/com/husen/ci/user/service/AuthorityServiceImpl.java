package com.husen.ci.user.service;

import com.husen.ci.user.dao.CustomerDao;
import com.husen.ci.user.dao.MenuDao;
import com.husen.ci.user.dao.RoleDao;
import com.husen.ci.user.entity.CustomerDTO;
import com.husen.ci.user.entity.MenuDTO;
import com.husen.ci.user.entity.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 @Author:MrHuang
 @Date: 2019/7/17 17:35
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Service
public class AuthorityServiceImpl implements IAuthorityService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Override
    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        return customerDao.insert(customerDTO);
    }

    @Override
    public void updateCustomerInfo(CustomerDTO customerDTO) {
        customerDao.updateById(customerDTO);
    }

    @Override
    public void updateCustomerRole(CustomerDTO customerDTO) {
        customerDao.updateById(customerDTO);
    }

    @Override
    public CustomerDTO findCustomeById(String id) {
        return customerDao.findById(id);
    }

    @Override
    public RoleDTO addRole(RoleDTO roleDTO) {
        return roleDao.insert(roleDTO);
    }

    @Override
    public void updateRoleInfo(RoleDTO roleDTO) {
        roleDao.updateById(roleDTO);
    }

    @Override
    public void updateRoleMenu(RoleDTO roleDTO) {
        roleDao.updateById(roleDTO);
    }

    @Override
    public RoleDTO findRoleById(String id) {
       return roleDao.findById(id);
    }

    @Override
    public MenuDTO addMenu(MenuDTO menuDTO) {
        return menuDao.insert(menuDTO);
    }

    @Override
    public void updateMenuInfo(MenuDTO menuDTO) {
        menuDao.updateById(menuDTO);
    }

    @Override
    public MenuDTO findMenuById(String id) {
        return menuDao.findById(id);
    }
}
