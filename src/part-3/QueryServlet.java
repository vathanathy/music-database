package edu.polytechnique.inf553;

//import java.beans.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.security.auth.message.callback.PrivateKeyCallback.IssuerSerialNumRequest;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class QueryServlet
 */
@WebServlet(
		urlPatterns = { "/QueryServlet" }, 
		initParams = { 
				@WebInitParam(name = "countryname", value = ""), 
				@WebInitParam(name = "year", value = "")
		})
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	protected static ResultSet rs;  //Result set from the database. Make it static in order to be able to be use from other method in the same package
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = "jdbc:postgresql://localhost:5432/assignment";  //Database url with port and database's name
		
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString()); //will be print if the method is called
		
		//Retrieving the parameters
		String countryname = request.getParameter("countryname");
		int year = Integer.parseInt(request.getParameter("year"));
		
		
		if ( countryname.isEmpty() || year == 0 || year >= 2050) {
			throw new ServletException("Expected countryname and year parameters but did not get one, URL malformed"); 
		}
		
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(url);
			PreparedStatement st = con.prepareStatement("SELECT a.id, a.name, a.gender, a.syear, a.area, COUNT(*) "
					+ "FROM release_country rc, release_has_artist rha, artist a, country c1 "
					+ "WHERE rc.year >= " + year + " AND rc.country = c1.id AND c1.name LIKE '" + countryname +"%' AND rc.release = rha.release AND rha.artist = a.id "
					+ "GROUP BY a.id;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			rs = st.executeQuery();
			
			
			PrintWriter out = response.getWriter();
			out.println("<html>" + 
					"<body>" +
					"<table cellspacing=\"20\" >" + 
					"	<tr>" + 
					"		<td><b>Artist Id</b></td>" + 
					"		<td><b>Artist Name</b></td>" + 
					"	</tr>");
			while(rs.next()) {
				out.println("<tr>" + 
						"		<td>"+ rs.getInt(1) +"</td>" +
						"		<td><a href=\""+
						request.getRequestURI() + "/DetailServlet?artistid=" + rs.getInt(1) + 
						"\">"+ rs.getString(2) + "</a></td>" +
						"	</tr>");
			}
			out.println("</table>" +
					"</body>" + 
					"</html>");
			
			rs.beforeFirst();
			RequestDispatcher rd = request.getRequestDispatcher("DetailServlet");
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
