package in.gcet.sis;
import in.gcet.sis.config.Database;
import in.gcet.sis.ui.LoginFrame;
import javax.swing.*;
public final class Main {
 private Main(){}
 public static void main(String[] args){ SwingUtilities.invokeLater(()->{
  try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); Database.load(); new LoginFrame().setVisible(true); }
  catch(Exception e){ JOptionPane.showMessageDialog(null,e.getMessage(),"Startup error",JOptionPane.ERROR_MESSAGE); }
 });}
}
