/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.contacteditor;

import java.sql.*;
//import java.util.*;
/**
 *
 * @author joshcotogno
 */
public class Queries{
	
	public Queries(){

	}

	public static ResultSet query1(String companyID, Connection conn){
		try{
			String sqlStatement = 	"SELECT name " +
									"FROM works_job NATURAL JOIN pays NATURAL JOIN company NATURAL JOIN person " +
									"WHERE comp_id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, companyID);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query2(String companyID, Connection conn){
		try{
			String sqlStatement = 	"SELECT name, pay_rate "+
									"FROM works_job NATURAL JOIN pays NATURAL JOIN company NATURAL JOIN person NATURAL JOIN job " +
									"WHERE comp_id = ? "+
									"ORDER BY pay_rate DESC";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, companyID);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query3(Connection conn){
		try{
			String sqlStatement = 	"WITH salary AS( " +
									  "SELECT comp_id, SUM(pay_rate) AS payed " +
									  "FROM pays NATURAL JOIN job " +
									  "WHERE pay_type = 'salary' " +
									  "GROUP BY comp_id " +
									"), " +
									"wageWorker AS( " +
									  "SELECT comp_id, SUM(pay_rate * 1920) AS payed " +
									  "FROM pays NATURAL JOIN job " +
									  "WHERE pay_type = 'wage' " +
									  "GROUP BY comp_id " +
									"), " +
									"totalCost AS( " +
									  "SELECT * " + 
									  "FROM salary " +
									  "UNION " +
									  "SELECT * " +
									  "FROM wageWorker " +
									") " +
									"SELECT comp_id, SUM(payed) AS amount_payed " +
									"FROM totalCost " +
									"GROUP BY comp_id " +
									"ORDER BY amount_payed DESC";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query4(String name, Connection conn){
		try{
			String sqlStatement = 	"(SELECT job_title "+
									"FROM person NATURAL JOIN works_job NATURAL JOIN job NATURAL JOIN Job_Profile "+
									"WHERE name = ? ) "+
									"UNION "+
									"(SELECT job_title "+
									"FROM person NATURAL JOIN has_worked NATURAL JOIN job NATURAL JOIN Job_Profile "+
									"WHERE name = ? )";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, name);
			pStmt.setString(2, name);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}


	public static ResultSet query5(String name, Connection conn){
		try{
			String sqlStatement = 	"SELECT name, skill_title " +
									"FROM person NATURAL JOIN has_skill NATURAL JOIN knowledge_skill "+
									"WHERE name = ?";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, name);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query6(String name, Connection conn){
		try{
			String sqlStatement = 	"SELECT name, ks_code "+
									"FROM ( "+
									  /*skills required for worker's current job */
									  "(SELECT name, ks_code "+
									  "FROM person NATURAL JOIN works_job NATURAL JOIN job NATURAL JOIN requires "+
									  "WHERE name = ?) "+
									  "MINUS "+
									  /* skills person actually has*/
									  "(SELECT name, ks_code "+
									  "FROM person NATURAL JOIN has_skill "+
									  "WHERE name = ?) "+
									 ")";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, name);
			pStmt.setString(2, name);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query7(String jp_code, Connection conn){
		try{
			String sqlStatement = 	"SELECT skill_title " +
									"FROM requires NATURAL JOIN knowledge_skill " + 
									"WHERE jp_code = ?";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query8(String jp_code, String name, Connection conn){
		try{
			String sqlStatement = 	"SELECT skill_title "+
									"FROM (( "+
										"(SELECT ks_code "+
									  	"FROM job_profile NATURAL JOIN requires "+
									  	"WHERE jp_code = ?) "+
									 		"MINUS "+
									  	"(SELECT ks_code "+
									  	"FROM person NATURAL JOIN has_skill "+
									  	"WHERE name = ?)) "+
									"NATURAL JOIN knowledge_skill )";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);
			pStmt.setString(2, name);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query9(String jp_code, String name, Connection conn){
		try{
			String sqlStatement = 	"SELECT DISTINCT c_code, course_title " +
									"FROM course C " +
									"WHERE NOT EXISTS ((	SELECT ks_code " +
									  					"FROM job_profile NATURAL JOIN requires " +
									  					"WHERE jp_code = ?) " +
									 			"MINUS " + 
									  				"(	SELECT ks_code " +
									  					"FROM person NATURAL JOIN has_skill " +
									  					"WHERE name = ?) " +
									  			"MINUS " +
									  				"(	SELECT ks_code " +
									  					"FROM covers S " +
									  					"WHERE C.c_code = S.c_code) " +
									  				")";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);
			pStmt.setString(2, name);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query10(String jp_code, String name, Connection conn){
		try{
			String sqlStatement = 	"SELECT * "+
									"FROM ( "+
										"SELECT DISTINCT course_title, sec_no, complete_date " +
										"FROM course NATURAL JOIN section NATURAL JOIN covers " +
										"WHERE NOT EXISTS ((	SELECT ks_code " +
										  					"FROM job_profile NATURAL JOIN requires " +
										  					"WHERE jp_code = ?) " +
										 			"MINUS " + 
										  				"(	SELECT ks_code " +
										  					"FROM person NATURAL JOIN has_skill " +
										  					"WHERE name = ?) " +
										  			"MINUS " +
										  				"(	SELECT ks_code " +
										  					"FROM covers S " +
										  					"WHERE c_code = S.c_code) " +
										  				") "+
										  			"ORDER BY complete_date ASC) "+
									"WHERE ROWNUM <=1";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);
			pStmt.setString(2, name);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	public static ResultSet query12(String jp_code, String per_id, Connection conn){
		try{
			String sqlStatement = 	"CREATE OR REPLACE TABLE courseSet( " +
								    "csetID      number(8,0), " +
								    "c_code1     number(6,0), " +
								    "c_code2     number(6,0), " +
								    "c_code3     number(6,0), " +
								    "sizeCS      number(2,0), " +
								    "primary key(csetID) " +
								    ")";
                        PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
                        pStmt.executeQuery();
			sqlStatement =                              "INSERT INTO CourseSet " +
								        "SELECT CourseSet_seq.NEXTVAL, C1.c_code, C2.c_code, null, 2  " +
								        "FROM Course C1, Course C2 " +
								        "WHERE C1.c_code < C2.c_code ";
                        pStmt = conn.prepareStatement(sqlStatement);   
                        pStmt.executeQuery();
			sqlStatement = 				"INSERT INTO CourseSet " +
								    "SELECT CourseSet_seq.NEXTVAL, C1.c_code, C2.c_code, C3.c_code, 3 " +
								    "FROM Course C1, Course C2, Course C3 " +
								    "WHERE C1.c_code < C2.c_code AND C2.c_code < C3.c_code ";
                        pStmt = conn.prepareStatement(sqlStatement);
                        pStmt.executeQuery();
			sqlStatement =					"WITH missSk AS( " +
								    "SELECT ks_code " +
								    "FROM requires " +
								    "WHERE jp_code = ? " +
								    "MINUS " +
								    "SELECT ks_code " +
								    "FROM has_skill " +
								    "WHERE per_id = ? " +
								"), " +
								"CourseSet_Skill(csetID, ks_code) AS ( " +
								    "SELECT csetID, ks_code " +
								    "FROM CourseSet CSet JOIN covers CS ON CSet.c_code1=CS.c_code " +
								    "UNION " +
								    "SELECT csetID, ks_code " +
								    "FROM CourseSet CSet JOIN covers CS ON CSet.c_code2=CS.c_code " +
								    "UNION " +
								    "SELECT csetID, ks_code " +
								    "FROM CourseSet CSet JOIN covers CS ON CSet.c_code3=CS.c_code " +
								"), " +
								"Cover_CSet(csetID, sizeCS) AS (" +
								    "SELECT csetID, sizeCS " +
								    "FROM CourseSet CSet " +
								    "WHERE NOT EXISTS ( " +
								        "SELECT ks_code " +
								        "FROM missSk " +
								        "MINUS " +
								        "SELECT ks_code " +
								        "FROM CourseSet_Skill CSSk WHERE CSSk.csetID = Cset.csetID) " +
								" )" +
								"SELECT c_code1, c_code2, c_code3 " +
								"FROM Cover_CSet NATURAL JOIN CourseSet " +
								"WHERE sizeCS = (SELECT MIN(sizeCS) FROM Cover_CSet)";
			pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);
			pStmt.setString(2, per_id);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}
        public static ResultSet query11(String jp_code, String name, Connection conn){
		try{
			String sqlStatement = 	"WITH courses AS( " +
									  "SELECT course_title, retail_price " +
									  "FROM course C " +
									  "WHERE NOT EXISTS ((	SELECT ks_code " +
									    					"FROM job_profile NATURAL JOIN requires " +
									    					"WHERE jp_code = ?) " +
									   			"MINUS " +
									    				"(	SELECT ks_code " +
									    					"FROM person NATURAL JOIN has_skill " +
									    					"WHERE name = ?) " +
									    			"MINUS " +
									    				"(	SELECT ks_code " +
									    					"FROM covers S " +
									    					"WHERE C.c_code = S.c_code) " +
									    				") " +
									") " + 
									"SELECT course_title, retail_price " +
									"FROM courses " +
									"WHERE retail_price = (SELECT MIN(retail_price) " +
									                      "FROM courses)";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);
			pStmt.setString(2, name);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}


	public static ResultSet query13(String per_id, Connection conn){
		try{
			String sqlStatement = 	"SELECT J.job_title " +
									"FROM job_profile J " +
									"WHERE NOT EXISTS( " +
									  "(SELECT ks_code " +
									  "FROM requires R " +
									  "WHERE J.jp_code = R.jp_code " +
									  ") " +
									  "MINUS " +
									  "(SELECT ks_code " +
									  "FROM has_skill " +
									  "WHERE per_id = ? " +
									  "))";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, per_id);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static ResultSet query14(String per_id, Connection conn){
		try{
			String sqlStatement = 	"SELECT * "+
									"FROM ( "+
										"SELECT J.job_title, J.avg_pay " +
										"FROM job_profile J " +
										"WHERE NOT EXISTS( " +
										  "(SELECT ks_code " +
										  "FROM requires NATURAL JOIN job_profile " +
										  "WHERE J.jp_code = jp_code " +
										  ") " +
										  "MINUS " +
										  "(SELECT ks_code " +
										  "FROM has_skill " +
										  "WHERE per_id = ? " +
										  ")) "+
										  "ORDER BY J.avg_pay DESC) "+
									"WHERE ROWNUM <=1";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, per_id);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
	}


	public static ResultSet query15(String jp_code, Connection conn){
		try{
			String sqlStatement = 	"SELECT P.name, P.email " +
									"FROM person P " +
									"WHERE NOT EXISTS( " +
									  "(SELECT ks_code " +
									  "FROM requires NATURAL JOIN job_profile " +
									  "WHERE jp_code = ? " +
									  ") " +
									  "MINUS " +
									  "(SELECT ks_code " +
									  "FROM has_skill NATURAL JOIN person " +
									  "WHERE per_id = P.per_id " +
									  "))"; 
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static ResultSet query16(String jp_code, Connection conn){
		try{
			String sqlStatement = 	"SELECT P.name " +
									"FROM person P " +
									"WHERE 1 = " +
									  "(SELECT COUNT (ks_code) " +
									  "FROM (( SELECT ks_code "+
									  		"FROM requires NATURAL JOIN job_profile " +
									  		"WHERE jp_code = ? " +
									  		") " +
									  		"MINUS " +
									  		"(SELECT ks_code " +
									  		"FROM has_skill NATURAL JOIN person " +
									  		"WHERE per_id = P.per_id " +
									  ")))"; 
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static ResultSet query17(String jp_code, Connection conn){
		try{
			String sqlStatement = 	"WITH perReqSkillCnt(per_id, skillCnt) AS( " +
									    "SELECT per_id, COUNT(ks_code) " +
									    "FROM has_skill NATURAL JOIN requires " +
									    "WHERE jp_code = ? " +
									    "GROUP BY per_id), " +
									/* calculates the # of required skills*/
									"reqSkillCnt(cnt) AS( " +
									    "SELECT COUNT(*) " +
									    "FROM requires " +
									    "WHERE jp_code = ?), " +
									/*returns the per_id of those needing only 1 additional skill */
									"missingOneSk(per_id) AS( " +
									  "SELECT per_id " +
									  "FROM perReqSkillCnt, reqSkillCnt " +
									  "WHERE skillCnt = cnt - 1) " +
									"SELECT ks_code, COUNT(*) AS people " +
									"FROM knowledge_skill, missingOneSk M " +
									"WHERE ks_code = ( " +
									                "SELECT ks_code " +
									                "FROM requires " +
									                "WHERE jp_code = ? " +
									                "MINUS " +
									                "SELECT H.ks_code " +
									                "FROM has_skill H " +
									                "WHERE H.per_id = M.per_id " +
									                ") " +
									"GROUP BY ks_code " +
									"ORDER BY people ASC";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);
			pStmt.setString(2, jp_code);
			pStmt.setString(3, jp_code);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query18(String jp_code, Connection conn){
		try{
			String sqlStatement = 	"WITH perReqSkillCnt(per_id, skillCnt) AS( " +
									  "SELECT per_id, COUNT(ks_code) " +
									  "FROM has_skill NATURAL JOIN requires " +
									  "WHERE jp_code = ? " +
									  "GROUP BY per_id), " +
									/* calculates the # of required skills*/
									"reqSkillCnt(cnt) AS( " +
									  "SELECT COUNT(*) " +
									  "FROM requires " +
									  "WHERE jp_code = ?) " +
									"SELECT MAX(skillCnt) AS cnt, per_id " +
									"FROM perReqSkillCnt, reqSkillCnt " +
									"WHERE skillCnt = (" +
										"SELECT MAX(skillCnt) "+
										"FROM perReqSkillCnt, reqSkillCnt) "+
									"GROUP BY per_id"; 
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);
			pStmt.setString(2, jp_code);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query19(String jp_code, String min_sk_missing, Connection conn){
		try{
			String sqlStatement = 	"WITH perReqSkillCnt(per_id, skillCnt) AS( " +
									  "SELECT per_id, COUNT(ks_code) " +
									  "FROM has_skill NATURAL JOIN requires " +
									  "WHERE jp_code = ? " +
									  "GROUP BY per_id), " +
									/* calculates the # of required skills*/
									"reqSkillCnt(cnt) AS( " +
									  "SELECT COUNT(*) " +
									  "FROM requires " +
									  "WHERE jp_code = ?) " +
									"SELECT cnt - skillCnt Skills_Missing, per_id " +
									"FROM perReqSkillCnt, reqSkillCnt " +
									"WHERE skillCnt > cnt - ? " +
									"ORDER BY skillCnt ASC"; 
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);
			pStmt.setString(2, jp_code);
                        pStmt.setString(3, min_sk_missing);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query21(String jp_code, Connection conn){
		try{
			String sqlStatement = 	"SELECT per_id, name " +
									"FROM person NATURAL JOIN has_worked " +
									"WHERE jp_code = ? ";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query22(String jp_code, Connection conn){
		try{
			String sqlStatement = 	"((SELECT per_id " +
									"FROM person) " +
									"MINUS "+
									"(SELECT per_id "+
									"FROM works_job)) "+
									"INTERSECT "+
									"(SELECT per_id "+
									"FROM has_worked NATURAL JOIN job "+
									"WHERE jp_code = ? )";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, jp_code);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	public static ResultSet query23(Connection conn){
		try{
			String sqlStatement = 	"WITH companyEmployeeCount (comp_id, empCount) AS( " +
									  "SELECT comp_id, COUNT(comp_id) " +
									  "FROM pays " +
									  "GROUP BY comp_id) " +
									"SELECT MAX(empCount) cnt, comp_id " +
									"FROM companyEmployeeCount " +
									"GROUP BY comp_id";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	public static ResultSet query24(Connection conn){
		try{
			String sqlStatement = 	"SELECT * " +
									"FROM ( " +
										"WITH company_job_totals (company_name, empCount) AS ( "+
											"SELECT company, COUNT(job_code) "+
											"FROM job j "+
											"GROUP BY company "+
										") "+
										"SELECT primary_sector, empCount "+
										"FROM company NATURAL JOIN company_job_totals "+
										"ORDER BY empCount DESC) "+
									"WHERE ROWNUM <=1";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			
			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}
        public static ResultSet query25(Connection conn){
		try{
			String sqlStatement = 	"SELECT pos/neg AS ratio " +
									"FROM " +
									    "(SELECT COUNT(*) AS pos " +
									    "FROM job " +
									    "WHERE salary_change > 0), " +
									    "(SELECT COUNT(*) AS neg " +
									    "FROM job " +
									    "WHERE salary_change < 0) "; 
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			
			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}
        
        public static ResultSet query26(Connection conn){
		try{
			String sqlStatement = 	"WITH joblessPer(per_id) AS( " +
									    "SELECT per_id " +
									    "FROM person " +
									    "MINUS " +
									    "SELECT per_id " +
									    "FROM works_job " +
									"), " +
									"openJobs AS( " +
									    "SELECT job_code " +
									    "FROM job " +
									    "MINUS " +
									    "SELECT job_code " +
									    "FROM works_job " +
									"), " +
									"openJobsAdd AS( " +
									  "SELECT job_code, jp_code " +
									  "FROM openJobs JOIN job USING (job_code) " +
									"), " +
									"qualified(per_id, jp_code) AS( " +
									  "SELECT DISTINCT per_id, jp_code " +
									  "FROM openJobsAdd O, joblessPer J " +
									  "WHERE NOT EXISTS( " +
									    "SELECT ks_code " +
									    "FROM requires NATURAL JOIN openJobsAdd " +
									    "WHERE job_code = O.job_code " +
									    "MINUS " +
									    "SELECT ks_code " +
									    "FROM has_skill NATURAL JOIN joblessPer " +
									    "where per_id = J.per_id " +
									    ") " +
									"), " +
									"numOpeningsByPos AS( " +
									  "SELECT COUNT(job_code) AS numOpenPos, jp_code " +
									  "FROM openJobsAdd " +
									  "GROUP BY jp_code " +
									"), " +
									"numQualifiedPeople AS( " +
									  "SELECT COUNT(Distinct per_id) as numQualPeople, jp_code " +
									  "FROM qualified NATURAL JOIN job " +
									  "GROUP BY jp_code " +
									") " +
									"SELECT A.jp_code, A.numOpenPos - B.numQualPeople people" +
									"FROM numOpeningsByPos A, numQualifiedPeople B " +
									"WHERE A.jp_code = B.jp_code";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			
			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}

	public static ResultSet query27(Connection conn){
		try{
			String sqlStatement = 	"WITH joblessPer(per_id) AS( " +
									    "SELECT per_id " +
									    "FROM person " +
									    "MINUS " +
									    "SELECT per_id " +
									    "FROM works_job " +
									"), " +
									"openJobs AS( " +
									    "SELECT job_code " +
									    "FROM job " +
									    "MINUS " +
									    "SELECT job_code " +
									    "FROM works_job " +
									"), " +
									"openJobsAdd AS( " +
									  "SELECT job_code, jp_code " +
									  "FROM openJobs JOIN job USING (job_code) " +
									"), " +
									"qualified(per_id, jp_code) AS( " +
									  "SELECT DISTINCT per_id, jp_code " +
									  "FROM openJobsAdd O, joblessPer J " +
									  "WHERE NOT EXISTS( " +
									    "SELECT ks_code " +
									    "FROM requires NATURAL JOIN openJobsAdd " +
									    "WHERE job_code = O.job_code " +
									    "MINUS " +
									    "SELECT ks_code " +
									    "FROM has_skill NATURAL JOIN joblessPer " +
									    "where per_id = J.per_id " +
									    ") " +
									"), " +
									"numOpeningsByPos AS( " +
									  "SELECT COUNT(job_code) AS numOpenPos, jp_code " +
									  "FROM openJobsAdd " +
									  "GROUP BY jp_code " +
									"), " +
									"numQualifiedPeople AS( " +
									  "SELECT COUNT(Distinct per_id) as numQualPeople, jp_code " +
									  "FROM qualified NATURAL JOIN job " +
									  "GROUP BY jp_code " +
									"), " +
									"maxJobPos (value) AS ( " +
									  "SELECT MAX(A.numOpenPos - B.numQualPeople) " +
									  "FROM numOpeningsByPos A, numQualifiedPeople B " +
									  "WHERE A.jp_code = B.jp_code " +
									"), " +
									"availPos (jp_code, difference ) AS( " +
									  "SELECT A.jp_code , A.numOpenPos - B.numQualPeople " +
									  "FROM numOpeningsByPos A, numQualifiedPeople B " +
									  "WHERE A.jp_code = B.jp_code " +
									"), " +
									"maxPos AS( " +
									  "SELECT jp_code " +
									  "FROM availPos, maxJobPos " +
									  "WHERE availPos.difference = maxJobPos.value " +
									"), " +
									"skRequired AS( " +
									  "SELECT ks_code " +
									  "FROM maxPos NATURAL JOIN requires " +
									") " +
									"SELECT c_code " +
									"FROM skRequired NATURAL JOIN covers";


			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}
        public static ResultSet query28(String primary_sector, Connection conn){
		try{
			String sqlStatement = 	"WITH daysWorked AS( " +
									    "SELECT sysdate - date_started diff, pay_type, job_code, salary_change, pay_rate " +
									    "FROM job NATURAL JOIN pays NATURAL JOIN company " +
									    "WHERE primary_sector = ? " +
									"), " +
									"avgChangePersonSalary AS( " +
									    "SELECT salary_change/diff AS finalDiff, job_code " +
									    "FROM daysWorked " +
									    "WHERE pay_type = 'salary' " +
									"), " +
									"wageToSalary AS( " +
									    "SELECT diff * 5 * pay_rate currentMaking, diff * 5 * (pay_rate - salary_change) startedMaking, job_code, diff " +
									    "FROM daysWorked " +
									    "WHERE pay_type = 'wage' " +
									"), " +
									"wageDifference AS( " +
									    "SELECT currentMaking - startedMaking wageDiff, diff, job_code " +
									    "FROM wageToSalary " +
									"), " +
									"avgChangePersonWage AS ( " +
									    "SELECT wageDiff/diff as finalDiff, job_code " +
									    "FROM wageDifference " +
									"), " +
									"combineChangePerson AS ( " +
									    "SELECT * " +
									    "FROM avgChangePersonWage " +
									    "UNION " +
									    "SELECT * " +
									    "FROM avgChangePersonSalary " +
									") " +
									"SELECT SUM(finalDiff) / COUNT(*) AS answer "+
									"FROM combineChangePerson";
			PreparedStatement pStmt = conn.prepareStatement(sqlStatement);
			pStmt.setString(1, primary_sector);

			return pStmt.executeQuery();
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}
        
        

}
