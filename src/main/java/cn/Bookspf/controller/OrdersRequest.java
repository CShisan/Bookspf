package cn.Bookspf.controller;

import cn.Bookspf.mapper.OrderMapper;
import cn.Bookspf.mapper.UserMapper;
import cn.Bookspf.model.DO.DBOrder;
import cn.Bookspf.model.DTO.Order;
import cn.Bookspf.model.RO.OrderResponse;
import cn.Bookspf.model.RO.Response;
import cn.Bookspf.utils.Operator;
import cn.Bookspf.utils.Validator;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
public class OrdersRequest {
    HttpSession httpSession;
    Validator validator;
    Operator operator;
    UserMapper userMapper;
    OrderMapper orderMapper;

    @Autowired
    public OrdersRequest(HttpSession httpSession, UserMapper userMapper,OrderMapper orderMapper){
        this.httpSession=httpSession;
        this.validator=new Validator(httpSession);
        this.operator=new Operator();
        this.userMapper=userMapper;
        this.orderMapper=orderMapper;
    }

    //获取订单信息列表
    @PostMapping("/getOrderList")
    public Response getOrderList(@RequestBody String request) {
        if(!validator.isLogin()) return new Response(false,"请登录再操作");
        if(validator.isIdentity(userMapper)!=2) return new Response(false,"请登录普通用户帐号");
        OrderResponse orderResponse=new OrderResponse();
        Integer uid= (Integer) httpSession.getAttribute("userToken");
        JSONObject Obj=JSONObject.parseObject(request);
        int index = Obj.getInteger("index");
        if(index==0){
            ArrayList<DBOrder> order = orderMapper.getOrderOfUid(uid);
            ArrayList<Integer> price = orderMapper.getOrderPriceOfUid(uid);
            orderResponse.setOrders(operator.getOrders(order,price));
        }else if(index==1){
            long orderid=Obj.getLong("str");
            ArrayList<DBOrder> order = orderMapper.getOrderOfOrderidAndUid(orderid,uid);
            if (order==null) return new Response(false,"请输入正确订单号");
            ArrayList<Integer> price = orderMapper.getOrderPriceOfOrderid(orderid);
            orderResponse.setOrders(operator.getOrders(order,price));
        }else if(index==2){
            String createtime=Obj.getString("str");
            createtime=createtime.replace("T"," ");
            ArrayList<DBOrder> order = orderMapper.getOrderOfCreatetimeAndUid(createtime,uid);
            if (order==null) return new Response(false,"没有该时间订单记录");
            ArrayList<Integer> price = orderMapper.getOrderPriceOfCreatetime(createtime);
            orderResponse.setOrders(operator.getOrders(order,price));
        }
        return orderResponse;
    }

    //获取订单详情列表
    @PostMapping("/checkOrder")
    public Response checkOrder(@RequestBody Order request) {
        if(!validator.isLogin()) return new Response(false,"请登录再操作");
        if(validator.isIdentity(userMapper)!=2) return new Response(false,"请登录普通用户帐号");
        OrderResponse orderResponse=new OrderResponse();
        orderResponse.setOrdersinfo(orderMapper.getOrderinfoOfOrderid(request.getOrderid()));
        return orderResponse;
    }
}


