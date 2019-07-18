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
    public boolean addCustomer(CustomerDTO customerDTO) {
        customerDao.insert(customerDTO);
        return true;
    }

    @Override
    public boolean updateCustomerInfo(CustomerDTO customerDTO) {
        customerDao.updateById(customerDTO);
        return true;
    }

    @Override
    public boolean updateCustomerRole(CustomerDTO customerDTO) {
        customerDao.updateById(customerDTO);
        return true;
    }

    @Override
    public CustomerDTO findCustomeById(String id) {
        return customerDao.findById(id);
    }

    @Override
    public boolean addRole(RoleDTO roleDTO) {
        roleDao.insert(roleDTO);
        return true;
    }

    @Override
    public boolean updateRoleInfo(RoleDTO roleDTO) {
        roleDao.updateById(roleDTO);
        return true;
    }

    @Override
    public boolean updateRoleMenu(RoleDTO roleDTO) {
        roleDao.updateById(roleDTO);
        return true;
    }

    @Override
    public RoleDTO findRoleById(String id) {
       return roleDao.findById(id);
    }

    @Override
    public boolean addMenu(MenuDTO menuDTO) {
        menuDao.insert(menuDTO);
        return true;
    }

    @Override
    public boolean updateMenuInfo(MenuDTO menuDTO) {
        menuDao.updateById(menuDTO);
        return true;
    }

    @Override
    public MenuDTO findMenuById(String id) {
        return menuDao.findById(id);
    }
}
