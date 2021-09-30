package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import modelo.Conexion;
import modelo.Curso;
import modelo.Escuela;
import modelo.PlanEstudios;
import vista.Formulario;

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
     this.curso.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria);
     this.plan.cargaCodigosPlanes(vista.BoxCodigoPlanCursoPlan);
     this.plan.cargaCodigosCursos(vista.BoxCodigoCursoCursoPlan);
     //public void cargaPlanesRegistrarCursos(JComboBox BoxPlanRegistroCurso){
     this.curso.cargaPlanesRegistrarCursos(vista.BoxPlanRegistroCurso);
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
   this.plan.cargaCodigosCursos(vista.BoxCodigoCursoCursoPlan);
   System.out.println("Cargué combobox cursos");
   plan.insertarCursoAPlan(Integer.parseInt(String.valueOf(vista.BoxPlanRegistroCurso.getSelectedItem())), codigoCurso, String.valueOf(vista.BoxBloqueRegistrarCurso.getSelectedItem()));
   System.out.println("Index:" + vista.BoxBloqueRegistrarCurso.getSelectedIndex());
   
   plan.añadirCursos(nuevoCurso, vista.BoxBloqueRegistrarCurso.getSelectedIndex());
   
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
   this.curso.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria);
   this.plan.cargaCodigosPlanes(vista.BoxCodigoPlanCursoPlan);
   this.curso.cargaPlanesRegistrarCursos(vista.BoxPlanRegistroCurso);
  }
  
  @Override
  public void actionPerformed(ActionEvent e){
  
    if(e.getSource() == vista.botonRegistrarEscuela){
        insertarEscuela();
        this.curso.cargaEscuelaPropietaria(vista.BoxEscuelaPropietaria);

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
