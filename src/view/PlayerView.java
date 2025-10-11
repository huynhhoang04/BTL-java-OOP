package view;

import model.Song;
import util.ButtonUtil;

import javax.swing.*;
import java.awt.*;

import controller.*;

public class PlayerView extends JPanel {
    private JLabel lblImage, lblTitle, lblArtist;
    private JButton btnPause, btnStop;
    private ButtonUtil btnPlay;
    private PlayerController controller;

    public PlayerView() {
        controller = new PlayerController();

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 245, 245));

        // ===== Ảnh album =====
        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(400, 400));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        add(lblImage, BorderLayout.CENTER);

        // ===== Thông tin bài hát =====
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(245, 245, 245));

        lblTitle = new JLabel("Chưa có bài hát");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblArtist = new JLabel("");
        lblArtist.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        infoPanel.add(lblTitle);
        infoPanel.add(lblArtist);
        add(infoPanel, BorderLayout.NORTH);

        // ===== Các nút điều khiển =====
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPlay = new ButtonUtil("Play");
        btnPause = new JButton("⏸ Pause");
        btnStop = new JButton("⏹ Stop");

        btnPlay.addActionListener(e -> controller.play());
        btnStop.addActionListener(e -> controller.stop());


        controlPanel.add(btnPlay);
        controlPanel.add(btnPause);
        controlPanel.add(btnStop);

        add(controlPanel, BorderLayout.SOUTH);
    }

    // ===== cập nhật khi chọn bài mới =====
    public void setSong(Song song) {
        if (song == null) return;
        lblTitle.setText(song.getTitle());
        lblArtist.setText(song.getArtist());
        controller.setPlaySong(song);

        try {
            ImageIcon img = new ImageIcon(new java.net.URL(song.getImgUrl()));
            Image scaled = img.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblImage.setIcon(null);
        }
    }
}
