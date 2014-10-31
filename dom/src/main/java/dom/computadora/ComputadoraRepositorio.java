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
package dom.computadora;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;
import org.json.JSONException;

import dom.computadora.Computadora.CategoriaDisco;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.impresora.ImpresoraRepositorio;
import dom.usuario.Usuario;
import dom.usuario.UsuarioRepositorio;
import dom.zabbix.ZabbixRepositorio;
import dom.zabbix.monitoreo.item.RamItem;

@DomainService(menuOrder="10")
@Named("COMPUTADORA")
public class ComputadoraRepositorio {

	public ComputadoraRepositorio() {

	}

	// //////////////////////////////////////
	// Icono
	// //////////////////////////////////////

	public String getId() {
		return "computadora";
	}

	public String iconName() {
		return "Computadora";
	}

	// //////////////////////////////////////
	// Agregar Computadora
	// //////////////////////////////////////
	@NotContributed
	@MemberOrder(sequence = "10")
	@Named("Agregar Computadora")
	public Computadora addComputadora(final @Named("Usuario") Usuario usuario,
			final @Named("Direccion Ip") String ip,
			final @Named("Mother") String mother,
			final @Named("Procesador") String procesador,
			final @Named("Disco") CategoriaDisco disco,
			final @Optional @Named("Impresora") Impresora impresora) throws JSONException {
		return nuevaComputadora(usuario, ip, mother, procesador, disco,
				 impresora, this.currentUserName());
	}

	@Programmatic
	public Computadora nuevaComputadora(final Usuario usuario, final String ip,
			final String mother, final String procesador,
			final CategoriaDisco disco, 
			final Impresora impresora, final String creadoPor) throws JSONException {
		final Computadora unaComputadora = container
				.newTransientInstance(Computadora.class);
		
		unaComputadora.modifyUsuario(usuario);
		unaComputadora.setIp(ip);
		unaComputadora.setMother(mother);
		unaComputadora.setProcesador(procesador);
		unaComputadora.setCreadoPor(creadoPor);
		unaComputadora.setHabilitado(true);
		unaComputadora.setImpresora(impresora);
		unaComputadora.setDisco(disco);
		
		RamItem ram = new RamItem();
		if(this.zabbixRepositorio!=null)
		{System.out.println("NNO NULL");
			unaComputadora.setMemoria(ram.requestItemGet(ip,zabbixRepositorio.obtenerCuentaZabbix()));
		}else
			System.out.println("NULL");
		if (impresora != null) {
			impresora.agregarComputadora(unaComputadora);
		}

		container.persistIfNotAlready(unaComputadora);
		container.flush();
		return unaComputadora;
	}
	@javax.inject.Inject
	public ZabbixRepositorio zabbixRepositorio;
	public String validateAddComputadora(final @Named("Usuario") Usuario usuario,
			final @Named("Direccion Ip") String ip,
			final @Named("Mother") String mother,
			final @Named("Procesador") String procesador,
			final @Named("Disco") CategoriaDisco disco,
			final @Optional @Named("Impresora") Impresora impresora) {
		if (usuario.getComputadora()==null)
			return null;
		return "El Usuario ya posee una Computadora. Seleccione otro. "; // TODO: return reason why proposed value is invalid, null if valid
	}

	// //////////////////////////////////////
	// Buscar Impresora
	// //////////////////////////////////////

	// @Named("Impresora")
	public List<Impresora> choices5AddComputadora() {
		return this.impresoraRepositorio.listar();

	}

	// //////////////////////////////////////
	// Buscar Usuario
	// //////////////////////////////////////

	@Named("Usuario")
	@DescribedAs("Buscar el Usuario en mayuscula")
	public List<Usuario> autoComplete0AddComputadora(
			final @MinLength(2) String search) {
		return usuarioRepositorio.autoComplete(search);

	}

	// //////////////////////////////////////
	// Listar Computadora
	// //////////////////////////////////////

	@MemberOrder(sequence = "20")
	public List<Computadora> listar() {
		final List<Computadora> listaComputadoras = this.container
				.allMatches(new QueryDefault<Computadora>(Computadora.class,
						"eliminarComputadoraTrue"));
		if (listaComputadoras.isEmpty()) {
			this.container.warnUser("No hay Computadoras cargados en el sistema.");
		}
		return listaComputadoras;
	}

	// //////////////////////////////////////
	// Buscar Computadora
	// //////////////////////////////////////

	@MemberOrder(sequence = "30")
	public List<Computadora> buscar(
			final @RegEx(validation = "[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*") @Named("Ip") @MinLength(2) String apellido) {
		final List<Computadora> listaComputadoras = this.container
				.allMatches(new QueryDefault<Computadora>(Computadora.class,
						"buscarPorIp", "creadoPor", this.currentUserName(),
						"ip", apellido.toUpperCase().trim()));
		if (listaComputadoras.isEmpty())
			this.container
					.warnUser("No se encontraron Computadoras cargados en el sistema.");
		return listaComputadoras;
	}

	@Programmatic
	public List<Computadora> autoComplete(@Named("Ip") @MinLength(2) String ip) {
		return container.allMatches(new QueryDefault<Computadora>(
				Computadora.class, "autoCompletePorComputadora", "creadoPor",
				this.currentUserName(), "ip", ip.toUpperCase().trim()));
	}

	// //////////////////////////////////////
	// CurrentUserName
	// //////////////////////////////////////

	private String currentUserName() {
		return container.getUser().getName();
	}

	// //////////////////////////////////////
	// Injected Services
	// //////////////////////////////////////

	@javax.inject.Inject
	private DomainObjectContainer container;

	@SuppressWarnings("unused")
	@javax.inject.Inject
	private ComputadoraRepositorio computadoraRepositorio;

	@javax.inject.Inject
	private UsuarioRepositorio usuarioRepositorio;

	@javax.inject.Inject
	private ImpresoraRepositorio impresoraRepositorio;
}