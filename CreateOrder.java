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
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.sollers.javaprog.tradingsystem.Order.OrderStatus;
import edu.sollers.javaprog.tradingsystem.Order.OrderType;
import edu.sollers.javaprog.tradingsystem.Order.PriceType;
import edu.sollers.javaprog.tradingsystem.Order.TimeInForce;

/**
 * Servlet implementation class CreateOrder
 */
@WebServlet("/CreateOrder")
public class CreateOrder extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection conn;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateOrder() {
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
	    ResultSet rs   = stmt.executeQuery("SELECT full_name, bid, ask, last FROM stocks WHERE ticker=\"" + ticker + "\";");
	    if (rs.next()) {
		String fullName = rs.getString(1);
		double bid 	= rs.getDouble(2);
		double ask	= rs.getDouble(3);
		double last	= rs.getDouble(4);
		currentStock = new Stock(ticker, fullName, bid, ask, last);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return currentStock;
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
     * This method retrieves the given account's funds
     * i.e the "MONEY" position for the account. 
     * 
     * @param userId int
     * @return Position object with for symbol "MONEY" or null
     */
    private Position getMoneyPosition(int userId) {
	Position p = null;
	try {
	    Statement stmt = conn.createStatement();
	    ResultSet rs   = stmt.executeQuery("SELECT id, side, size, price, creation_date FROM positions WHERE account_id=" + userId + " AND symbol=\"MONEY\";");
	    if (rs.next()) {
		int positionId = rs.getInt(1);
		int side       = rs.getInt(2);
		double size    = rs.getDouble(3);
		double price   = rs.getDouble(4);
		Date date      = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(5));
		p              = new Position(positionId, "MONEY", userId, side, size, price, date);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return p;
    }

    private Hashtable<Integer, Order> getPendingOrders(){
        ServletContext sc = getServletContext();
        @SuppressWarnings("unchecked")
        Hashtable<Integer,Order> pendingOrderQueue = (Hashtable<Integer,Order>)sc.getAttribute("pendingOrders");
        return pendingOrderQueue;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	Integer userId = (Integer) request.getSession().getAttribute("userId");
	
	// check if account has a MONEY position
	Position moneyPosition = getMoneyPosition(userId);
	if (moneyPosition == null) {
	    request.setAttribute("errorMessage", "Must add funds before creating order");
	    request.getRequestDispatcher("accountHome.jsp").forward(request, response);
	}
	
	response.setContentType("text/html");
	PrintWriter writer = response.getWriter();
	writer.println("<!DOCTYPE html>");
	writer.println("<html>");
	writer.println("<head>");
	writer.println("<title>" + "Sollers Trading System - Create Order" + "</title></head>");
	writer.println(Helper.getCSS());
	writer.println("<body>");
	writer.println("Current session user id: " + userId);
	writer.println("<h3><a href=\"createOrder.jsp\">New Order</a></h3>");
	writer.println("<h3><a href=\"ClosePosition\">Close Existing Position</a></h3>");
	writer.println("<br><br>");
	writer.println("<ul class=\"navList\">");
	writer.println("<li class=\"navItem\"><a href=\"accountHome.jsp\">Home</a></li>");
	writer.println("</ul");
	writer.println("</body>");
	writer.println("</html>");
	
	request.getSession().setAttribute("userId", userId);
    }
    

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// TODO get userid from session
	Integer userId = (Integer) request.getSession().getAttribute("userId");
	
//	int userId = 2; // John Doe in database
	
	/*
	 * Algorithm:
	 * 
	 * Assuming some session variable is available 
	 * 
	 * 1) Get form parameters
	 * 
	 * 2) Ensure that order is for a valid stock symbol
	 * (stock symbol exists in stocks table)
	 * 
	 * 3) Ensure that symbol, side, and size are unique for this account's positions
	 * 
	 * 4) Ensure there are sufficient funds in account for buy/buy to cover order
	 * 
	 * 5) Ensure that order is not a duplicate order 
	 * (side, symbol combination is unique for this account's positions)
	 * 
	 * 6) Ensure that value of an order to close a position is less than $25,000
	 * 
	 * If this is a limit buy order, limit price must be less than last price
	 * If this is a limit sell order, limit price must be greater than last price
	 * If this is a stop buy order, stop price must be higher than last price
	 * If this is a stop sell order, stop price must be lower than last price
	 * 
	 * If price validation failse, user is alerted. 
	 * 
	 */
	
	// 1) Get form parameters
	OrderType orderType = OrderType.valueOf(request.getParameter("orderType"));
	PriceType priceType = PriceType.valueOf(request.getParameter("priceType"));
	TimeInForce timeInForce = TimeInForce.valueOf(request.getParameter("duration"));
	String symbol = request.getParameter("symbol");
	double size = Double.parseDouble(request.getParameter("quantity"));
	
	double stopLevel = 0;
	// if not market order, parse stopPrice value
	if (!priceType.equals(PriceType.MARKET)) {
	    stopLevel = Double.parseDouble(request.getParameter("stopPrice"));
	}
	
	
	if (userId == null) {
	    System.out.println("User id is null");
	}
	// Sunny day scenario
	Order order = new Order(orderType, priceType, timeInForce, userId, symbol, size, stopLevel);
	
	ArrayList<String> errorMessage = new ArrayList<>();

	Stock stock = getStock(order.getSymbol());
	double orderCost = 0;
	
	// 2) Ensure that order is for a valid stock symbol
	if (stock == null) {
	    errorMessage.add("Invalid stock symbol");
	}
	else {
	    orderCost = stock.getLast() * order.getSize();

	    // 3) Ensure that symbol, side, and size are unique for this account's positions
	    ArrayList<Position> positions = getOpenPositionsForAccount(userId);

	    for (Position p: positions) {
		if (order.getSymbol().equals(p.getSymbol())) {

		    // get appropriate side according to order type
		    int orderSide = 0;
		    if (order.getOrderType() == OrderType.BUY || order.getOrderType() == OrderType.SELL_SHORT) {
			orderSide = (order.getOrderType() == OrderType.BUY) ? 1 : -1;
		    }

		    // compare order's side with existing positions's side
		    if (orderSide == p.getSide()) {
			errorMessage.add("Order matches existing position: order invalid"); 
			break;
		    }
		}
	    }
	    
	    // 4) Ensure there are sufficient funds in account for buy/buy to cover order
	    Position moneyPosition = getMoneyPosition(userId);
	    
	    if (order.getOrderType().equals(OrderType.BUY) || order.getOrderType().equals(OrderType.BUY_TO_COVER)) {
		if (moneyPosition == null) {
		    errorMessage.add("No funds in account");
		}
		else if (moneyPosition.getSize() < orderCost) {
		    errorMessage.add("Insufficient funds for order");
		}
	    }

	    // 5) Ensure that order is not a duplicate order 
	    // If existing open order's symbol, ordertype match, then deny this order. 
	    try {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE account_id=\"" + userId + "\" AND order_status=\"PENDING\";");
		while (rs.next()) {
		    String existing_symbol = rs.getString("symbol");
		    OrderType existing_ot  = OrderType.valueOf(rs.getString("order_type"));
		    
		    if (existing_symbol.equals(order.getSymbol()) && existing_ot.equals(order.getOrderType())) {
			errorMessage.add("Existing open order of the same type for this symbol: order invalid");
			break;
		    }
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }

	    // 6) If order is to close an existing position, ensure that total position cash value is less than 25000.
	    if (order.getOrderType() == OrderType.SELL || order.getOrderType() == OrderType.BUY_TO_COVER) {
		if (orderCost > 25000) {
		    errorMessage.add("Cannot close a position of value greater than $25000");
		}
	    }
	    
	    // add order to be processed if no errors
	    if (errorMessage.isEmpty()) {		
		if (order.getPriceType() != PriceType.MARKET) {
		    
		    // add to pendingOrder hashtable
		    Hashtable<Integer, Order> pendingOrders = getPendingOrders();
		    String poStatus = (pendingOrders == null) ? "null" : "not null";
		    System.out.println("pendingOrders is " + poStatus);
		    if (pendingOrders != null) {
			pendingOrders.put(order.getOrderID(), order);
			System.out.println("\n" + order.toString() + "\nadded to pending orders \n");
		    }
		}
		else {
		    
		    // add to market order queue 
		    ServletContext sc = getServletContext();
		    @SuppressWarnings("unchecked")
		    Vector<Order> marketOrderQueue = (Vector<Order>)sc.getAttribute("marketOrderQueue");
		    if(marketOrderQueue == null) {
			response.getWriter().append("Error: market order queue is null");
		    }
		    else {
			synchronized(Thread.currentThread()) {
			    marketOrderQueue.add(order);
			    System.out.println("\n" + order.toString() + "\nadded to market queue \n");
			}
		    }
		}
		
		// TODO add order to 'orders' table in database
		int updateQuery = insertOrderRecord(order);
		if (updateQuery == 1) {
		    System.out.println("Successfully inserted order into orders table");
		}
		
	    } // end if where error_message.isEmpty()
	} // end else where stock is not null
	
	if (!errorMessage.isEmpty()) {
	    request.setAttribute("message", errorMessage);
	    request.getRequestDispatcher("/createOrder.jsp").forward(request, response);
	}
	else {
	    request.setAttribute("successMessage", "Order created successfully");
	    request.getRequestDispatcher("/accountHome.jsp").forward(request, response);
	}
    }
    
    /**
     * Method to insert newly created order into database table
     * @param order
     * @return int value 0 if nothing was inserted or 1 if successfully inserted
     */
    private int insertOrderRecord(Order order) {
	int num = 0;
	try {
	    Statement stmt = conn.createStatement();
	     num = stmt.executeUpdate("INSERT INTO orders "
	     	+ "(order_type, price_type, time_in_force, order_status, account_id, symbol, size, stop_price, creation_date)"
		+ " VALUES ("
	     	+ "\"" + order.getOrderType() + "\", "
	     	+ "\"" + order.getPriceType() + "\", "
	     	+ "\"" + order.getTimeInForce() + "\", "
	     	+ "\"" + order.getStatus() +  "\", "
	     	+ order.getAccountId() + ", "
	     	+ "\"" + order.getSymbol() + "\", "
	     	+ order.getSize() + ", "
	     	+ order.getStopPrice() + ", "
	     	+ "\"" + new SimpleDateFormat("yyyy-MM-dd").format(order.getCreationDateTime()) + "\");"
		);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return num;
    }
    
    
     

}
