package com.wonder.dao;

import com.wonder.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Controller;

/**
 * @Author: blank
 * @Date: 2019/12/31
 */

@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME, "(",
            INSERT_FIELDS, ") values (#{name},#{password},#{salt},#{headUrl})"})

    int addUser(User user);

    @Update({"update ",TABLE_NAME," set password=#{password} where id = #{id}"})
    int updatePassword(User user);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id = #{id}"})
    User selectById(int id);

    @Delete({"delete from ",TABLE_NAME," where id = #{id}"})
    void deleteById(int id);

}
