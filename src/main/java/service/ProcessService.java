package service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import data.DataSource;
import exceptions.WorkFlowException;
import model.Currency;
import model.User;
import model.Wallet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class ProcessService {

    private IOService ioService;

    private Session session;

    private DataSource dataSource;

    public ProcessService() {
        this.ioService = new IOService();
        this.session = new Session();
        this.dataSource = new DataSource();
    }

    public User registrate(String login, String password, String repeatPassword) throws IOException {
        // Проверка переданных параметров на null. START
        String log = Optional.ofNullable(login)
                .orElseThrow(() -> new WorkFlowException("Не заполнен логин"));
        String pass = Optional.ofNullable(password)
                .orElseThrow(() -> new WorkFlowException("Не заполнен password"));
        String repPass = Optional.ofNullable(repeatPassword)
                .orElseThrow(() -> new WorkFlowException("Не заполнено обязательное поле"));
        // Проверка переданных параметров на null. END
        // Проверка паролей на совпадение
        if (!pass.equals(repPass)) {
            throw new WorkFlowException("Пароли не совпадают");
        }
        // Проверка пользователя на существование
        if (ioService.checkUserExists(login)) {
            throw new WorkFlowException("Пользватель " + login + " уже существует");
        }

        // Шифрование пароля
        String bcryptHashString = BCrypt
                .withDefaults()
                .hashToString(12, pass.toCharArray());
        // Генерация нового уникального идентификатора объекта User
        UUID uuid = UUID.randomUUID();
        // Создание объекта User
        User user = new User(uuid, log, bcryptHashString);
        // Сохранение созданного объекта в файл
        ioService.save(user);
        return user;
    }

    public User login(String login, String password) {
        String pass = Optional.ofNullable(password)
                .orElseThrow(() -> new WorkFlowException("Не заполнен password"));
        // Проверяем логин на != null
        User u = Optional.ofNullable(login)
                // Пытаемся найти файл с именем login.json и получаем пользователя из файла,
                // если такой файл найден
                .map(log -> ioService.getUserFromFile(log))
                // Проверяем совпадение введенного пароля с тем, который храним в файле
                .filter(user -> BCrypt
                        .verifyer()
                        .verify(pass.toCharArray(), user.getPasswordHash())
                        .verified)
                // иначе бросаем ошибку при любом неудовлетворительном исходе логики выше
                .orElseThrow(() -> new WorkFlowException("Не верный логин или пароль"));
        session.setCurrentUser(u);
        return u;
    }

    public Wallet addWallet(String currency) {
        Wallet wallet = getCurrency(currency)
                .map(cur -> {
                    UUID walletId = UUID.randomUUID();
                    return Optional.ofNullable(this.session.getCurrentUser())
                            .map(currentUser -> new Wallet(walletId, cur, 0.0f, currentUser))
                            .orElseThrow(() -> new WorkFlowException("Не удалось создать кошелек"));
                })
                .orElseThrow(() -> new WorkFlowException("Не удалось создать кошелек"));
        session.getCurrentUser().addWallet(wallet);
        ioService.save(session.getCurrentUser());
        return wallet;
    }

    public Float addAmount(String currency, Float amount) {
        return getCurrency(currency)
                .map(cur -> session.getCurrentUser()
                        .getWalletList()
                        .stream()
                        .filter(wallet -> wallet.getCurrency().equals(cur))
                        .findFirst()
                        .orElse(null))
                .map(wallet -> wallet.addAmount(getAmount(amount)))
                .orElseThrow(() -> new WorkFlowException("Не удалось пополнить кошелек"));
    }

    private Float getAmount(Float amount) {
        return Optional.ofNullable(amount)
                .filter(a -> a > 0.0f)
                .orElseThrow(() -> new WorkFlowException("Не верная сумма пополнения"));
    }

    private Optional<Currency> getCurrency(String currency) {
        return Optional.ofNullable(currency)
                .map(cur -> cur.toUpperCase())
                .filter(checkCurrencyExists()) // USD/EUR
                .map(cur -> new Currency(cur));
    }

    private Predicate<String> checkCurrencyExists() {
        return cur -> dataSource
                .getCurrencyNames()
                .anyMatch(currencyName -> currencyName.equals(cur));
    }
}
