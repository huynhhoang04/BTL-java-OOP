package util;

import javax.swing.*;
import java.awt.*;

public class ButtonUtil extends JButton {
    private Color backgroundColor = new Color(233, 89, 97);  // hồng kiểu #e95961
    private Color hoverColor = new Color(245, 105, 110);     // màu sáng hơn khi hover
    private boolean hovered = false;

    public ButtonUtil(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // sự kiện hover
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hovered = true;
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // chọn màu nền theo trạng thái
        g2.setColor(hovered ? hoverColor : backgroundColor);

        // bo tròn nhiều => gần như hình oval
        g2.fillRoundRect(0, 0, width, height, height, height);

        // bóng nhẹ bên dưới
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(2, 2, width, height, height, height);

        // vẽ chữ
        super.paintComponent(g);
        g2.dispose();
    }
}
