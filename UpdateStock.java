/**
 * 
 */
package edu.sollers.javaprog.tradingsystem;



/**
 * 
 * @author rutpatel
 *
 */

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletContext;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class UpdateStock implements Runnable
{
	Connection conn;
	ServletContext sc;
	//Vector<Order> marketOrderQueue;
    //String DB_URL = "jdbc:mysql://localhost/phptest";
    
    //Hashtable<Integer, Order> pO;// = new Hashtable<Integer, Order>();
    
    /**
     * Used to get Pending Order Collection as a Hashtable and to call run() method for updating stocks price every 20 seconds.  
     */
    
    public UpdateStock(ServletContext sc, Connection c)
    {
/*    	Order o1 = new Order("buy","limit",120,"pending","fb",2,160);
		Order o2 = new Order("sell","limit",120,"pending","fb",2,150);
		Order o3 = new Order("buy","stop",120,"pending","fb",2,152);
		Order o4 = new Order("sell","stop",120,"pending","fb",2,162);
		Order o5 = new Order("sell","stop",120,"pending","amzn",2,1800);
		pO.put(1, o1);
		pO.put(2, o2);
		pO.put(3, o3);
		pO.put(4, o4);
		pO.put(5, o5);
*/		
    	this.sc = sc;
    	conn = c;
		//pO = getPendingOrders();
		
//    	run();
    }
    
    /**
     * Used for updating stocks price every 20 seconds and to call trigger_fun() for checking whether change in stocks price triggers 
     * any Pending Order or not.
     */
    
	@Override
	public void run() 
	{		
		for(int i = 1; i > 0 ; i++)
		{
			try
		    {
/*		      	Class.forName("com.mysql.cj.jdbc.Driver");
		      	conn = DriverManager.getConnection(DB_URL,"root","");
*/
			    Statement stmt = null;
			    ResultSet rs = null;
			    while (conn == null) {
			    	System.out.println("Connection is null");
			    	Thread.sleep(10000);
			    }
			    stmt = conn.createStatement();
		        rs = stmt.executeQuery("select ticker from stocks");

		        while(rs.next())
		        {
		            Stock stock = YahooFinance.get(rs.getString("ticker"));
		            stmt.executeUpdate("update stocks set bid = "+stock.getQuote(true).getBid()
		            									+", last = "+stock.getQuote().getPrice()
		            									+", ask = "+stock.getQuote().getAsk()+" where ticker='"+rs.getString("ticker")+"';");
		        }
		        System.out.println("\n/\\Stocks updated..."+i+"/\\\n");
		        trigger_fun();
		        Thread.sleep(20000);
		     } catch(Exception e){
		    	e.printStackTrace();
		     }
		}
	}

	/**
	 * 
	 * Used to check whether change in stocks price, triggers any Pending Order or not.
	 * If triggered, then Pending Order should be removed from Hashtable and should be added to Execute Order i.e. in Vector.
	 * If not triggered, then Pending Order remains in Hashtable.
	 */
	
	@SuppressWarnings("unchecked")
	public void trigger_fun() throws IOException 
	{
		Hashtable<Integer, Order> pO;
		Vector<Order> marketOrderQueue;
		pO = (Hashtable<Integer,Order>)sc.getAttribute("pendingOrders");
		marketOrderQueue = (Vector<Order>)sc.getAttribute("marketOrderQueue");
		Set<Integer> keys = pO.keySet();
        for(Integer key : keys)
        {
        	Order temp = pO.get(key);
        	Stock stock = YahooFinance.get(temp.getSymbol());
        	BigDecimal lP = stock.getQuote().getPrice();
        	double lastPrice = lP.doubleValue();
        	Order.OrderType orderType = temp.getOrderType();
        	Order.PriceType priceType = temp.getPriceType();
        	double limitPrice = temp.getStopPrice();
        	
        	if(orderType==Order.OrderType.BUY & priceType==Order.PriceType.LIMIT & lastPrice < limitPrice)
        	{
        		pO.remove(key);
        		marketOrderQueue.add(temp);
        		//addMarketOrder(temp);
        		System.out.println("Simulation: adding Limit Order to execute queue ..."+temp.getSymbol());
        	}
        	else if(orderType==Order.OrderType.SELL & priceType==Order.PriceType.LIMIT & lastPrice > limitPrice)
        	{
        		pO.remove(key);
        		marketOrderQueue.add(temp);
        		System.out.println("Simulation: adding Limit Order to execute queue..."+temp.getSymbol());
        	}
        	else if(orderType==Order.OrderType.BUY & priceType==Order.PriceType.STOP & lastPrice > limitPrice)
        	{
        		pO.remove(key);
        		marketOrderQueue.add(temp);
        		//addMarketOrder(temp);
        		System.out.println("Simulation: adding Stop Order to execute queue..."+temp.getSymbol());
        	}
        	else if(orderType==Order.OrderType.SELL & priceType==Order.PriceType.STOP & lastPrice < limitPrice)
        	{
        		pO.remove(key);
        		marketOrderQueue.add(temp);
        		//addMarketOrder(temp);
        		System.out.println("Simulation: adding Limit Order to execute queue..."+temp.getSymbol());
        	}
        	else
        	{
        		System.out.println(temp.getSymbol()+" not triggered...");
        	}
        }
	}
}
