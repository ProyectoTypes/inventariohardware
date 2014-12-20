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

package fixture.instalar;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Prototype;
import org.apache.isis.applib.fixturescripts.FixtureResult;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

@Named("Test")
@DomainService(menuOrder = "200")
public class InstalarFixture extends FixtureScripts {

    public InstalarFixture() {
    	super("fixture.instalar");
	}
      
  
    @Prototype
    @MemberOrder(sequence="80")
    public Object instalarFixturesSector() {
        final List<FixtureResult> Sector = findFixtureScriptFor(SectorFixture.class).run(null);
        return Sector.get(0).getObject();
    }    
    
    @MemberOrder(sequence="99")
    public String IntstalarTodosLosFixtures()
    {    	
    	this.instalarFixturesSector();
    	
    	this.container.informUser("Entro hasta aqui 2");
    	
    	return "Todos los fixtures intastalados";
    }

    DomainObjectContainer container;
}