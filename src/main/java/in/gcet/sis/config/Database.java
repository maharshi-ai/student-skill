package in.gcet.sis.config;
import java.io.*; import java.nio.file.*; import java.sql.*; import java.util.*;
public final class Database {
 private static String url,user,password; private Database(){}
 public static void load() throws IOException {
  Path f=Path.of("application.properties");
  if(!Files.isRegularFile(f)) throw new FileNotFoundException("Copy application.properties.example to application.properties and configure MySQL.");
  Properties p=new Properties(); try(Reader r=Files.newBufferedReader(f)){p.load(r);}
  url=required(p,"db.url"); user=required(p,"db.username"); password=p.getProperty("db.password","");
 }
 private static String required(Properties p,String k){
  String v=p.getProperty(k); if(v==null||v.isBlank()||v.startsWith("YOUR_")) throw new IllegalStateException("Configure "+k+" in application.properties."); return v.trim();
 }
 public static Connection connect() throws SQLException { if(url==null) throw new SQLException("Database configuration not loaded."); return DriverManager.getConnection(url,user,password); }
}
