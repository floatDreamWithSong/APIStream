package com.daydreamer.apistream.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daydreamer.apistream.entity.ApiEndpointEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApiEndpointMapper extends BaseMapper<ApiEndpointEntity> {
    @Select("SELECT * FROM api_endpoints WHERE module_id = #{moduleId}")
    List<ApiEndpointEntity> selectByModuleId(@Param("moduleId") String moduleId);
}
