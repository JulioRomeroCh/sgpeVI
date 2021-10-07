package modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import modelo.PlanEstudios;
import modelo.Conexion;
import modelo.Curso;

public class Escuela {//Inicio de la clase Escuela
    
  //Atributos de la clase  
  private String codigoEscuela;
  private String nombreEscuela;
  private ArrayList<PlanEstudios> planes;
  private ArrayList<Curso> cursosAsociados;
  
  /**
   * 
   */
  public Escuela(){
      
    cursosAsociados = new ArrayList<Curso>();  
    planes = new ArrayList<PlanEstudios>();
    
  }
  
  /**
   * 
   * @param pCodigoEscuela
   * @param pNombreEscuela 
   */
  public Escuela (String pCodigoEscuela, String pNombreEscuela){
      
    planes = new ArrayList<PlanEstudios>();
    cursosAsociados = new ArrayList<Curso>();
    setCodigoEscuela(pCodigoEscuela);  
    setNombreEscuela(pNombreEscuela);
    //cursosAsociados = new ArrayList<Curso>();  
    //planes = new ArrayList<PlanEstudios>();
    
  }

  //Métodos accesores
  public String getCodigoEscuela() {
    return codigoEscuela;
  }

  public void setCodigoEscuela(String pCodigoEscuela) {
    this.codigoEscuela = pCodigoEscuela;
  }

  public String getNombreEscuela() {
    return nombreEscuela;
  }
  
  public void setNombreEscuela(String pNombreEscuela) {
    this.nombreEscuela = pNombreEscuela;
  }

  /**
   * 
   * @return salida
   */
  public String toString(){
    String salida = "";
    salida+= "Código escuela: " + getCodigoEscuela() + "\n";
    salida+= "Nombre escuela: " + getNombreEscuela()+ "\n";
    return salida;
  }
  
  /**
   * 
   * @param pNumeroPlan
   * @param pVigenciaPlan 
   */
  public void crearPlanEstudios(int pNumeroPlan, Date pVigenciaPlan){
    PlanEstudios nuevoPlan = new PlanEstudios(pNumeroPlan, pVigenciaPlan);
    planes.add(nuevoPlan);
  }
  
  /**
   * 
   * @param pCurso 
   */
  public void asociarCurso(Curso pCurso){
    cursosAsociados.add(pCurso);  
  }
  
  /**
   * 
   * @param pPlan 
   */
  public void asociarPlan(PlanEstudios pPlan){
    planes.add(pPlan);  
  }
  
  /**
   * 
   * @param pCodigoEscuela
   * @param pNombreEscuela
   * @return 
   */
  public boolean agregarEscuela(String pCodigoEscuela, String pNombreEscuela){
      
    boolean salida = true;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      CallableStatement insertar = conectar.prepareCall("{CALL insertEscuela(?,?)}");
      insertar.setString(1, pCodigoEscuela);
      insertar.setString(2, pNombreEscuela);
      insertar.execute(); 
      salida = true;
    }
    catch(SQLException error){ 
      salida = false;
    }
    return salida;
    
  }

}//Fin de la clase Escuela
