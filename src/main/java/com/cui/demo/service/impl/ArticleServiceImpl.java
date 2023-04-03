package com.cui.demo.service.impl;

import com.cui.demo.mapper.ArticleMapper;
import com.cui.demo.pojo.entity.Article;
import com.cui.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public int insert(List<Article> article) {
        return articleMapper.insertData(article);
    }

    @Override
    public List<Article> selectData(int offset, int limit) {
        return articleMapper.selectData(offset, limit);
    }
}
