package es.albavm.tfg.trifly.Model;

public enum BudgetCurrency {
    EUR("€"),
    GBP("£"),
    JPY("¥");

    private final String symbol;

    BudgetCurrency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
