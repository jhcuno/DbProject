import java.sql.*;

public class Company{
	private String comp_id;
	private String company_name;
	private String company_street;
	private String company_city;
	private int company_address;
	private int company_zip_code;
	private String primary_sector;
	private String website;

	PreparedStatement pStmt;

	/**
	 * Company constructor.
	 * @param comp_id String compnay id
	 * @param company_name String company's name
	 * @param company_street String street name of comapany's address
	 * @param company_city String city of company's address
	 * @param company_address int street number of company's address
	 * @param company_zip_code int company's zip code
	 * @param primary_sector String business sector the company falls under
	 * @param website String company's website address
	**/
	public Company(	String comp_id, String company_name, String company_street, 
					String company_city, int company_address, int company_zip_code,
					String primary_sector, String website){
		this.comp_id = comp_id;
		this.company_name = company_name;
		this.company_street = company_street;
		this.company_city = company_city;
		this.company_address = company_address;
		this.company_zip_code = company_zip_code;
		this.primary_sector = primary_sector;
		this.website = website;
	}

	/**
	 * Adds the company to the database
	 * @param conn Connection connection needed to connect to the database
	**/
	public void addCompany(Connection conn) throws SQLException{
		pStmt = conn.prepareStatement("insert into company values(?,?,?,?,?,?,?,?)");
		pStmt.setString(1, this.comp_id);
		pStmt.setString(2, this.company_name);
		pStmt.setString(3, this.company_street);
		pStmt.setString(4, this.company_city);
		pStmt.setInt(5, this.company_address);
		pStmt.setInt(6, this.company_zip_code);
		pStmt.setString(7, this.primary_sector);
		pStmt.setString(8, this.website);
		pStmt.executeUpdate();
	}

	/**
	 * Removes the company from the database.
	 * conn Connection connection needed to connect to the database
	**/
	public void deleteCompany(Connection conn) throws SQLException{
		pStmt = conn.prepareStatement(	"delete from pays "+
										"WHERE comp_id = ?");
		pStmt.setString(1, this.comp_id);
		pStmt.executeUpdate();
		pStmt.clearParameters();

		pStmt = conn.prepareStatement(	"delete from company " + 
									 	"where comp_id = ?");
		pStmt.setString(1, this.comp_id);
		pStmt.executeUpdate();
	}

	/**
	 * Update the name of the company.
	 * @param conn Connection connection needed to connect to the database
	**/
	public void updateName(Connection conn, String new_name) throws SQLException{
		
			pStmt = conn.prepareStatement(	"UPDATE company "+
											"SET company_name = ? " +
											"WHERE comp_id = ?");
			pStmt.setString(1, new_name);
			pStmt.setString(2, this.comp_id);
			pStmt.executeUpdate();

			this.company_name = new_name;
	}

	/**
	 * Update the street of the company.
	 * @param conn Connection connection needed to connect to the database
	**/
	public void updateStreet(Connection conn, String new_street) throws SQLException{
		
			pStmt = conn.prepareStatement(	"UPDATE company "+
											"SET company_street = ? " +
											"WHERE comp_id = ?");
			pStmt.setString(1, new_street);
			pStmt.setString(2, this.comp_id);
			pStmt.executeUpdate();

			this.company_street = new_street;
	}

	/**
	 * Update the city of the company.
	 * @param conn Connection connection needed to connect to the database
	**/
	public void updateCity(Connection conn, String new_city) throws SQLException{
		
			pStmt = conn.prepareStatement(	"UPDATE company "+
											"SET company_city = ? " +
											"WHERE comp_id = ?");
			pStmt.setString(1, new_city);
			pStmt.setString(2, this.comp_id);
			pStmt.executeUpdate();

			this.company_city = new_city;
	}

	/**
	 * Update the address number of the company.
	 * @param conn Connection connection needed to connect to the database
	**/
	public void updateAddress(Connection conn, int new_address) throws SQLException{
		
			pStmt = conn.prepareStatement(	"UPDATE company "+
											"SET company_address = ? " +
											"WHERE comp_id = ?");
			pStmt.setInt(1, new_address);
			pStmt.setString(2, this.comp_id);
			pStmt.executeUpdate();

			this.company_address = new_address;
	}

	/**
	 * Update the zip code of the company.
	 * @param conn Connection connection needed to connect to the database
	**/
	public void updateZipCode(Connection conn, int new_zip_code) throws SQLException{
		
			pStmt = conn.prepareStatement(	"UPDATE company "+
											"SET company_zip_code = ? " +
											"WHERE comp_id = ?");
			pStmt.setInt(1, new_zip_code);
			pStmt.setString(2, this.comp_id);
			pStmt.executeUpdate();

			this.company_zip_code = new_zip_code;
	}

	/**
	 * Update the primary buisiness sector of the company.
	 * @param conn Connection connection needed to connect to the database
	**/
	public void updatePrimarySector(Connection conn, String new_prim_sect) throws SQLException{
		
			pStmt = conn.prepareStatement(	"UPDATE company "+
											"SET primary_sector = ? " +
											"WHERE comp_id = ?");
			pStmt.setString(1, new_prim_sect);
			pStmt.setString(2, this.comp_id);
			pStmt.executeUpdate();

			this.primary_sector = new_prim_sect;
	}

	/**
	 * Update the website of the company.
	 * @param conn Connection connection needed to connect to the database
	**/
	public void updateWebsite(Connection conn, String new_website) throws SQLException{
		
			pStmt = conn.prepareStatement(	"UPDATE company "+
											"SET website = ? " +
											"WHERE comp_id = ?");
			pStmt.setString(1, new_website);
			pStmt.setString(2, this.comp_id);
			pStmt.executeUpdate();

			this.website = new_website;
	}

	public String toString(){

		return ((	"Company ID: " + comp_id + 
					"\nName: " + company_name + 
					"\nAddress: " + company_address + " " + company_street + 
					", " + company_city + ", " + company_zip_code +
					"\nPrimary Sector: " + primary_sector +
					"\nWebsite: " + website
				));
	}


}