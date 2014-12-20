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
package fixture.sector;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import dom.sector.Sector;
import dom.sector.SectorRepositorio;

public class SectorFixture extends FixtureScript {

	public SectorFixture() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	// //////////////////////////////////////

	private Sector create(final String nombre, ExecutionContext executionContext) {
		return executionContext.add(this, sectores.create(nombre));
	}

	// //////////////////////////////////////
	@SuppressWarnings("deprecation")
	@Override
	protected void execute(ExecutionContext executionContext) {

		// prereqs
		execute(new SectorFixtureBaja(), executionContext);

		// create
		create("Administracion", executionContext);
		create("Informatica", executionContext);
		create("Ventas", executionContext);
		create("Contaduria", executionContext);
		create("Mesa de Entrada", executionContext);
		create("Deposito", executionContext);
	}

	@javax.inject.Inject
	private SectorRepositorio sectores;
}