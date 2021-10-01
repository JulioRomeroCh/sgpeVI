package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    
  private Curso curso;
  private Escuela escuela;
  private PlanEstudios plan;
  private Conexion conexion;
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
     this.curso.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria, vista.BoxEscuelaPropietariaConsultaPlan);
     //this.plan.cargaCodigosPlanes(vista.BoxCodigoPlanCursoPlan);
     //this.plan.cargaCodigosCursos(vista.BoxCodigoCursoCursoPlan);
     //public void cargaPlanesRegistrarCursos(JComboBox BoxPlanRegistroCurso){
     //this.curso.cargaPlanesRegistrarCursos(vista.BoxPlanRegistroCurso);
  }
  
  public void iniciar(){

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
     escuela.insertarEscuela(codigoEscuela, nombreEscuela);  
     JOptionPane.showMessageDialog(null, "Escuela registrada con éxito");
     this.plan.cargaEscuelaRegistrarPlan(vista.BoxEscuelaPropietariaPlan);
     vista.textCodigoEscuela.setText("");
     vista.textNombreEscuela.setText("");

   }
   
  }
  
  public void cargarPlanesConsulta(JComboBox boxCodigoPlanConsulta, String codigoEscuela){
    ResultSet resultado;
    PreparedStatement consultaPlan;
    Conexion nuevaConexion = new Conexion();
    Connection conectar = nuevaConexion.conectar();
    boxCodigoPlanConsulta.removeAllItems();
      System.out.println("Codigo Escuela: " + codigoEscuela);
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
      //botonRegistrarPlanAEscuela
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
        Connection conectar = nuevaConexion.conectar();  //selectPlanEstudiosParaPDF
        //CallableStatement insertar = conectar.prepareCall("{CALL insertCurso(?,?,?,?)}");
        

        
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
  
  public void enviarPDFCorreo() throws Exception{
    Pdf archivo = new Pdf();
    Email correo = new Email();
    archivo.crearPdf(Integer.parseInt(String.valueOf(vista.boxCodigoPlanConsulta.getSelectedItem())));
    correo.enviarCorreo();
  }
  
  public void insertarCurso(){ 
      
   curso.setCodigoCurso(vista.textCodigoCurso.getText());
   curso.setNombreCurso(vista.textNombreCurso.getText());
   //curso.setCantidadCreditos(Integer.parseInt(vista.textCantidadCreditos.getText()));
   curso.setCantidadCreditos(Integer.parseInt(String.valueOf(vista.BoxCantidadCreditos.getSelectedItem())));
   System.out.println(vista.BoxCantidadCreditos.getSelectedItem());
   //curso.setCantidadHorasLectivas(Integer.parseInt(vista.textCantidadHorasLectivas.getText()));
   curso.setCantidadHorasLectivas(Integer.parseInt(String.valueOf(vista.boxCantidadHoras.getSelectedItem())));
      
   String codigoCurso = curso.getCodigoCurso();
   String nombreCurso = curso.getNombreCurso();
   int cantidadCreditos = curso.getCantidadCreditos();
   int cantidadHoras = curso.getCantidadHorasLectivas();
   String codigoEscuela = String.valueOf(vista.BoxEscuelaPropietaria.getSelectedItem());
   Curso nuevoCurso = new Curso (codigoCurso, nombreCurso, cantidadCreditos, cantidadHoras);

   
   curso.insertarCurso(codigoCurso, nombreCurso, cantidadCreditos, cantidadHoras, codigoEscuela);
   System.out.println("Inserté el curso");
   escuela.asociarCurso(nuevoCurso);
   System.out.println("Inserté el curso a la escuela");
   //this.plan.cargaCodigosCursos(vista.BoxCodigoCursoCursoPlan);
   System.out.println("Cargué combobox cursos");
   plan.insertarCursoAPlan(Integer.parseInt(String.valueOf(vista.BoxPlanRegistroCurso.getSelectedItem())), codigoCurso, String.valueOf(vista.BoxBloqueRegistrarCurso.getSelectedItem()));
   System.out.println("Index:" + vista.BoxBloqueRegistrarCurso.getSelectedIndex());
   
   //plan.añadirCursos(nuevoCurso, vista.BoxBloqueRegistrarCurso.getSelectedIndex());
   
   System.out.println("Sí lo añadi a la matriz:");
  }
  

  public void insertarPlan() throws ParseException{ 
      
   SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");  
   plan.setNumeroPlan(Integer.parseInt(vista.textNumeroPlan.getText()));
   plan.setVigenciaPlan(formatoFecha.parse(vista.textVigencia.getText()));
   
   int numeroPlan = plan.getNumeroPlan();
   String codigoEscuela = String.valueOf(vista.BoxEscuelaPropietariaPlan.getSelectedItem());
  
   Date vigencia = formatoFecha.parse(plan.getVigenciaPlan());
   
   java.sql.Date fechaSQl = new java.sql.Date(vigencia.getTime());
   
   plan.insertarPlanEstudios(numeroPlan, fechaSQl, codigoEscuela);
   this.curso.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria, vista.BoxEscuelaPropietariaConsultaPlan);
   //this.plan.cargaCodigosPlanes(vista.BoxCodigoPlanCursoPlan);
   //this.curso.cargaPlanesRegistrarCursos(vista.BoxPlanRegistroCurso);
  }
  
  @Override
  public void actionPerformed(ActionEvent e){
  
    if(e.getSource() == vista.botonRegistrarEscuela){
        insertarEscuela();
        this.curso.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria, vista.BoxEscuelaPropietariaConsultaPlan);

    }
    
    else if(e.getSource() == vista.botonCargarPlanes){

      System.out.println("Toqué botón");
      cargarPlanesConsulta(vista.boxCodigoPlanConsulta, String.valueOf(vista.BoxEscuelaPropietariaConsultaPlan.getSelectedItem()));    
      
    }
    
    else if(e.getSource() == vista.botonCargarPlanRegistroCursos){

      System.out.println("Toqué botón");
      cargarPlanesRegistroCurso(vista.BoxPlanRegistroCurso, String.valueOf(vista.BoxEscuelaPropietaria.getSelectedItem()));    
      
    }
          
    else if(e.getSource() == vista.botonRegistrarPlanAEscuela){
        cargarEscuelasConPlan(vista.BoxEscuelaPropietariaPlan);
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
