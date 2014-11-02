Hardware inventory
===================
Management System and Technical Support - Project Types

This project began in March 2014 in the city of Neuquén , Argentina country .
It is an academic project that is done for the course " Final Project" delivered in Institituto Training and Higher Education (IFES).

The project is modeled on Domain - Driven Design ( DDD ) and its implementation is carried out under the Java language using the framework Apache ISIS.

## Is to manage the Systems Department of the Interior Ministry , which can be administered:

  * Technicians.
  * Users.
  * Computers (United ) .
  * Sectors.
  * Statistics .

## The project has the following enhanced services.
  * Being able to see the email inbox of the office.
  * Export to excel teams that are in repair.
  * Export to Word equipment is made an order of inputs.

## In addition it will have the following services:

  * Notify the user about the state of the computer via Email.
  * Make a statistic based on the number of computers that has a repair technician.


## The project is running.
    Apache isis Framework.
      version 1.6.
    Postgresql 9.1.
    Api de Zabbix


## Installation
1 Download the source code InventarioHardware.
 
$ Git clone https://github.com/ProyectoTypes/inventariohardware.git

$ Cd inventariohardware

2 Update the source code.

With $ mvn clean install

3 Compile the project inventariohardware

With $ mvn -P self -host antrun:run

License

InventarioHardware is covered by the GNU GPLv2
