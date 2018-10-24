/**
 * 
 */
package edu.sollers.javaprog.tradingsystem;
/**
 * @author Karanveer
 */

import java.util.Date;

public class Position {
    private int    accountId;
    private int	   positionId;
    private String symbol;
    private int	   side;
    private double    size;
    private double price;
    private boolean isOpen;
    private Date   creationDate;

    /**
     * @param symbol
     * @param accountId
     * @param side
     * @param size
     * @param price
     * @param creationDate
     */
    public Position(int positionId, String symbol, int accountId, int side, double size, double price, Date creationDate) {
	this.positionId = positionId;
	this.symbol = symbol;
	this.side = side;
	this.size = size;
	this.price = price;
	this.isOpen = true;	// set to false when closing the position
	this.creationDate = creationDate;
    }

    /**
     * @return the positionId
     */
    public int getPositionId() {
	return positionId;
    }
    
    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
	return creationDate;
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
     * @return the side
     */
    public int getSide() {
	return side;
    }

    /**
     * @return the size
     */
    public double getSize() {
	return size;
    }

    /**
     * @return the price
     */
    public double getPrice() {
	return price;
    }

    /**
     * @return the is Open
     */
    public boolean getIsOpen() {
	return isOpen;
    }

    /**
     * @param status the status to set when closing a position
     */
    public void setIsOpen(boolean status) {
	this.isOpen = status;
    }

    @Override
    public String toString() {
	String result = symbol + ": ";
	result += (side == 1) ? "long" : "short";
	result += ", ";
	result += size + " shares, $";
	result += price + ", ";
	result += creationDate;
	return result;
    }
}
