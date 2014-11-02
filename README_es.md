Inventario Hardware
===================
Sistema de Gestión y Soporte Técnico - Proyecto Types

Este proyecto se inició en Marzo del 2014 en la ciudad de Neuquén Capital, pais Argentina.
Es un proyecto académico que se realiza para la asignatura "Proyecto Final" dictada en el Institituto de Formación y Educación Superior (IFES).

El proyecto está basado en el modelo Domain-Driven Design (DDD) y su implementación es realizada bajo el lenguaje Java utilizando el framework Apache ISIS. 

## El Departamento de Sistemas del Ministerio de Gobierno, podra administrar:
  * Técnicos.
  * Usuarios.
  * Computadoras.
  * Sectores.
  * Estadísticas.


## Servicios del Sistema:
  * Notificar al Usuario sobre el estado de reparación de la computadora via Email.
  * Realizar una estadistica de la cantidad de computadoras en reparacion que un Técnico posea.


## Se contará con los siguientes servicios mejorados:
  * Poder ver la bandeja de entrada del correo de la oficina.
  * Exportar a Excel un informe de los equipos que esten en reparación.
  * Exportar a Word un informe de los equipos que posean un pedido de insumos.


## El proyecto esta funcionando con:
    Framework isis apache. Versión 1.6.
    Postgresql 9.1.
    Api de Zabbix.


## Instalación
1. Descargar el código fuente de InventarioHardware.
 
$ git clone https://github.com/ProyectoTypes/inventariohardware.git

$ cd inventariohardware

2. Actualizar el codigo fuente.

Con $ mvn clean install

3. Compilar el proyecto inventariohardware.

Con $ mvn -P self-host antrun:run


##Licencia
InventarioHardware está regulado por la licencia GNU GPLv2
