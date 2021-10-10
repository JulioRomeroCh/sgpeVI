package modelo;

//import java.util.Calendar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.text.SimpleDateFormat;
import modelo.Curso;

public class PlanEstudios {//Inicio clase PlanEstudios
  /// 
  //Atributos de la clase
  private int numeroPlan;
  private Date vigenciaPlan;
  private Curso [][] cursosBloque;

  /**
  *<p> Método PlanEstudios: constructor que crea un objeto de tipo PlanEstudios.
  * 
  */
  public PlanEstudios(){
      
  }
  
  /**
  *<p> Método PlanEstudios: constructor que crea una matriz para almacenar los cursos de los bloques, además
  *    usa los métodos set de los atributos.
  *@param pNumeroPlan: int que representa el número que tiene el plan.
  *@param pVigenciaPlan: Date que representa la fecha de vigencia del plan de estudios.
  */
  public PlanEstudios (int pNumeroPlan, Date pVigenciaPlan){
    cursosBloque = new Curso[12][20];
    setNumeroPlan(pNumeroPlan);   
    setVigenciaPlan(pVigenciaPlan); 
  }

  //Métodos accesores
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
  
  /**
  *<p> Método añadirCursos: método que añade un curso a la matriz cursosBloque.
  * @param pCurso: objeto de tipo Curso que se añade a la matriz cursosBloque.
  * @param pBloque: int que representa el bloque del curso, el cual se agrega usando como índice este parámetro.
  */
  public void añadirCursos(Curso pCurso, int pBloque){
    try{
      for(int contador=0;contador<=cursosBloque.length;contador++){
        if (cursosBloque[pBloque][contador]==null){
          cursosBloque[pBloque][contador]=pCurso;
          break;
        }
      }
    }
    catch(Exception error){
      System.err.println("Error: " + error);
    }
  }
  
  /**
  *<p> Método toString: llama a los métodos get de los atributos para colocarlos en 
  *    una misma cadena de caracteres.
  * @return salida: String que posee los valores de cada atributo.
  */
  public String toString(){
    String salida = "";
    salida+= "Número plan: " + getNumeroPlan()+ "\n";
    salida+= "Vigencia plan: " + getVigenciaPlan()+ "\n";
    return salida;
  }
  
  /**
  *<p> Método eliminarCursoDePlan: Método que elimina un curso de la matriz cursosBloque y de la base de datos. 
  *@param pCodigoCurso: String que representa el código del curso que se va a eliminar del plan de estudios.
  *@param pNumeroPlan: String que representa el número del plan al cual se le va a eliminar un curso.
  */
  public void eliminarCursoDePlan(String pCodigoCurso, String pNumeroPlan){
    int resultado;
    PreparedStatement consultaCurso;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar(); 
    
    try{  
      consultaCurso = conectar.prepareStatement("DELETE FROM plan_estudios_curso WHERE codigoCurso = (?) AND numeroPlan = (?)"); 
      consultaCurso.setString(1, pCodigoCurso);
      consultaCurso.setString(2, pNumeroPlan);
      resultado = consultaCurso.executeUpdate();
      
      for (int contador = 0; cursosBloque.length != contador; contador++){
        if (cursosBloque[contador] != null){
          for (int columna = 0; cursosBloque[contador].length != columna; columna++){
            if (cursosBloque[contador][columna] != null){
              if (cursosBloque[contador][columna].getCodigoCurso().equalsIgnoreCase(pCodigoCurso)){
                cursosBloque[contador][columna] = null;
              }
            }     
          }
        }
      }
    }
    catch(Exception error){ 
      System.out.println(error);
    }
    
  }
  
  
  
  
  /**
  *<p> Método agregarPlanEstudios: método que inserta un plan de estudios a la base de datos, además se le asocia
  *    una escuela.
  * @param pNumeroPlan: int que representa el número del plan que se va a insertar.
  * @param pVigenciaPlan: Date que representa la fecha de vigencia del plan de estudios.
  * @param pCodigoEscuela: String que representa la escuela a la cual pertenece el plan de estudios
  * @return salida: Booleano que representa el éxito o fracaso de la inserción.
  */
  public boolean agregarPlanEstudios(int pNumeroPlan, Date pVigenciaPlan, String pCodigoEscuela){
    boolean salida = true;
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
      salida = true;
    }
    catch(Exception error){ 
      salida = false;
    }
    return salida;
  }
  
  /**
  *<p> Método insertarCursosAPlan: método que asocia un curso a un plan de estudios en la base de datos.
  * @param pNumeroPlan: int que representa el número del plan a la que se asocia el curso.
  * @param pCodigoCurso: String que representa el código del curso al cual se le asocia un plan de estudios.
  * @param pNumeroBloque:String que representa el bloque del curso dentro del plan de estudios.
  */
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
