package in.gcet.sis.ui;
import java.awt.*;
public final class Theme {
 public static final Color BG=Color.decode("#171717"),PANEL=Color.decode("#202124"),CARD=Color.decode("#292B30"),COPPER=Color.decode("#C47A44"),TEXT=Color.decode("#F5F5F5"),MUTED=Color.decode("#B0B3B8");
 private Theme(){} public static void button(Component c){c.setBackground(COPPER);c.setForeground(Color.WHITE);c.setFont(new Font("Segoe UI",Font.BOLD,13));}
}
