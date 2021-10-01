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
  //private ArrayList ArrayList<Curso> cursosBloque;
  //private Curso [][] cursosBloque;

  
  public PlanEstudios(){
      
  }
  
  public PlanEstudios (int pNumeroPlan, Date pVigenciaPlan){
    
    //cursosBloque = new Curso[10][15];
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
  
  /*public void añadirCursos(Curso pCurso, int pBloque){
    //cursosBloque.(pCurso);
    try{

    cursosBloque [pBloque+1][cursosBloque[pBloque+1].length]=pCurso;

    }
    catch(Exception error){
        System.out.println("Index matriz: " + pBloque+1);
        System.out.println("Error: " + error);
    }
  }*/
  
  
  public String toString(){
    String salida = "";
    salida+= "Número plan: " + getNumeroPlan()+ "\n";
    salida+= "Vigencia plan: " + getVigenciaPlan()+ "\n";
    return salida;
  }
  
  public void insertarPlanEstudios(int pNumeroPlan, Date pVigenciaPlan, String pCodigoEscuela){
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
  
  public void cargaEscuelaRegistrarPlan(JComboBox BoxEscuelaPropietariaPlan){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxEscuelaPropietariaPlan.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT codigoEscuela FROM escuela");
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          BoxEscuelaPropietariaPlan.addItem(resultado.getObject(indice));
        }   
      } 
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
  public void cargaCodigosPlanes(JComboBox BoxCodigoPlanCursoPlan){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxCodigoPlanCursoPlan.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT numeroPlan FROM plan_estudios;");
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          BoxCodigoPlanCursoPlan.addItem(resultado.getObject(indice));
        }   
      } 
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
  public void cargaCodigosCursos(JComboBox BoxCodigoCursoCursoPlan){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxCodigoCursoCursoPlan.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT codigoCurso FROM curso;");
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          BoxCodigoCursoCursoPlan.addItem(resultado.getObject(indice));
        }   
      } 
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
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
