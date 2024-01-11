package model;

import java.util.Objects;

public class Account {
    private String from;
    private String to;
    private int amount;

    public Account(String from, String to, int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return amount == account.amount && Objects.equals(from, account.from) && Objects.equals(to, account.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                '}';
    }
}