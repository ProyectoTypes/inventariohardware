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
package fixture.servicio;

import java.util.List;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Prototype;
import org.apache.isis.applib.fixturescripts.FixtureResult;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.fixturescripts.SimpleFixtureScript;

import fixture.permisos.PermisoFixture;
import fixture.roles.RolesFixture;
import fixture.sector.SectorFixture;
import fixture.tecnico.TecnicoFixture;
import fixture.usuario.UsuariosFixture;



/**
 * Enables fixtures to be installed from the application.
 */
@Named("Prototyping")
@DomainService(menuOrder = "200")
public class FixturesService extends FixtureScripts {

    public FixturesService() {
        super("fixture");
    }

    //@Override // compatibility with core 1.5.0
    public FixtureScript default0RunFixtureScript() {
        return findFixtureScriptFor(SimpleFixtureScript.class);
    }

    /**
     * Raising visibility to <tt>public</tt> so that choices are available for first param
     * of {@link #runFixtureScript(FixtureScript, String)}.
     */
    @Override
    public List<FixtureScript> choices0RunFixtureScript() {
        return super.choices0RunFixtureScript();
    }


    // //////////////////////////////////////

	// @Prototype
    @Programmatic
    @MemberOrder(sequence="20")
    public Object instalarFixturesSectores() {
        final List<FixtureResult> run = findFixtureScriptFor(SectorFixture.class).run(null);
        return run.get(0).getObject();
    }
    
    @Prototype
    @MemberOrder(sequence="30")
    public Object instalarFixturesTecnicos() {
        final List<FixtureResult> run = findFixtureScriptFor(TecnicoFixture.class).run(null);
        return run.get(0).getObject();
    }
    @Prototype
    @MemberOrder(sequence="30")
    public Object instalarFixturesUsuariosConSector() {
//        findFixtureScriptFor(SectorFixture.class).run(null);
    	this.instalarFixturesSectores();
        final List<FixtureResult> run = findFixtureScriptFor(UsuariosFixture.class).run(null);
        return run.get(0).getObject();
    }
    @Prototype
    @MemberOrder(sequence="30")
    public Object instalarRolesYPermisosFixture() {
    	final List<FixtureResult> run = findFixtureScriptFor(PermisoFixture.class).run(null);
    	this.instalarRoles();
    	//this.instalarUsuariosShiro();
        return run.get(0).getObject();
    }
    @Programmatic
    @MemberOrder(sequence="30")
    public Object instalarRoles() {
        final List<FixtureResult> run = findFixtureScriptFor(RolesFixture.class).run(null);
        return run.get(0).getObject();
    }
  
    
    
}