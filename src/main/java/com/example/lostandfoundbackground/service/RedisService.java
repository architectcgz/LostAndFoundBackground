package com.example.lostandfoundbackground.service;

public interface RedisService {
    public boolean saveToken();
    public boolean getToken();
}
