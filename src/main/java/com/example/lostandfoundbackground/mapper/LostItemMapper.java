package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.entity.LostItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author archi
 */
@Mapper
public interface LostItemMapper {
    void add(LostItem lostItem);

    void update(LostItem lostItem);

    List<LostItem> searchByTitle(String title);

    void deleteById(Long id);

    LostItem getDetailById(Long id);
}
