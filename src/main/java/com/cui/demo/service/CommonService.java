package com.cui.demo.service;

import com.apifan.common.random.constant.Province;
import com.apifan.common.random.source.AreaSource;
import com.cui.demo.mapper.UserEsMapper;
import com.cui.demo.pojo.entity.UserEs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommonService {

    public Province find_province(String find_province){
        for(Province province: Province.values()){
            if (province.getName().equals(find_province)){
                return province;
            }
        }

        return null;
    }
}
