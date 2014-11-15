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
package dom.computadora.hardware.gabinete.placadered;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({
@javax.jdo.annotations.Query(name = "listar", language = "JDOQL", value = "SELECT "
		+ "FROM dom.computadora.hardware.gabinete.placadered.PlacaDeRed") })
@ObjectType("PlacaDeRed")
public class PlacaDeRed {

	// //////////////////////////////////////
	// Identificacion en la UI.
	// //////////////////////////////////////

	public PlacaDeRed(String ip, String mac) {
		this.ip = ip;
		this.mac = mac;
	}

	public String title() {
		return getIp();
	}

	public String iconName() {
		return "PLACA DE RED";
	}

	// //////////////////////////////////////
	// IP placa de red
	// //////////////////////////////////////

	private String ip;

	@javax.jdo.annotations.Column(allowsNull = "false")
//	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("IP de la placa de red")
	@MemberOrder(sequence = "170")
	public String getIp() {
		return ip;
	}

	public void setIp(final String ip) {
		this.ip = ip;
	}

	// //////////////////////////////////////
	// MAC de la placa de red
	// //////////////////////////////////////

	private String mac;

	@javax.jdo.annotations.Column(allowsNull = "false")
//	@RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	@DescribedAs("MAC de la placa de red")
	@MemberOrder(sequence = "180")
	public String getMac() {
		return mac;
	}

	public void setMac(final String mac) {
		this.mac = mac;
	}
}
