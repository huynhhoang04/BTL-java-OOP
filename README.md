Class & Object

User, Song, Artist, Album, Playlist, ListeningHistory được xây dựng thành các lớp đối tượng với thuộc tính (attributes) và phương thức (methods) riêng.

Ví dụ: Song có thuộc tính songId, title, duration, artist, album và phương thức play(), pause().

Encapsulation (Đóng gói)

Các thuộc tính như password, email của User được khai báo private, chỉ truy cập thông qua getter/setter để đảm bảo an toàn dữ liệu.

Inheritance (Kế thừa)

Lớp PremiumUser kế thừa từ User, mở rộng thêm tính năng nghe nhạc chất lượng cao hoặc tải nhạc offline.

Các lớp giao diện kế thừa từ BaseView để tái sử dụng code.

Polymorphism (Đa hình)

Phương thức play() được định nghĩa đa hình: có thể phát Song đơn lẻ hoặc phát cả Playlist.

Interface Playable quy định mọi đối tượng có thể phát nhạc (bài hát, playlist, album) đều phải cài đặt phương thức play().

Abstraction (Trừu tượng)

Xây dựng lớp trừu tượng Media làm cha cho Song và Podcast (nếu mở rộng), giúp quản lý các loại nội dung đa phương tiện.

Association / Aggregation / Composition

Association: User liên kết với Playlist (1 user có nhiều playlist).

Aggregation: Album chứa nhiều Song, nhưng bài hát có thể tồn tại độc lập.

Composition: Playlist chứa danh sách Song; nếu playlist bị xóa thì danh sách đó cũng mất.
