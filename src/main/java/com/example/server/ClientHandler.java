package com.example.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Socket socket;
    private final ChatServer server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String nick;

    public ClientHandler(Socket socket, ChatServer server) {
        try {
            this.nick = " ";
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    authenticate();//сначала метод аутентификация клиента
                    readMassages();//метод в котором мы будем читать сообщение от клиента
                }finally {
                    closeConnection();
                }
            }).start();




        } catch (IOException e) {
            throw new RuntimeException(e);//бросаем исключение, что бы исключить ошибку не заполнения какого то поля
        }
    }

    private void closeConnection() {
        try {
            if (in != null){
                in.close();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            if (socket != null){
                server.unsubscribe(this);
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void authenticate() {
        while (true){
            try {
                final String str = in.readUTF();//"/auth" login0 pass0
                if (str.startsWith("/auth")){
                    final String[] split = str.split(" ");
                    final String login = split[1];
                    final String password = split[2];
                    final String nick = server.getAuthService().getNicByLoginAndPassword(login, password);
                    if (nick != null){
                        if (server.isNickBusy(nick)){
                            System.out.println("Пользователь уже авторизован");
                            continue;
                        }
                        sendMessage("/authok" + nick);//метод уведомляет клиента что он авторизировался
                        this.nick = nick;
                        server.subscribe(this);
                    }else {
                        sendMessage("Неверный логин и пароль");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String message) {
        try {
            System.out.println("SERVER: Send message to " + nick);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMassages() {
        try {
            while (true){
                final String msg = in.readUTF();
                if ("/end".equals(msg)){
                    break;
                }
                sendMessage(nick + ": " + msg);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }
}
