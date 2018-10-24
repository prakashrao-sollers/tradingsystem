package edu.sollers.javaprog.tradingsystem;
/**
 * @author Sowmya, Karanveer
 */

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddPersonalInfo
 */
@WebServlet("/AddPersonalInfo")
public class AddAccountInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection conn;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddAccountInfo() {
	super();
	// TODO Auto-generated constructor stub
    }

    /**
     * init. Initialized database connection
     */
    public void init(ServletConfig config) throws ServletException 
    {
	super.init(config);
	try {
	    Class.forName("org.mariadb.jdbc.Driver");

	    // conenction string
	    String url = "jdbc:mariadb://localhost:3306/sollerstrading";
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
	// TODO Auto-generated method stub
	response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	Integer userId = (Integer) request.getSession().getAttribute("userId");

	System.out.println("User Id in session: " + userId);


	String firstname = request.getParameter("fName");
	String lastname = request.getParameter("lName");
	String birthdate = request.getParameter("dob");
	String social = request.getParameter("ssn");
	String email = request.getParameter("email");
	
	LocalDate dob = LocalDate.parse(birthdate);
	LocalDate now = LocalDate.now();
	
	if (now.minusYears(18).getYear() < dob.getYear()) {
	    deleteUserFromLoginTable(userId);
	    String message = "User is a minor";
	    request.setAttribute("message", message);
	    request.getRequestDispatcher("/addUser.jsp").forward(request, response);
	}
	else {
	    try {
		Statement stmt = conn.createStatement();
		int affectedrows = stmt.executeUpdate("INSERT INTO accounts (id, first_name, last_name, dob, ssn, email) VALUES (\"" + userId + "\", \""  + firstname + "\", \"" + lastname + "\", \"" + dob + "\", \"" + social + "\", \"" + email + "\");");
		if (affectedrows == 1)
		    System.out.print("Your record has been successfully inserted");
	    } catch (SQLException e) {
		e.printStackTrace();
	    }

	    request.getSession().setAttribute("userId", userId);
	    response.sendRedirect("/TradingSystem/addAddress.jsp");
	}
    }
    
    
    
    private void deleteUserFromLoginTable(Integer userId) {
	try {
	    Statement stmt = conn.createStatement();
	    int affectedRows = stmt.executeUpdate("DELETE FROM login WHERE id=\"" + userId + "\";");
	    if (affectedRows == 1) System.out.println("User id: " + userId + " deleted from login");
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

}
