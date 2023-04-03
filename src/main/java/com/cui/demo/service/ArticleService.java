package com.cui.demo.service;


import com.cui.demo.pojo.entity.Article;

import java.util.List;

public interface ArticleService {
    int insert(List<Article> article);
    List<Article> selectData(int offset, int limit);
}
