package view;

import controller.LoginController;
import controller.RegisterController;
import model.User;
import util.FontUtil;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame{
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JTextField tfEmail;
    private JButton btnRegister;

    public RegisterView(){
        setTitle("lồn");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        FontUtil fontUtil = new FontUtil();
        Font mainFont = fontUtil.getFont();

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 245, 250));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JLabel title = new JLabel("DIT ME MAY");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.DARK_GRAY);

        tfUsername = new JTextField();
        pfPassword = new JPasswordField();
        tfEmail = new JTextField();
        btnRegister = new JButton("Create Account");

        tfUsername.setMaximumSize(new Dimension(300, 40));
        pfPassword.setMaximumSize(new Dimension(300, 40));
        tfEmail.setMaximumSize(new Dimension(300, 40));
        btnRegister.setMaximumSize(new Dimension(300, 40));

        btnRegister.setBackground(new Color(139, 91, 91));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(mainFont);
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(tfUsername);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(pfPassword);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(tfEmail);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(btnRegister);

        add(formPanel, BorderLayout.CENTER);

        btnRegister.addActionListener(e ->{
            String username = tfUsername.getText();
            String password = new String(pfPassword.getPassword());
            String email = tfEmail.getText();
            RegisterController controller = new RegisterController();
            User user = controller.registerUser(username, password, email);

            if (user != null){
                JOptionPane.showMessageDialog(this, " successful!");
                dispose();
            }
            else
                JOptionPane.showMessageDialog(this, " Invalid");
        });

        setVisible(true);
    }
    
}
