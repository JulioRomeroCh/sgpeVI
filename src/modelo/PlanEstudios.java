package modelo;

//import java.util.Calendar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JComboBox;
import modelo.Curso;


public class PlanEstudios {//Inicio clase PlanEstudios
    
  private int numeroPlan;
  private Date vigenciaPlan;
  private Curso [][] cursosBloque;

  
  public PlanEstudios(){
      
  }
  
  public PlanEstudios (int pNumeroPlan, Date pVigenciaPlan){
    
    cursosBloque = new Curso[12][20];
    setNumeroPlan(pNumeroPlan);   
    setVigenciaPlan(pVigenciaPlan); 
  }

  public int getNumeroPlan() {
    return numeroPlan;
  }

  public void setNumeroPlan(int pNumeroPlan) {
    this.numeroPlan = pNumeroPlan;
  }
  
  public String getVigenciaPlan() {
    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
    return formatoFecha.format(vigenciaPlan);
  }

  public void setVigenciaPlan(Date pVigenciaPlan) {
    this.vigenciaPlan = pVigenciaPlan;
  }
  
  //AÑADIR ESTO AL CONTROLADOR
  public void añadirCursos(Curso pCurso, int pBloque){
    try{
      System.err.println("Bloque: "+pBloque);
      for(int contador=0;contador<=cursosBloque.length;contador++){
        if (cursosBloque[pBloque][contador]==null){
          cursosBloque[pBloque][contador]=pCurso;
        }
      }
    }
    catch(Exception error){
      System.err.println("Error: " + error);
    }
  }
  
  
  public String toString(){
    String salida = "";
    salida+= "Número plan: " + getNumeroPlan()+ "\n";
    salida+= "Vigencia plan: " + getVigenciaPlan()+ "\n";
    return salida;
  }
  
  //--¿¿HABRÁ QUE ARREGLAR ESTA PICHA??--
  public void agregarPlanEstudios(int pNumeroPlan, Date pVigenciaPlan, String pCodigoEscuela){
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      CallableStatement insertar = conectar.prepareCall("{CALL insertPlanEstudios(?,?)}");
      insertar.setInt(1, pNumeroPlan);
      insertar.setDate(2, (java.sql.Date) pVigenciaPlan);
      insertar.execute();  
      CallableStatement insertarPlanEscuela = conectar.prepareCall("{CALL insertEscuelaPlanEstudios(?,?)}");
      insertarPlanEscuela.setString(1, pCodigoEscuela);
      insertarPlanEscuela.setInt(2,  pNumeroPlan);
      insertarPlanEscuela.execute();
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  //--¿¿HABRÁ QUE ARREGLAR ESTA PICHA??--
  public void insertarCursoAPlan(int pNumeroPlan, String pCodigoCurso, String pNumeroBloque){
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      CallableStatement insertar = conectar.prepareCall("{CALL insertPlanEstudiosCurso(?,?,?)}");
      insertar.setInt(1, pNumeroPlan);
      insertar.setString(2,  pCodigoCurso);
      insertar.setString(3, pNumeroBloque);
      insertar.execute();  
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
}//Fin clase PlanEstudios
