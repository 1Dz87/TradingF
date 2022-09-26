package service;

import model.User;

import java.io.*;

public class UserWritter implements FileWritterProcessor<User> {

    @Override
    public BufferedWriter getWritter(User obj) throws IOException {
        // Создаём/получаем файл с именем равным User.login
        File file = new File("classpath:/users/" + obj.getLogin() + ".json");
        // Открываем файл для записи
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        return writer;
    }
}
