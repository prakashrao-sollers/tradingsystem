/**
 * 
 */
package edu.sollers.javaprog.tradingsystem;

/**
 * @author Karanveer
 *
 */
public class Helper {
    /**
     * Method that provides the CSS for displaying a table
     * Simply call this method to insert the style into HTML. 
     * It includes the 'style' opening and closing tags. 
     * 
     * @return style tag with contents as String
     * @author Karanveer
     */
    public static String getCSS() {
	String style = "<style>";
	
	style += "	ul.navList {\r\n" + 
		"		list-style-type: none;\r\n" + 
		"		margin: 0;\r\n" + 
		"		padding: 0;\r\n" + 
		"		width: 200px;\r\n" + 
		"		background-color: #f1f1f1;\r\n" + 
		"		border: 1px solid #555;\r\n" + 
		"	}\r\n" + 
		"	\r\n" + 
		"	li.navItem a {\r\n" + 
		"		display: block;\r\n" + 
		"		color: #000;\r\n" + 
		"		padding: 8px 16px;\r\n" + 
		"		text-decoration: none;\r\n" + 
		"	}\r\n" + 
		"	\r\n" + 
		"	li.navItem {\r\n" + 
		"		text-align: center;\r\n" + 
		"		border-bottom: 1px solid #555;\r\n" + 
		"	}\r\n" + 
		"	\r\n" + 
		"	li.navItem:last-child {\r\n" + 
		"		border-bottom: none;\r\n" + 
		"	}\r\n" + 
		"	\r\n" + 
		"	li.navItem a.active {\r\n" + 
		"		background-color: #4CAF50;\r\n" + 
		"		color: white;\r\n" + 
		"	}\r\n" + 
		"	\r\n" + 
		"	li.navItem a:hover:not(.active) {\r\n" + 
		"	    background-color: #555;\r\n" + 
		"	    color: white;\r\n" + 
		"	}\r\n" + 
		"	\r\n" + 
		"	li.navItem a.logout {\r\n" + 
		"		float:right;\r\n" + 
		"	}";
	
	style += ".rTable {\r\n" + 
		"  display: table;\r\n" + 
		"  width: 100%;\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		".rTableRow {\r\n" + 
		"  display: table-row;\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		".rTableHeading {\r\n" + 
		"  display: table-header-group;\r\n" + 
		"  background-color: #ddd;\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		".rTableCell,\r\n" + 
		".rTableHead {\r\n" + 
		"  display: table-cell;\r\n" + 
		"  padding: 3px 10px;\r\n" + 
		"  border: 1px solid #999999;\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		".rTableHeading {\r\n" + 
		"  display: table-header-group;\r\n" + 
		"  background-color: #ddd;\r\n" + 
		"  font-weight: bold;\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		".rTableFoot {\r\n" + 
		"  display: table-footer-group;\r\n" + 
		"  font-weight: bold;\r\n" + 
		"  background-color: #ddd;\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		".rTableBody {\r\n" + 
		"  display: table-row-group;\r\n" + 
		"}\r\n";
	
	style += "</style>";
	return style;
    }
}
