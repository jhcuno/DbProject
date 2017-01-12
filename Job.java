import java.sql.*;

public class Job{
	private String job_code;
	private String jp_code;
	private String type;
	private double pay_rate;
	private String pay_type;
	private String company;
	PreparedStatement pStmt;

/**
 * Constructor.
 * @param job_code String unique job code
 * @param jp_code String job profile code
 * @param type String 'part time' or 'full time'
 * @param pay_rate double given pay rate for the job
 * @param pay_type String 'wage' or 'salary'
 * @param company String company where the job exists
**/
	public Job(String job_code, String jp_code, String type, double pay_rate, 
						String pay_type, String company){
		this.job_code = job_code;
		this.jp_code = jp_code;
		this.type = type;
		this.pay_rate = pay_rate;
		this.pay_type = pay_type;
		this.company = company;
	}

	/**
	 * Adds job to the database.
	 * @param conn Connection connection to the needed database
	**/
	public void addJob(Connection conn) throws SQLException{
		pStmt = conn.prepareStatement("insert into job values(?,?,?,?,?,?)");
		pStmt.setString(1, this.job_code);
		pStmt.setString(2, this.jp_code);
		pStmt.setString(3, this.type);
		pStmt.setDouble(4, this.pay_rate);
		pStmt.setString(5, this.pay_type);
		pStmt.setString(6, this.company);
		pStmt.executeUpdate();
	}

	/**
	 * Deletes a job from the database.
	 * @param conn Connection connection of the needed database
	**/
	public void deleteJob(Connection conn) throws SQLException{
		pStmt = conn.prepareStatement(	"delete from job " + 
									 	"where job_code = ?");
		pStmt.setString(1, this.job_code);
		pStmt.executeUpdate();
	}

	public String toString(){
		return (("job_code: " + job_code + "\njp_code: " + jp_code + "\ntype: " + type +
					"\npay_rate + " + Double.toString(pay_rate) + "\npay_type: " + pay_type +
					"\ncompany: " + company));
	}


}