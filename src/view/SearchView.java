package view;

import controller.SearchController;
import model.Song;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class SearchView extends JPanel {
    private JTextField tfKeyword;
    private JButton btnSearch, btnPlay;
    private JList<Song> songList;
    private DefaultListModel<Song> listModel;
    private SearchController controller;
    private Song selectedSong;
    private Consumer<Song> onSongSelected;

    public SearchView() {
        setLayout(new BorderLayout(10, 10));
        controller = new SearchController();

        // ===== Thanh tìm kiếm =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tfKeyword = new JTextField(20);
        btnSearch = new JButton("Tìm kiếm");
        btnPlay = new JButton("▶ Play");
        btnPlay.setEnabled(false); // chỉ bật khi chọn bài

        topPanel.add(new JLabel("Từ khóa:"));
        topPanel.add(tfKeyword);
        topPanel.add(btnSearch);
        topPanel.add(btnPlay);
        add(topPanel, BorderLayout.NORTH);

        // ===== Danh sách bài hát =====
        listModel = new DefaultListModel<>();
        songList = new JList<>(listModel);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(songList);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Sự kiện =====
        btnSearch.addActionListener(e -> searchSongs());
        songList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedSong = songList.getSelectedValue();
                btnPlay.setEnabled(selectedSong != null);
                if(selectedSong != null && onSongSelected != null){
                    onSongSelected.accept(selectedSong);
                }
            }
        });

        btnPlay.addActionListener(e -> {
            if (selectedSong != null) {
                JOptionPane.showMessageDialog(this,
                        "Đang chọn phát bài: " + selectedSong.getTitle() +
                        "\nTác giả: " + selectedSong.getArtist(),
                        "Play Song", JOptionPane.INFORMATION_MESSAGE);
                // 🔥 sau này chỗ này sẽ gọi hàm playAudio(selectedSong.getAudioUrl());
            }
        });
    }

    private void searchSongs() {
        String keyword = tfKeyword.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa!");
            return;
        }

        listModel.clear();
        List<Song> songs = controller.searchSong(keyword);

        if (songs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy bài hát nào!");
        } else {
            for (Song s : songs) listModel.addElement(s);
        }
    }

    public void setOnSongSelected(Consumer<Song> callback){
        this.onSongSelected = callback;
    }
}


