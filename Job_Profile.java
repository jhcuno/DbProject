import java.sql.*;

/**
 * 
**/
public class Job_Profile {
	private String jp_code;
	private String job_title;
	private String description;
	private String avg_pay;
	PreparedStatement pStmt;

	/**
	 * Contructor.
	 * @param jp_code String with jp_code value
	 * @param job_title String job title for the job profile
	 * @param description String description brief description of the job
	 * @param avg_pay String listing average pay of the position
	**/
	public Job_Profile(String jp_code, String job_title, String description, String avg_pay){
		this.jp_code = jp_code;
		this.job_title = job_title;
		this.description = description;
		this.avg_pay = avg_pay;
	}
	/**
	 * Adds the jobProfile to the database
	 * @param conn Connection connection to the needed database
	**/
	public void addJobProfile(Connection conn) throws SQLException{
		pStmt = conn.prepareStatement("insert into job_profile values(?,?,?,?)");
		pStmt.setString(1, this.jp_code);
		pStmt.setString(2, this.job_title);
		pStmt.setString(3, this.description);
		pStmt.setString(4, this.avg_pay);
		pStmt.executeUpdate();
	}
	/**
	 * delete the job profile from the database
	 * @param conn Connection connection to the needed database
	**/
	public void deleteJobProfile(Connection conn) throws SQLException{
		pStmt = conn.prepareStatement(	"delete from job_profile " + 
									 	"where jp_code = ?");
		pStmt.setString(1, this.jp_code);
		pStmt.executeUpdate();
	}

	public String toString(){
		return (("jp_code: " + this.jp_code + "\njob_title: " + this.job_title +
					"\ndescription: " + this.description + "\navg_pay: " + this.avg_pay));
	}
}