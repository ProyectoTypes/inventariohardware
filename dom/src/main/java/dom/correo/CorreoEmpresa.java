/*
 * This is a software made for inventory control
 * 
 * Copyright (C) 2014, ProyectoTypes
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * 
 * 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/
package dom.correo;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@ObjectType("CORREOEMPRESA")
@AutoComplete(repository = CorreoRepositorio.class, action = "autoComplete")
@Immutable
public class CorreoEmpresa {
	
	// //////////////////////////////////////
	// Titulo
	// //////////////////////////////////////
	
	public String title(){
			return getCorreo();
	}
	
	public String iconName(){
		return "config";
	}
	
	// //////////////////////////////////////
	// Correo (propiedad)
	// //////////////////////////////////////
	
	private String correo;
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence="1")
	public String getCorreo(){
		return correo;
	}
	public void setCorreo(String correo){
		this.correo=correo;
	}
	
	// //////////////////////////////////////
	// Pass (propiedad)
	// //////////////////////////////////////
	
	private String pass;
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@MemberOrder(sequence="2")
	public String getPass(){
		return pass;
	}
	public void setPass(String pass){
		this.pass=pass;
	}
}
