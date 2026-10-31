import java.util.Objects;

public final class BankAccount {
    private final String id;
    private final String owner;
    private Balance balance;
    private final long withdrawalLimitKopecks;
    private boolean blocked;

    public BankAccount(String id, String owner, Balance initialBalance, long withdrawalLimitKopecks) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID не может быть пустым");
        if (owner == null || owner.isBlank()) throw new IllegalArgumentException("Владелец не может быть пустым");
        if (initialBalance == null) throw new IllegalArgumentException("Баланс не может быть null");
        if (withdrawalLimitKopecks < 0) throw new IllegalArgumentException("Лимит не может быть отрицательным");
        this.id = id;
        this.owner = owner;
        this.balance = initialBalance;
        this.withdrawalLimitKopecks = withdrawalLimitKopecks;
    }

    public String getId() { return id; }
    public String getOwner() { return owner; }
    public Balance getBalance() { return balance; }
    public long getWithdrawalLimitKopecks() { return withdrawalLimitKopecks; }
    public boolean isBlocked() { return blocked; }

    public void deposit(long kopecks) {
        ensureNotBlocked();
        if (kopecks <= 0) throw new IllegalArgumentException("Сумма должна быть > 0");
        this.balance = balance.add(kopecks);
    }

    public void withdraw(long kopecks) {
        ensureNotBlocked();
        if (kopecks <= 0) throw new IllegalArgumentException("Сумма должна быть > 0");
        if (kopecks > withdrawalLimitKopecks) throw new IllegalArgumentException("Превышен лимит на вывод");
        this.balance = balance.subtract(kopecks);
    }

    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    public void transferTo(BankAccount recipient, long kopecks) {
        if (recipient == null) throw new IllegalArgumentException("Получатель не может быть null");
        if (recipient == this) throw new IllegalArgumentException("Нельзя перевести на себя");
        ensureNotBlocked();
        recipient.ensureNotBlocked();
        if (kopecks <= 0) throw new IllegalArgumentException("Сумма должна быть > 0");
        if (kopecks > withdrawalLimitKopecks) throw new IllegalArgumentException("Перевод превышает лимит");

        Currency senderCurrency = this.balance.getCurrency();

        this.balance = balance.subtract(kopecks);
        long credit = kopecks;
        if (!recipient.balance.getCurrency().equals(senderCurrency)) {
            credit = new Balance(kopecks, senderCurrency)
                    .convertTo(recipient.balance.getCurrency())
                    .getAmountInKopecks();
        }
        recipient.balance = recipient.balance.add(credit);
    }

    private void ensureNotBlocked() {
        if (blocked) throw new IllegalStateException("Счёт " + id + " заблокирован");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return String.format("BankAccount{id='%s', owner='%s', balance=%s, limit=%d коп., blocked=%s}",
                id, owner, balance, withdrawalLimitKopecks, blocked);
    }
}