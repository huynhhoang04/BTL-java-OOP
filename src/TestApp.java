import controller.UserDataAccess;
import model.User;

public class TestApp {
    public static void main(String[] args) {
        // Tạo 1 instance DAO để thao tác với DB
UserDataAccess dao = new UserDataAccess();
User loginUser = new User("huynh", "12345");  // chỉ cần user/pass
User loggedIn = dao.login(loginUser);

if (loggedIn != null) {
    System.out.println("✅ Đăng nhập thành công!");
    System.out.println("Xin chào: " + loggedIn.getUsername());
} else {
    System.out.println("❌ Sai tài khoản hoặc mật khẩu!");
}
    }
}
