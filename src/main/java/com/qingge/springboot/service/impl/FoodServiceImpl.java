package com.qingge.springboot.service.impl;

import com.qingge.springboot.entity.Food;
import com.qingge.springboot.mapper.FoodMapper;
import com.qingge.springboot.service.IFoodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyc
 * @since 2022-05-21
 */
@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements IFoodService {

}
