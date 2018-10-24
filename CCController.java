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
 * Servlet implementation class CCController
 */
@WebServlet("/CCController")
public class CCController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CCController() {
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
	 * Storing Customer Input(Credit Card Info) values into Database and then Redirecting to another page on successful execution of SQL Query.
	 * Database Design :- creditcard (Database Table Name)
	 * #	Name	Type	
	 * 1	U_Id	varchar(10)---------Primary Key
	 * 2	C_Owner	tinytext	
	 * 3	C_Type	tinytext	
	 * 4	C_CCNo	tinytext			(NOTE: - Get Credit Card No. as String because of leading zeroes)
	 * 5	C_ExpM	tinyint(2)			
	 * 6	C_ExpY	smallint(4)			
	 * 7	C_CVVNo	smallint(4)
	*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer u_id = (Integer)request.getSession().getAttribute("userId");
		String c_owner = request.getParameter("c_owner");
		String c_type = request.getParameter("c_type");
		String c_ccno = request.getParameter("c_ccno");
		String c_expm = request.getParameter("c_expm");
		String c_expy = request.getParameter("c_expy");
		String c_cvvno = request.getParameter("c_cvvno");
		
	    try
	    {
	    	Statement stmt = null;
		    
	    	stmt = conn.createStatement();
	        stmt.executeUpdate("INSERT INTO creditcard VALUES ("+u_id+",'"+c_owner+"','"+c_type+"','"+c_ccno+"',"+c_expm+","+c_expy+","+c_cvvno+")");
	    	
	        /*ResultSet rs = null;
	        rs = stmt.executeQuery("SELECT * FROM bank");
	    	while(rs.next())
	        {
	            System.out.println("Owner="+rs.getString("b_owner")+",Name="+rs.getString("b_name")+",AccNo="+rs.getString("b_accno")+",ABA="+rs.getString("b_rno"));
	        }*/
	        
	        System.out.println("Success adding values to CreditCard table");
	        
	        stmt.close();
	        response.sendRedirect("accountHome.jsp");
	        
	        //response.sendRedirect("AddCCInfo.html");
			//return;
	     }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
}
