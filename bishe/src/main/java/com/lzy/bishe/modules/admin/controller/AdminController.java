package com.lzy.bishe.modules.admin.controller;

import com.lzy.bishe.annotation.UserLoginToken;
import com.lzy.bishe.modules.admin.service.AdminService;
import com.lzy.bishe.modules.tie.service.TieService;
import com.lzy.bishe.util.ResultDTO;
import com.lzy.bishe.modules.user.model.dto.requestDTO.UserLoginDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lizhongyi
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@Slf4j
public class AdminController {

    @Autowired
    private TieService tieService;

    @Autowired
    private AdminService adminService;

    @CrossOrigin @UserLoginToken
    @ApiOperation(value = "查询所有帖子", notes = "查询所有帖子")
    @GetMapping("/query")
    public ResultDTO deSelectAllTie2(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(name = "size", defaultValue = "5") Integer size){
        ResultDTO resultDTO = tieService.selectAllTie(page, size);
        return resultDTO;
    }

    @PostMapping("/login")
    @CrossOrigin
    @ApiOperation(value = "管理员登录接口", notes = "管理员登录接口")
    public ResultDTO adminLogin(@RequestBody UserLoginDTO userLoginInfo){
        ResultDTO resultDTO = adminService.adminLogin(userLoginInfo);
        return resultDTO;
    }


    @GetMapping("/allUser")
    @UserLoginToken @CrossOrigin
    @ApiOperation(value = "获取所有用户信息", notes = "获取所有用户信息")
    public ResultDTO getAllUserInfo(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                    @RequestParam(name = "size", defaultValue = "5") Integer size){
        ResultDTO allUserInfo = adminService.getAllUserInfo(page,size);
        return allUserInfo;
    }

    @GetMapping("/update")
    @UserLoginToken @CrossOrigin
    @ApiOperation(value = "更改用户状态", notes = "更改用户状态")
    public ResultDTO updateUserStatus(@RequestParam(name = "id") Integer id,
                                      @RequestParam(name = "status") Integer status){
        ResultDTO allUserInfo = adminService.updateUserStatus(id,status);
        return allUserInfo;
    }

    @GetMapping("/deleteUser/{id}")
    @UserLoginToken @CrossOrigin
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public ResultDTO deleteUser(@PathVariable("id") Integer id){
        ResultDTO allUserInfo = adminService.deleteUser(id);
        return allUserInfo;
    }

    @GetMapping("/deleteTie/{tieId}")
    @UserLoginToken @CrossOrigin
    @ApiOperation(value = "删除帖子与评论", notes = "删除帖子与评论")
    public ResultDTO deleteTie(@PathVariable("tieId") Integer tieId, HttpServletRequest httpServletRequest){
        ResultDTO allUserInfo = adminService.deleteTie(tieId,httpServletRequest);
        return allUserInfo;
    }

    @GetMapping("/deleteComment/{id}")
    @UserLoginToken @CrossOrigin
    @ApiOperation(value = "删除评论", notes = "删除评论")
    public ResultDTO deleteComment(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest){
        ResultDTO allUserInfo = adminService.deleteComment(id,httpServletRequest);
        return allUserInfo;
    }


}
