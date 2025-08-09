package com.leaderboard.ui;

import com.leaderboard.model.Game;
import com.leaderboard.model.ScoreEntry;
import com.leaderboard.model.User;
import com.leaderboard.service.GameService;
import com.leaderboard.service.ScoreService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyRankPanel extends JPanel {
    private final MainFrame frame;
    private final GameService gameService;
    private final ScoreService scoreService;

    private JComboBox<Game> gameBox;
    private JLabel rankLabel;
    private JLabel bestScoreLabel;
    private JTable historyTable;

    public MyRankPanel(MainFrame frame, GameService gameService, ScoreService scoreService) {
        this.frame = frame;
        this.gameService = gameService;
        this.scoreService = scoreService;
        buildUi();
        refreshGames();
        refreshData();
    }

    private void buildUi() {
        setLayout(new BorderLayout(8,8));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("My Rank");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        gameBox = new JComboBox<>();
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");
        top.add(title);
        top.add(new JLabel("Game:"));
        top.add(gameBox);
        top.add(refreshBtn);
        top.add(backBtn);

        JPanel stats = new JPanel(new GridLayout(1, 2, 8, 8));
        rankLabel = new JLabel("Rank: -");
        bestScoreLabel = new JLabel("Best Score: -");
        stats.add(rankLabel);
        stats.add(bestScoreLabel);

        String[] cols = {"Timestamp", "Score"};
        historyTable = new JTable(new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });

        add(top, BorderLayout.NORTH);
        add(stats, BorderLayout.CENTER);
        add(new JScrollPane(historyTable), BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshData());
        gameBox.addActionListener(e -> refreshData());
        backBtn.addActionListener(e -> frame.showCard("mainMenu"));
    }

    private void refreshGames() {
        DefaultComboBoxModel<Game> model = new DefaultComboBoxModel<>();
        for (Game g : gameService.getAllGames()) model.addElement(g);
        gameBox.setModel(model);
    }

    private void refreshData() {
        User user = frame.getCurrentUser();
        Game game = (Game) gameBox.getSelectedItem();
        if (user == null || game == null) return;
        Long rank = scoreService.getUserRank(user.getId(), game.getName());
        Double best = scoreService.getUserBestScore(user.getId(), game.getName());
        rankLabel.setText("Rank: " + (rank == null ? "-" : rank));
        bestScoreLabel.setText("Best Score: " + (best == null ? "-" : best));

        List<ScoreEntry> history = scoreService.getUserHistory(user.getId(), game.getName(), 100);
        DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        for (ScoreEntry e : history) {
            String ts = fmt.format(Instant.ofEpochMilli(e.getTimestampMs()));
            model.addRow(new Object[]{ts, e.getScore()});
        }
    }
}