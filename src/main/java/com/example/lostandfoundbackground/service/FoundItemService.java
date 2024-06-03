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
}
