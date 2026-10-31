import java.util.Objects;

public final class Balance {
    private final long amountInKopecks;
    private final Currency currency;

    public Balance(long amountInKopecks, Currency currency) {
        if (currency == null) throw new IllegalArgumentException("Валюта не может быть null");
        if (amountInKopecks < 0) throw new IllegalArgumentException("Сумма не может быть отрицательной");
        this.amountInKopecks = amountInKopecks;
        this.currency = currency;
    }

    public static Balance zero(Currency currency) { return new Balance(0, currency); }

    public long getAmountInKopecks() { return amountInKopecks; }
    public Currency getCurrency() { return currency; }

    public Balance add(long kopecks) {
        if (kopecks < 0) throw new IllegalArgumentException("Нельзя прибавить отрицательную сумму");
        if (kopecks == 0) return this;
        if (amountInKopecks > Long.MAX_VALUE - kopecks) throw new ArithmeticException("Переполнение");
        return new Balance(amountInKopecks + kopecks, currency);
    }

    public Balance subtract(long kopecks) {
        if (kopecks < 0) throw new IllegalArgumentException("Нельзя вычесть отрицательную сумму");
        if (kopecks > amountInKopecks) throw new IllegalArgumentException("Недостаточно средств");
        if (kopecks == 0) return this;
        return new Balance(amountInKopecks - kopecks, currency);
    }

    public Balance convertTo(Currency target) {
        if (target == null) throw new IllegalArgumentException("Целевая валюта не может быть null");
        if (target == this.currency) return this;
        long rubKopecks = amountInKopecks * currency.getPriceInRublesKopecks();
        long converted = rubKopecks / target.getPriceInRublesKopecks();
        return new Balance(converted, target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance = (Balance) o;
        return amountInKopecks == balance.amountInKopecks && currency == balance.currency;
    }

    @Override
    public int hashCode() { return Objects.hash(amountInKopecks, currency); }

    @Override
    public String toString() {
        return String.format("%d.%02d %s",
                amountInKopecks / 100, amountInKopecks % 100, currency.getSymbol());
    }
}