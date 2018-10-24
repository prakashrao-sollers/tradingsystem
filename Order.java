package edu.sollers.javaprog.tradingsystem;
/**
 * @author Karanveer
 */

import java.util.Date;

public class Order {
    
    // define enums
    public enum OrderType {
	BUY, SELL, SELL_SHORT, BUY_TO_COVER;
    }
    public enum PriceType {
	MARKET, LIMIT, STOP;
    }
    public enum TimeInForce {
	DAY, GTX, GTC;
    }
    public enum OrderStatus {
	PENDING, CANCELLED, EXECUTED;
    }
    
    static Integer lastOrderId = 1;
    
    private int		  orderId;
    private OrderType     orderType;
    private PriceType     priceType;
    private TimeInForce   timeInForce;
    private int           accountId;
    private String        symbol;
    private double        size;
    private double        stopPrice;
    private OrderStatus	  status;
    private Date creationDateTime;
   
    /**
     * @param orderType 
     * @param priceType
     * @param timeInForce
     * @param accountId          int
     * @param symbol		 string
     * @param size		 double
     * @param stopPrice		 double
     */
    public Order(OrderType orderType, PriceType priceType, TimeInForce timeInForce, int accountId, String symbol,
	    double size, double stopPrice) {
	super();
	this.orderId 		= getNextOrderId();	// set automatically
	this.orderType 		= orderType;
	this.priceType 		= priceType;
	this.timeInForce 	= timeInForce;
	this.accountId 		= accountId;
	this.symbol 		= symbol;
	this.size 		= size;
	this.stopPrice 		= stopPrice; 		// if market order, set to 0, else set to specified stopPrice
	this.status 		= OrderStatus.PENDING;		
	this.creationDateTime 	= new Date();		// set automatically
    }

    /**
     * @return the orderType
     */
    public OrderType getOrderType() {
        return orderType;
    }
    
    /**
     * @return the orderId
     */
    public int getOrderID() {
        return orderId;
    }

    /**
     * @return the priceType
     */
    public PriceType getPriceType() {
        return priceType;
    }

    /**
     * @return the timeInForce
     */
    public TimeInForce getTimeInForce() {
        return timeInForce;
    }

    /**
     * @return the accountId
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * @return the stopPrice
     */
    public double getStopPrice() {
        return stopPrice;
    }

    /**
     * @return the status
     */
    public OrderStatus getStatus() {
        return status;
    }
    

    /**
     * @param status the status to set
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * @return the creationDateTime
     */
    public Date getCreationDateTime() {
        return creationDateTime;
    }
    
    
    static Integer getNextOrderId() {
	synchronized(Thread.currentThread()) {
	    return lastOrderId++;
	}
    }
    
    static void setLastOrderId(Integer id) {
	lastOrderId = id;
    }
    
    public String toString() {
	String result = "Order Type: " + orderType + "\n"
			+ "Symbol: " + symbol + "\n"
			+ "Size: " + size + "\n"
			+ "Price Type: " + priceType + "\n";
	result += (stopPrice == 0) ? "" : "Stop Level: $" + stopPrice + "\n";
	result += "Creation Date: " + creationDateTime;
	return result;
	
    }
}
