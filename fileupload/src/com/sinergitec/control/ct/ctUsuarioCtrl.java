package com.sinergitec.control.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.progress.open4gl.Open4GLException;
import com.sinergitec.dao.ct.ctUsuarioDao;
import com.sinergitec.dao.ct.imp.ctUsuarioDaoImp;
import com.sinergitec.model.ct.ctUsuario;

public class ctUsuarioCtrl extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ctUsuarioDao ctUsuario_dao;
	private List<ctUsuario> lista = new ArrayList<ctUsuario>();
	
	public ctUsuarioCtrl() {
		super();
		ctUsuario_dao = new ctUsuarioDaoImp();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("entro al doGet");
		String action = request.getParameter("action");
		//String cCveCia = request.getParameter("cCveCia");

		if (action.equals("list")) {
			try {
				lista = ctUsuario_dao.list_ctUsuario(true);
			} catch (Open4GLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			request.setAttribute("lista_ctUsuario", lista);

		}

		RequestDispatcher view = request.getRequestDispatcher("/ctUsuario_List.jsp");
		view.forward(request, response);

	}
}
