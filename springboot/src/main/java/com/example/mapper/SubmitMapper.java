package com.example.mapper;

import com.example.entity.Submit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SubmitMapper {

    int insert(Submit submit);

    void updateById(Submit submit);

    void deleteById(Integer id);

    @Select("select * from `submit` where id = #{id}")
    Submit selectById(Integer id);

    List<Submit> selectAll(Submit submit);

    @Select("select * from submit where user_id = #{userId} and position_id = #{positionId}")
    List<Submit> selectByUserIdAndPositionId(@Param("userId") Integer userId, @Param("positionId") Integer positionId);
}
