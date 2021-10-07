package modelo;

import com.mysql.cj.protocol.Resultset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import vista.Formulario;

public class Curso {//Inicio clase Curso
    
  private String codigoCurso;
  private String nombreCurso;
  private int cantidadCreditos;
  private int cantidadHorasLectivas;
  private ArrayList <Curso> requisitos;
  private ArrayList <Curso> correquisitos;
  private ArrayList <PlanEstudios> planesAsociados;
  
  /**
   * 
   */
  public Curso(){
  
    requisitos = new ArrayList <Curso>();
    correquisitos = new ArrayList <Curso>();
    planesAsociados = new ArrayList<PlanEstudios>();
    
  }
  
  /**
   * 
   * @param pCodigoCurso
   * @param pNombreCurso
   * @param pCantidadCreditos
   * @param pCantidadHorasLectivas 
   */
  public Curso(String pCodigoCurso, String pNombreCurso, int pCantidadCreditos, int pCantidadHorasLectivas){
    requisitos = new ArrayList <Curso>();
    correquisitos = new ArrayList <Curso>();
    planesAsociados = new ArrayList<PlanEstudios>();
    setCodigoCurso(pCodigoCurso); 
    setNombreCurso(pNombreCurso);
    setCantidadCreditos(pCantidadCreditos);
    setCantidadHorasLectivas(pCantidadHorasLectivas);
    //setEscuelaAsociada(pEscuela);  
  }

  //Métodos accesores
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
    
  public void añadirCorrequisitos(Curso pCorrequisito){
    correquisitos.add(pCorrequisito);
  }
 
  /**
   * 
   * @return salida
   */
  public String toString(){
    String salida = "";
    salida+= "Código curso: " + getCodigoCurso()+ "\n";
    salida+= "Nombre curso: " + getNombreCurso()+ "\n";
    salida+= "Cantidad créditos: " + getCantidadCreditos()+ "\n";
    salida+= "Cantidad horas lectivas: " + getCantidadHorasLectivas()+ "\n";
    return salida;
  }
  
  /**
   * 
   * @param pPlan 
   */
  public void asociarPlan(PlanEstudios pPlan){
    planesAsociados.add(pPlan);
  }
  
  /**
   * 
   * @param pCodigoCurso
   * @param pCodigoRequisito 
   */
  public void eliminarRequisito(String pCodigoCurso, String pCodigoRequisito){
    int resultado;
    PreparedStatement consultaCurso;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar(); 
    
    try{  
      consultaCurso = conectar.prepareStatement("DELETE FROM requisito_curso WHERE codigoCurso = (?) AND codigoRequisito = (?)"); 
      consultaCurso.setString(1, pCodigoCurso);
      consultaCurso.setString(2, pCodigoRequisito);
      resultado = consultaCurso.executeUpdate();
      
      for (int contador = 0; requisitos.size() != contador; contador++){
          
        if (requisitos.get(contador).getCodigoCurso().equalsIgnoreCase(pCodigoRequisito)){
          requisitos.remove(requisitos.get(contador));
        }
        
     }
      
    }
    
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
  /**
   * 
   * @param pCodigoCurso
   * @return salida
   * @throws Exception 
   */
  public boolean eliminarCurso(String pCodigoCurso) throws Exception{
    int resultado;
    boolean salida = true;
    PreparedStatement consultaCurso;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar(); 
   
    if (planesAsociados != null || planesAsociados.isEmpty() == false){
      for (int indice = 0; planesAsociados.size() != indice; indice++){
        if (planesAsociados.get(indice) != null){
          salida = false;
          break;
        }
        else{
          salida = true;
        }
      }
    }
    if (salida == true){ 
      consultaCurso = conectar.prepareStatement("DELETE FROM curso WHERE codigoCurso = (?)"); 
      consultaCurso.setString(1, pCodigoCurso);
      resultado = consultaCurso.executeUpdate();
    }
 
    else{
      salida = false;
    } 
    return salida;
  }
  
  /**
   * 
   * @param pCodigoCurso
   * @param pNombreCurso
   * @param pCantidadCreditos
   * @param pCantidadHoras
   * @param pCodigoEscuela
   * @return salida
   */
  public boolean insertarCurso(String pCodigoCurso, String pNombreCurso, int pCantidadCreditos, int pCantidadHoras, 
      String pCodigoEscuela){
    boolean salida = true;
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
      salida = true;
    }
    catch(Exception error){ 
        salida = false;
    }
    return salida;
  }

  /**
   * 
   * @param pCodigoCurso 
   */
  public void anadirCorrequisito(String pCodigoCurso){
      
    PreparedStatement insertar;
    ResultSet resultado;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      insertar = conectar.prepareCall("{CALL cargarDatosCurso(?)}");
      insertar.setString(1, pCodigoCurso);
      resultado = insertar.executeQuery();
      while(resultado.next()){ 
        String codigoCurso = String.valueOf(resultado.getObject(1));
        String nombreCurso = String.valueOf(resultado.getObject(2));
        int cantidadCreditos = Integer.parseInt(String.valueOf(resultado.getObject(3)));
        int cantidadHoras = Integer.parseInt(String.valueOf(resultado.getObject(4)));       
        Curso nuevoCurso = new Curso (codigoCurso, nombreCurso, cantidadCreditos, cantidadHoras);
        correquisitos.add(nuevoCurso);   
      }
    }
    catch(Exception error){ 
          System.err.println(error);
    } 
    
  }
   
  /**
   * 
   * @param pCodigoCurso
   * @param pCodigoCorrequisito 
   */
  public void asignarCorrequisito(String pCodigoCurso, String pCodigoCorrequisito){
      
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      CallableStatement insertar = conectar.prepareCall("{CALL insertCorrequisitoCurso(?,?)}");
      insertar.setString(1, pCodigoCurso);
      insertar.setString(2, pCodigoCorrequisito);
      insertar.execute(); 
    }
    catch(Exception error){ 
      System.out.println(error);
    } 
    
  }
   
  /**
   * 
   * @param pCodigoCurso 
   */
  public void anadirRequisito(String pCodigoCurso){
      
    PreparedStatement insertar;
    ResultSet resultado;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      insertar = conectar.prepareCall("{CALL cargarDatosCurso(?)}");
      insertar.setString(1, pCodigoCurso);
      resultado = insertar.executeQuery();
      while(resultado.next()){ 
        String codigoCurso = String.valueOf(resultado.getObject(1));
        String nombreCurso = String.valueOf(resultado.getObject(2));
        int cantidadCreditos = Integer.parseInt(String.valueOf(resultado.getObject(3)));
        int cantidadHoras = Integer.parseInt(String.valueOf(resultado.getObject(4)));       
        Curso nuevoCurso = new Curso (codigoCurso, nombreCurso, cantidadCreditos, cantidadHoras);
        requisitos.add(nuevoCurso);       
      }
    }
    catch(Exception error){ 
      System.err.println(error);
    } 
    
  }
  
  /**
   * 
   * @param pCodigoCurso
   * @param pCodigoRequisito 
   */
  public void asignarRequisito(String pCodigoCurso, String pCodigoRequisito){
      
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      CallableStatement insertar = conectar.prepareCall("{CALL insertRequisitoCurso(?,?)}");
      insertar.setString(1, pCodigoCurso);
      insertar.setString(2, pCodigoRequisito);
      insertar.execute();  
    }
    catch(Exception error){ 
      System.out.println(error);
    }
    
  }
  
}//Fin clase Curso
