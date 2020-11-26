package com.lzy.springbootjwtcaptcha.modules.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.lzy.springbootjwtcaptcha.modules.user.model.dto.responseDTO.UserInfoResponseDTO;
import com.lzy.springbootjwtcaptcha.modules.user.model.entity.RedisBlackToken;
import com.lzy.springbootjwtcaptcha.modules.user.model.entity.User;
import com.lzy.springbootjwtcaptcha.modules.base.model.entity.ResultDTO;
import com.lzy.springbootjwtcaptcha.modules.user.mapper.UserMapper;
import com.lzy.springbootjwtcaptcha.util.DateUtil;
import com.lzy.springbootjwtcaptcha.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * @author lizhongyi
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CheckService checkService;

    @Autowired
    private RedisUtil redisUtil;

    public User findByUsername(String username){
        return userMapper.findByUsernameToToken(username);
    }

    public User findUserById(String userId) {
        return userMapper.findUserById(userId);
    }

    public ResultDTO getUserMessage(HttpServletRequest httpServletRequest) {
        JSONObject jsonObject=new JSONObject();
        String token = httpServletRequest.getHeader("token");
        String username = JWT.decode(token).getAudience().get(1);
        String power = JWT.decode(token).getAudience().get(2);
        jsonObject.put("用户名：",username);
        jsonObject.put("当前权限：",power);
        return ResultDTO.successOf("用户获取成功！",jsonObject);
    }

    public ResultDTO getAllUserByAdmin(HttpServletRequest httpServletRequest) {
        if (checkService.checkPowerByAdmin(httpServletRequest)){
            List<UserInfoResponseDTO> users = userMapper.findUser();
            return ResultDTO.successOf("所有用户获取成功",users);
        }else{
            return ResultDTO.errorOf(500,"权限不足");
        }
    }

    public ResultDTO logout(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        RedisBlackToken blackToken = new RedisBlackToken();
        blackToken.setId(JWT.decode(token).getAudience().get(0));
        blackToken.setUsername(JWT.decode(token).getAudience().get(1));
        blackToken.setPower(JWT.decode(token).getAudience().get(2));
        blackToken.setTime(DateUtil.getNowDate());
        redisUtil.set(token,blackToken);
        log.info("用户：" + JWT.decode(token).getAudience().get(1) + "已登出");
        return ResultDTO.successOf("登出成功");
    }

}
