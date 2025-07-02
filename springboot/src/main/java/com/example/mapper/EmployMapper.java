package com.example.mapper;

import com.example.entity.Employ;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployMapper {

    int insert(Employ employ);

    void updateById(Employ employ);

    void deleteById(Integer id);

    @Select("select * from `employ` where id = #{id}")
    Employ selectById(Integer id);

    @Select("select * from `employ` where username = #{username}")
    Employ selectByUsername(String username);

    List<Employ> selectAll(Employ employ);

}
