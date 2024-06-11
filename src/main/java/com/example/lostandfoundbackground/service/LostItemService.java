package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.LostItemDTO;
import com.example.lostandfoundbackground.dto.Result;

/**
 * @author archi
 */
public interface LostItemService {
    Result add(LostItemDTO lostItemDTO);

    Result update(LostItemDTO lostItemDTO);

    Result searchByTitle(String title);

    Result delete(Long id);

    Result getDetailById(Long id);


    Result previewList(int pageNum, int pageSize);
}
