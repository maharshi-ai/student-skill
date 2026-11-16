package in.gcet.sis.dao;
import in.gcet.sis.config.Database; import in.gcet.sis.model.User; import java.sql.*;
public class UserDao {
 public void register(String name,String email,String hash) throws SQLException {
  try(Connection c=Database.connect()){ c.setAutoCommit(false); try {
   long id; try(PreparedStatement p=c.prepareStatement("INSERT INTO users(full_name,email,password_hash) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)){
    p.setString(1,name);p.setString(2,email);p.setString(3,hash);p.executeUpdate();
    try(ResultSet r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("Missing generated user id");id=r.getLong(1);}
   }
   try(PreparedStatement p=c.prepareStatement("INSERT INTO student_profiles(user_id) VALUES(?)")){p.setLong(1,id);p.executeUpdate();}
   c.commit();
  }catch(SQLException e){c.rollback();throw e;} }
 }
 public String passwordHash(String email) throws SQLException {
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("SELECT password_hash FROM users WHERE email=?")){
   p.setString(1,email);try(ResultSet r=p.executeQuery()){return r.next()?r.getString(1):null;}
  }
 }
 public User findByEmail(String email) throws SQLException {
  try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("SELECT id,full_name,email,role FROM users WHERE email=?")){
   p.setString(1,email);try(ResultSet r=p.executeQuery()){return r.next()?new User(r.getLong(1),r.getString(2),r.getString(3),r.getString(4)):null;}
  }
 }
}
