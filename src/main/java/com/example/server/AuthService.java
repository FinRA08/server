package com.example.server;

public interface AuthService {
    String getNicByLoginAndPassword(String login, String password);//перелаем логин и пароль
}
