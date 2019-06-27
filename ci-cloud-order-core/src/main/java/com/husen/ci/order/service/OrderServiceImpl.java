package com.husen.ci.order.service;

import com.husen.ci.order.dao.OrderDao;
import com.husen.ci.order.entity.OrderDTO;
import com.husen.ci.order.pojo.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/***
 @Author:MrHuang
 @Date: 2019/6/4 20:10
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public boolean saveOrder(Order order) {
        return orderDao.saveOrder(Optional.ofNullable(order)
        .map(o -> {
            OrderDTO dto = new OrderDTO();
            BeanUtils.copyProperties(o, dto);
            return dto;
        }).orElse(null));
    }

    @Override
    public Order queryOrder(Long orderNo) {
        return Optional.ofNullable(orderDao.queryOrder(orderNo))
        .map(o -> {
                Order order = new Order();
                BeanUtils.copyProperties(o, order);
                return order;
        }).orElse(null);
    }
}
