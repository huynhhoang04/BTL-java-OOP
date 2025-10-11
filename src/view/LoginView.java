package view;

import controller.LoginController;
import model.User;
import util.ButtonUtil;
import util.FontUtil;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private ButtonUtil btnLogin;
    private ButtonUtil btnRegister;

    public LoginView() {
        setTitle("Cặc");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === FONT ===
        FontUtil fontUtil = new FontUtil();
        Font mainFont = fontUtil.getFont();

        // ===== LEFT PANEL (form) =====
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(8, 8, 8));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Sign in to continue");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setForeground(Color.GRAY);

        tfUsername = new JTextField();
        pfPassword = new JPasswordField();
        btnLogin = new ButtonUtil("Dit me may");
        btnRegister = new ButtonUtil("dit bo m");

        tfUsername.setMaximumSize(new Dimension(300, 40));
        tfUsername.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2, true));
        pfPassword.setMaximumSize(new Dimension(300, 40));
        pfPassword.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2, true));
        btnLogin.setMaximumSize(new Dimension(300, 40));
        btnRegister.setMaximumSize(new Dimension(300, 40));

        btnLogin.setBackground(new Color(139, 91, 91));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(mainFont);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegister.setBorderPainted(false);
        btnRegister.setBackground(new Color(230, 230, 230));
        btnRegister.setFont(mainFont);
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(sub);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(tfUsername);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(pfPassword);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(btnLogin);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(btnRegister);

        // ===== RIGHT PANEL (image) =====
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon("/asset/loginbg.jpg"); // ← đặt ảnh vào /assets/
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        imagePanel.setPreferredSize(new Dimension(400, 500));

        add(formPanel, BorderLayout.WEST);
        add(imagePanel, BorderLayout.CENTER);

        // ===== EVENT LOGIN =====
        btnLogin.addActionListener(e -> {
            String username = tfUsername.getText();
            String password = new String(pfPassword.getPassword());
            LoginController controller = new LoginController();
            User user = controller.handleLogin(username, password);

            if (user != null){
                new MainView();
                dispose();
            }
            else
                JOptionPane.showMessageDialog(this, "❌ Invalid credentials.");
        });

        btnRegister.addActionListener(e ->{
            new RegisterView();
        });

        setVisible(true);
    }
}
