package com.lzy.bishe.modules.repair.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzy.bishe.modules.repair.mapper.RepairDao;
import com.lzy.bishe.modules.repair.model.entry.Repair;
import com.lzy.bishe.util.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OkRepairServer {

    @Autowired
    private RepairDao repairDao;

    public ResultDTO doSelectNoRepair(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<Repair> repairs = repairDao.doSelectNoRepair();
        PageInfo pageInfo = new PageInfo(repairs);
        return ResultDTO.successOf("获取成功",pageInfo);
    }

    public ResultDTO doAcceptRepair(Repair repair, Integer repairId) {
        repairDao.doAcceptRepair(repair);
        return ResultDTO.successOf("接单成功",repair);
    }

    public ResultDTO doSelectAcceptRepair(Integer okRepairUserId, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<Repair> repairs = repairDao.doSelectAcceptRepair(okRepairUserId);
        PageInfo pageInfo = new PageInfo(repairs);
        return ResultDTO.successOf("获取成功",pageInfo);
    }
}