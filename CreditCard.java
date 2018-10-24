/**
 * 
 */
package edu.sollers.javaprog.tradingsystem;


/**
 * 
 * @author rutpatel
 *
 */

public class CreditCard 
{
	String u_id;
	String c_owner;
	String c_type;
	String c_ccno;
	int c_expm;
	int c_expy;
	int c_cvvno;
	
	public CreditCard() {}
	
	public CreditCard(String u_id, String c_owner, String c_type, String c_ccno, int c_expm, int c_expy, int c_cvvno) {
		super();
		this.u_id = u_id;
		this.c_owner = c_owner;
		this.c_type = c_type;
		this.c_ccno = c_ccno;
		this.c_expm = c_expm;
		this.c_expy = c_expy;
		this.c_cvvno = c_cvvno;
	}

	public String getU_id() {
		return u_id;
	}

	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	public String getC_owner() {
		return c_owner;
	}

	public void setC_owner(String c_owner) {
		this.c_owner = c_owner;
	}

	public String getC_type() {
		return c_type;
	}

	public void setC_type(String c_type) {
		this.c_type = c_type;
	}

	public String getC_ccno() {
		return c_ccno;
	}

	public void setC_ccno(String c_ccno) {
		this.c_ccno = c_ccno;
	}

	public int getC_expm() {
		return c_expm;
	}

	public void setC_expm(int c_expm) {
		this.c_expm = c_expm;
	}

	public int getC_expy() {
		return c_expy;
	}

	public void setC_expy(int c_expy) {
		this.c_expy = c_expy;
	}

	public int getC_cvvno() {
		return c_cvvno;
	}

	public void setC_cvvno(int c_cvvno) {
		this.c_cvvno = c_cvvno;
	}
}
