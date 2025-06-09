package crud;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;

public class programs extends JFrame {

    private JPanel contentPane;
    private JTextField txtName, txtEmail, txtCustomerId, txtAccountId, txtAmount;
    private JTextArea textArea;
    private finall db;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                programs frame = new programs();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public programs() {
        db = new finall();

        setTitle("Banking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 600);
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Customer and Account Info"));

        inputPanel.add(new JLabel("Customer Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        inputPanel.add(txtEmail);

        inputPanel.add(new JLabel("Customer ID:"));
        txtCustomerId = new JTextField();
        inputPanel.add(txtCustomerId);

        inputPanel.add(new JLabel("Account ID:"));
        txtAccountId = new JTextField();
        inputPanel.add(txtAccountId);

        inputPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        inputPanel.add(txtAmount);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        JButton btnAddCustomer = new JButton("Add Customer");
        JButton btnCreateAccount = new JButton("Create Account");
        JButton btnDeposit = new JButton("Deposit");
        JButton btnWithdraw = new JButton("Withdraw");
        JButton btnViewAccounts = new JButton("View Accounts");

        buttonPanel.add(btnAddCustomer);
        buttonPanel.add(btnCreateAccount);
        buttonPanel.add(btnDeposit);
        buttonPanel.add(btnWithdraw);
        buttonPanel.add(btnViewAccounts);

        // Add input and button panels to the top
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        contentPane.add(topPanel, BorderLayout.NORTH);

        // Output Area
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Account Details"));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Button Actions
        btnAddCustomer.addActionListener(e -> {
            db.addCustomer(txtName.getText().trim(), txtEmail.getText().trim());
            JOptionPane.showMessageDialog(this, "Customer added.");
        });

        btnCreateAccount.addActionListener(e -> {
            try {
                int customerId = Integer.parseInt(txtCustomerId.getText().trim());
                db.createAccount(customerId);
                JOptionPane.showMessageDialog(this, "Account created.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Customer ID.");
            }
        });

        btnDeposit.addActionListener(e -> {
            try {
                int accountId = Integer.parseInt(txtAccountId.getText().trim());
                double amount = Double.parseDouble(txtAmount.getText().trim());
                db.deposit(accountId, amount);
                JOptionPane.showMessageDialog(this, "Deposit successful.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Account ID or Amount.");
            }
        });

        btnWithdraw.addActionListener(e -> {
            try {
                int accountId = Integer.parseInt(txtAccountId.getText().trim());
                double amount = Double.parseDouble(txtAmount.getText().trim());
                db.withdraw(accountId, amount);
                JOptionPane.showMessageDialog(this, "Withdrawal processed.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Account ID or Amount.");
            }
        });

        btnViewAccounts.addActionListener(e -> {
            try {
                ResultSet rs = db.viewAccounts();
                textArea.setText("");
                while (rs != null && rs.next()) {
                    textArea.append("Account ID: " + rs.getInt("account_id") +
                            " | Name: " + rs.getString("name") +
                            " | Email: " + rs.getString("email") +
                            " | Balance: " + rs.getDouble("balance") + "\n");
                }
            } catch (Exception ex) {
                textArea.setText("Error: " + ex.getMessage());
            }
        });
    }
}
