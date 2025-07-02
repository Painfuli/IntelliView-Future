package com.example.mapper;

import com.example.entity.Position;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PositionMapper {

    int insert(Position position);

    void updateById(Position position);

    void deleteById(Integer id);

    @Select("select position.*, " +
            "employ.name as employName, employ.avatar as employAvatar, employ.city as employCity, " +
            "employ.address as employAddress, employ.scale as employScale, employ.stage as employStage " +
            "from position " +
            "left join employ on position.employ_id = employ.id " +
            "where position.id = #{id}")
    Position selectById(Integer id);

    List<Position> selectAll(Position position);

    @Select("select * from position where employ_id = #{id}")
    List<Position> selectByEmployId(Integer id);
}
