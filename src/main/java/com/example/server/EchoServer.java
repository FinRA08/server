package com.example.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    public static void main(String[] args) {
        Socket socket = null;//класс для отправки информации куда-то
        try (ServerSocket serverSocket = new ServerSocket(8189)){
            System.out.println("Ожидаем подключения клиента ....");
            socket = serverSocket.accept();//вызов этого метода остановит работу и будет ждать подключения клиента, как только клиент подключится работа сокета возобновится
            System.out.println("Клиент подключился");
            final DataInputStream in = new DataInputStream(socket.getInputStream());//входящее сообщение через socket
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());//метод, который будет отправлять сообщение в socket
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true){
                            String message = sc.nextLine();
                            out.writeUTF("Сообщение с сервера: " + message);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
            while (true){//бесконечный цикл на прием сообщения от клиента
                final String message = in.readUTF();//получаем и сохраняем входящее сообщение в message
                if (message.startsWith("/end")){//если сообщение начинается с /end, то цикл прерывается и сервер отключается
                    out.writeUTF("/end");
                    break;
                }
                out.writeUTF("Эхо: " + message);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Scanner sc = new Scanner(System.in);
}
