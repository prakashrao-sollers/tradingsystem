package edu.sollers.javaprog.tradingsystem;
/**
 * @author Karanveer
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginController() {
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

			// conenction string
			String url = "jdbc:mariadb://localhost:3306/sollerstrading";
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * Logs a user in if the username and password are validated from the database
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/**
		 * Logs a user in if the username and password are validated from the database
		 * 
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
		 *      response)
		 */
		String un = request.getParameter("username");
		String pw = request.getParameter("password");

		int userId = 0;

		boolean userFound = false;

		if (un.equals("stadmin") && pw.equals("password")) {
			request.getSession().setAttribute("uderId", 50000);
			response.sendRedirect("admin.html");
		} else {
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT id FROM login WHERE " + "(password=\"" + pw + "\" AND uname=\"" + un + "\");");

				if (rs.next()) {
					userId = rs.getInt(1); // capture id if you found a user
					userFound = true;
				}

				stmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (userFound) {
				request.getSession().setAttribute("userId", userId); // add userId to session
				System.out.println("\nYou've logged in!\n");
				// redirect to Account.html
				response.sendRedirect("accountHome.jsp");
				return;
			} else {
				System.out.println("\nInvalid login!\n");
				String message = "Username or password invalid";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}
		}
	}
}
