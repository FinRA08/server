package com.example.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    private Socket socket;//клиенту нужен сокет для подключения
    private DataInputStream in;//переменная для получения текста
    private DataOutputStream out;//переменная для отправления текса


    private Controller controller;//передаем класс Контроллер в ЭхоКлиент

    public ChatClient(Controller controller) {//передаем его в конструктор
        this.controller = controller;
        openConnection();
    }

    public void openConnection(){
        try {
            socket = new Socket("localhost",8189);// инициализируем сокет и указываем порт EchoServera
            in = new DataInputStream(socket.getInputStream());//переменная принимающая через сокет
            out = new DataOutputStream(socket.getOutputStream());//переменная отправляющая через сокет
            new Thread(()-> {//отдельный поток направления сообщений
                    try {
                        while (true){
                            final String msgAuth = in.readUTF();
                            if (msgAuth.startsWith("/authok")){
                                final String[] split = msgAuth.split(" ");
                                final String nick = split[1];
                                controller.addMessage("Успешная авторизация под ником " + nick);
                                controller.setAuth(true);
                                break;
                            }
                        }
                        while (true) {
                            final String message = in.readUTF();
                            if ("/end".equals(message)) {
                                controller.setAuth(false);
                                break;
                            }
                            controller.addMessage(message);
                        }
                        } catch(IOException e){
                            e.printStackTrace();
                        }finally{
                            closeConnection();
                        }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null){
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
