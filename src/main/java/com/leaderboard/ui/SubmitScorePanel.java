package com.leaderboard.ui;

import com.leaderboard.model.Game;
import com.leaderboard.model.User;
import com.leaderboard.service.GameService;
import com.leaderboard.service.ScoreService;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.List;

public class SubmitScorePanel extends JPanel {
    private final MainFrame frame;
    private final GameService gameService;
    private final ScoreService scoreService;

    private JComboBox<Game> gameBox;
    private JTextField scoreField;

    public SubmitScorePanel(MainFrame frame, GameService gameService, ScoreService scoreService) {
        this.frame = frame;
        this.gameService = gameService;
        this.scoreService = scoreService;
        buildUi();
        refreshGames();
    }

    private void buildUi() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Submit Score", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        gameBox = new JComboBox<>();
        scoreField = new JTextField(10);
        JButton submitBtn = new JButton("Submit");
        JButton backBtn = new JButton("Back");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Game:"), gbc);
        gbc.gridx = 1; add(gameBox, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Score:"), gbc);
        gbc.gridx = 1; add(scoreField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        add(submitBtn, gbc);
        gbc.gridx = 1; add(backBtn, gbc);

        submitBtn.addActionListener(e -> doSubmit());
        backBtn.addActionListener(e -> frame.showCard("mainMenu"));
    }

    private void refreshGames() {
        List<Game> games = gameService.getAllGames();
        DefaultComboBoxModel<Game> model = new DefaultComboBoxModel<>();
        for (Game g : games) model.addElement(g);
        gameBox.setModel(model);
    }

    private void doSubmit() {
        User user = frame.getCurrentUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Please log in again.", "Session", JOptionPane.WARNING_MESSAGE);
            frame.showCard("login");
            return;
        }
        Game game = (Game) gameBox.getSelectedItem();
        if (game == null) {
            JOptionPane.showMessageDialog(this, "Please select a game.");
            return;
        }
        String scoreText = scoreField.getText();
        double score;
        try {
            score = Double.parseDouble(scoreText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Score must be a number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long now = Instant.now().toEpochMilli();
        try {
            scoreService.submitScore(user.getId(), game.getName(), score, now);
            JOptionPane.showMessageDialog(this, "Score submitted.");
            scoreField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}