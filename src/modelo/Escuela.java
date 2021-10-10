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
  ///
  //Atributos de la clase  
  private String codigoEscuela;
  private String nombreEscuela;
  private ArrayList<PlanEstudios> planes;
  private ArrayList<Curso> cursosAsociados;
  
  /**
  *<p> Método Escuela: Constructor de la clase que inicializa los arrayList: requisitos y correquisitos.
  */
  public Escuela(){
      
    cursosAsociados = new ArrayList<Curso>();  
    planes = new ArrayList<PlanEstudios>();
    
  }
  
  /**
  *<p> Método Escuela: Constructor de la clase que inicializa los arrayList: requisitos y correquisitos,
  *    además establece los atributos por medio de los métodos set.
  * @param pCodigoEscuela: String que representa el código de la escuela.
  * @param pNombreEscuela: String que representa el nombre de la escuela.
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
  *<p> Método toString: llama a los métodos get de los atributos para colocarlos en 
  *    una misma cadena de caracteres.
  * @return salida: String que posee los valores de cada atributo.
  */
  public String toString(){
    String salida = "";
    salida+= "Código escuela: " + getCodigoEscuela() + "\n";
    salida+= "Nombre escuela: " + getNombreEscuela()+ "\n";
    return salida;
  }
  
  /**
  *<p> Método crearPlanEstudios: crea un nuevo plan de estudios y lo agrega al array planes.
  * @param pNumeroPlan: int que representa el número del nuevo plan.
  * @param pVigenciaPlan: Date que representa la vigencia del plan.
  */
  public void crearPlanEstudios(int pNumeroPlan, Date pVigenciaPlan){
    PlanEstudios nuevoPlan = new PlanEstudios(pNumeroPlan, pVigenciaPlan);
    planes.add(nuevoPlan);
  }
  
  /**
  *<p> Método asociarCurso: agrega un curso a una escuela, por medio del array cursosAsociados.
  * @param pCurso: Objeto de tipo curso que se añade al array cursosAsociados.
  */
  public void asociarCurso(Curso pCurso){
    cursosAsociados.add(pCurso);  
  }
  
  /**
  *<p> Método asociarPlan: asocia un plan con una escuela, por medio del array planes.
  * @param pPlan: Objeto de tipo PlanEstudios que se añade al array planes.
  */
  public void asociarPlan(PlanEstudios pPlan){
    planes.add(pPlan);  
  }
  
  /**
  *<p> Método agregarEscuela: asocia una escuela con el plan de estudios en la base de datos.
  * @param pCodigoEscuela: String que representa el código de la escuela que será asociada al plan.
  * @param pNombreEscuela: String que representa el nombre de la escuela que será asociada al plan.
  * @return salida: Booleano que representa el éxito o fracaso de la inserción.
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
