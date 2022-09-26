package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import connection.httpmodel.ProjectObjectMapper;
import exceptions.WorkFlowException;
import model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class IOService {

    private BufferedReader terminal;

    private FileWritterProcessor<User> processor;

    private ObjectMapper objectMapper;

    private UserReader reader;

    public IOService() {
        this.terminal = new BufferedReader(new InputStreamReader(System.in));
        processor = new UserWritter();
        this.objectMapper = new ProjectObjectMapper();
    }

    public String readInput() throws IOException {
        return this.terminal.readLine();
    }

    public void save(User user) {
        try {
            // Сериализация объекта User в строку типа JSON
            String json = objectMapper.writeValueAsString(user);
            // Получаем инструмент записи в файл
            BufferedWriter writer = processor.getWritter(user);
            // Записываем строку представления User в файл
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            throw new WorkFlowException(e.getMessage(), e);
        }
    }

    public User getUserFromFile(String login) {
        try {
            return this.reader.getUserFromFile(login);
        } catch (IOException e) {
            throw new WorkFlowException(e.getMessage(), e);
        }
    }

    public void message(String message) {
        System.out.println(message);
    }

    public boolean checkUserExists(String login) {
        return reader.checkUserExists(login);
    }
}
