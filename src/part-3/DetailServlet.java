package edu.polytechnique.inf553;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DetailServlet
 */
@WebServlet(urlPatterns = { "/QueryServlet/DetailServlet" }, initParams = {
		@WebInitParam(name = "artistid", value = "") })
public class DetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DetailServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI()
				+ " and parameters " + request.getQueryString());

		/// Get the table of countries
		Map<Integer, String> countryTable = new HashMap<Integer, String>();

		String url = "jdbc:postgresql://localhost:5432/assignment";

		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(url);
			PreparedStatement st = con.prepareStatement("SELECT * " + "FROM country;");

			ResultSet rsCountry = st.executeQuery();
			while (rsCountry.next()) {
				countryTable.put(rsCountry.getInt(1), rsCountry.getString(2));
			}

			/// Print the data
			int artistid = Integer.parseInt(request.getParameter("artistid"));

			if (artistid == 0) {
				throw new ServletException(
						"Expected countryname and year parameters but did not get one, URL malformed");
			}

			PrintWriter out = response.getWriter();

			ResultSet rs = QueryServlet.rs;
			while (rs.next()) {
				if (rs.getInt(1) == artistid) {
					out.println("<html>" + "<body>" + "<table cellspacing=\"20\"  align =\"center\">");

					if (rs.getInt(3) == 1) {
						out.println(
								"	<tr>" + "		<td><b>Artist Gender</b></td>" + "		<td>Mr.</td>" + "	</tr>");
					}
					if (rs.getInt(3) == 2) {
						out.println(
								"	<tr>" + "		<td><b>Artist Gender</b></td>" + "		<td>Mrs.</td>" + "	</tr>");
					}

					out.println("	<tr>" + "		<td><b>Artist Name</b></td>" + "		<td>" + rs.getString(2)
							+ "</td>" + "	</tr>");

					if (rs.getInt(4) != 0) {
						out.println("	<tr>" + "		<td><b>Artist Birth Year</b></td>" + "		<td>"
								+ rs.getString(4) + "</td>" + "	</tr>");
					}

					if (rs.getInt(5) != 0) {
						out.println("	<tr>" + "		<td><b>Artist's Geographical Area</b></td>" + "		<td>"
								+ countryTable.get(rs.getInt(5)) + "</td>" + "	</tr>");
					}
				}
			}

			out.println("</table>" + "</body>" + "</html>");

			rs.beforeFirst();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
