package com.example.mapper;

import com.example.entity.Advertise;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdvertiseMapper {

    int insert(Advertise advertise);

    void updateById(Advertise advertise);

    void deleteById(Integer id);

    @Select("select * from `advertise` where id = #{id}")
    Advertise selectById(Integer id);

    List<Advertise> selectAll(Advertise advertise);

    @Select("select * from advertise where location = #{location}")
    List<Advertise> selectByLocation(String location);
}
