package com.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private final AuthService authService;//сервис аутентификации
    private final List<ClientHandler> clients;//поле которое содержит список клиентов

    public ChatServer() {//не создаем, а передаем
        this.authService = new SimpleAuthService();//создаем пользователей с генерацией ника, логина и пароля
        this.clients = new ArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true){
                final Socket socket = serverSocket.accept();//для каждого клиента будет создан свой сокет
                new ClientHandler(socket, this);//передаем классу клиент хендер созданный сокет и этот конструктор
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}