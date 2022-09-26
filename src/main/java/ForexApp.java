import at.favre.lib.crypto.bcrypt.BCrypt;
import data.DataSource;
import model.Currency;
import model.CurrencyPair;
import model.Symbol;
import model.history.History;
import model.history.Period;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ForexApp {

    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        //String hash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        //List<Symbol> symbols = dataSource.getSymbolsList();
    }
}
