package edu.sollers.javaprog.tradingsystem;
/**
 * @author Karanveer
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ClosePosition
 */
@WebServlet("/ClosePosition")
public class ClosePosition extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection conn;
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClosePosition() {
	super();
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	try {
	    Class.forName("org.mariadb.jdbc.Driver");

	    String url = "jdbc:mariadb://localhost:3306/sollerstrading";
	    // create connection to db
	    conn = DriverManager.getConnection(url, "webuser", "Sollers@123");

	    System.out.println("\nConnection made\n\n");
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    /**
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
     * This method retrieves all open positions for a given user id
     * @author Karanveer
     * @param userId of user in session
     * @return ArrayList of all open positions or empty array list if no positions
     */
    private ArrayList<Position> getOpenPositionsForAccount(int userId) {
	ArrayList<Position> positions = new ArrayList<>();
	try {
	    // Build array list of positions
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT symbol, id, side, size, price, creation_date FROM positions WHERE account_id=\"" + userId + "\" AND is_open=\"1\";");
	    while (rs.next()) {

		String symbol = rs.getString(1);
		// MONEY position will NOT be included in array list
		if (symbol.equals("MONEY")) {
		    continue;
		}
		int positionId = rs.getInt(2);
		int    side    = rs.getInt(3);
		double size    = rs.getDouble(4);
		double price   = rs.getDouble(5);
		Date date      = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(6));

		Position p = new Position(positionId, symbol, userId, side, size, price, date);
		positions.add(p);
		System.out.println(p.toString());
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return positions;
    }
    
    /**
     * The get method:
     * 
     * 1) Gets all open positions
     * 
     * 2) Prints the html page showing positions with radio buttons to select one and submit.
     * 
     * If no position exists, the user is notified and a link is shown for createOrder.jsp
     * 
     * If positions exist, then one of them must have been submitted to this servlet's post method.
     * @author Karanveer
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	// TODO get userid from session
	Integer userId = (Integer)request.getSession().getAttribute("userId");
//	Integer userId = 2; // John Doe
	ArrayList<Position> positions = getOpenPositionsForAccount(userId);

	// For this user_id, print list of positions
	response.setContentType("text/html");
	PrintWriter writer = response.getWriter();
	writer.println("<!DOCTYPE html>");
	writer.println("<html>");
	writer.println("<head>");
	writer.println("<title>" + "Sollers Trading System - Close Position" + "</title></head>");
	writer.println(Helper.getCSS()); // grab the css from Helper class
	writer.println("<body>");
	
	if (positions.isEmpty()) {
	    writer.println("<p>No positions found</p>");
	    writer.println("<br><br>");
	    writer.println("<ul class=\"navList\">");
	    writer.println("<li class=\"navItem\"><a href=\"createOrder.jsp\">New Trade</a></li>");
	    writer.println("</ul>");
	}
	else {
	    writer.println("<h3>Open Positions</h3>");
	    writer.println("<form name=\"positionform\" method=\"post\">");
	    for (Position p: positions) {
		writer.println("<input type=\"radio\" name=\"openPosition\" value=\"" + p.getPositionId() + "\" required>");
		writer.println(p.toString() + "<br>");
	    }
	    writer.println("<br><input type=\"submit\" name=\"submit\" value=\"submit\">  <br>");
	    writer.println("</form>");
	}

	writer.println("</body>");
	writer.println("</html>");
    }

    /**
     * This post method receives the value of the 
     * chosen position's radio button which gives the
     * position id.
     * That id is selected from the database, its contents
     * set as attributes and redirected to the createOrder.jsp page. 
     * @author Karanveer
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	// get value of radio button
	int positionId = Integer.parseInt(request.getParameter("openPosition"));
	
	try {
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT symbol, account_id, side, size, price, creation_date "
	    	+ "FROM positions WHERE id=\"" + positionId + "\";");
	    rs.next();
	    String symbol = rs.getString(1);
	    Integer accId = rs.getInt(2);
	    int side = rs.getInt(3);
	    double size = rs.getDouble(4);
	    double price = rs.getDouble(5);
	    Date creationDate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(6));
	    
	    // create position object
	    Position position = new Position(positionId, symbol, accId, side, size, price, creationDate);
	    
	    // send position object as attribute to send to createOrder.jsp
	    request.setAttribute("position", position);
	    request.setAttribute("symbol", symbol);
	    request.setAttribute("side", side);
	    request.setAttribute("size", size);
	    request.getSession().setAttribute("userId", accId);
	    
	    request.getRequestDispatcher("/createOrder.jsp").forward(request, response);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
