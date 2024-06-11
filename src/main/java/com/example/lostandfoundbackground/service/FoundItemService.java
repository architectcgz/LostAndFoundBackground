package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.FoundItemDTO;
import com.example.lostandfoundbackground.dto.Result;

/**
 * @author archi
 */
public interface FoundItemService {

    Result add(FoundItemDTO foundItemDTO);

    Result update(FoundItemDTO foundItemDTO);

    Result searchByTitle(String title);

    Result delete(Long id);

    Result previewList(int pageNum, int pageSize);

    Result getDetailById(Long id);

    Result claimedList(int pageNum, int pageSize);
}
