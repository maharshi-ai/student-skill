package in.gcet.sis.service;
import in.gcet.sis.dao.DashboardDao; import in.gcet.sis.model.DashboardStats; import java.sql.SQLException;
public class DashboardService { private final DashboardDao dao=new DashboardDao(); public DashboardStats load(long id)throws SQLException{return dao.forStudent(id);} }
