package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityUserDetails;
import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.LostItemDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.LostItem;
import com.example.lostandfoundbackground.entity.User;
import com.example.lostandfoundbackground.mapper.LostItemMapper;
import com.example.lostandfoundbackground.service.LostItemService;
import com.example.lostandfoundbackground.utils.JsonUtils;
import com.example.lostandfoundbackground.utils.RedisUtils;
import com.example.lostandfoundbackground.utils.SecurityContextUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.lostandfoundbackground.constants.RedisConstants.LOST_ITEM_KEY;

/**
 * @author archi
 */
@Slf4j
@Service
public class LostItemServiceImpl implements LostItemService {
    @Resource
    private LostItemMapper lostItemMapper;
    @Override
    public Result add(LostItemDTO lostItemDTO) {
        log.info("进入此处");
        LostItem lostItem = BeanUtil.copyProperties(lostItemDTO,LostItem.class);

        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        if(userDetails==null){
            return Result.error(HttpStatus.UNAUTHORIZED,"请先登录再进行操作!");
        }

        User user = userDetails.getUser();

        lostItem.setFounded(0);
        lostItem.setCreateUser(user.getId());

        lostItemMapper.add(lostItem);
        //TODO 使用数据同步技术,先写入mysql，然后再同步到redis
        //canal + rocketmq
        //将新添加的信息保存到redis中，3天后过期
        //RedisUtils.storeBeanAsJson(lostItem,LOST_ITEM_KEY+lostItem.getId(),REDIS_THREE_DAYS_EXPIRATION);
        return Result.ok();
    }

    @Override
    public Result update(LostItemDTO lostItemDTO) {
        LostItem lostItem = BeanUtil.copyProperties(lostItemDTO,LostItem.class);
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        if(userDetails==null){
            return Result.error(HttpStatus.UNAUTHORIZED,"请先登录再进行操作!");
        }
        User user = userDetails.getUser();
        lostItem.setCreateUser(user.getId());
        lostItemMapper.update(lostItem);
        //RedisUtils.storeBeanAsJson(lostItem,LOST_ITEM_KEY+lostItem.getId(),REDIS_THREE_DAYS_EXPIRATION);
        return Result.ok();
    }

    @Override
    public Result searchByTitle(String title) {
        List<LostItem> lostItemList = lostItemMapper.searchByTitle(title);
        return Result.ok(lostItemList,(long)lostItemList.size());
    }

    @Override
    public Result delete(Long id) {
         lostItemMapper.deleteById(id);
         return Result.ok();
    }

    @Override
    public Result getDetailById(Long id) {
        LostItem lostItem;
        if(RedisUtils.hasKey(LOST_ITEM_KEY+id)){
            String jsonStr = RedisUtils.get(LOST_ITEM_KEY+id);
            lostItem = JsonUtils.jsonStrToJavaBean(jsonStr,LostItem.class);
        }else {
            lostItem = lostItemMapper.getDetailById(id);
            //将结果放到redis中,3天
            //RedisUtils.storeBeanAsJson(lostItem,LOST_ITEM_KEY+id,REDIS_THREE_DAYS_EXPIRATION);
        }
        if(lostItem==null){
            return Result.error(1,"没找到该物品的详情信息");
        }
        return Result.ok(lostItem);
    }

    @Override
    public Result previewList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<LostItem> page = (Page<LostItem>)lostItemMapper.previewList();
        return Result.ok(page.getResult(), page.getTotal());
    }
}
