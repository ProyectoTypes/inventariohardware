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
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;

import dom.computadora.hardware.gabinete.disco.Disco;
import dom.computadora.hardware.gabinete.disco.Disco.CategoriaDisco;
import dom.computadora.hardware.gabinete.memoria.MemoriaRam;
import dom.computadora.hardware.gabinete.motherboard.Motherboard;
import dom.computadora.hardware.gabinete.placadered.PlacaDeRed;
import dom.computadora.hardware.gabinete.procesador.Procesador;
import dom.computadora.hardware.impresora.Impresora;
import dom.computadora.hardware.impresora.ImpresoraRepositorio;
import dom.computadora.hardware.monitor.Monitor;
import dom.computadora.hardware.monitor.MonitorRepositorio;
import dom.usuario.Usuario;
import dom.usuario.UsuarioRepositorio;

/**
 * Clase ComputadoraRepositorio.
 */
@DomainService(menuOrder = "10")
@Named("COMPUTADORA")
public class ComputadoraRepositorio {

	public ComputadoraRepositorio() {

	}

	/**
	 * Id
	 * @return
	 */
	public String getId() {
		return "computadora";
	}

	/**
	 * Nombre del Icono.
	 * @return string
	 */
	public String iconName() {
		return "Computadora";
	}

	/**
	 * Agregar computadora.
	 * @param usuario
	 * @param ip
	 * @param mac
	 * @param marcaDisco
	 * @param tipoDisco
	 * @param tamanoDisco
	 * @param modeloProcesador
	 * @param modeloRam
	 * @param tamanoRam
	 * @param marcaRam
	 * @param modeloMotherboard
	 * @param fabricante
	 * @param monitor
	 * @param impresora
	 * @param rotulo
	 * @return computadora
	 */
	@MemberOrder(name = "Hardware", sequence = "10")
	@Named("(+) Computadora")
	@DescribedAs("Agregar Computadora manualmente.")
<<<<<<< HEAD
	public Computadora agregarComputadora(
			final @Named("Nombre de Equipo") String rotulo,
=======
	public Computadora create(
>>>>>>> 6ca70bf950b9552273f8f1fb55eca0856030e2a2
			final @Named("Usuario") Usuario usuario,
			final @RegEx(validation= "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])")@Named("IP") String ip, 
			final @RegEx(validation = "([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])") @Named("MAC") String mac,
			final @Named("HDD Marca ") String marcaDisco,
			final @Named("HDD Categoria ") CategoriaDisco tipoDisco,
			final @Named("HDD Tamaño ") int tamanoDisco,
			final @Named("CPU Modelo ") String modeloProcesador,
			final @Named("RAM Modelo") String modeloRam,
			final @Named("RAM Tamaño") int tamanoRam,
			final @Named("RAM Marca") String marcaRam,
			final @Named("Modelo Motherboard") String modeloMotherboard,
			final @Named("Fabricante") String fabricante,
			final @Optional @Named("Monitor") Monitor monitor,
			final @Optional @Named("Impresora") Impresora impresora
			) {
		PlacaDeRed placaDeRed = new PlacaDeRed(ip, mac);
		Disco disco = new Disco(marcaDisco, tipoDisco, tamanoDisco);
		Procesador procesador = new Procesador(modeloProcesador);
		MemoriaRam memoriaRam = new MemoriaRam(modeloRam, tamanoRam, marcaRam);
		Motherboard motherboard = new Motherboard(modeloMotherboard);
		return this.nuevaComputadora(usuario, placaDeRed, motherboard,
				procesador, disco, memoriaRam, impresora,
				this.currentUserName(), rotulo);
	}
	
	@Hidden
	@NotContributed
	@MemberOrder(name = "Computadoras", sequence = "20")
	@Named("Agregar Computadora")
	public Computadora addComputadora(final @Named("Usuario") Usuario usuario,
			final @Named("Direccion Ip") PlacaDeRed placaDeRed,
			final @Named("Mother") Motherboard motherboard,
			final @Named("Procesador") Procesador procesador,
			final @Named("Disco") Disco disco,
			final @Named("Memoria") MemoriaRam memoria,
			final @Optional @Named("Impresora") Impresora impresora) {
		return nuevaComputadora(usuario, placaDeRed, motherboard, procesador,
				disco, memoria, impresora, this.currentUserName(), "");
	}

	/**
	 * Nueva computadora.
	 * @param usuario
	 * @param placaDeRed
	 * @param motherboard
	 * @param procesador
	 * @param disco
	 * @param memoria
	 * @param impresora
	 * @param creadoPor
	 * @param rotulo
	 * @return unaComputadora
	 */
	@Programmatic
	public Computadora nuevaComputadora(final Usuario usuario,
			final PlacaDeRed placaDeRed, final Motherboard motherboard,
			final Procesador procesador, final Disco disco,
			final MemoriaRam memoria, final Impresora impresora,
			final String creadoPor, final String nombreEquipo) {
		final Computadora unaComputadora = container
				.newTransientInstance(Computadora.class);
		unaComputadora.setNombreEquipo(nombreEquipo);
		unaComputadora.modifyUsuario(usuario);
		unaComputadora.setPlacaDeRed(placaDeRed);
		unaComputadora.setMotherboard(motherboard);
		unaComputadora.setProcesador(procesador);
		unaComputadora.setDisco(disco);
		unaComputadora.setMemoria(memoria);
		unaComputadora.setImpresora(impresora);
		unaComputadora.setHabilitado(true);
		unaComputadora.setCreadoPor(creadoPor);
		if (impresora != null) {
			impresora.agregarComputadora(unaComputadora);
		}
		container.persistIfNotAlready(unaComputadora);
		container.flush();
		return unaComputadora;
	}

	/**
<<<<<<< HEAD
	 * Validate add computadora.
	 *
	 * @param usuario        
	 * @param placaDeRed       
	 * @param motherboard
	 * @param procesador  
	 * @param disco 
	 * @param memoria    
	 * @param impresora
	 * @return  string
=======
	 * Validar los datos de Computadora.
	 * @param usuario
	 * @param placaDeRed
	 * @param motherboard
	 * @param procesador
	 * @param disco
	 * @param memoria
	 * @param impresora
	 * @return 
>>>>>>> 6ca70bf950b9552273f8f1fb55eca0856030e2a2
	 */
	public String validateAddComputadora(Usuario usuario,
			PlacaDeRed placaDeRed, Motherboard motherboard,
			Procesador procesador, Disco disco, MemoriaRam memoria,
			Impresora impresora) {
		if (usuario.getComputadora() == null)
			return null;
		return "El Usuario ya posee una Computadora. Seleccione otra. ";
	}

<<<<<<< HEAD
	// //////////////////////////////////////
	// Buscar Monitor
	// //////////////////////////////////////

	/**
	 * Lista de los monitores cargados
	 *
	 * @return list
	 */
	public List<Monitor> choices13AgregarComputadora() {
		return this.monitorRepositorio.listar();

	}

	// //////////////////////////////////////
	// Buscar Impresora
	// //////////////////////////////////////

=======
	/**
	 * Método que lista los Monitores cargados.
	 * @return the list
	 */
	public List<Monitor> choices12Create() {
		return this.monitorRepositorio.listAll();
	}

>>>>>>> 6ca70bf950b9552273f8f1fb55eca0856030e2a2
	/**
	 * Método que lista las Impresoras cargadas.
	 * @return the list
	 */
<<<<<<< HEAD
	public List<Impresora> choices14AgregarComputadora() {
		return this.impresoraRepositorio.listar();

=======
	public List<Impresora> choices13Create() {
		return this.impresoraRepositorio.listAll();
>>>>>>> 6ca70bf950b9552273f8f1fb55eca0856030e2a2
	}

	/**
	 * Auto complete búsqueda de usuario a medida que se cargan los datos.
	 * @param search       
	 * @return
	 */
	@Named("Usuario")
	@DescribedAs("Buscar el Usuario en mayuscula")
<<<<<<< HEAD
	public List<Usuario> autoComplete1AgregarComputadora(
=======
	public List<Usuario> autoComplete0Create(
>>>>>>> 6ca70bf950b9552273f8f1fb55eca0856030e2a2
			final @MinLength(2) String search) {
		return usuarioRepositorio.autoComplete(search);

	}

	/**
	 * Método que permite listar las Computadoras.
	 * @return
	 */
<<<<<<< HEAD
	@MemberOrder(name = "Hardware", sequence = "16")
	@Named("--Listar Computadoras")
	public List<Computadora> listar() {
=======
	@MemberOrder(name = "Computadoras", sequence = "30")
	@Named("Listar Computadoras")
	public List<Computadora> listAll() {
>>>>>>> 6ca70bf950b9552273f8f1fb55eca0856030e2a2
		final List<Computadora> listaComputadoras;

		listaComputadoras = this.container
				.allMatches(new QueryDefault<Computadora>(Computadora.class,
						"listarHabilitados"));

		if (listaComputadoras.isEmpty()) {
			this.container
					.warnUser("No hay Computadoras cargadas en el sistema.");
		}
		return listaComputadoras;
	}

	/**
	 * Permite realizar la búsqueda de Computadora por el parámetro Ip.
	 * @param ip         
	 * @return
	 */
	@MemberOrder(name = "Hardware", sequence = "17")
	@Named("--Buscar Computadora")
	public List<Computadora> buscar(
			final @RegEx(validation = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
					+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
					+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
					+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$") @Named("Ip") @MinLength(2) String ip) {
		final List<Computadora> listaComputadoras = this.container
				.allMatches(new QueryDefault<Computadora>(Computadora.class,
						"buscarPorIp", "ip", ip.toUpperCase().trim()));
		if (listaComputadoras.isEmpty())
			this.container
					.warnUser("No se encontraron Computadoras cargadas en el sistema.");
		return listaComputadoras;
	}

	/**
	 * Auto complete.
	 * @param ip     
	 * @return
	 */
	@Programmatic
	public List<Computadora> autoComplete(@Named("Ip") @MinLength(2) String ip) {
		return container.allMatches(new QueryDefault<Computadora>(
				Computadora.class, "autoCompletePorComputadora", "ip", ip
						.toUpperCase().trim()));
	}

	/**
	 * Current user name, devuelve el nombre del usuario.
	 * @return  string 
	 */
	private String currentUserName() {
		return container.getUser().getName();
	}

	/**
	 * Inyección del Contenedor.
	 */
	@javax.inject.Inject
	private DomainObjectContainer container;

	/**
	 * Inyección del servicio para Usuario.
	 */
	@javax.inject.Inject
	private UsuarioRepositorio usuarioRepositorio;

	/**
	 * Inyección del servicio para Impresora.
	 */
	@javax.inject.Inject
	private ImpresoraRepositorio impresoraRepositorio;

	/**
	 * Inyección del servicio para Monitor.
	 */
	@javax.inject.Inject
	private MonitorRepositorio monitorRepositorio;
}