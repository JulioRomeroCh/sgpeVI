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
  *<p> Método Curso: constructor que inicializa los arrayList: requisitos, correquisitos 
  *    y planes asociados.
  */
  public Curso(){
  
    requisitos = new ArrayList <Curso>();
    correquisitos = new ArrayList <Curso>();
    planesAsociados = new ArrayList<PlanEstudios>();
    
  }
  
  /** 
  *<p> Método Curso: constructor que llama a los métodos set de los atributos y les 
  *    asigna el valor de cada parámetro.
  * @param pCodigoCurso: String que representa el código del curso.
  * @param pNombreCurso: String que representa el nombre del curso.
  * @param pCantidadCreditos: int que representa la cantidad de créditos del curso.
  * @param pCantidadHorasLectivas: int que representa las horas lectivas que posee el curso.
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
  *<p> Método toString: llama a los métodos get de los atributos para colocarlos en 
  *    una misma cadena de caracteres.
  * @return salida: String que posee los valores de cada atributo.
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
  *<p> Método asociarPlan: agrega un nuevo plan al array List de planesAsociados.
  * @param pPlan: Objeto de tipo PlanEstudios que se va a agregar a los planes asociados.
  */
  public void asociarPlan(PlanEstudios pPlan){
    planesAsociados.add(pPlan);
  }
  
  /**
  *<p> Método eliminarRequisito: elimina una relación de requisito entre dos cursos,
  *    usando el ArrayList de requisitos.
  * @param pCodigoCurso: String que representa el curso que tiene asignado el requisito.
  * @param pCodigoRequisito: String que representa el código del curso que es requisito. 
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

  public void eliminarPlanDeCurso(String pNumeroPlan){
      for (int contador = 1; planesAsociados.size() != contador; contador++){
        if (planesAsociados.get(contador).getNumeroPlan() ==Integer.parseInt(pNumeroPlan)){
            planesAsociados.remove(planesAsociados.get(contador));
        }
      }
  }

  
  
  /**
  *<p> Método eliminarCurso: método que verifica si un curso no tiene asociado un plan para 
  *    eliminarlos.
  * @param pCodigoCurso: representa el codigo del curso que se quiere eliminar.
  * @return salida: booleano que representa si un curso se puede eliminar o no.
  * @throws Exception: Excepción en caso de que la consulta presenta un error.
  */
  public boolean eliminarCurso(String pCodigoCurso) throws Exception{
    int resultado;
    boolean salida = true;
    PreparedStatement consultaCurso;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar(); 
   
    if (planesAsociados.isEmpty()){
        System.out.println("Array: " + planesAsociados.toString());
      System.err.println("LOS PLANES NO ESTÁN VACÍOS");  
      /*for (int indice = 0; planesAsociados.size() != indice; indice++){
        planesAsociados.remove(this)
        /*if (planesAsociados.get(indice) != null){
          salida = false;
          System.err.println("SALIDA FALSE");
          break;
        }
        else{
          salida = true;
          System.err.println("SALIDA TRUE");
        }
      }*/
      salida = false;
    }
    if (salida == true){ 
      System.err.println("ENTRÉ AL DELETE");
      System.err.println("Size" + planesAsociados.size());
      if(planesAsociados.size() == 1){
        planesAsociados.remove(0); 
      }
      else{
        for (int indice = 0; planesAsociados.size() != indice; indice++){
          planesAsociados.remove(indice);        
        }    
      }

      consultaCurso = conectar.prepareStatement("DELETE FROM curso WHERE codigoCurso = (?)"); 
      consultaCurso.setString(1, pCodigoCurso);
      resultado = consultaCurso.executeUpdate();
    }
 
    /*else{
      salida = false;
    } */
    return salida;
  }
  
  /**
  *<p> Método insertarCurso: Método que inserta un curso y lo asocia a una escuela en la base de 
  *    datos.
  * @param pCodigoCurso: String que representa el código del curso.
  * @param pNombreCurso: String que representa el nombre del curso.
  * @param pCantidadCreditos: int que representa la cantidad de créditos del curso.
  * @param pCantidadHoras: int que representa la cantidad de horas lectivas.
  * @param pCodigoEscuela: String que contiene el código de la escuela al que pertenece el curso.
  * @return salida: booleano que representa el éxito o fracaso de la inserción.
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
  *<p> Método anadirCorrequisito: Método que busca en la base de datos la información del correquisito y lo
  *    asocia al curso usando el arreglo de correquisitos.
  *@param pCodigoCurso: String que representa el código del curso que es correquisito.
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
  *<p> Método asignarCorrequisito: método que asocia un correquisito a un curso usando la base de datos.
  * @param pCodigoCurso: String que representa el codigo del curso al que se le asocia el correquisito.
  * @param pCodigoCorrequisito: String que representa el código del curso que es correquisito.
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
  *<p> Método anadirRequisito: Método que busca en la base de datos la información del requisito y lo
  *    asocia al curso usando el arreglo de requisitos.
  * @param pCodigoCurso: String que representa el código del curso que es requisito.
  */
  public void anadirRequisito(String pCodigoCurso){
      
    PreparedStatement insertar;
    ResultSet resultado;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    try{
      insertar = conectar.prepareCall("{CALL cargarDatosCurso(?)}");
      //insertar = conectar.prepareStatement("SELECT * FROM curso WHERE codigoCurso != (?)");
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
  *<p> Método asignarRequisito: método que asocia un requisito a un curso usando la base de datos.
  * @param pCodigoCurso: String que representa el codigo del curso al que se le asocia el requisito.
  * @param pCodigoRequisito: String que representa el código del curso que es requisito.
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
