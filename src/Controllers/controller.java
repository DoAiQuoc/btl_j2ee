package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Models.Import_Emloyment;
import Models.dbConnects;

@WebServlet(urlPatterns= {"/update/*","/insert","/update","/list","/"})
public class controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private dbConnects bd;
    /**
     * @see HttpServlet#HttpServlet()
     */
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		String  url = "jdbc:mysql://localhost:3306/quan_ly_kho";
		String userName="root";
		String pass = "";
		bd = new dbConnects(url,userName,pass);
	}
    

	public controller() {
		super();
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			switch (action) {
				case "/insert":	
					insertProduct(request,response);
					break;
				case "/new":
					newForm(request, response);
					break;
				case "/update":
					uppdateProduct(request,response);
					break;
				case "/edit":
					editForm(request,response);
					break;
				case "/delete":
					deleteProduct(request,response);
					break;
				default:
					listProduct(request, response);
					break;
			}
	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	private void newForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("productForm.jsp");
		dispatcher.forward(request, response);
	}
	private void editForm(HttpServletRequest request,HttpServletResponse response ) throws ServletException,IOException, SQLException{
		int id_product=Integer.parseInt(request.getParameter("ID"));
		Import_Emloyment product = bd.getProduct(id_product);
		request.setAttribute("product", product);
		RequestDispatcher dispatcher=request.getRequestDispatcher("productForm.jsp");
		dispatcher.forward(request, response);		
	}
	private void uppdateProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException,IOException{
		int id=Integer.parseInt(request.getParameter("ID"));
		String code=request.getParameter("code");
		String code_sp=request.getParameter("code_sp");
		int price=Integer.parseInt(request.getParameter("price_in"));
		Date date_in;
		try {
			date_in = new SimpleDateFormat("yyyy MM dd").parse(request.getParameter("date_in"));
			Import_Emloyment product = new Import_Emloyment(id, code, code_sp, price, date_in);
			bd.updateProduct(product);
			response.sendRedirect("list");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void insertProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
		//int id=Integer.parseInt(request.getParameter("id"));
		String code=request.getParameter("code");
		String code_sp=request.getParameter("code_sp");
		int price_in=Integer.parseInt(request.getParameter("price_in"));
		Date date_in;
		try {
			date_in = new SimpleDateFormat("yyyy MM dd").parse(request.getParameter("date_in"));
			Import_Emloyment product=new Import_Emloyment(code, code_sp, price_in, date_in);
			System.out.print(bd.insertProduct(product));
			if(bd.insertProduct(product) == true) {
				response.sendRedirect("list");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
		int id=Integer.parseInt(request.getParameter("ID"));		
		Import_Emloyment product = new Import_Emloyment(id);
		bd.deleteProduct(product);
		response.sendRedirect("list");
	}
	private void listProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException,ServletException {
		List<Import_Emloyment> listproduct = bd.getAllProduct();
		request.setAttribute("listProduct",listproduct);
		RequestDispatcher	dispatcher= request.getRequestDispatcher("ListProduct.jsp");
		dispatcher.forward(request, response);
		for(Import_Emloyment product : listproduct) {
			product.print();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
