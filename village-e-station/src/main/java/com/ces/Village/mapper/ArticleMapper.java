package com.ces.Village.mapper;

import com.ces.Village.pojo.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【article(文章表)】的数据库操作Mapper
* @createDate 2023-11-15 12:29:19
* @Entity generator.Article
*/
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据用户id删除文章信息
     * @param userId
     * @return
     */
    @Delete("delete from article where author_id = #{userId}")
    boolean deleteByUserId(Long userId);

    /**
     * 根据用户id查询文章信息
     * @param userId
     * @return
     */
    @Select("select thumbnail_url from article where author_id = #{userId}")
    List<String> getThumbnailUrls(Long userId);
}




