package com.example.server;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {

    private final List<UserData> users;//поле для хранение списка пользователей

    public SimpleAuthService() {//в этом конструкторе создаем пользователей
        users = new ArrayList<>();//инициализация листа, иначе в него ничего не положить
        for (int i = 0; i < 5; i++){
            users.add(new UserData("login" + i, "pass" + i, "nick" + i));
        }
    }

    @Override
    public String getNicByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)){
                return user.nick;// если логин и пароль соответствуют клиенту возвращаем ник
            }
        }
        return null;//если ничего не нашли ничего не возвращаем
    }


    private static class UserData{//класс хранит логи, пароль и ник
        private final String login;
        private final String password;
        private final String nick;

        public UserData(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }
}
