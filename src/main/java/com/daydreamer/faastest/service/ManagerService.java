package com.daydreamer.faastest.service;


import com.daydreamer.faastest.entity.dto.manage.ManageDeleteServiceEntity;
import com.daydreamer.faastest.entity.dto.manage.ManageGetServiceEntity;
import com.daydreamer.faastest.entity.dto.manage.ManagePostServiceEntity;
import com.daydreamer.faastest.entity.dto.manage.ManagePutServiceEntity;

// TODO : 实现平台的增删查改API函数
public class ManagerService {
    // TODO: 查询API
    public ManageGetServiceEntity getService() {
        return new ManageGetServiceEntity();
    }
    // TODO: 增加API
    public ManagePostServiceEntity postService(){
        return new ManagePostServiceEntity();
    }
    // TODO: 删除API
    public ManageDeleteServiceEntity deleteService(){
        return new ManageDeleteServiceEntity();
    }
    //TODO: 更改API
    public ManagePutServiceEntity putService(){
        return new ManagePutServiceEntity();
    }
}
