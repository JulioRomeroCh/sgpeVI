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
     
     this.cursos = new ArrayList<Curso>();
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
     this.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria, vista.BoxEscuelaPropietariaConsultaPlan);
     //this.plan.cargaCodigosPlanes(vista.BoxCodigoPlanCursoPlan);
     //this.plan.cargaCodigosCursos(vista.BoxCodigoCursoCursoPlan);
     //public void cargaPlanesRegistrarCursos(JComboBox BoxPlanRegistroCurso){
     //this.curso.cargaPlanesRegistrarCursos(vista.BoxPlanRegistroCurso);
  }
  
  public void iniciar(){

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
  
  
  public void enviarPDFCorreo() throws Exception{
    Pdf archivo = new Pdf();
    Email correo = new Email();
    archivo.crearPdf(Integer.parseInt(String.valueOf(vista.boxCodigoPlanConsulta.getSelectedItem())));
    correo.enviarCorreo();
  }
  
  
  //SI REGISTRO UN CURSO LO TENGO QUE AÑADIR AL ARRAYLIST DE ACÁ
  
  
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
