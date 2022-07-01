package com.qingge.springboot.common;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.Food;
import com.qingge.springboot.service.IFoodService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/food1")
public class FoodController_backup {

    @Resource
    private IFoodService foodService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Food food) {
        return Result.success(foodService.saveOrUpdate(food));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(foodService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(foodService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(foodService.list());
    }

    @GetMapping("/{id}")

    public Result findOne(@PathVariable Integer id) {
        return Result.success(foodService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Food> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(foodService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

