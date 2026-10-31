public class Main {
    public static void main(String[] args) {
        System.out.println("=== BankAccount Demo ===\n");

        BankAccount alice = new BankAccount("ACC-001", "Alice",
                new Balance(500_00, Currency.RUB), 100_00);
        BankAccount bob = new BankAccount("ACC-002", "Bob",
                new Balance(200_00, Currency.USD), 50_00);

        System.out.println("Изначально:");
        System.out.println("  " + alice);
        System.out.println("  " + bob + "\n");

        alice.deposit(200_00);
        System.out.println("После deposit(200₽): " + alice.getBalance());

        alice.withdraw(50_00);
        System.out.println("После withdraw(50₽): " + alice.getBalance());

        try {
            alice.withdraw(200_00);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка лимита: " + e.getMessage());
        }

        System.out.println("Баланс Alice в USD: " + alice.getBalance().convertTo(Currency.USD));

        System.out.println("\nПеревод Bob -> Alice 10$:");
        bob.transferTo(alice, 10_00);
        System.out.println("  Bob:   " + bob.getBalance());
        System.out.println("  Alice: " + alice.getBalance());

        bob.setBlocked(true);
        try {
            bob.deposit(1_00);
        } catch (IllegalStateException e) {
            System.out.println("\nОшибка блокировки: " + e.getMessage());
        }

        System.out.println("\nИтог:");
        System.out.println("  " + alice);
        System.out.println("  " + bob);
    }
}