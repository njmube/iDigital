package com.sinergitec.control.sy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.progress.open4gl.Open4GLException;
import com.sinergitec.dao.ct.ctCompaniaDao;
import com.sinergitec.dao.ct.ctUsuarioDao;
import com.sinergitec.dao.ct.imp.ctCompaniaDaoImp;
import com.sinergitec.dao.ct.imp.ctUsuarioDaoImp;
import com.sinergitec.dao.sy.syUsuCompaniaDao;
import com.sinergitec.dao.sy.imp.syUsuCompaniaDaoImp;
import com.sinergitec.model.ct.ctCompania;
import com.sinergitec.model.ct.ctUsuario;
import com.sinergitec.model.sg.sysUsuCompania;

/**
 * Servlet implementation class syConfigUsu
 */
public class syConfigUsuCtrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ctCompaniaDao ctCompania_Dao;
	private ctUsuarioDao  ctUsuario_Dao;
	private syUsuCompaniaDao syUsuCompania_Dao;
	private List<ctCompania> list_Compania = new ArrayList<ctCompania>();
	private List<ctUsuario>  list_Usuario = new ArrayList<ctUsuario>();
	private List<sysUsuCompania> list_UsuCompania = new ArrayList<sysUsuCompania>();
	
	private static String PRINCIPAL = "/sysConfigUsu.jsp";
	private static String ADDUSER   = "/sysConfigUsu_Add_UsuComp.jsp";
	private String forward = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public syConfigUsuCtrl() {
		super();
		ctCompania_Dao = new ctCompaniaDaoImp();
		ctUsuario_Dao  = new ctUsuarioDaoImp();
		syUsuCompania_Dao = new syUsuCompaniaDaoImp();
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.getWriter().append("Served at: ").append(request.getContextPath());
		String sUsuario;
		String sCompania;
		String sAction;
		
		
		

		sAction = request.getParameter("action");
		
		System.out.println("ENTRO -->"+ sAction);
		if (sAction.equalsIgnoreCase("inicial")) {

			try {
				list_Compania = ctCompania_Dao.list_ctCompania(true);
				list_UsuCompania = syUsuCompania_Dao.list_sysUsuCompania(true);
			} catch (Open4GLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			request.setAttribute("list_ctCompania", list_Compania);
			request.setAttribute("list_syUsuCompania", list_UsuCompania);
			forward = PRINCIPAL;

		}else if (sAction.equalsIgnoreCase("list_Usu")){
			
			/*Este opcion permite cargar la lista de usuarios que no estan
			 * en una determinada compania, es decir filtra a los usuarios*/
			
			sCompania = request.getParameter("cCveCia");// Esta variable rescata a la compa�ia
			
			List<ctUsuario> Lista_nueva = new ArrayList<ctUsuario>();
			List<String> catUsuarios = new ArrayList<String>();
			List<String> catUsuEmpresa = new ArrayList<String>();
			// Lista para almacenar a los usuarios filtrados
			
			try {
				
				//Nos traen los datos de la BD de los usuarios y las compa�ias
				list_Usuario = ctUsuario_Dao.list_ctUsuario(true);
				list_Compania = ctCompania_Dao.list_ctCompania(true);
				list_UsuCompania = syUsuCompania_Dao.list_sysUsuCompania(true);
				
				
				for (ctUsuario usuario_filtro : list_Usuario) {
					
					/*Primer for que va a recorrer a todos los usuarios
					 * este for no termina hasta que todo el codigo dentro de el termine*/
					
					//Nos permite almacenar los usuarios en una lista para compararlos
					catUsuarios.add(usuario_filtro.getcUsuario());
					
						for (ctCompania Company : list_Compania) {
							
							/*Segundo For que va a recorrer a todas las compa�ias
							 * no termina hasta que se completen los if*/
							
							for (sysUsuCompania usuCompany : list_UsuCompania) {
								
								/*Tercer For va a recorrer a todos los usuarios que ya 
								 * cuentan con compa�ia*/
								
								//Nos permite almacenar los usuarios en una lista para compararlos
								catUsuEmpresa.add(usuCompany.getcUsuario());
								
								if(!usuCompany.getcCveCia().equals(sCompania) &&
										
										usuCompany.getcUsuario().equals(usuario_filtro.getcUsuario()) &&

										usuario_filtro.getlActivo().equals(true) && Company.getcCveCia().equals(sCompania)){
									
									System.out.println("Esta entrando al primer if");

									/*Este if tiene por objetivo descartar a los usuarios
									 * que ya estuviesen inscritos dentro de la empresa
									 * seleccionada*/

									//Creacion del objeto usuario
									ctUsuario obj_nuevo = new ctUsuario();

									obj_nuevo.setcUsuario(usuario_filtro.getcUsuario());
									obj_nuevo.setcNombre(usuario_filtro.getcNombre());
									obj_nuevo.setcPassword(usuario_filtro.getcPassword());
									obj_nuevo.setlActivo(usuario_filtro.getlActivo());
									obj_nuevo.setDtFechaAlta(usuario_filtro.getDtFechaAlta());
									obj_nuevo.setiPuesto(usuario_filtro.getiPuesto());
									obj_nuevo.setPuesto(usuario_filtro.getPuesto());
									obj_nuevo.setId(usuario_filtro.getId());
									Lista_nueva.add(obj_nuevo);
									
									}
								}
							}
						}
				
				
				/*Esta seccion nos permite filtar a los usuarios que no estan con una 
				 * compa�ia es decir es la primera vez que los van agregar*/
				Collection<String> similar = new HashSet<String>( catUsuarios );
		        Collection<String> different = new HashSet<String>();
		        different.addAll( catUsuarios );
		        different.addAll( catUsuEmpresa );
		        
		        similar.retainAll( catUsuEmpresa );
		        different.removeAll( similar );
		        
		        
		        //Recorremos la lista original de usuarios
		        for (ctUsuario usuario_filtro : list_Usuario) {
		        	
		        	//Recorremos la lista con los usuarios que no tienen empresa
		        	for (String string : different) {
		        		
		        		/*Verificamos que sea igual el usuario existente en la base con
		        		 * el usuario que no tiene empresa*/
						if(usuario_filtro.getcUsuario().equals(string)){
							
							System.out.println("Usuario no existente en sysUsuCompania: "+string);
							//Creacion del objeto usuario
							ctUsuario obj_nuevo = new ctUsuario();

							obj_nuevo.setcUsuario(usuario_filtro.getcUsuario());
							obj_nuevo.setcNombre(usuario_filtro.getcNombre());
							obj_nuevo.setcPassword(usuario_filtro.getcPassword());
							obj_nuevo.setlActivo(usuario_filtro.getlActivo());
							obj_nuevo.setDtFechaAlta(usuario_filtro.getDtFechaAlta());
							obj_nuevo.setiPuesto(usuario_filtro.getiPuesto());
							obj_nuevo.setPuesto(usuario_filtro.getPuesto());
							obj_nuevo.setId(usuario_filtro.getId());
							Lista_nueva.add(obj_nuevo);
						}
					}
				}
				
			} catch (Open4GLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("list_UsuCompania", Lista_nueva);
			forward = ADDUSER;			
		}else if (sAction.equalsIgnoreCase("delete")){
			/*Por el momento se borra unicamente con el cUsuario 
			 * esto no debe de ser se debe de borrar con cUsuario y cCveCia*/
			System.out.println("Cachorros estoy dentro del delete");
			sUsuario = request.getParameter("cUsuario");
			System.out.println(sUsuario);
			try {
				syUsuCompania_Dao.remove_sysUsuCompaniaDao("SISIMB", "SINERGIT", sUsuario);
				list_UsuCompania = syUsuCompania_Dao.list_sysUsuCompania(true);
				list_Compania = ctCompania_Dao.list_ctCompania(true);
				
			} catch (Open4GLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			request.setAttribute("list_syUsuCompania", list_UsuCompania);
			request.setAttribute("list_ctCompania", list_Compania);
			forward = PRINCIPAL;
		}
		

		RequestDispatcher view = request.getRequestDispatcher(forward);
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String action = request.getParameter("action");		

		if (action.equals("add") || action.equals("update")) {
			
			sysUsuCompania obj = new sysUsuCompania();
			
			obj.setcCveCia(request.getParameter("cCveCia"));
			obj.setcUsuario(request.getParameter("cUsuario"));
			obj.setlActivo(Boolean.parseBoolean(request.getParameter("lActivo")));
			
			/*obj.setcCveCia("SINERGIT");
			obj.setcUsuario("EMA");
			obj.setlActivo(true);*/
			
			if(action.equals("add")){
				try {
					syUsuCompania_Dao.add_sysUsuCompaniaDao("SISIMB", obj);
				} catch (Open4GLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(action.equals("update")){
				/*try {
				syUsuCompania_Dao.update_sysUsuCompaniaDao("SISTEMAS", obj);
				} catch (Open4GLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			
		}
	}

}
