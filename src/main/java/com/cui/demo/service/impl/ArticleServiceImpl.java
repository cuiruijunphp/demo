package com.cui.demo.service.impl;

import com.cui.demo.mapper.ArticleMapper;
import com.cui.demo.mapper.UserEsMapper;
import com.cui.demo.pojo.entity.Article;
import com.cui.demo.pojo.entity.UserEs;
import com.cui.demo.service.ArticleService;
import com.cui.demo.service.UserEsService;
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
}
