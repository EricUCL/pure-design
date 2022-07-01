package com.qingge.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qingge.springboot.common.Result;

import com.qingge.springboot.service.IFoodService;
import com.qingge.springboot.entity.Food;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wyc
 * @since 2022-05-21
 */
@RestController
@RequestMapping("/food")
public class FoodController {

    @Resource
    private IFoodService foodService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Food food) {
        foodService.saveOrUpdate(food);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        foodService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        foodService.removeByIds(ids);
        return Result.success();
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
                           @RequestParam Integer pageSize,
                           @RequestParam String name) {
        QueryWrapper<Food> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("product_name",name);
        queryWrapper.orderByDesc("id");
        return Result.success(foodService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @PostMapping("/forecast")
    public Food forecast(@RequestBody Map<String, String> params) throws InterruptedException {
        String selectProv = params.get("selectProv");
        Food food = new Food();
        if (selectProv.equals("西藏自治区")) {
            food.setQualified("不合格");
        } else food.setQualified("合格");
        Thread.sleep(1000);
        return food;
    }

    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Food> list = reader.readAll(Food.class);
        for (Food food : list) {
            food.setId(null);
        }
        // 方式2：忽略表头的中文，直接读取表的内容
//        List<List<Object>> list = reader.read(1);
//        List<User> users = CollUtil.newArrayList();
//        for (List<Object> row : list) {
//            User user = new User();
//            user.setUsername(row.get(0).toString());
//            user.setPassword(row.get(1).toString());
//            user.setNickname(row.get(2).toString());
//            user.setEmail(row.get(3).toString());
//            user.setPhone(row.get(4).toString());
//            user.setAddress(row.get(5).toString());
//            user.setAvatarUrl(row.get(6).toString());
//            users.add(user);
//        }

        foodService.saveBatch(list);
        return Result.success(true);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Food> list = foodService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("createTime", "创建时间");
//        writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("抽检数据", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
}

