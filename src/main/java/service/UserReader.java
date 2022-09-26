package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import connection.httpmodel.ProjectObjectMapper;
import exceptions.WorkFlowException;
import model.User;

import java.io.*;
import java.util.Arrays;

public class UserReader {

    private ObjectMapper objectMapper;

    public UserReader() {
        this.objectMapper = new ProjectObjectMapper();
    }

    public User getUserFromFile(String login) throws IOException {
        File file = new File("classpath:/users/" + login + ".json");
        if (file.exists()) {
            return objectMapper.readValue(file, User.class);
        }
        throw new WorkFlowException("Пользователь " + login + " не найден");
    }

    public boolean checkUserExists(String login) {
        File file = new File("classpath:/users");
        return Arrays.stream(
                // Из списка файлов в каталоге classpath:/users выбираем по названию, совпадающему с переданным login
                file.listFiles((dir, name) -> name.substring(0, name.lastIndexOf(".json")).equals(login)))
                // Берем из них первый попавшийся
                .findFirst()
                // Если такой существует, то возвращаем true
                .isPresent();
    }
}
