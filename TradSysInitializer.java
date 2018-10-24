package edu.sollers.javaprog.tradingsystem;
/**
 * @author praka
 */

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Servlet implementation class Daemon
 */
@WebServlet(description = "This is the background process that creates common objects needed for the trading system", urlPatterns = { "/Daemon" })
public class TradSysInitializer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Vector<Order> marketOrders;
	private Hashtable<Integer, Order> pendingOrders;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TradSysInitializer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("Initializing Trading System");
		Order.setLastOrderId(new Integer(1));
		System.out.println("New Orders will begin with Order Id 1");
		marketOrders = new Vector<Order>();
		pendingOrders = new Hashtable<Integer, Order>();
		ServletContext sc = getServletContext();
		sc.setAttribute("marketOrderQueue", marketOrders);
		sc.setAttribute("pendingOrderCollection", pendingOrders);
		System.out.println("Market Order Queue and Pending Order Collection added to context");
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {

	}


}
