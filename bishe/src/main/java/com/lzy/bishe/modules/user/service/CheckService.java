package com.lzy.bishe.modules.user.service;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.lzy.bishe.modules.base.model.entity.ResultDTO;
import com.lzy.bishe.modules.jwt.service.TokenService;
import com.lzy.bishe.modules.user.model.dto.requestDTO.UserLoginDTO;
import com.lzy.bishe.modules.user.model.dto.responseDTO.UserInfoResponseDTO;
import com.lzy.bishe.modules.user.model.entity.User;
import com.lzy.bishe.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lizhongyi
 */
@Slf4j
@Service
public class CheckService {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisUtil redisUtil;

    public ResultDTO checkLogin(UserLoginDTO userLoginInfo) {

        Object code2 = redisUtil.get(userLoginInfo.getCode());
        System.out.println("登录中的验证码："+code2);
        String code = userLoginInfo.getCode();

        JSONObject jsonObject=new JSONObject();
        if (!code.equals(code2)){
            redisUtil.del(code);
            return ResultDTO.errorOf(500,"验证码错误");
        }
        User userForBase=userService.findByUsername(userLoginInfo.getUsername());
        if(userForBase==null){
            redisUtil.del(code);
            return ResultDTO.errorOf(500,"登录失败，用户不存在");
        }else if(!userForBase.getPassword().equals(userLoginInfo.getPassword())){
            redisUtil.del(code);
            return ResultDTO.errorOf(500,"登录失败，密码错误");
        }else {
            redisUtil.del(code);
            String token = tokenService.getToken(userForBase);
            jsonObject.put("token", token);
            UserInfoResponseDTO res = new UserInfoResponseDTO();
            res.setId(userForBase.getId());
            res.setUsername(userForBase.getUsername());
            res.setPower(userForBase.getPower());
            jsonObject.put("user", res);
            log.info("用户：" + userLoginInfo.getUsername() + "登录");
            return ResultDTO.successOf("登录成功",jsonObject);
        }
    }

    public boolean checkPowerByAdmin(HttpServletRequest request){
        String token = request.getHeader("token");
        String power = JWT.decode(token).getAudience().get(2);
        if ("admin".equals(power)){
            return true;
        } else {
            return false;
        }
    }
}
