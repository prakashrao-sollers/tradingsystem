package edu.sollers.javaprog.tradingsystem;
/**
 * @author KK
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddFunds
 */
@WebServlet(description = "This method will add funds to user account", urlPatterns = { "/AddFunds" })
public class AddFunds extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddFunds() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			Class.forName("org.mariadb.jdbc.Driver");	    
		    String url = "jdbc:mariadb://localhost:3306/sollerstrading";
		    // create a connection to the database
		    conn = DriverManager.getConnection(url, "webuser", "Sollers@123");
		    System.out.println("\nConnection made\n\n");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes db connection
	 * 
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("All Good");
		//String un = request.getParameter("uname");
		String fundMethod = request.getParameter("fundSource");
		response.getWriter().print(" " + fundMethod);
		Integer fundAmt = Integer.parseInt(request.getParameter("amount"));
		response.getWriter().print(" " + fundAmt);
		Integer userId = (Integer) request.getSession().getAttribute("userId");
		//Integer userId = Integer.parseInt((String) request.getSession().getAttribute("userId"));
		response.getWriter().print(" " + userId);
		System.out.println(fundMethod + " " + fundAmt + " " + userId);
		try {
			// db parameters
//	            Class.forName("com.mysql.cj.jdbc.Driver");
//	            // create a connection to the database
//	            Connection conn = DriverManager.getConnection("jdbc:mysql:"
//	    	    		+ "//localhost/testdb","root","");
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT side, size FROM positions WHERE account_id = " + userId 
					+ " AND symbol = 'MONEY';");
			if (rs.next()) {
				System.out.println("MONEY position exists");
				stmt.executeUpdate("UPDATE positions SET size = size + " + fundAmt + 
						" WHERE account_id = " + userId + " AND symbol = 'MONEY';");
			} else {
				System.out.println("MONEY position exists");
				stmt.executeUpdate("INSERT INTO positions " +
			"(account_id, side, size, price, symbol, is_open, creation_date) " +
						"VALUES (" + userId + ", 1, " + fundAmt + ", 1.0, 'MONEY', 1, '" + fmt.format(new Date()) + "');");
			}

			// loop through the result set

			stmt.close();
			response.sendRedirect("accountHome.jsp");
			//conn.close();

		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}

//		if(un.equals(storedPassword))
//		{
//			response.sendRedirect("success.html");
//    		//response.getWriter().append("<p>").append("success").append("</p>");
//			return;
//		}
//		else
//		{
//    		//response.getWriter().append("<p>").append("error").append("</p>");
//			response.sendRedirect("error.html");
//			return;
//		}
	}

}
