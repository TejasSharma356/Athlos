package com.leaderboard.ui;

import com.leaderboard.model.User;
import com.leaderboard.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private final MainFrame frame;
    private final AuthService authService;

    public RegisterPanel(MainFrame frame, AuthService authService) {
        this.frame = frame;
        this.authService = authService;
        buildUi();
    }

    private void buildUi() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        JTextField usernameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"USER", "ADMIN"});
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; add(roleBox, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(registerBtn, gbc);
        gbc.gridx = 1; add(backBtn, gbc);

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String pw = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();
            if (username.isBlank() || email.isBlank() || pw.isBlank()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                User user = authService.register(username, email, pw, role);
                JOptionPane.showMessageDialog(this, "Account created. Please log in.");
                frame.showCard("login");
            } catch (IllegalStateException ise) {
                JOptionPane.showMessageDialog(this, ise.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> frame.showCard("login"));
    }
}