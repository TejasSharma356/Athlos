package com.leaderboard.ui;

import com.leaderboard.model.Game;
import com.leaderboard.service.GameService;
import com.leaderboard.service.ScoreService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private final MainFrame frame;
    private final GameService gameService;
    private final ScoreService scoreService;

    private JComboBox<Game> gameBox;
    private JTable table;

    public LeaderboardPanel(MainFrame frame, GameService gameService, ScoreService scoreService) {
        this.frame = frame;
        this.gameService = gameService;
        this.scoreService = scoreService;
        buildUi();
        refreshGames();
        refreshTable();
    }

    private void buildUi() {
        setLayout(new BorderLayout(8, 8));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Leaderboard");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        gameBox = new JComboBox<>();
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");
        top.add(title);
        top.add(new JLabel("Game:"));
        top.add(gameBox);
        top.add(refreshBtn);
        top.add(backBtn);

        String[] cols = {"Rank", "Username", "Score", "Game"};
        table = new JTable(new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
        JScrollPane scroll = new JScrollPane(table);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refreshTable());
        gameBox.addActionListener(e -> refreshTable());
        backBtn.addActionListener(e -> frame.showCard("mainMenu"));
    }

    private void refreshGames() {
        DefaultComboBoxModel<Game> model = new DefaultComboBoxModel<>();
        for (Game g : gameService.getAllGames()) model.addElement(g);
        gameBox.setModel(model);
    }

    private void refreshTable() {
        Game game = (Game) gameBox.getSelectedItem();
        if (game == null) return;
        List<ScoreService.ScoreRow> rows = scoreService.getLeaderboard(game.getName(), 50);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (ScoreService.ScoreRow r : rows) {
            model.addRow(new Object[]{r.rank, r.username, r.score, r.gameName});
        }
    }
}