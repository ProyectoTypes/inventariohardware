package fixture.datos;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class DatosFixtureBaja extends FixtureScript{
	
private String Etable="";
	
	public DatosFixtureBaja(String table)
	{
		withDiscoverability(Discoverability.DISCOVERABLE);
		Etable=table;
	}
	
	public DatosFixtureBaja()
	{
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext executionContext) {
		
		if(Etable!="")
			BorrarContains();
		else
		{
		BorrarDB();
		
		String Data="Se ha completado la operacion. Toda la DB ah ido borrada.";
		create(Data,executionContext);
		}
	}
    
    private String create(final String Data, ExecutionContext executionContext) {
        return executionContext.add(this, Data);
    }
	
    private void BorrarContains()
    {
    	DeleteAndTruncateTable(Etable);
    	
    	for(String tabla:Tablas())
    	{
    		if(tabla.contains(Etable))
    		{
    			DeleteAndTruncateTable(tabla);
    		}
    	}
    	
    }
    
	private void DeleteAndTruncateTable(String table)
	{
    	if(table!=null || table!="")
    	{
    			isisJdoSupport.executeUpdate("TRUNCATE \""+table+"\" CASCADE");
    	}
	}

	private void DeleteTable(String table)
	{
    	if(table!=null || table!="")
    	{
    		isisJdoSupport.executeUpdate("DELETE FROM \""+table+"\"");
    	}
		
	}
	
    private void BorrarDB()
    {
    	for(String tabla:Tablas())
    	{
    		DeleteAndTruncateTable(tabla);
    		DeleteTable(tabla);
    		RestarTable(tabla);
    	}
    	
    	isisJdoSupport.executeSql("SELECT pg_stat_reset()");
    	
    	//isisJdoSupport.executeUpdate("SELECT tablename FROM pg_tables WHERE schemaname = 'public'");
    }
    
    protected List<String> Tablas()
    {
    	List<String> Tablas=new ArrayList<String>();
    	
    	String tablas="Funcion,Localidad,AlumnoCalificacion,Tarjeta,Legajo,Hora,Direccion,Materia,Anio_collectionName,Asistencia,Asistencia_asistenciasDiaList,Personal,LibroDiario,LibroDiario_materiaDelLibroDiarioList,HorarioHora,HorarioDia_horarioHoraList,HoraCatedra,Plan,HorarioPlan,Plan_aniolist,EntradaLibroDiario_horacatedra,Anio,HorarioPlan_horarioPlanHoraList,HorarioPlanHora,Periodo,Calificaciones,Calificaciones_periodos,HorarioDia,MateriaDelCurso,HorarioCurso_horarioDiaList,Curso,HorarioCurso,Curso_materiaDelCursoList,Alumno,EntradaLibroDiario,MateriaCalificacion,Curso_ListaAlumno,CargarNotas,CargarNotas_listMateriaCalificacion,AsistenciaDia,AsistenciaAlumno,AsistenciaDia_asistenciaAlumnoList,MateriaDelLibroDiario,MateriaDelLibroDiario_entradalibrodiario";
    	String[] partes = tablas.split(",");
    	
    	for(int x=0;x<partes.length;x++)
    		Tablas.add(partes[x]);

    	return Tablas;
    }
    
    protected void RestarTable(String table)
    {
    	boolean ok=false;
    	String tables="Funcion,Curso,Legajo,Localidad,Periodo,Tarjeta,Direccion";
    	String[] partes = tables.split(",");
    	    	
    	if(!table.contains("_"))
    	{
        	for(int x=0;x<partes.length;x++)
        	{
        		if(table.length()==partes[x].length())
        			if(table.contains(partes[x]))
        			{
        				table= table+"_"+table+"_ID_seq";
        				ok=true;
        				x=partes.length;
        			}
        	}
        	
    	if(ok==false)
    		table= table+"_id_seq";
    	
    	isisJdoSupport.executeUpdate("ALTER SEQUENCE \""+table+"\" RESTART WITH 1");
    	}
    			//ALTER SEQUENCE "Alumno_id_seq" RESTART WITH 1_seq
    			//table= table+"_"+table+"_ID_seq";
    			//ALTER SEQUENCE "Funcion_Funcion_ID_seq" RESTART WITH 1		
    }
    
    @javax.inject.Inject
    private IsisJdoSupport isisJdoSupport;
}