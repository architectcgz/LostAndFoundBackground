package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityUserDetails;
import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.FoundItemDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.FoundItem;
import com.example.lostandfoundbackground.entity.LostItem;
import com.example.lostandfoundbackground.entity.User;
import com.example.lostandfoundbackground.mapper.FoundItemMapper;
import com.example.lostandfoundbackground.service.FoundItemService;
import com.example.lostandfoundbackground.utils.JsonUtils;
import com.example.lostandfoundbackground.utils.RedisUtils;
import com.example.lostandfoundbackground.utils.SecurityContextUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.lostandfoundbackground.constants.RedisConstants.FOUND_ITEM_KEY;
import static com.example.lostandfoundbackground.constants.RedisConstants.LOST_ITEM_KEY;

/**
 * @author archi
 */
@Service
public class FoundItemServiceImpl implements FoundItemService {
    @Resource
    private FoundItemMapper foundItemMapper;
    @Override
    public Result add(FoundItemDTO foundItemDTO) {
        FoundItem foundItem = BeanUtil.copyProperties(foundItemDTO,FoundItem.class);
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        if(userDetails==null){
            return Result.error(HttpStatus.UNAUTHORIZED,"请先登录再进行操作!");
        }
        User user = userDetails.getUser();
        foundItem.setClaimed(0);
        foundItem.setCreateUser(user.getId());
        foundItemMapper.add(foundItem);
        //TODO 使用数据同步技术,先写入mysql，然后再同步到redis
        //canal + rocketmq
        //将新添加的信息保存到redis中，3天后过期
        //RedisUtils.storeBeanAsJson(foundItem,LOST_ITEM_KEY+foundItem.getId(),REDIS_THREE_DAYS_EXPIRATION);
        return Result.ok("招领信息发布成功!");
    }

    @Override
    public Result update(FoundItemDTO foundItemDTO) {
        FoundItem foundItem = BeanUtil.copyProperties(foundItemDTO,FoundItem.class);
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        if(userDetails==null){
            return Result.error(HttpStatus.UNAUTHORIZED,"请先登录再进行操作!");
        }
        User user = userDetails.getUser();
        foundItem.setCreateUser(user.getId());
        foundItemMapper.update(foundItem);
        //RedisUtils.storeBeanAsJson(foundItem,LOST_ITEM_KEY+foundItem.getId(),REDIS_THREE_DAYS_EXPIRATION);
        return Result.ok();

    }

    @Override
    public Result searchByTitle(String title) {
        List<FoundItem> foundItemList = foundItemMapper.searchByTitle(title);
        return Result.ok(foundItemList,(long)foundItemList.size());
    }

    @Override
    public Result delete(Long id) {
        foundItemMapper.deleteById(id);
        return Result.ok();
    }

    @Override
    public Result previewList(int pageNum, int pageSize) {
        try {
            PageHelper.startPage(pageNum,pageSize);
            Page<FoundItem> page = (Page<FoundItem>) foundItemMapper.previewList();
            return Result.ok(page.getResult(),page.getTotal());
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Result getDetailById(Long id) {
        FoundItem foundItem;
        if(RedisUtils.hasKey(LOST_ITEM_KEY+id)){
            String jsonStr = RedisUtils.get(FOUND_ITEM_KEY+id);
            foundItem = JsonUtils.jsonStrToJavaBean(jsonStr,FoundItem.class);
        }else {
            foundItem = foundItemMapper.getDetailById(id);
            //将结果放到redis中,3天
            //RedisUtils.storeBeanAsJson(lostItem,LOST_ITEM_KEY+id,REDIS_THREE_DAYS_EXPIRATION);
        }
        if(foundItem==null){
            return Result.error(1,"没有找到该物品的详情信息");
        }
        return Result.ok(foundItem);
    }

    @Override
    public Result claimedList(int pageNum, int pageSize) {
        try {
            PageHelper.startPage(pageNum,pageSize);
            Page<FoundItem> page = (Page<FoundItem>) foundItemMapper.cliamedList();
            return Result.ok(page.getResult(),page.getTotal());
        }catch (Exception e){
            throw e;
        }
    }
}
