import controller.SearchController;
import model.Song;
import java.util.List;

public class TestApi {
    public static void main(String[] args) {
        // Tạo controller
        SearchController searchController = new SearchController();

        // Gọi API tìm bài hát
        String keyword = "pop"; // có thể đổi thành "relax", "jazz", "pop"
        System.out.println("🔍 Searching songs with keyword: " + keyword);

        List<Song> songs = searchController.searchSong(keyword);

        // In kết quả ra màn hình
        if (songs.isEmpty()) {
            System.out.println("❌ Không tìm thấy bài hát nào!");
        } else {
            System.out.println("✅ Tìm thấy " + songs.size() + " bài hát:\n");
            for (Song s : songs) {
                System.out.println(s.toString());
            }
        }
    }
}
