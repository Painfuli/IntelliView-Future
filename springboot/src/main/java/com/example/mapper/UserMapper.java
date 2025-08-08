package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    int insert(User user);

    void updateById(User user);

    void deleteById(Integer id);

    @Select("select * from `user` where id = #{id}")
    User selectById(Integer id);

    @Select("select * from `user` where username = #{username}")
    User selectByUsername(String username);

    List<User> selectAll(User user);

}
