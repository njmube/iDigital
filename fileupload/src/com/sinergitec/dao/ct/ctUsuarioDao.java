package com.sinergitec.dao.ct;

import java.io.IOException;
import java.util.List;

import com.progress.open4gl.Open4GLException;
import com.sinergitec.model.ct.ctUsuario;


public interface ctUsuarioDao {
	
public void add_ctUsuario(ctUsuario obj_ctUsuario) throws Open4GLException, IOException;
	
	public void update_ctUsuario(ctUsuario obj_ctUsuario) throws Open4GLException, IOException;
	
	public void remove_ctUsuario(String cUsuario) throws Open4GLException, IOException;
	
	public List<ctUsuario> list_ctUsuario() throws Open4GLException, IOException;
	
	public ctUsuario get_ctUsuario(String cUsuario) throws Open4GLException, IOException;


}
