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
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.query.QueryDefault;

import dom.computadora.hardware.Hardware;
import dom.computadora.hardware.gabinete.Gabinete;
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
import dom.computadora.software.Software;
import dom.computadora.software.SoftwareRepositorio;
import dom.usuario.Usuario;
import dom.usuario.UsuarioRepositorio;

/**
 * Clase ComputadoraRepositorio.
 */
@DomainService(menuOrder = "40")
@Named("Computadora")
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
	 * Método que permite listar las Computadoras.
	 * @return
	 */
    @ActionSemantics(Of.SAFE)
    @MemberOrder(name = "Hardware", sequence = "20")
	@Named("Listar Computadoras")
	public List<Computadora> listAll() {
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
	 * @param software
	 * @param rotulo
	 * @return computadora
	 */
    @ActionSemantics(Of.SAFE)
	@MemberOrder(name = "Hardware", sequence = "20")
	@Named("Agregar Computadora")
	@DescribedAs("Agregar Computadora.")
	public Computadora create(
			final @Named("Nombre de Equipo") String nombreEquipo,
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
			final @Optional @Named("Impresora") Impresora impresora,	
			final @Optional @Named("Software") Software sotfware
			) {
		PlacaDeRed placaDeRed = new PlacaDeRed(ip, mac);
		Disco disco = new Disco(marcaDisco, tipoDisco, tamanoDisco);
		Procesador procesador = new Procesador(modeloProcesador);
		MemoriaRam memoriaRam = new MemoriaRam(modeloRam, tamanoRam, marcaRam);
		Motherboard motherboard = new Motherboard(modeloMotherboard);
		return this.nuevaComputadora(nombreEquipo, usuario, placaDeRed, motherboard,
				procesador, disco, memoriaRam, impresora, sotfware, monitor, this.currentUserName());
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
	public Computadora nuevaComputadora(final String nombreEquipo, final Usuario usuario,
			final PlacaDeRed placaDeRed, final Motherboard motherboard,
			final Procesador procesador, final Disco disco,
			final MemoriaRam memoria, final Impresora impresora, final Software software,
			final Monitor monitor,final String creadoPor) {
		final Computadora unaComputadora = container.newTransientInstance(Computadora.class);
		
		final Gabinete gabinete = new Gabinete();
		gabinete.agregarHdd(disco);
		gabinete.agregarMemoriaRam(memoria);
		gabinete.setPlacaDeRed(placaDeRed);
		gabinete.setMotherboard(motherboard);
		gabinete.setProcesador(procesador);
		
		final Hardware hardware = new Hardware();
		hardware.setGabinete(gabinete);
		hardware.setImpresora(impresora);
		hardware.setMonitor(monitor);
		
		unaComputadora.setCreadoPor(creadoPor);
		unaComputadora.setHabilitado(true);
		unaComputadora.setNombreEquipo(nombreEquipo);
		unaComputadora.modifyUsuario(usuario);	
		unaComputadora.setImpresora(impresora);
		unaComputadora.setSoftware(software);
		unaComputadora.setTecnico(null);
		
		if (impresora != null) {
			impresora.agregarComputadora(unaComputadora);
		}
		container.persistIfNotAlready(unaComputadora);
		container.persistIfNotAlready(hardware);
		container.persistIfNotAlready(gabinete);
		container.flush();
		return unaComputadora;
	}


	/**
	 * Método que lista los Monitores cargados.
	 * @return the list
	 */
	public List<Monitor> choices13Create() {
		return this.monitorRepositorio.listAll();
	}

	/**
	 * Método que lista las Impresoras cargadas.
	 * @return the list
	 */
	public List<Impresora> choices14Create() {
		return this.impresoraRepositorio.listAll();
	}

	/**
	 * Auto complete búsqueda de usuario a medida que se cargan los datos.
	 * @param search       
	 * @return
	 */
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@Named("Usuario")
	@DescribedAs("Buscar el Usuario en mayuscula")
	public List<Usuario> autoComplete1Create(
			final @MinLength(2) String search) {
		return usuarioRepositorio.autoComplete(search);

	}

	
	@Bookmarkable
	@ActionSemantics(Of.SAFE)
	@Named("Software")
	@DescribedAs("Buscar el Software en mayuscula")
	public List<Software> autoComplete15Create(
			final @MinLength(2) String search) {
		return softwareRepositorio.autoComplete(search);

	}
	
	/**
	 * Permite realizar la búsqueda de Computadora por el parámetro Ip.
	 * @param ip         
	 * @return
	 */
	@MemberOrder(name = "Hardware", sequence = "20")
	@Named("Buscar Computadora")
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
	
	
	/**
	 * Inyección del servicio para Software.
	 */
	@javax.inject.Inject
	private SoftwareRepositorio softwareRepositorio;
}