package com.yyc.lock_demo.mapper;

import com.yyc.lock_demo.pojo.ShopStock;
import com.yyc.lock_demo.pojo.ShopStockExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopStockMapper {
    int countByExample(ShopStockExample example);

    int deleteByExample(ShopStockExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShopStock record);

    int insertSelective(ShopStock record);

    List<ShopStock> selectByExample(ShopStockExample example);

    ShopStock selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShopStock record, @Param("example") ShopStockExample example);

    int updateByExample(@Param("record") ShopStock record, @Param("example") ShopStockExample example);

    int updateByPrimaryKeySelective(ShopStock record);

    int updateByPrimaryKey(ShopStock record);
}