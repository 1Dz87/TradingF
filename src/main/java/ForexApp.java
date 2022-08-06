import data.DataSource;
import model.CurrencyPair;
import model.Symbol;
import model.history.History;
import model.history.Period;

import java.util.Collection;
import java.util.List;

public class ForexApp {

    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        //List<CurrencyPair> pair = dataSource.getPairById(14);
        Symbol symbol = new Symbol();
        symbol.setSymbol("AUD/CHF");
        //List<CurrencyPair> pairs = dataSource.getPairBySymbol(symbol);
   /*     List<Symbol> symbols = dataSource.getSymbolsList();
        for (Symbol s : symbols) {
            System.out.println(s);
        }*/
        Collection<History> histories = dataSource.getHistory(symbol, 5, Period.MINUTE);
        System.out.println(histories);
    }
}
