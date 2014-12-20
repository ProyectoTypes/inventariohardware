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
package fixture.usuario;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScript.ExecutionContext;
import org.joda.time.LocalDate;

import dom.persona.Persona;
import dom.sector.Sector;
import dom.usuario.Usuario;
import dom.usuario.UsuarioRepositorio;
import fixture.datos.DatosFixture;

public class UsuariosFixture extends FixtureScript {

	public UsuariosFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext executionContext) {
		
		int Cantidad=DatosFixture.ObtenerCantidad()*14;
		
		List<Usuario> listUs=new ArrayList<Usuario>();
		
		// create
		for(int x=0; x<=Cantidad;x++)
		{
			Usuario usuario=new Usuario();
			usuario.setNombre(DatosFixture.ObtenerNombre());
			usuario.setApellido(DatosFixture.ObtenerApellido());
			usuario.setSector(DatosFixture.ObtenerSector());
			usuario.setEmail(DatosFixture.ObtenerEmail());
			
			listUs.add(usuario);
     	}
        	for(Usuario us:removerrepetidos(listUs))
        		create(us.getNombre(), us.getApellido(), us.getSector(), us.getEmail(), executionContext);
	}

	// //////////////////////////////////////

	private List<Usuario> removerrepetidos(List<Usuario> listaUsuario) {
		for (int x = 0; x < listaUsuario.size() - 1; x++) {
			for (int y = x + 1; y < listaUsuario.size(); y++) {
				if (listaUsuario.get(x).getNombre()
						.equals(listaUsuario.get(y).getNombre())
						&& listaUsuario.get(x).getApellido()
								.equals(listaUsuario.get(y).getApellido())) {
					listaUsuario.remove(y);
				}
			}
		}
		return listaUsuario;
	}
	    
	private Usuario create(final String nombre, String apellido, Sector sector, String email, ExecutionContext executionContext) {
		return executionContext.add(this, usuarios.create(nombre, apellido, sector, email));
	}

	// //////////////////////////////////////

	public void BorrarDBAlumnos(ExecutionContext executionContext) {
		execute(new GenericTearDownFixture("Usuario"), executionContext);
	        return;
	}
	    
	    
	@javax.inject.Inject
	private UsuarioRepositorio usuarios;
}