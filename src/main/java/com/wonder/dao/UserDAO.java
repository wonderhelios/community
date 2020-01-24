package com.wonder.dao;

import com.wonder.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Controller;

/**
 * @Author: wonder
 * @Date: 2019/12/31
 */

@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 增加用户
     * @param user
     * @return
     */
    @Insert({"insert into ",TABLE_NAME, "(",
            INSERT_FIELDS, ") values (#{name},#{password},#{salt},#{headUrl})"})

    int addUser(User user);

    /**
     * 更新用户密码
     * @param user
     * @return
     */
    @Update({"update ",TABLE_NAME," set password=#{password} where id = #{id}"})
    int updatePassword(User user);

    /**
     * 根据用户id选取用户实体
     * @param id
     * @return
     */
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id = #{id}"})
    User selectById(int id);

    /**
     * 根据用户名选取用户实体
     * @param name
     * @return
     */
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where name = #{name}"})
    User selectByName(String name);

}
