package cn.Bookspf.controller;

import cn.Bookspf.mapper.UserMapper;
import cn.Bookspf.model.DTO.Book;
import cn.Bookspf.model.DTO.User;
import cn.Bookspf.model.RO.Response;
import cn.Bookspf.utils.Operator;
import cn.Bookspf.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class AccountRequest {
    HttpSession httpSession;
    Validator validator;
    Operator operator;
    UserMapper userMapper;

    @Autowired
    public AccountRequest(HttpSession httpSession,UserMapper userMapper){
        this.httpSession=httpSession;
        this.validator=new Validator(httpSession);
        this.operator=new Operator();
        this.userMapper=userMapper;
    }

    @PostMapping("/uploadAvatar")
    public Response uploadAvatar(@RequestParam("file") MultipartFile file){
        if(!validator.isLogin()) return new Response(false,"请登录再操作");
        if(validator.isIdentity(userMapper)!=2) return new Response(false,"请登录普通用户帐号");
        if(file==null) return new Response(false,"上传头像失败");
        try{
            Integer uid = (Integer) httpSession.getAttribute("userToken");
            if(!operator.uploadAvatar(file,uid))return new Response(false,"上传头像失败");
        }catch (IOException e){ return new Response(false,"上传头像失败");}
        return new Response(true,"上传成功");
    }

    @PostMapping("/changePassword")
    public Response changePassword(@RequestBody User request){
        if(!validator.isLogin()) return new Response(false,"请登录再操作");
        if(validator.isIdentity(userMapper)!=2) return new Response(false,"请登录普通用户帐号");
        Integer uid = (Integer) httpSession.getAttribute("userToken");
        String newPassword = request.getPassword();
        if(newPassword.equals(userMapper.getPasswordOfUid(uid))) return new Response(true,"新密码与旧密码一致");
        userMapper.updatePassword(uid,newPassword);
        return new Response(true,"修改成功");
    }

    @PostMapping("/changePhone")
    public Response changePhone(@RequestBody User request){
        //if(!validator.isLogin()) return new Response(false,"请登录再操作");
        //if(validator.isIdentity(userMapper)!=2) return new Response(false,"请登录普通用户帐号");
        Integer uid = (Integer) httpSession.getAttribute("userToken");
        uid=5354;
        userMapper.updatePhone(uid,request.getPhone());
        return new Response(true,"修改成功");
    }

    @PostMapping("/changeRealname")
    public Response changeRealname(@RequestBody User request){
        //if(!validator.isLogin()) return new Response(false,"请登录再操作");
        //if(validator.isIdentity(userMapper)!=2) return new Response(false,"请登录普通用户帐号");
        Integer uid = (Integer) httpSession.getAttribute("userToken");
        uid=5354;
        userMapper.updateRealname(uid,request.getRealname());
        return new Response(true,"修改成功");
    }

    @PostMapping("/changeAddress")
    public Response changeAddress(@RequestBody User request){
        //if(!validator.isLogin()) return new Response(false,"请登录再操作");
        //if(validator.isIdentity(userMapper)!=2) return new Response(false,"请登录普通用户帐号");
        Integer uid = (Integer) httpSession.getAttribute("userToken");
        uid=5354;
        userMapper.updateAddress(uid,request.getAddress());
        return new Response(true,"修改成功");
    }
}