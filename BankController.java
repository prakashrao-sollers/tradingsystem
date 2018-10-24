package edu.sollers.javaprog.tradingsystem;


/**
 * 
 * @author rutpatel
 *
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BankController
 */
@WebServlet("/BankController")
public class BankController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BankController() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * init() method :- Initializes database connection
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try 
        {
			Class.forName("org.mariadb.jdbc.Driver");

			// conenction string
			String url = "jdbc:mariadb://localhost:3306/sollerstrading";
			conn = DriverManager.getConnection(url, "webuser", "Sollers@123");
        } catch (SQLException e) {
        	e.printStackTrace();
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
        }
      }
    /** 
     * destroy() method :- Closes database connection
     */
    public void destroy() {
    	try {
    		conn.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Storing Customer Input(Bank Info) values into Database and then Redirecting to AddCCInfo.html on successful execution of SQL Query
	 * Database Design :- bank (Database Table Name)
	 * 	#	Name	Type	
	 *	1	U_Id	varchar(10)-----Primary Key
	 *	2	B_Owner	tinytext		
	 *	3	B_Name	tinytext	
	 *	4	B_AccNo	tinytext		(NOTE: - Get Account No. as String because of leading zeroes)
	 *	5	B_RNo	tinytext		(NOTE: - Get Routing No. as String because of leading zeroes)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer u_id = (Integer)request.getSession().getAttribute("userId");
		String b_owner = request.getParameter("b_owner");
		String b_name = request.getParameter("b_name");
		String b_accno = request.getParameter("b_accno");
		String b_rno = request.getParameter("b_rno");
		
	    try
	    {
	    	Statement stmt = null;
		    
	    	stmt = conn.createStatement();
	        stmt.executeUpdate("INSERT INTO bank VALUES ("+u_id+",'"+b_owner+"','"+b_name+"','"+b_accno+"','"+b_rno+"')");
	    	
	        /*ResultSet rs = null;
	        rs = stmt.executeQuery("SELECT * FROM bank");
	    	while(rs.next())
	        {
	            System.out.println("Owner="+rs.getString("b_owner")+",Name="+rs.getString("b_name")+",AccNo="+rs.getString("b_accno")+",ABA="+rs.getString("b_rno"));
	        }*/
	    	
	        stmt.close();
	        
	        response.sendRedirect("AddCCInfo.jsp");
			return;
	     }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
}
