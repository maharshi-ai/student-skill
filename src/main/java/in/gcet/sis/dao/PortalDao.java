package in.gcet.sis.dao;

import in.gcet.sis.config.Database;
import java.sql.*;
import java.util.*;

public class PortalDao {
 public List<Object[]> skills(long studentId) throws SQLException {
  String q="SELECT s.id,s.name,s.category,ss.proficiency,ss.experience_years FROM skills s LEFT JOIN student_skills ss ON ss.skill_id=s.id AND ss.student_id=? ORDER BY s.name";
  List<Object[]> rows=new ArrayList<>();
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(q)){p.setLong(1,studentId);try(ResultSet r=p.executeQuery()){while(r.next())rows.add(new Object[]{r.getLong(1),r.getString(2),r.getString(3),r.getString(4),r.getBigDecimal(5)});}}
  return rows;
 }
 public void addSkill(long studentId,String name,String category,String level,double years) throws SQLException {
  if(name==null||name.isBlank()||category==null||category.isBlank())throw new IllegalArgumentException("Skill and category are required.");
  try(Connection c=Database.connect()){
   c.setAutoCommit(false);
   try {
    long skillId;
    try(PreparedStatement p=c.prepareStatement("INSERT INTO skills(name,category) VALUES(?,?) ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)")){p.setString(1,name.trim());p.setString(2,category.trim());p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("Could not resolve skill.");skillId=r.getLong(1);}}
    try(PreparedStatement p=c.prepareStatement("INSERT INTO student_skills(student_id,skill_id,proficiency,experience_years) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE proficiency=VALUES(proficiency),experience_years=VALUES(experience_years)")){p.setLong(1,studentId);p.setLong(2,skillId);p.setString(3,level);p.setDouble(4,years);p.executeUpdate();}
    c.commit();
   }catch(Exception e){c.rollback();throw e;}
  }
 }
 public void removeSkill(long studentId,long skillId)throws SQLException{try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("DELETE FROM student_skills WHERE student_id=? AND skill_id=?")){p.setLong(1,studentId);p.setLong(2,skillId);p.executeUpdate();}}
 public List<Object[]> internships()throws SQLException{
  List<Object[]> rows=new ArrayList<>();String q="SELECT id,title,company,location,duration,stipend,deadline,COALESCE(required_skills,''),description FROM internships WHERE status='OPEN' AND deadline>=CURRENT_DATE ORDER BY deadline";
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){while(r.next())rows.add(new Object[]{r.getLong(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getBigDecimal(6),r.getDate(7),r.getString(8),r.getString(9)});}
  return rows;
 }
 public void apply(long studentId,long internshipId,String letter)throws SQLException{
  if(letter==null||letter.trim().length()<30)throw new IllegalArgumentException("Cover letter must contain at least 30 characters.");
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("INSERT INTO applications(student_id,internship_id,cover_letter) SELECT ?,id,? FROM internships WHERE id=? AND status='OPEN' AND deadline>=CURRENT_DATE")){p.setLong(1,studentId);p.setString(2,letter.trim());p.setLong(3,internshipId);if(p.executeUpdate()!=1)throw new SQLException("Internship is no longer open.");}
 }
 public List<Object[]> myApplications(long studentId)throws SQLException{
  List<Object[]> rows=new ArrayList<>();String q="SELECT a.id,i.title,i.company,a.status,a.admin_feedback,a.applied_at FROM applications a JOIN internships i ON i.id=a.internship_id WHERE a.student_id=? ORDER BY a.applied_at DESC";
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(q)){p.setLong(1,studentId);try(ResultSet r=p.executeQuery()){while(r.next())rows.add(new Object[]{r.getLong(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getTimestamp(6)});}}
  return rows;
 }
 public void createInternship(String title,String company,String description,String skills,String location,String duration,double stipend,java.sql.Date deadline)throws SQLException{
  if(title.isBlank()||company.isBlank()||description.isBlank())throw new IllegalArgumentException("Title, company and description are required.");
  String q="INSERT INTO internships(title,company,description,required_skills,location,duration,stipend,deadline) VALUES(?,?,?,?,?,?,?,?)";
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(q)){p.setString(1,title.trim());p.setString(2,company.trim());p.setString(3,description.trim());p.setString(4,skills);p.setString(5,location);p.setString(6,duration);p.setDouble(7,stipend);p.setDate(8,deadline);p.executeUpdate();}
 }
 public List<Object[]> allInternships()throws SQLException{
  List<Object[]> rows=new ArrayList<>();String q="SELECT id,title,company,status,deadline FROM internships ORDER BY created_at DESC";
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){while(r.next())rows.add(new Object[]{r.getLong(1),r.getString(2),r.getString(3),r.getString(4),r.getDate(5)});}return rows;
 }
 public void setInternshipStatus(long id,String status)throws SQLException{try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("UPDATE internships SET status=? WHERE id=?")){p.setString(1,status);p.setLong(2,id);p.executeUpdate();}}
 public List<Object[]> allApplications()throws SQLException{
  List<Object[]> rows=new ArrayList<>();String q="SELECT a.id,u.full_name,u.email,i.title,a.status,a.cover_letter,a.admin_feedback,a.applied_at FROM applications a JOIN users u ON u.id=a.student_id JOIN internships i ON i.id=a.internship_id ORDER BY a.applied_at DESC";
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){while(r.next())rows.add(new Object[]{r.getLong(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getString(6),r.getString(7),r.getTimestamp(8)});}return rows;
 }
 public void review(long applicationId,String status,String feedback)throws SQLException{
  if(!List.of("UNDER_REVIEW","ACCEPTED","REJECTED").contains(status))throw new IllegalArgumentException("Invalid application status.");
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("UPDATE applications SET status=?,admin_feedback=? WHERE id=?")){p.setString(1,status);p.setString(2,feedback);p.setLong(3,applicationId);if(p.executeUpdate()!=1)throw new SQLException("Application not found.");}
 }
}
