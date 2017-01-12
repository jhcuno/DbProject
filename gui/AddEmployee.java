/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.contacteditor;

/**
 *
 * @author joshcotogno
 */
import java.sql.*;


public class AddEmployee{
	private String per_id;
	private String job_code;
	private String type;
	private String pay_rate;
	private String pay_type;
	private String comp_id;
	PreparedStatement pStmt;

	public AddEmployee(String per_id, String job_code, String type,
						String pay_rate, String pay_type, String comp_id, Connection conn){
		this.per_id = per_id;
		this.job_code = job_code;
		this.type = type;
		this.pay_rate = pay_rate;
		this.pay_type = pay_type;
		this.comp_id = comp_id;
	}

	public void addToJob(Connection conn)throws SQLException{
		String sqlStatement = "insert into works_job values (?,?)";
                pStmt = conn.prepareStatement(sqlStatement);
		pStmt.setString(1, this.job_code);
		pStmt.setString(2, this.per_id);
		pStmt.executeUpdate();
		sqlStatement = 	"update job " +
										"set type = ?, pay_rate = ?, pay_type = ? " +
										"where job_code = ?"; 
                pStmt = conn.prepareStatement(sqlStatement);
		pStmt.setString(1, this.type);
		pStmt.setString(2, this.pay_rate);
		pStmt.setString(3, this.pay_type);
		pStmt.setString(4, this.job_code);
		pStmt.executeUpdate();

                sqlStatement = "insert into pays values (?,?)";
		pStmt = conn.prepareStatement(sqlStatement);
		pStmt.setString(1, this.job_code);
		pStmt.setString(2, this.comp_id);
		pStmt.executeUpdate();

	}

}
