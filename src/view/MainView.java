package view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame{
    private SearchView searchView;
    private PlayerView playerView;
    public MainView(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        searchView = new SearchView();
        playerView = new PlayerView();

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.add(searchView, BorderLayout.CENTER);
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JPanel playPanel = new JPanel();
        playPanel.setBackground(new Color(240, 240, 240));
        playPanel.add(playerView, BorderLayout.CENTER);
        playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.Y_AXIS));
        playPanel.setBorder(BorderFactory.createEmptyBorder(50, 80,  50, 80));

        add(searchPanel, BorderLayout.WEST);
        add(playPanel, BorderLayout.CENTER);

        searchView.setOnSongSelected(song -> {
            playerView.setSong(song);
        });

        setVisible(true);
    }
    
}
