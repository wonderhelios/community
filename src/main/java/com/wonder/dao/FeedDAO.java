package com.wonder.dao;

import com.wonder.model.Comment;
import com.wonder.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/21
 */
@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";

    String INSERT_FIELDS = " user_id, created_date, data, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,
            " ) values (#{userId},#{createdDate},#{data},#{type})"})
    boolean addFeed(Feed feed);


    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,
            " where id=#{id}"})
    Feed getFeedById(@Param("id")int id);

    List<Feed> selectUserFeeds(@Param("maxId")int maxId,
                               @Param("userIds")List<Integer> userIds,
                               @Param("count")int count);

}