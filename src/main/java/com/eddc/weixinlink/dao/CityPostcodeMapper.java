package com.eddc.weixinlink.dao;

import com.eddc.weixinlink.entity.CityPostcode;
import org.apache.ibatis.annotations.Param;

public interface CityPostcodeMapper {
    int insert(CityPostcode record);

    int insertSelective(CityPostcode record);

    CityPostcode selectByCity(@Param("city") String city);
}