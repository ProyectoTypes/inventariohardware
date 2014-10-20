Inventario Hardware
===================
Sistema de Gestión y Soporte Técnico - Proyecto Types

Este proyecto se inició en Marzo del 2014 en la ciudad de Neuquén Capital, pais Argentina.
Es un proyecto académico que se realiza para la asignatura "Proyecto Final" dictada en el Institituto de Formación y Educación Superior (IFES).

El proyecto está basado en el modelo Domain-Driven Design (DDD) y su implementación es realizada bajo el lenguaje Java utilizando el framework Apache ISIS. 

## Es para la gestión del Departamento de Sistemas del Ministerio de Gobierno, en el cual se pueden administrar:

  * Técnicos.  
  * Usuarios.
  * Computadoras (Estados).
  * Sectores.
  * Estadísticas.

## El proyecto cuenta con los siguientes servicios mejorados.
  * Poder ver la bandeja de entrada del correo de la oficina.
  * Exportar a excel los equipos que esten en reparación.
  * Exportar a word los equipos que se haga un pedido de insumos.

## Además va a contar con los siguientes servicios:

  * Notificar al Usuario sobre el estado de la computadora via Email.
  * Realiza una estadistica a partir de la cantidad de computadoras que posee un Técnico en reparacion.


## El proyecto esta funcionando con.
    Framework isis apache.
      versión 1.6.
    Postgresql 9.1.
    SNMP4J.


## Instalación
1. Descargar el código fuente de InventarioHardware.
 
$ git clone https://github.com/ProyectoTypes/inventariohardware.git

$ cd inventariohardware

2. Actualizar el codigo fuente.

Con $ mvn clean install

3. Compilar el proyecto inventariohardware

Con $ mvn -P self-host antrun:run

Licencia

InventarioHardware está regulado por la licencia GNU GPLv2
