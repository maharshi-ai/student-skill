package in.gcet.sis.ui;
import in.gcet.sis.model.*;import in.gcet.sis.service.DashboardService;import javax.swing.*;import java.awt.*;
public class StudentFrame extends JFrame {
 private final User user;private final JPanel cards=new JPanel(new GridLayout(2,3,12,12));
 public StudentFrame(User u){user=u;setTitle("Student Dashboard");setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(1000,650);setLocationRelativeTo(null);
  JPanel shell=new JPanel(new BorderLayout(18,18));shell.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));shell.setBackground(Theme.BG);
  JLabel title=new JLabel("Welcome, "+u.fullName());title.setForeground(Theme.TEXT);title.setFont(new Font("Segoe UI",Font.BOLD,25));shell.add(title,BorderLayout.NORTH);
  cards.setBackground(Theme.BG);shell.add(cards,BorderLayout.CENTER);JButton refresh=new JButton("REFRESH DATA");Theme.button(refresh);refresh.addActionListener(e->load());JPanel foot=new JPanel();foot.setOpaque(false);foot.add(refresh);shell.add(foot,BorderLayout.SOUTH);add(shell);load();
 }
 private void load(){cards.removeAll();JLabel wait=new JLabel("Loading database metrics...",SwingConstants.CENTER);wait.setForeground(Theme.MUTED);cards.add(wait);cards.revalidate();cards.repaint();
  new SwingWorker<DashboardStats,Void>(){protected DashboardStats doInBackground()throws Exception{return new DashboardService().load(user.id());}
   protected void done(){cards.removeAll();try{DashboardStats s=get();metric("MY SKILLS",s.skills());metric("OPEN INTERNSHIPS",s.openInternships());metric("MY APPLICATIONS",s.applications());metric("PENDING",s.pending());metric("ACCEPTED",s.accepted());metric("REJECTED",s.rejected());}
    catch(Exception e){JLabel err=new JLabel("Database query failed: "+(e.getCause()==null?e.getMessage():e.getCause().getMessage()));err.setForeground(Theme.TEXT);cards.add(err);}cards.revalidate();cards.repaint();}}.execute();
 }
 private void metric(String label,long value){JPanel c=new JPanel(new BorderLayout(5,12));c.setBackground(Theme.CARD);c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.COPPER),BorderFactory.createEmptyBorder(20,20,20,20)));
  JLabel l=new JLabel(label),v=new JLabel(Long.toString(value));l.setForeground(Theme.MUTED);v.setForeground(Theme.TEXT);v.setFont(new Font("Segoe UI",Font.BOLD,32));c.add(l,BorderLayout.NORTH);c.add(v,BorderLayout.CENTER);cards.add(c);}
}
