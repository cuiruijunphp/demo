package com.cui.demo.mapper;

import com.cui.demo.pojo.entity.Article;
import com.cui.demo.pojo.entity.UserEs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int insertData(List<Article> article);

    List<Article> selectData(int offset, int limit);
}
