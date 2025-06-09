package crud;

import java.sql.*;

public class finall {

    private int id;

	public Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_db", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    public void addCustomer(String name, String email) {
        String sql = "INSERT INTO customers (name, email) VALUES (?, ?)";
        try (Connection con = connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createAccount(int customerId) {
        String sql = "INSERT INTO accounts (id, balance) VALUES (?, 0.0)";
        try (Connection con = connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deposit(int Id, double amount) {
        try (Connection con = connect()) {
            con.setAutoCommit(false);

            PreparedStatement updateBalance = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?");
            updateBalance.setDouble(1, amount);
            updateBalance.setInt(2, id);
            updateBalance.executeUpdate();

            PreparedStatement log = con.prepareStatement("INSERT INTO transactions (id, amount, type) VALUES (?, ?, 'DEPOSIT')");
            log.setInt(1, id);
            log.setDouble(2, amount);
            log.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdraw(int id, double amount) {
        try (Connection con = connect()) {
            con.setAutoCommit(false);

            PreparedStatement checkBalance = con.prepareStatement("SELECT balance FROM accounts WHERE id = ?");
            checkBalance.setInt(1, id);
            ResultSet rs = checkBalance.executeQuery();
            if (rs.next() && rs.getDouble("balance") >= amount) {
                PreparedStatement updateBalance = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?");
                updateBalance.setDouble(1, amount);
                updateBalance.setInt(2, id);
                updateBalance.executeUpdate();

                PreparedStatement log = con.prepareStatement("INSERT INTO transactions (id, amount, type) VALUES (?, ?, 'WITHDRAWAL')");
                log.setInt(1, id);
                log.setDouble(2, amount);
                log.executeUpdate();

                con.commit();
            } else {
                System.out.println("Insufficient balance.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet viewAccounts() {
        try {
            Connection con = connect();
            Statement stmt = con.createStatement();
            
            return stmt.executeQuery(
                "SELECT a.id AS id, c.name, c.email, a.balance " +
                "FROM accounts a JOIN customers c ON a.id = c.id"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
