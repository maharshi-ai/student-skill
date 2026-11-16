package in.gcet.sis.dao;
import in.gcet.sis.config.Database; import in.gcet.sis.model.DashboardStats; import java.sql.*;
public class DashboardDao {
 public DashboardStats forStudent(long id) throws SQLException {
  String q="SELECT (SELECT COUNT(*) FROM student_skills WHERE student_id=?) skills_count,"+
   "(SELECT COUNT(*) FROM internships WHERE status='OPEN' AND deadline>=CURRENT_DATE) open_count,"+
   "(SELECT COUNT(*) FROM applications WHERE student_id=?) app_count,"+
   "(SELECT COUNT(*) FROM applications WHERE student_id=? AND status IN ('PENDING','UNDER_REVIEW')) pending_count,"+
   "(SELECT COUNT(*) FROM applications WHERE student_id=? AND status='ACCEPTED') accepted_count,"+
   "(SELECT COUNT(*) FROM applications WHERE student_id=? AND status='REJECTED') rejected_count";
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(q)){
   for(int i=1;i<=5;i++)p.setLong(i,id);try(ResultSet r=p.executeQuery()){r.next();return new DashboardStats(r.getLong(1),r.getLong(2),r.getLong(3),r.getLong(4),r.getLong(5),r.getLong(6));}
  }
 }
}
