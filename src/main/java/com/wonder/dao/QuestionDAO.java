package com.wonder.dao;

import com.wonder.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: wonder
 * @Date: 2020/1/1
 */
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 添加问题
     * @param question
     * @return
     */
    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,
            " ) values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    /**
     * 根据问题id获取问题
     * @param id
     * @return
     */
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
    Question selectQuestionById(@Param("id")int id);

    /**
     * 更新评论数量
     * @param id
     * @param commentCount
     * @return
     */
    @Update({"update ",TABLE_NAME," set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id")int id,
                           @Param("commentCount")int commentCount);

    /**
     * 选取最新的问题
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Question> selectLatestQuestions(@Param("userId")int userId,
                                         @Param("offset")int offset,
                                         @Param("limit")int limit);
}
