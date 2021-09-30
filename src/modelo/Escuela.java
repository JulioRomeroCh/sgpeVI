package modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import modelo.PlanEstudios;
import modelo.Conexion;
import modelo.Curso;

public class Escuela {
    
  private String codigoEscuela;
  private String nombreEscuela;
  private ArrayList<PlanEstudios> planes;
  private ArrayList<Curso> cursosAsociados;
  
  public Escuela(){
    planes = new ArrayList<PlanEstudios>();
    cursosAsociados = new ArrayList<Curso>();   
  }
  
  public Escuela (String pCodigoEscuela, String pNombreEscuela){
      
    //planes = new ArrayList<PlanEstudios>();
    //cursosAsociados = new ArrayList<Curso>();
    setCodigoEscuela(pCodigoEscuela);  
    setNombreEscuela(pNombreEscuela);
  }

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
  /*
  public ArrayList <Curso> getCursosAsociados() {
    return cursosAsociados;
  }


  public void setCursosAsociados(ArrayList <Curso> cursosAsociados) {
    this.cursosAsociados = cursosAsociados;
  }
  */
  public void crearPlanEstudios(int pNumeroPlan, Date pVigenciaPlan){
    PlanEstudios nuevoPlan = new PlanEstudios(pNumeroPlan, pVigenciaPlan);
    planes.add(nuevoPlan);
  }
  
  public String toString(){
    String salida = "";
    salida+= "Código escuela: " + getCodigoEscuela() + "\n";
    salida+= "Nombre escuela: " + getNombreEscuela()+ "\n";
    return salida;
  }
  
  public void asociarCurso(Curso pCurso){
    cursosAsociados.add(pCurso);  
  }
  
  public void insertarEscuela(String pCodigoEscuela, String pNombreEscuela){
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      CallableStatement insertar = conectar.prepareCall("{CALL insertEscuela(?,?)}");
      insertar.setString(1, pCodigoEscuela);
      insertar.setString(2, pNombreEscuela);
      insertar.execute(); 
    }
    catch(SQLException error){ 
        System.out.println(error);
    }
  }

  

}
