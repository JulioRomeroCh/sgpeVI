package modelo;

import com.mysql.cj.protocol.Resultset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JComboBox;
import vista.Formulario;

public class Curso {//Inicio clase Curso
    
  private String codigoCurso;
  private String nombreCurso;
  private int cantidadCreditos;
  private int cantidadHorasLectivas;
  private ArrayList <Curso> requisitos;
  private ArrayList <Curso> correquisitos;
  
  public Curso(){
      
  }
  
  public Curso(String pCodigoCurso, String pNombreCurso, int pCantidadCreditos, int pCantidadHorasLectivas){
    
    setCodigoCurso(pCodigoCurso); 
    setNombreCurso(pNombreCurso);
    setCantidadCreditos(pCantidadCreditos);
    setCantidadHorasLectivas(pCantidadHorasLectivas);
    //setEscuelaAsociada(pEscuela);
    
  }

  public String getCodigoCurso() {
    return codigoCurso;
  }

  public void setCodigoCurso(String pCodigoCurso) {
    this.codigoCurso = pCodigoCurso;
  }
  
  public String getNombreCurso() {
    return nombreCurso;
  }

  public void setNombreCurso(String pNombreCurso) {
    this.nombreCurso = pNombreCurso;
  }

  public int getCantidadCreditos() {
    return cantidadCreditos;
  }

  public void setCantidadCreditos(int pCantidadCreditos) {
    this.cantidadCreditos = pCantidadCreditos;
  }

  public int getCantidadHorasLectivas() {
    return cantidadHorasLectivas;
  }

  public void setCantidadHorasLectivas(int pCantidadHorasLectivas) {
    this.cantidadHorasLectivas = pCantidadHorasLectivas;
  } 
    
  public void añadirRequisitos(Curso pRequisito){
    requisitos.add(pRequisito);
  }
    
  public void añadirCursos(Curso pCorrequisito){
    correquisitos.add(pCorrequisito);
  }
  
  public String toString(){
    String salida = "";
    salida+= "Código curso: " + getCodigoCurso()+ "\n";
    salida+= "Nombre curso: " + getNombreCurso()+ "\n";
    salida+= "Cantidad créditos: " + getCantidadCreditos()+ "\n";
    salida+= "Cantidad horas lectivas: " + getCantidadHorasLectivas()+ "\n";
    return salida;
  }
   
  public void insertarCurso(String pCodigoCurso, String pNombreCurso, int pCantidadCreditos, int pCantidadHoras, String pCodigoEscuela){
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      CallableStatement insertar = conectar.prepareCall("{CALL insertCurso(?,?,?,?)}");
      insertar.setString(1, pCodigoCurso);
      insertar.setString(2, pNombreCurso);
      insertar.setInt(3, pCantidadCreditos);
      insertar.setInt(4, pCantidadHoras);
      insertar.execute();  
      CallableStatement asignarEscuela = conectar.prepareCall("{CALL insertEscuelaCurso(?,?)}");
      asignarEscuela.setString(1, pCodigoEscuela);
      asignarEscuela.setString(2, pCodigoCurso);
      asignarEscuela.execute();
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  /*
    public void insertarCursoAEscuela(String pCodigoEscuela, String pCodigoCurso){
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();    
    try{ 
      CallableStatement asignarEscuela = conectar.prepareCall("{CALL insertEscuelaCurso(?,?)}");
      asignarEscuela.setString(1, pCodigoEscuela);
      asignarEscuela.setString(2, pCodigoCurso);
      asignarEscuela.execute();
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }*/
  
  public void cargaEscuelaPropietaria(JComboBox BoxEscuelaPropietaria){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxEscuelaPropietaria.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT escuela.codigoEscuela FROM escuela JOIN escuela_plan_estudios ON escuela.codigoEscuela = escuela_plan_estudios.codigoEscuela");
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          BoxEscuelaPropietaria.addItem(resultado.getObject(indice));
        }   
      } 
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
  public void cargaPlanesRegistrarCursos(JComboBox BoxPlanRegistroCurso){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxPlanRegistroCurso.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT numeroPlan FROM plan_estudios");
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          BoxPlanRegistroCurso.addItem(resultado.getObject(indice));
        }   
      } 
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }

}//Fin clase Curso
