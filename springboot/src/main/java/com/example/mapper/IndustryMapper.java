package com.example.mapper;

import com.example.entity.Industry;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IndustryMapper {

    int insert(Industry industry);

    void updateById(Industry industry);

    void deleteById(Integer id);

    @Select("select * from `industry` where id = #{id}")
    Industry selectById(Integer id);

    List<Industry> selectAll(Industry industry);

}
