package com.example.lostandfoundbackground.mapper;
import com.example.lostandfoundbackground.entity.FoundItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author archi
 */
@Mapper
public interface FoundItemMapper {
    void add(FoundItem foundItem);

    void update(FoundItem foundItem);

    List<FoundItem> searchByTitle(String title);

    void deleteById(Long id);
}
