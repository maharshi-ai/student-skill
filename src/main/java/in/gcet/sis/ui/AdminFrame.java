package in.gcet.sis.ui;

import in.gcet.sis.dao.PortalDao;
import in.gcet.sis.model.User;
import javax.swing.*;import javax.swing.table.DefaultTableModel;import java.awt.*;import java.sql.Date;import java.time.LocalDate;

public class AdminFrame extends JFrame {
 private final PortalDao dao=new PortalDao(); private final JTable internships=table(),applications=table();
 public AdminFrame(User user){setTitle("Administration • Student Skill & Internship");setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(1120,720);setLocationRelativeTo(null);
  JPanel root=new JPanel(new BorderLayout(12,12));root.setBackground(Theme.BG);root.setBorder(BorderFactory.createEmptyBorder(22,22,22,22));
  JLabel heading=new JLabel("ADMIN CONTROL CENTRE   /   "+user.fullName());heading.setForeground(Theme.TEXT);heading.setFont(new Font("Segoe UI",Font.BOLD,22));root.add(heading,BorderLayout.NORTH);
  JTabbedPane tabs=new JTabbedPane();tabs.addTab("Internship management",panel(internships,new String[]{"ID","Title","Company","Status","Deadline"},this::loadInternships));tabs.addTab("Application review",panel(applications,new String[]{"ID","Student","Email","Internship","Status","Cover letter","Feedback","Applied"},this::loadApplications));
  root.add(tabs,BorderLayout.CENTER);JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT));actions.setOpaque(false);
  JButton add=button("POST INTERNSHIP"),close=button("CLOSE SELECTED"),review=button("REVIEW APPLICATION"),logout=button("LOG OUT");
  actions.add(add);actions.add(close);actions.add(review);actions.add(logout);root.add(actions,BorderLayout.SOUTH);
  add.addActionListener(e->post());close.addActionListener(e->changeInternship());review.addActionListener(e->review());logout.addActionListener(e->{new LoginFrame().setVisible(true);dispose();});
  add(root);loadInternships();loadApplications();
 }
 private JTable table(){JTable t=new JTable();t.setRowHeight(28);t.setAutoCreateRowSorter(true);return t;}
 private JPanel panel(JTable t,String[] cols,Runnable refresh){JPanel p=new JPanel(new BorderLayout());p.setBackground(Theme.BG);t.setModel(new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}});p.add(new JScrollPane(t));JButton b=button("REFRESH");b.addActionListener(e->refresh.run());p.add(b,BorderLayout.SOUTH);return p;}
 private JButton button(String s){JButton b=new JButton(s);Theme.button(b);return b;}
 private void setRows(JTable t,java.util.List<Object[]> rows){DefaultTableModel m=(DefaultTableModel)t.getModel();m.setRowCount(0);for(Object[] r:rows)m.addRow(r);}
 private void loadInternships(){work(()->setRows(internships,dao.allInternships()));}
 private void loadApplications(){work(()->setRows(applications,dao.allApplications()));}
 private void work(Runnable r){new SwingWorker<Void,Void>(){protected Void doInBackground(){r.run();return null;}protected void done(){try{get();}catch(Exception e){error(e);}}}.execute();}
 private void post(){
  JTextField title=new JTextField(),company=new JTextField(),skills=new JTextField(),location=new JTextField(),duration=new JTextField(),stipend=new JTextField("0"),deadline=new JTextField(LocalDate.now().plusDays(30).toString());JTextArea desc=new JTextArea(4,24);
  JPanel p=new JPanel(new GridLayout(0,2,6,6));for(Object[] x:new Object[][]{{"Title",title},{"Company",company},{"Required skills (comma separated)",skills},{"Location",location},{"Duration",duration},{"Monthly stipend",stipend},{"Deadline (YYYY-MM-DD)",deadline},{"Description",new JScrollPane(desc)}}){p.add(new JLabel((String)x[0]));p.add((Component)x[1]);}
  if(JOptionPane.showConfirmDialog(this,p,"Create internship",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
  work(()->{try{dao.createInternship(title.getText(),company.getText(),desc.getText(),skills.getText(),location.getText(),duration.getText(),Double.parseDouble(stipend.getText()),Date.valueOf(deadline.getText()));setRows(internships,dao.allInternships());}catch(Exception e){throw new RuntimeException(e);}});
 }
 private void changeInternship(){int row=internships.getSelectedRow();if(row<0){warn("Select an internship.");return;}long id=((Number)internships.getValueAt(internships.convertRowIndexToModel(row),0)).longValue();String[] states={"CLOSED","OPEN","ARCHIVED"};String s=(String)JOptionPane.showInputDialog(this,"Set status","Internship status",JOptionPane.QUESTION_MESSAGE,null,states,states[0]);if(s!=null)work(()->{try{dao.setInternshipStatus(id,s);setRows(internships,dao.allInternships());}catch(Exception e){throw new RuntimeException(e);}});}
 private void review(){int row=applications.getSelectedRow();if(row<0){warn("Select an application.");return;}int mr=applications.convertRowIndexToModel(row);long id=((Number)applications.getValueAt(mr,0)).longValue();String[] states={"UNDER_REVIEW","ACCEPTED","REJECTED"};String s=(String)JOptionPane.showInputDialog(this,"Decision","Review application",JOptionPane.QUESTION_MESSAGE,null,states,states[0]);if(s==null)return;String feedback=JOptionPane.showInputDialog(this,"Feedback for student (optional):");work(()->{try{dao.review(id,s,feedback==null?"":feedback);setRows(applications,dao.allApplications());}catch(Exception e){throw new RuntimeException(e);}});}
 private void warn(String s){JOptionPane.showMessageDialog(this,s);}
 private void error(Exception e){Throwable t=e.getCause()==null?e:e.getCause();warn(t.getMessage());}
}
