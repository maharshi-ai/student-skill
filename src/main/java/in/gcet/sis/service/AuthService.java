package in.gcet.sis.service;
import in.gcet.sis.dao.UserDao; import in.gcet.sis.model.User; import org.mindrot.jbcrypt.BCrypt; import java.sql.*; import java.util.regex.Pattern;
public class AuthService {
 private final UserDao dao=new UserDao(); private static final Pattern EMAIL=Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
 public void register(String name,String email,char[] password) throws SQLException {
  String n=name==null?"":name.trim(),e=email==null?"":email.trim().toLowerCase();
  if(n.isBlank()||!EMAIL.matcher(e).matches())throw new IllegalArgumentException("Enter a name and valid email.");
  if(password==null||password.length<8)throw new IllegalArgumentException("Password must contain at least 8 characters.");
  dao.register(n,e,BCrypt.hashpw(new String(password),BCrypt.gensalt(12)));
 }
 public User login(String email,char[] password) throws SQLException {
  if(email==null||password==null)return null;String e=email.trim().toLowerCase(),hash=dao.passwordHash(e);
  return hash!=null&&BCrypt.checkpw(new String(password),hash)?dao.findByEmail(e):null;
 }
}
