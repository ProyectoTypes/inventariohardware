/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
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
import fixture.usershiro.UsuarioShiroFixture;
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
    	this.instalarUsuariosShiro();
        return run.get(0).getObject();
    }
    @Programmatic
    @MemberOrder(sequence="30")
    public Object instalarRoles() {
        final List<FixtureResult> run = findFixtureScriptFor(RolesFixture.class).run(null);
        return run.get(0).getObject();
    }
    @Programmatic
    @MemberOrder(sequence="30")
    public Object instalarUsuariosShiro() {
        final List<FixtureResult> run = findFixtureScriptFor(UsuarioShiroFixture.class).run(null);
        return run.get(0).getObject();
    }
    
    
}