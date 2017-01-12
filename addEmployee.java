import java.sql.*;

/**
 * Process to add an employee with the needed information to the database.
**/
public class addEmployee{
	private String per_id;
	private String job_code;
	private String type;
	private String pay_rate;
	private String pay_type;
	private String comp_id;
	PreparedStatement pStmt;

	/**
	 * Constructor.
	 * @param per_id String per_id of the employee
	 * @param job_code String job code id for the job the employee will work
	 * @param type String 'full time' or 'part time'
	 * @param pay_rate String pay rate for the employee
	 * @param comp_id String comp_id value that the employee will work for
	**/
	public addEmployee(String per_id, String job_code, String type,
						String pay_rate, String pay_type, String comp_id){
		this.per_id = per_id;
		this.job_code = job_code;
		this.type = type;
		this.pay_rate = pay_rate;
		this.pay_type = pay_type;
		this.comp_id = comp_id;
	}

	/**
	 * Notifies the database of the job that the person will be working.
	 * @param conn Connection connection needed ot connect to the database
	**/
	public void addToJob(Connection conn)throws SQLException{
		pStmt = conn.prepareStatement("insert into works_job values (?,?)");
		pStmt.setString(1, this.job_code);
		pStmt.setString(2, this.per_id);
		pStmt.executeUpdate();
		pStmt = conn.prepareStatement(	"update job " +
										"set type = ?, pay_rate = ?, pay_type = ? " +
										"where job_code = ?");
		pStmt.setString(1, this.type);
		pStmt.setString(2, this.pay_rate);
		pStmt.setString(3, this.pay_type);
		pStmt.setString(4, this.job_code);
		pStmt.executeUpdate();

		pStmt = conn.prepareStatement("insert into pays values (?,?)");
		pStmt.setString(1, this.job_code);
		pStmt.setString(2, this.comp_id);
		pStmt.executeUpdate();

	}

}