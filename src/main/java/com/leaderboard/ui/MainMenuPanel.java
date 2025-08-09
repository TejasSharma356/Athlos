package com.leaderboard.ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private final MainFrame frame;

    public MainMenuPanel(MainFrame frame) {
        this.frame = frame;
        buildUi();
    }

    private void buildUi() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcome = new JLabel("Main Menu", SwingConstants.CENTER);
        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 22f));

        JButton submitScoreBtn = new JButton("Submit Score");
        JButton leaderboardBtn = new JButton("View Leaderboard");
        JButton myRankBtn = new JButton("My Rank");
        JButton adminBtn = new JButton("Admin Panel");
        JButton logoutBtn = new JButton("Logout");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(welcome, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        add(submitScoreBtn, gbc);
        gbc.gridy++;
        add(leaderboardBtn, gbc);
        gbc.gridy++;
        add(myRankBtn, gbc);
        gbc.gridy++;
        add(adminBtn, gbc);
        gbc.gridy++;
        add(logoutBtn, gbc);

        submitScoreBtn.addActionListener(e -> frame.showCard("submitScore"));
        leaderboardBtn.addActionListener(e -> frame.showCard("leaderboard"));
        myRankBtn.addActionListener(e -> frame.showCard("myRank"));
        adminBtn.addActionListener(e -> frame.showCard("admin"));
        logoutBtn.addActionListener(e -> frame.logout());
    }
}