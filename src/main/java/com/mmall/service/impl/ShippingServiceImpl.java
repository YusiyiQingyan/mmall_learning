package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by weiwen on 2020/2/22
 */

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping) {
        //防止横向越权，需要将插入数据中的shipping记录的userId属性设置为当前登录用户的userId
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            //表示新增的收货地址插入数据库成功
            //根据和前端的约定，需要将shippingId返回
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("添加收货地址成功！", result);
        }
        return ServerResponse.createByErrorMessage("添加收货地址失败！");
    }


    public ServerResponse<String> del(Integer userId, Integer shippingId) {

        int rowCount = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("删除收货地址成功！");
        }
        return ServerResponse.createByErrorMessage("删除收货地址失败！");
    }

    public ServerResponse update(Integer userId, Shipping shipping) {
        //防止横向越权，需要将插入数据中的shipping记录的userId属性设置为当前登录用户的userId
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新收货地址成功！");
        }
        return ServerResponse.createByErrorMessage("更新收货地址失败！");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {

        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping==null) {
            return ServerResponse.createByErrorMessage("无法查询到该地址！");
        }
        return ServerResponse.createBySuccess("更新地址成功！", shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}