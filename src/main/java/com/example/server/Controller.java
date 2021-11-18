package com.example.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField textField;

    @FXML
    private TextArea textArea;

    private ChatClient client;//ссылка на Эхоклиента

    public Controller(){
        client = new ChatClient(this);
    }

    public void btnSendClick(ActionEvent event) {
        final String message = textField.getText().trim();
        if (message.isEmpty()){
            return;
        }
        client.sendMessage(message);//отправляем сообщение метод в клиенте
        textField.clear();//очищаем текстовое поле
        textField.requestFocus();//ставим фокус на поле ввода текста
    }

    public void addMessage(String message) {
        textArea.appendText(message + "\n");
    }
}