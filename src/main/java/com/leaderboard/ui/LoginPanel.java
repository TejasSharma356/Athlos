package com.leaderboard.ui;

import com.leaderboard.model.User;
import com.leaderboard.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final MainFrame frame;
    private final AuthService authService;

    public LoginPanel(MainFrame frame, AuthService authService) {
        this.frame = frame;
        this.authService = authService;
        buildUi();
    }

    private void buildUi() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");
        JButton toRegisterBtn = new JButton("Create an account");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(loginBtn, gbc);
        gbc.gridx = 1; add(toRegisterBtn, gbc);

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String pw = new String(passwordField.getPassword());
            if (username == null || username.isBlank() || pw.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                User user = authService.login(username, pw);
                if (user == null) {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    frame.onLoginSuccess(user);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error connecting to Redis: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        toRegisterBtn.addActionListener(e -> frame.showCard("register"));
    }
}