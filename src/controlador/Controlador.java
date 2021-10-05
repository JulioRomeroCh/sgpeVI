package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Conexion;
import modelo.Curso;
import modelo.Escuela;
import modelo.PlanEstudios;
import vista.Formulario;
import modelo.Pdf;
import modelo.Email;

public class Controlador implements ActionListener{
  
  private ArrayList <Curso> cursos;
  private ArrayList <Escuela> escuelas;
  private ArrayList <PlanEstudios> planes;
  
  private Curso curso;
  private Escuela escuela;
  private PlanEstudios plan;
  //private Conexion conexion;
  private Formulario vista;
    

  public Controlador (Curso pCurso, Escuela pEscuela, PlanEstudios pPlan, Formulario pVista){
     
     this.curso = pCurso;
     this.escuela = pEscuela;
     this.plan = pPlan;
     this.vista = pVista;
     //this.vista.principalRegistrarEscuela.addActionListener(this);
     this.vista.botonRegistrarEscuela.addActionListener(this);
     this.vista.botonRegistrarCurso.addActionListener(this);
     this.vista.botonRegistrarPlan.addActionListener(this);
     this.vista.botonCargarPlanes.addActionListener(this);
     this.vista.botonCargarInformacionPlan.addActionListener(this);
     this.vista.botonEnviarPDFCorreo.addActionListener(this);
     this.vista.botonRegistrarPlanAEscuela.addActionListener(this);
     this.vista.botonCargarPlanRegistroCursos.addActionListener(this);
     this.vista.botonRegistrarCoRequisito.addActionListener(this);
     this.vista.botonCargarCursoAsignarRequisito.addActionListener(this);
     this.vista.botonCargarRequisitoCorrequisito.addActionListener(this);
     this.vista.botonRegistrarRequisito.addActionListener(this);
     this.vista.botonRegistrarCorrequisito.addActionListener(this);
     this.vista.botonBuscarPlanesCurso.addActionListener(this);
     this.vista.botonBuscarRequisitosCurso.addActionListener(this);
     this.vista.botonBuscarCorrequisitosCurso.addActionListener(this);
     this.vista.botonEliminaciones.addActionListener(this);
     this.vista.botonCargarRequisitoEliminaciones.addActionListener(this);
     this.vista.botonEliminarRequisito.addActionListener(this);
     this.vista.botonEliminarCurso.addActionListener(this);
     this.vista.botonCargarCursosEliminaciones.addActionListener(this);
     this.vista.botonEliminarCursoDos.addActionListener(this);
     this.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria, vista.BoxEscuelaPropietariaConsultaPlan);
     this.cursos = new ArrayList<Curso>();
     this.escuelas = new ArrayList<Escuela>();
     this.planes = new ArrayList<PlanEstudios>();
     //this.plan.cargaCodigosPlanes(vista.BoxCodigoPlanCursoPlan);
     //this.plan.cargaCodigosCursos(vista.BoxCodigoCursoCursoPlan);
     //public void cargaPlanesRegistrarCursos(JComboBox BoxPlanRegistroCurso){
     //this.curso.cargaPlanesRegistrarCursos(vista.BoxPlanRegistroCurso);
  }
  
  public void iniciar(){
   
  }
 
  /*
    DELETE FROM requisito_curso WHERE codigoCurso = 'TI1000' AND codigoRequisito = 'TI2000';

    DELETE FROM plan_estudios_curso WHERE codigoCurso = 'AE1234' AND numeroPlan = '1800';

    DELETE FROM curso WHERE codigoCurso = 'AE1234';
  */
  

  
  public void primerConsulta(JTable tablaPrimerConsulta, String pCodigoCurso){
      
      DefaultTableModel modeloTabla = (DefaultTableModel) tablaPrimerConsulta.getModel();
      modeloTabla.setRowCount(0);
      PreparedStatement consultaInfo;
      ResultSet resultado;
      ResultSetMetaData datosResultado;
      int columnas;
      
      try{
          
        Conexion nuevaConexion = new Conexion();
        Connection conectar = nuevaConexion.conectar();
        
        consultaInfo = conectar.prepareStatement("SELECT plan_estudios.numeroPlan FROM plan_estudios JOIN plan_estudios_curso ON plan_estudios.numeroPlan = plan_estudios_curso.numeroPlan WHERE plan_estudios_curso.codigoCurso = (?)");
        consultaInfo.setString(1, pCodigoCurso);
        resultado = consultaInfo.executeQuery();
        datosResultado = resultado.getMetaData();
        columnas = datosResultado.getColumnCount();
        
        while(resultado.next()){
          Object [] fila = new Object[columnas];
          for(int indice = 0; indice<columnas; indice++){
            fila[indice] = resultado.getObject(indice +1);
          }
          modeloTabla.addRow(fila);
        }
      }
      catch(Exception error){
          System.out.println(error);    
      }
  }
  
  
  public void segundaConsulta(JTable tablaSegundaConsulta, String pCodigoCurso){
      
      DefaultTableModel modeloTabla = (DefaultTableModel) tablaSegundaConsulta.getModel();
      modeloTabla.setRowCount(0);
      PreparedStatement consultaInfo;
      ResultSet resultado;
      ResultSetMetaData datosResultado;
      int columnas;
      
      try{
          
        Conexion nuevaConexion = new Conexion();
        Connection conectar = nuevaConexion.conectar();
        
        consultaInfo = conectar.prepareStatement("SELECT curso.codigoCurso, curso.nombreCurso, curso.cantidadCreditos, curso.cantidadHorasLectivas FROM curso WHERE codigoCurso = (SELECT requisito_curso.codigoRequisito FROM requisito_curso WHERE requisito_curso.codigoCurso = (?))");
        consultaInfo.setString(1, pCodigoCurso);
        resultado = consultaInfo.executeQuery();
        datosResultado = resultado.getMetaData();
        columnas = datosResultado.getColumnCount();
        
        while(resultado.next()){
          Object [] fila = new Object[columnas];
          for(int indice = 0; indice<columnas; indice++){
            fila[indice] = resultado.getObject(indice +1);
          }
          modeloTabla.addRow(fila);
        }
      }
      catch(Exception error){
          System.out.println(error);    
      }
  }
  
  
  public void tercerConsulta(JTable tablaTerceraConsulta, String pCodigoCurso){
      
      DefaultTableModel modeloTabla = (DefaultTableModel) tablaTerceraConsulta.getModel();
      modeloTabla.setRowCount(0);
      PreparedStatement consultaInfo;
      ResultSet resultado;
      ResultSetMetaData datosResultado;
      int columnas;
      
      try{
          
        Conexion nuevaConexion = new Conexion();
        Connection conectar = nuevaConexion.conectar();
        
        consultaInfo = conectar.prepareStatement("SELECT curso.codigoCurso, curso.nombreCurso, curso.cantidadCreditos, curso.cantidadHorasLectivas FROM curso WHERE codigoCurso = (SELECT correquisito_curso.codigoCorrequisito FROM correquisito_curso WHERE correquisito_curso.codigoCurso = (?))");
        consultaInfo.setString(1, pCodigoCurso);
        resultado = consultaInfo.executeQuery();
        datosResultado = resultado.getMetaData();
        columnas = datosResultado.getColumnCount();
        
        while(resultado.next()){
          Object [] fila = new Object[columnas];
          for(int indice = 0; indice<columnas; indice++){
            fila[indice] = resultado.getObject(indice +1);
          }
          modeloTabla.addRow(fila);
        }
      }
      catch(Exception error){
          System.out.println(error);    
      }
  }
  
  
  public void insertarCurso(){   
   curso.setCodigoCurso(vista.textCodigoCurso.getText());
   curso.setNombreCurso(vista.textNombreCurso.getText());
   curso.setCantidadCreditos(Integer.parseInt(String.valueOf(vista.BoxCantidadCreditos.getSelectedItem())));
   curso.setCantidadHorasLectivas(Integer.parseInt(String.valueOf(vista.boxCantidadHoras.getSelectedItem()))); 
   
   String codigoCurso = curso.getCodigoCurso();
   String nombreCurso = curso.getNombreCurso();
   int cantidadCreditos = curso.getCantidadCreditos();
   int cantidadHoras = curso.getCantidadHorasLectivas();
   String codigoEscuela = String.valueOf(vista.BoxEscuelaPropietaria.getSelectedItem());
   
   Curso nuevoCurso = new Curso (codigoCurso, nombreCurso, cantidadCreditos, cantidadHoras);
   cursos.add(nuevoCurso);
   curso.insertarCurso(codigoCurso, nombreCurso, cantidadCreditos, cantidadHoras, codigoEscuela);

   Escuela unaEscuela = buscarEscuela(String.valueOf(vista.BoxEscuelaPropietaria.getSelectedItem()));
   unaEscuela.asociarCurso(nuevoCurso);
   
   PlanEstudios unPlan = buscarPlanEstudios(String.valueOf(vista.BoxPlanRegistroCurso.getSelectedItem()));
   unaEscuela.asociarPlan(unPlan);
   
   //
   unPlan.añadirCursos(nuevoCurso, vista.BoxBloqueRegistrarCurso.getSelectedIndex());
   unPlan.insertarCursoAPlan(Integer.parseInt(String.valueOf(vista.BoxPlanRegistroCurso.getSelectedItem())), codigoCurso, String.valueOf(vista.BoxBloqueRegistrarCurso.getSelectedItem())); 
   
   nuevoCurso.asociarPlan(unPlan);
  }
  
  
  public void insertarPlan() throws ParseException{  
   SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");  
   plan.setNumeroPlan(Integer.parseInt(vista.textNumeroPlan.getText()));
   plan.setVigenciaPlan(formatoFecha.parse(vista.textVigencia.getText()));
   
   int numeroPlan = plan.getNumeroPlan();
   String codigoEscuela = String.valueOf(vista.BoxEscuelaPropietariaPlan.getSelectedItem());
   Date vigencia = formatoFecha.parse(plan.getVigenciaPlan());
   java.sql.Date fechaSQl = new java.sql.Date(vigencia.getTime());
   
   plan.agregarPlanEstudios(numeroPlan, fechaSQl, codigoEscuela);
   PlanEstudios nuevoPlan = new PlanEstudios(numeroPlan, vigencia);
   planes.add(nuevoPlan);

   this.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria, vista.BoxEscuelaPropietariaConsultaPlan);
  }
  
  
  public void insertarEscuela(){ 
   escuela.setCodigoEscuela(vista.textCodigoEscuela.getText());
   escuela.setNombreEscuela(vista.textNombreEscuela.getText());
   String codigoEscuela = escuela.getCodigoEscuela();
   String nombreEscuela = escuela.getNombreEscuela();  
   if(vista.textCodigoEscuela.getText().length() >2){
     JOptionPane.showMessageDialog(null, "Escuela NO registrada, el código excede los dos caracteres");  
   }
   else{
     escuela.agregarEscuela(codigoEscuela, nombreEscuela);  
     Escuela nuevaEscuela = new Escuela(codigoEscuela, nombreEscuela);
     escuelas.add(nuevaEscuela);
     JOptionPane.showMessageDialog(null, "Escuela registrada con éxito");
     this.cargaEscuelaRegistrarPlan(vista.BoxEscuelaPropietariaPlan);
     vista.textCodigoEscuela.setText("");
     vista.textNombreEscuela.setText("");
   }  
  }
  
  
  public Curso buscarCurso(String codigoCurso){
    Curso curso = new Curso();
      for (int contador = 0; contador != cursos.size(); contador++){
        if (cursos.get(contador).getCodigoCurso().equalsIgnoreCase(codigoCurso)){
          curso = cursos.get(contador);
        }
      }
    return curso;
  }
  
  
  public Escuela buscarEscuela(String codigoEscuela){
    Escuela escuela = new Escuela();
      for (int contador = 0; contador != escuelas.size(); contador++){
        if (escuelas.get(contador).getCodigoEscuela().equalsIgnoreCase(codigoEscuela)){
          escuela = escuelas.get(contador);
        }
      }
    return escuela;
  }
  
  
  public PlanEstudios buscarPlanEstudios(String numeroPlan){
    PlanEstudios plan = new PlanEstudios();
      for (int contador = 0; contador != planes.size(); contador++){
        if (planes.get(contador).getNumeroPlan() == Integer.parseInt(numeroPlan)){
          plan = planes.get(contador);
        }
      }
    return plan;
  }
  
  
  public void cargarInformacionPlan(JTable tablaInformacionPlan, String pCodigoPlan){
      DefaultTableModel modeloTabla = (DefaultTableModel) tablaInformacionPlan.getModel();
      modeloTabla.setRowCount(0);
      PreparedStatement consultaInfo;
      PreparedStatement consultaVigencia;
      PreparedStatement consultaCreditos;
      PreparedStatement consultaCursos;
      ResultSet resultado;
      ResultSet resultadoVigencia;
      ResultSet resultadoCreditos;
      ResultSet resultadoCursos;
      ResultSetMetaData datosResultado;
      int columnas;
      try{
        Conexion nuevaConexion = new Conexion();
        Connection conectar = nuevaConexion.conectar();
        
        consultaInfo = conectar.prepareCall("{CALL selectPlanEstudiosParaPDF (?)}");
        consultaInfo.setString(1, pCodigoPlan);
        resultado = consultaInfo.executeQuery();
        datosResultado = resultado.getMetaData();
        columnas = datosResultado.getColumnCount();
        
        while(resultado.next()){
          Object [] fila = new Object[columnas];
          for(int indice = 0; indice<columnas; indice++){
            fila[indice] = resultado.getObject(indice +1);
          }
          modeloTabla.addRow(fila);
        }

        consultaVigencia = conectar.prepareStatement("SELECT vigenciaPlan FROM plan_estudios WHERE numeroPlan = (?)");
        consultaVigencia.setString(1, pCodigoPlan);
        resultadoVigencia = consultaVigencia.executeQuery();
        while(resultadoVigencia.next()){
            vista.textVigenciaPlanConsultaPlan.setText(String.valueOf(resultadoVigencia.getObject(1)));
        }
        
        consultaCreditos = conectar.prepareCall("{CALL totalCreditos (?)}");
        consultaCreditos.setString(1, pCodigoPlan);
        resultadoCreditos = consultaCreditos.executeQuery();
        while(resultadoCreditos.next()){
          vista.labelTotalCreditos.setText(String.valueOf(resultadoCreditos.getObject(1)));
        }
        
        consultaCursos = conectar.prepareCall("{CALL totalCursos (?)}");
        consultaCursos.setString(1, pCodigoPlan);
        resultadoCursos = consultaCursos.executeQuery();
        while(resultadoCursos.next()){
          vista.labelTotalCursos.setText(String.valueOf(resultadoCursos.getObject(1)));
        }
        
        
      }
      catch(Exception error){
          System.out.println(error);    
      }
  }
  

  public void cargarPlanesConsulta(JComboBox boxCodigoPlanConsulta, String codigoEscuela){
    ResultSet resultado;
    PreparedStatement consultaPlan;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    boxCodigoPlanConsulta.removeAllItems();
    try{
      consultaPlan = conectar.prepareStatement("SELECT numeroPlan FROM escuela_plan_estudios WHERE codigoEscuela = (?)");
      consultaPlan.setString(1, codigoEscuela);
      resultado = consultaPlan.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){   
          boxCodigoPlanConsulta.addItem(resultado.getObject(indice));        
        }   
      } 
    }
    catch(Exception error){ 
        System.out.println(error);
    }   
  }
  
  
  public void cargarPlanesRegistroCurso(JComboBox BoxEscuelaPropietaria, String codigoEscuela){
    ResultSet resultado;
    PreparedStatement consultaPlan;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxEscuelaPropietaria.removeAllItems();
    try{
      consultaPlan = conectar.prepareStatement("SELECT numeroPlan FROM escuela_plan_estudios WHERE codigoEscuela = (?)");
      consultaPlan.setString(1, codigoEscuela);
      resultado = consultaPlan.executeQuery();
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
  
  
  public void cargarEscuelasConPlan(JComboBox BoxEscuelaPropietariaPlan){
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
  
  
  public void cargarEscuelaAsignarCorrequisito(JComboBox BoxEscuelaPropietariaAsignarReqCor){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxEscuelaPropietariaAsignarReqCor.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT DISTINCT escuela.codigoEscuela FROM escuela JOIN escuela_plan_estudios ON escuela.codigoEscuela = escuela_plan_estudios.codigoEscuela");
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          BoxEscuelaPropietariaAsignarReqCor.addItem(resultado.getObject(indice));
        }   
      } 
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
  
  public void cargarCursosAsignarCorrequisito(JComboBox BoxCodigoCursoAsignarReqCor, String codigoEscuela){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxCodigoCursoAsignarReqCor.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT codigoCurso FROM escuela_curso WHERE codigoEscuela = (?)");
      consultaEscuela.setString(1, codigoEscuela);
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          BoxCodigoCursoAsignarReqCor.addItem(resultado.getObject(indice));
        }   
      }
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
  
  public void cargarRequisitosCorrequitos(JComboBox BoxAsignarRequisito, JComboBox BoxAsignarCorrequisito, String codigoCurso){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxAsignarRequisito.removeAllItems();
    BoxAsignarCorrequisito.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT codigoCurso FROM escuela_curso WHERE codigoCurso != (?)");
      consultaEscuela.setString(1, codigoCurso);
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          BoxAsignarRequisito.addItem(resultado.getObject(indice));
          BoxAsignarCorrequisito.addItem(resultado.getObject(indice));
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
  
  
  public void cargaEscuelaPropietaria(JComboBox BoxEscuelaPropietaria, JComboBox BoxEscuelaPropietariaConsultaPlan){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    BoxEscuelaPropietaria.removeAllItems();
    BoxEscuelaPropietariaConsultaPlan.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT DISTINCT escuela.codigoEscuela FROM escuela JOIN escuela_plan_estudios ON escuela.codigoEscuela = escuela_plan_estudios.codigoEscuela");
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          BoxEscuelaPropietaria.addItem(resultado.getObject(indice));
          BoxEscuelaPropietariaConsultaPlan.addItem(resultado.getObject(indice));
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
  
  
  public void cargaEliminaciones(JComboBox boxCursoEliminaciones, JComboBox boxPlanEliminaciones, JComboBox boxCursoEliminacionesDos){
    ResultSet primerResultado;
    ResultSet segundoResultado;
    ResultSet tercerResultado;
    PreparedStatement primerConsulta;
    PreparedStatement segundaConsulta;
    PreparedStatement tercerConsulta;
    
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    
    boxCursoEliminaciones.removeAllItems();
    boxPlanEliminaciones.removeAllItems();
    boxCursoEliminacionesDos.removeAllItems();
    
    try{
        
      primerConsulta = conectar.prepareStatement("SELECT codigoCurso FROM curso");
      primerResultado = primerConsulta.executeQuery();
      while(primerResultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          boxCursoEliminaciones.addItem(primerResultado.getObject(indice));
        }   
      } 
      
      segundaConsulta = conectar.prepareStatement("SELECT numeroPlan FROM plan_estudios");
      segundoResultado = segundaConsulta.executeQuery();
      while(segundoResultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          boxPlanEliminaciones.addItem(segundoResultado.getObject(indice));
        }   
      } 
      
      tercerConsulta = conectar.prepareStatement("SELECT codigoCurso FROM curso");
      tercerResultado = tercerConsulta.executeQuery();
      while(tercerResultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          boxCursoEliminacionesDos.addItem(tercerResultado.getObject(indice));
        }   
      } 
      
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
  
  public void cargaRequisitosCurso(JComboBox boxRequisitoEliminaciones, String pCodigoCurso){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    boxRequisitoEliminaciones.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT codigoRequisito FROM requisito_curso WHERE codigoCurso = (?)");
      consultaEscuela.setString(1, pCodigoCurso);
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          boxRequisitoEliminaciones.addItem(resultado.getObject(indice));
        }   
      } 
    }
    catch(Exception error){ 
        System.out.println(error);
    }
  }
  
  
  public void cargarCursosPlan(JComboBox boxCursosEliminaciones, String pnumeroPlan){
    ResultSet resultado;
    PreparedStatement consultaEscuela;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    boxCursosEliminaciones.removeAllItems();
    try{
      consultaEscuela = conectar.prepareStatement("SELECT codigoCurso FROM plan_estudios_curso WHERE numeroPlan = (?)");
      consultaEscuela.setString(1, pnumeroPlan);
      resultado = consultaEscuela.executeQuery();
      while(resultado.next()){ 
        for(int indice = 1; indice<2; indice++){  
          boxCursosEliminaciones.addItem(resultado.getObject(indice));
        }   
      } 
    }
    catch(Exception error){ 
        System.out.println(error);
    }    
  }
  
  
  public void llamarMetodoAnadirRequisito(){
    Curso cursoCorrequisito = buscarCurso(String.valueOf(vista.BoxCodigoCursoAsignarReqCor));
    cursoCorrequisito.anadirRequisito(String.valueOf(vista.BoxAsignarRequisito.getSelectedItem()));
  }
  
  
  public void llamarMetodoAsignarRequisito(){
    curso.asignarRequisito(String.valueOf(vista.BoxCodigoCursoAsignarReqCor.getSelectedItem()), String.valueOf(vista.BoxAsignarRequisito.getSelectedItem()));
  }
  
  
  public void llamarMetodoAnadirCorrequisito(){
    Curso cursoCorrequisito = buscarCurso(String.valueOf(vista.BoxCodigoCursoAsignarReqCor));
    cursoCorrequisito.anadirCorrequisito(String.valueOf(vista.BoxAsignarCorrequisito.getSelectedItem()));
  }
  
  
  public void llamarMetodoAsignarCorrequisito(){
    curso.asignarCorrequisito(String.valueOf(vista.BoxCodigoCursoAsignarReqCor.getSelectedItem()), String.valueOf(vista.BoxAsignarCorrequisito.getSelectedItem()));
  }
  
  
  public void llamarMetodoEliminarRequisito(){
    if (vista.boxRequisitoEliminaciones.getSelectedItem()==(null)){
      JOptionPane.showMessageDialog(null, "Error! El curso no cuenta con un requisito");
    }
    else{
      Curso nuevoCurso = buscarCurso(String.valueOf(vista.boxCursoEliminaciones.getSelectedItem()));
      nuevoCurso.eliminarRequisito(String.valueOf(vista.boxCursoEliminaciones.getSelectedItem()), String.valueOf(vista.boxRequisitoEliminaciones.getSelectedItem()));
      JOptionPane.showMessageDialog(null, "Requisito eliminado con éxito");
    }   
  }

  
  public void llamarMetodoEliminarCursoDePlan(){   
    if (vista.boxCursosEliminacionesDos.getSelectedItem()==(null)){
      JOptionPane.showMessageDialog(null, "Error! No ha seleccionado un curso o el plan aún no cuenta con cursos");
    }
    else{
      PlanEstudios nuevoPlan = buscarPlanEstudios(String.valueOf(vista.boxPlanEliminaciones.getSelectedItem()));
      nuevoPlan.eliminarCursoDePlan(String.valueOf(vista.boxCursosEliminacionesDos.getSelectedItem()), String.valueOf(vista.boxPlanEliminaciones.getSelectedItem()));
      JOptionPane.showMessageDialog(null, "Curso eliminado con éxito");    
    }
  }
  
  
  public void llamarMetodoEliminarCurso() throws Exception{
 
     /*for (int indice = 0; cursos.size() != indice; indice++){
        System.err.println(cursos.get(indice).toString());
     }*/ 

     
    Curso nuevoCurso = buscarCurso(String.valueOf(vista.boxCursoEliminacionesDos.getSelectedItem()));
    if(nuevoCurso.eliminarCurso(String.valueOf(vista.boxCursoEliminacionesDos.getSelectedItem()))==true){
        for (int indice = 0; cursos.size() != indice; indice++){
          if(cursos.get(indice).getCodigoCurso().equalsIgnoreCase(nuevoCurso.getCodigoCurso())){
            cursos.remove(cursos.get(indice));
          }
        }
        JOptionPane.showMessageDialog(null, "Curso eliminado con éxito");    
        vista.boxCursoEliminacionesDos.removeItem(String.valueOf(vista.boxCursoEliminacionesDos.getSelectedItem()));
      }
      else{
        JOptionPane.showMessageDialog(null, "Error! el curso pertenece a un plan");     
      }
  }
  
  
  public void enviarPDFCorreo() throws Exception{
    Pdf archivo = new Pdf();
    Email correo = new Email();
    archivo.crearPdf(Integer.parseInt(String.valueOf(vista.boxCodigoPlanConsulta.getSelectedItem())));
    correo.enviarCorreo();
  }
  
  
  //SI REGISTRO UN CURSO LO TENGO QUE AÑADIR AL ARRAYLIST DE ACÁ
  public void recorreArrayList(){
    System.out.println("PRUEBA:" + cursos.get(0).toString());
  }
  
  Curso nuevoCurso = new Curso();
  Escuela nuevaEscuela = new Escuela();
  PlanEstudios nuevoPlan = new PlanEstudios();
  public void cargarBaseDatos() throws SQLException, ParseException{
   
   //Consulta los cursos en la base de datos
   ResultSet resultado;
   PreparedStatement consultarCursos;
   Conexion nuevaConexion = new Conexion();
   Connection conectar = nuevaConexion.conectar(); 
   consultarCursos = conectar.prepareStatement("SELECT * FROM curso");
   resultado = consultarCursos.executeQuery(); 
   
   while (resultado.next()){
         String codigoCurso = (String.valueOf(resultado.getObject(1)));
         String nombreCurso = (String.valueOf(resultado.getObject(2)));
         int cantidadCreditos = (Integer.parseInt(String.valueOf(resultado.getObject(3))));
         int cantidadHoras = (Integer.parseInt(String.valueOf(resultado.getObject(4))));
         Curso nuevoCurso = new Curso(codigoCurso, nombreCurso, cantidadCreditos, cantidadHoras);
         cursos.add(nuevoCurso);  
   }
   

   /*for(int indice = 0; indice!=cursos.size(); indice++){
       System.out.println(cursos.get(indice).toString());
   }*/


   //Carga la tabla Escuela de la base de datos
   ResultSet resultadoEscuela;
   PreparedStatement consultarEscuelas;
   consultarCursos = conectar.prepareStatement("SELECT * FROM escuela");
   resultadoEscuela = consultarCursos.executeQuery(); 
   
   
   while (resultadoEscuela.next()){
       String codigoEscuela = (String.valueOf(resultadoEscuela.getObject(1)));
       String nombreEscuela = (String.valueOf(resultadoEscuela.getObject(2))); 
       Escuela nuevaEscuela = new Escuela(codigoEscuela, nombreEscuela);
       escuelas.add(nuevaEscuela); 
   }
   

   /*for(int indice = 0; indice!=escuelas.size(); indice++){
       System.out.println(escuelas.get(indice).toString());
   }*/

   
   //Carga la tabla planes de estudio de la base de datos
   ResultSet resultadoPlanes;
   PreparedStatement consultarPlanes;
   consultarPlanes = conectar.prepareStatement("SELECT * FROM plan_estudios");
   resultadoPlanes = consultarPlanes.executeQuery(); 
   
   
   while (resultadoPlanes.next()){
     
     SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
     int numeroPlan = (Integer.parseInt(String.valueOf(resultadoPlanes.getObject(1))));
     Date vigenciaPlan = (formatoFecha.parse(String.valueOf(resultadoPlanes.getObject(2)))); 
     
     PlanEstudios nuevoPlan = new PlanEstudios(numeroPlan, vigenciaPlan);
     planes.add(nuevoPlan);
   
   }
   

   /*for(int indice = 0; indice!=planes.size(); indice++){
       System.out.println(planes.get(indice).toString());
   }*/

   
   //Carga la tabla intermedia escuelaPlanEstudios de la base de datos
   ResultSet resultadoEscuelaPlan;
   PreparedStatement consultarEscuelaPlan;
   consultarEscuelaPlan = conectar.prepareStatement("SELECT * FROM escuela_plan_estudios");
   resultadoEscuelaPlan = consultarEscuelaPlan.executeQuery(); 
   
   String codigoEscuela;
   //int numeroPlan;
   while (resultadoEscuelaPlan.next()){
     codigoEscuela = String.valueOf(resultadoEscuelaPlan.getObject(1));
     for (int indice = 0; indice != escuelas.size(); indice++){
       if (escuelas.get(indice).getCodigoEscuela().equalsIgnoreCase(codigoEscuela)){
         for (int sumador = 0; sumador != planes.size(); sumador++){
           if (planes.get(sumador).getNumeroPlan() == Integer.parseInt(String.valueOf(resultadoEscuelaPlan.getObject(2)))){
               escuelas.get(indice).asociarPlan(planes.get(sumador));                
           }
         }
       }
     }
   }
   

   //Carga la tabla intermedia planEstudiosCurso de la base de datos
   ResultSet resultadoPlanCurso;
   PreparedStatement consultarPlanCurso;
   consultarPlanCurso = conectar.prepareStatement("SELECT * FROM plan_estudios_curso");
   resultadoPlanCurso = consultarPlanCurso.executeQuery(); 
   
   int numeroPlanDos;
   
   while (resultadoPlanCurso.next()){
     numeroPlanDos = Integer.parseInt(String.valueOf(resultadoPlanCurso.getObject(1)));
     for (int indice = 0; indice != planes.size(); indice++){
       if (planes.get(indice).getNumeroPlan()==(numeroPlanDos)){
         for (int sumador = 0; sumador != cursos.size(); sumador++){
           if (cursos.get(sumador).getCodigoCurso().equalsIgnoreCase(String.valueOf(resultadoPlanCurso.getObject(2)))){
             String bloque = String.valueOf(resultadoPlanCurso.getObject(3));
             int numeroBloque = 0;
             
             if(bloque.equalsIgnoreCase("I Semestre")){
               numeroBloque = 0;   
             }
             
             else if(bloque.equalsIgnoreCase("II Semestre")){
               numeroBloque = 1;   
             }
             
            else if(bloque.equalsIgnoreCase("III Semestre")){
               numeroBloque = 2;   
             }
             
             else if(bloque.equalsIgnoreCase("IV Semestre")){
               numeroBloque = 3;   
             }
             
             else if(bloque.equalsIgnoreCase("V Semestre")){
               numeroBloque = 4;   
             }
             
             else if(bloque.equalsIgnoreCase("VI Semestre")){
               numeroBloque = 5;   
             }
             
             else if(bloque.equalsIgnoreCase("VII Semestre")){
               numeroBloque = 6;   
             }
             
             else if(bloque.equalsIgnoreCase("VIII Semestre")){
               numeroBloque = 7;   
             }
             
             else if(bloque.equalsIgnoreCase("IX Semestre")){
               numeroBloque = 8;   
             }
             
             else if(bloque.equalsIgnoreCase("X Semestre")){
               numeroBloque = 9;   
             }
             
             else if(bloque.equalsIgnoreCase("XI Semestre")){
               numeroBloque = 10;   
             }
             
            else if(bloque.equalsIgnoreCase("XII Semestre")){
               numeroBloque = 11;   
             }
             
            else if(bloque.equalsIgnoreCase("XIII Semestre")){
               numeroBloque = 12;   
             }
             
             planes.get(indice).añadirCursos(cursos.get(sumador), numeroBloque); 
             cursos.get(sumador).asociarPlan(planes.get(indice));
           }
         }
       }
     }
   }
   
   //Carga la tabla intermedia escuelaCurso de la base de datos
   ResultSet resultadoEscuelaCurso;
   PreparedStatement consultarEscuelaCurso;
   consultarEscuelaCurso = conectar.prepareStatement("SELECT * FROM escuela_curso");
   resultadoEscuelaCurso = consultarEscuelaCurso.executeQuery(); 
   
   String codigoEscuelaDos;
   String codigoCursoDos;
   
   while (resultadoEscuelaCurso.next()){
     codigoEscuelaDos = String.valueOf(resultadoEscuelaCurso.getObject(1));
     for (int indice = 0; indice != escuelas.size(); indice++){
       if (escuelas.get(indice).getCodigoEscuela().equalsIgnoreCase(codigoEscuelaDos)){
         for (int sumador = 0; sumador != cursos.size(); sumador++){
           if (cursos.get(sumador).getCodigoCurso().equalsIgnoreCase(String.valueOf(resultadoEscuelaCurso.getObject(2)))){
             escuelas.get(indice).asociarCurso(cursos.get(sumador));             
           }
         }
       }
     }
   }
   
   //Carga la tabla intermedia requisitoCurso de la base de datos
   ResultSet resultadoRequisitoCurso;
   PreparedStatement consultarRequisitoCurso;
   consultarRequisitoCurso = conectar.prepareStatement("SELECT * FROM requisito_curso");
   resultadoRequisitoCurso = consultarRequisitoCurso.executeQuery(); 
   

   String codigoCursoTres;
   String codigoRequisito;
   
   while (resultadoRequisitoCurso.next()){
     codigoCursoTres = String.valueOf(resultadoRequisitoCurso.getObject(1));
     for (int indice = 0; indice != cursos.size(); indice++){
       if (cursos.get(indice).getCodigoCurso().equalsIgnoreCase(codigoCursoTres)){
         for (int sumador = 0; sumador != cursos.size(); sumador++){
           if (cursos.get(sumador).getCodigoCurso().equalsIgnoreCase(String.valueOf(resultadoRequisitoCurso.getObject(2)))){
             cursos.get(indice).añadirRequisitos(cursos.get(sumador));             
           }
         }
       }
     }
   }
   
   //Carga la tabla intermedia correquisitoCurso de la base de datos
   ResultSet resultadoCorrequisitoCurso;
   PreparedStatement consultarCorrequisitoCurso;
   consultarCorrequisitoCurso = conectar.prepareStatement("SELECT * FROM correquisito_curso");
   resultadoCorrequisitoCurso = consultarCorrequisitoCurso.executeQuery(); 
   

   String codigoCursoCuatro;
   String codigoCorrequisito;
   
   while (resultadoCorrequisitoCurso.next()){
     codigoCursoCuatro = String.valueOf(resultadoCorrequisitoCurso.getObject(1));
     for (int indice = 0; indice != cursos.size(); indice++){
       if (cursos.get(indice).getCodigoCurso().equalsIgnoreCase(codigoCursoCuatro)){
         for (int sumador = 0; sumador != cursos.size(); sumador++){
           if (cursos.get(sumador).getCodigoCurso().equalsIgnoreCase(String.valueOf(resultadoCorrequisitoCurso.getObject(2)))){
             cursos.get(indice).añadirCorrequisitos(cursos.get(sumador));             
           }
         }
       }
     }
   }
     
}//Fin método PADRE
  
  @Override
  public void actionPerformed(ActionEvent e){
  
    if(e.getSource() == vista.botonRegistrarEscuela){
        insertarEscuela();
        this.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria, vista.BoxEscuelaPropietariaConsultaPlan);

    }
    
    else if(e.getSource() == vista.botonCargarPlanes){

      cargarPlanesConsulta(vista.boxCodigoPlanConsulta, String.valueOf(vista.BoxEscuelaPropietariaConsultaPlan.getSelectedItem()));    
      
    }
    
    else if(e.getSource() == vista.botonCargarPlanRegistroCursos){

      cargarPlanesRegistroCurso(vista.BoxPlanRegistroCurso, String.valueOf(vista.BoxEscuelaPropietaria.getSelectedItem()));    
      
    }
    
    else if(e.getSource() == vista.botonBuscarPlanesCurso){
      primerConsulta(vista.tablaPrimerConsulta, vista.textCodigoPrimerConsulta.getText());    
    }
    
    else if(e.getSource() == vista.botonBuscarRequisitosCurso){
      segundaConsulta(vista.tablaSegundaConsulta, vista.textCodigoSegundaConsulta.getText());    
    }
    
    else if(e.getSource() == vista.botonBuscarCorrequisitosCurso){
      tercerConsulta(vista.tablaTerceraConsulta, vista.textCodigoTercerConsulta.getText());        
    }
    
    else if(e.getSource() == vista.botonEliminaciones){
        cargaEliminaciones(vista.boxCursoEliminaciones, vista.boxPlanEliminaciones, vista.boxCursoEliminacionesDos);
    }
    
    else if(e.getSource() == vista.botonCargarRequisitoEliminaciones){
      cargaRequisitosCurso(vista.boxRequisitoEliminaciones, String.valueOf(vista.boxCursoEliminaciones.getSelectedItem()));    
    }
          
    else if(e.getSource() == vista.botonRegistrarPlanAEscuela){
        cargarEscuelasConPlan(vista.BoxEscuelaPropietariaPlan);
    }
    
    else if(e.getSource() == vista.botonCargarCursosEliminaciones){
        cargarCursosPlan(vista.boxCursosEliminacionesDos, String.valueOf(vista.boxPlanEliminaciones.getSelectedItem()));
    }
    
    else if(e.getSource() == vista.botonEliminarRequisito){
      llamarMetodoEliminarRequisito();
    }
    
    else if(e.getSource() == vista.botonEliminarCurso){
      llamarMetodoEliminarCursoDePlan();
    }
    
    else if(e.getSource() == vista.botonEliminarCursoDos){
      try{
        llamarMetodoEliminarCurso();    
      }
      catch(Exception error){
          System.out.println("ERROR: " + error);
      }
      
    }
    
    else if(e.getSource() == vista.botonRegistrarCurso){
      try{
        insertarCurso();
        JOptionPane.showMessageDialog(null, "Curso registrado con éxito"); 
        JOptionPane.showMessageDialog(null, "Curso asociado con éxito"); 
      }  
      catch(Exception error){
          System.err.println(error);
      }
    }
    
    else if(e.getSource() == vista.botonCargarInformacionPlan){
      cargarInformacionPlan(vista.tablaInformacionPlan, String.valueOf(vista.boxCodigoPlanConsulta.getSelectedItem()));
    }
    
    else if(e.getSource() == vista.botonRegistrarCoRequisito){
      cargarEscuelaAsignarCorrequisito(vista.BoxEscuelaPropietariaAsignarReqCor);
    }
    
    else if(e.getSource() == vista.botonRegistrarRequisito){
      llamarMetodoAsignarRequisito();    
      JOptionPane.showMessageDialog(null, "Requisito registrado con éxito");
    }
    
    else if(e.getSource() == vista.botonRegistrarCorrequisito){
      llamarMetodoAsignarCorrequisito();
      llamarMetodoAnadirCorrequisito();
      JOptionPane.showMessageDialog(null, "Correquisito registrado con éxito");
    }
    
    else if(e.getSource() == vista.botonCargarCursoAsignarRequisito){
      cargarCursosAsignarCorrequisito(vista.BoxCodigoCursoAsignarReqCor, String.valueOf(vista.BoxEscuelaPropietariaAsignarReqCor.getSelectedItem()));    
    }
    
    else if(e.getSource() == vista.botonCargarRequisitoCorrequisito){
      cargarRequisitosCorrequitos(vista.BoxAsignarRequisito, vista.BoxAsignarCorrequisito,String.valueOf(vista.BoxCodigoCursoAsignarReqCor.getSelectedItem()));   
    }
    
    else if(e.getSource() == vista.botonEnviarPDFCorreo){
      try{
        enviarPDFCorreo();    
      }
      catch(Exception error){
          System.out.println("Error: " + error);    
      }
      
    }
    
    else if(e.getSource() == vista.botonRegistrarPlan){
        try {
            insertarPlan();
            JOptionPane.showMessageDialog(null, "Plan registrado con éxito");
        } 
        catch (ParseException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
    

   
  }
     
}
