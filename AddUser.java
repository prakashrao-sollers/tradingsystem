package edu.sollers.javaprog.tradingsystem;
/**
 * @author Karanveer
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
 * Servlet implementation class AddUser
 */
@WebServlet(description = "Adds new user to database if all form fields are validated successfully", urlPatterns = { "/AddUser" })
public class AddUser extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection conn;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUser() {
	super();
    }

    /**
     * init. Initialized database connection
     */
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	try {
	    Class.forName("org.mariadb.jdbc.Driver");
	    
	    String url = "jdbc:mariadb://localhost:3306/sollerstrading";
	    // create a connection to the database
	    conn = DriverManager.getConnection(url, "webuser", "Sollers@123");

	    System.out.println("\nConnection made\n\n");
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
    }

    /** 
     * destroy method closes db connection
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
	response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     *  Username validation:
     *  
     *  1. Check for username uniqueness. Query for username in users table
     *  and if nothing is returned, username is unique.
     *  
     *  We can safely add username to db, add userId to session-> move on to "/addAccountInfo.jsp"
     *  
     *  
     *  2. Username is not unique. We redirect to addUser.jsp with a message attribute
     *  that the username is taken.
     *  
     * 
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String username = request.getParameter("uname");
	String password = request.getParameter("password");

	// initialize
	boolean uniqueUsername = true;
	Statement stmt = null;
	ResultSet rs   = null;

	try {
	    stmt  = conn.createStatement();
	    rs    = stmt.executeQuery("SELECT uname FROM login WHERE uname=\"" + username + "\";");
	    if (rs.next()) {
		uniqueUsername = false;
	    }
	} catch (SQLException e) {
	    e.printStackTrace(System.out);
	}

	if (!uniqueUsername) {
	    String message = "Username " + username + " is not available";
	    request.setAttribute("message", message);
	    request.getRequestDispatcher("/addUser.jsp").forward(request, response);
	}
	else {

	    // success!
	    //				
	    // 1) insert username / password into database
	    try {
		String insertQuery = "INSERT INTO login (uname, password) VALUES (\"" + username + "\", \"" + password + "\");"; 
		stmt = conn.createStatement();
		int rowCount   = stmt.executeUpdate(insertQuery);
		rs = stmt.executeQuery("SELECT LAST_INSERT_ID();");
		Integer lastInsertId = 0;
		if (rs.next()) {
		    lastInsertId = rs.getInt(1);
		}
		
		// make userId value available in current session
		request.getSession().setAttribute("userId", lastInsertId);
		
		// redirect to next page
		response.sendRedirect("/TradingSystem/addAccountInfo.jsp");
	    } catch (SQLException e) {
		e.printStackTrace(System.out);
	    }
	}

    }

}
