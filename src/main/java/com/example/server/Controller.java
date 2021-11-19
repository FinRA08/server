package com.example.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Controller {
    @FXML
    private HBox messageBox;
    @FXML
    private HBox loginBox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginField;
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

    public void btnAuthClick(ActionEvent actionEvent) {
        client.sendMessage("/auth " + loginField.getText() + " " + passwordField.getText());

    }

    public void setAuth(boolean success){
        loginBox.setVisible(!success);
        messageBox.setVisible(success);
        textArea.setVisible(success);
    }
}