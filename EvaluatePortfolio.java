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
 * Servlet implementation class EvaluatePortfolio
 */
@WebServlet("/EvaluatePortfolio")
public class EvaluatePortfolio extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection conn;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EvaluatePortfolio() {
	super();
	// TODO Auto-generated constructor stub
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
     * This method retrieves all open positions for a given user id
     * 
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
     * Method to retrieve the information for a stock from 
     * the database matching the input parameter ticker.
     * 
     * @param ticker String symbol for stock
     * @return stock object if it was found, else null
     * @author Karanveer
     */
    private Stock getStock(String ticker) {
	Stock currentStock = null;
	try {
	    Statement stmt = conn.createStatement();
	    ResultSet rs   = stmt.executeQuery("SELECT * FROM stocks WHERE ticker=\"" + ticker + "\";");
	    if (rs.next()) {
		String fullName = rs.getString("full_name");
		double bid 	= rs.getDouble("bid");
		double ask	= rs.getDouble("ask");
		double last	= rs.getDouble("last");
		currentStock = new Stock(ticker, fullName, bid, ask, last);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return currentStock;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	Integer userId = (Integer) request.getSession().getAttribute("userId");
	
	ArrayList<Position> positions = getOpenPositionsForAccount(userId);
	
	response.setContentType("text/html");
	PrintWriter writer = response.getWriter();
	writer.println("<!DOCTYPE html>");
	writer.println("<html>");
	writer.println("<head>");
	writer.println("<title>" + "Sollers Trading System - Evaluated Portfolio" + "</title></head>");
	writer.println(Helper.getCSS());
	writer.println("<body>");
	writer.println("<h2>Evaluated Portfolio</h2>");
	
	
	if (positions.isEmpty()) {
	    writer.println("<p>No open positions to evaluate for account.</p>");
	}
	else {
	    
	    // build table
	    writer.println("<div class=\"rTable\">");
	    writer.println("<div class=\"rTableRow\">");
	    writer.println("<div class=\"rTableHead\"><strong>Symbol</strong></div>");
	    writer.println("<div class=\"rTableHead\"><strong>Description</strong></div>");
	    writer.println("<div class=\"rTableHead\"><strong>Quantity</strong></div>");
	    writer.println("<div class=\"rTableHead\"><strong>Trade Price</strong></div>");
	    writer.println("<div class=\"rTableHead\"><strong>Current Price</strong></div>");
	    writer.println("<div class=\"rTableHead\"><strong>Unrealized P&amp;L</strong></div>");
	    writer.println("</div>");

	    for (Position p: positions) {
		Stock stock = getStock(p.getSymbol());

		// position.side * position.size * stock.last
		double currentPrice = stock.getLast();

		// position.size * (position.price - stock.last)
		double unrealizedPL = p.getSide() * p.getSize() * (currentPrice - p.getPrice());

		writer.println("<div class=\"rTableRow\">");
		writer.println("<div class=\"rTableCell\">" + p.getSymbol() + "</div>");
		writer.println("<div class=\"rTableCell\">" + stock.getFullName() + "</div>");
		writer.println("<div class=\"rTableCell\">" + p.getSize() + "</div>");
		writer.println("<div class=\"rTableCell\">" + p.getPrice() + "</div>");
		writer.println("<div class=\"rTableCell\">" + currentPrice + "</div>");
		writer.println("<div class=\"rTableCell\">" + unrealizedPL + "</div>");
		writer.println("</div>");
	    }
	    writer.println("</div>");
	} // end else
	
	writer.println("<br><br>");
	writer.println("<ul class=\"navList\">");
	writer.println("<li class=\"navItem\"><a href=\"accountHome.jsp\">Home</a></li>");
	writer.println("</ul");
	
	writer.println("</body>");
	writer.println("</html>");
    }
    
   

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
	doGet(request, response);
    }

}
