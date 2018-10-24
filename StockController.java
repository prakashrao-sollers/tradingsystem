package edu.sollers.javaprog.tradingsystem;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Servlet implementation class AddStocks
 */
@WebServlet("/StockController")
public class StockController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn;

    /**
     * Default constructor. 
     */
    public StockController() {
        conn = null;
    }
 
    /**
     * init. Initialized database connection
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            // db parameters
            String url = "jdbc:mariadb://localhost:3306/sollerstrading";
            // create a connection to the database
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, "webuser", "Sollers@123");
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String s_ticker = request.getParameter("ticker1");
		
		//System.out.println(ticker);
		try {
			Statement stmt=conn.createStatement();  
			
			Stock stock = YahooFinance.get(s_ticker);
			
			
			if (stock.getName() == null)
				System.out.println("Ticker does not exist");
			else {
				//System.out.println("Ticker: " + stock.getSymbol());
				//System.out.println("Company: " + stock.getName());
				//System.out.println("Last trade: " + stock.getQuote(true).getPrice());
				//System.out.println("Bid: " + stock.getQuote().getBid());
				//System.out.println("Ask: " + stock.getQuote().getAsk());
				//System.out.println("Previous close: " + stock.getQuote().getPreviousClose());
				String insertQuery = "insert into stocks (ticker, full_name, bid, ask, last) values ('"+stock.getSymbol()+"', '"+stock.getName()+"',"+stock.getQuote().getBid()+", "+stock.getQuote().getAsk()+", "+stock.getQuote().getPrice()+")";
				//System.out.println(insertQuery);
				stmt.executeUpdate(insertQuery);
				
				response.sendRedirect("AddStocks.html");
				return;
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			response.sendRedirect("AddStocks.html");
		}catch (IOException e) {
			System.out.println(e.getMessage());
			response.sendRedirect("AddStocks.html");
		} catch (Exception e){
			System.out.println(e.getMessage());
			response.sendRedirect("AddStocks.html");
		}
	}
}
