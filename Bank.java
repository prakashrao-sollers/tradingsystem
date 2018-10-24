package edu.sollers.javaprog.tradingsystem;
/**
 * 
 * @author rutpatel
 *
 */

public class Bank 
{
	String u_id;
	String b_owner;
	String b_name;
	String b_accno;
	String b_rno;
	
	public Bank() {}
	
	public Bank(String u_id, String b_owner, String b_name, String b_accno, String b_rno) {
		this.u_id = u_id;
		this.b_owner = b_owner;
		this.b_name = b_name;
		this.b_accno = b_accno;
		this.b_rno = b_rno;
	}

	public String getU_id() {
		return u_id;
	}

	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	public String getB_owner() {
		return b_owner;
	}

	public void setB_owner(String b_owner) {
		this.b_owner = b_owner;
	}

	public String getB_name() {
		return b_name;
	}

	public void setB_name(String b_name) {
		this.b_name = b_name;
	}

	public String getB_accno() {
		return b_accno;
	}

	public void setB_accno(String b_accno) {
		this.b_accno = b_accno;
	}

	public String getB_rno() {
		return b_rno;
	}

	public void setB_rno(String b_rno) {
		this.b_rno = b_rno;
	}
}
