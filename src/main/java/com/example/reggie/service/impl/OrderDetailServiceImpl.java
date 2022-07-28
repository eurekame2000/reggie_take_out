package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.entity.Employee;
import com.example.reggie.entity.OrderDetail;
import com.example.reggie.entity.Orders;
import com.example.reggie.mapper.EmployeeMapper;
import com.example.reggie.mapper.OrderDetailMapper;
import com.example.reggie.mapper.OrderMapper;
import com.example.reggie.service.EmployeeService;
import com.example.reggie.service.OrderDetailService;
import com.example.reggie.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
