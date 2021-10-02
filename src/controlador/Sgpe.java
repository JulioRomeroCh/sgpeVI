package controlador;

import java.sql.Date;
import modelo.Curso;
import modelo.Escuela;
import modelo.PlanEstudios;
import vista.Formulario;
import modelo.Pdf;
import modelo.Email;
import javax.mail.MessagingException;

public class Sgpe {
    
  public static void main (String[] args) throws MessagingException, Exception{

    modelo.Curso modeloUno = new modelo.Curso();
    modelo.Escuela modeloDos = new modelo.Escuela();
    modelo.PlanEstudios modeloTres = new modelo.PlanEstudios();
    vista.Formulario nuevaVista = new vista.Formulario();
  
    Controlador controladores = new Controlador(modeloUno, modeloDos, modeloTres, nuevaVista);
    controladores.iniciar();
    
    nuevaVista.setVisible(true);
    
    /*
    Curso cursoUno = new Curso ("TI9000", "CostosIV", 4, 3);
    Curso cursoDos = new Curso ("TI9001", "CostosV", 4, 2);
    Curso cursoTres = new Curso ("TI9002", "CostosVI", 4, 1);
    
    PlanEstudios miPlan = new PlanEstudios(2090, Date.valueOf("2020-01-01"));
    miPlan.añadirCursos(cursoUno, 1);
    miPlan.añadirCursos(cursoDos, 4);
    miPlan.añadirCursos(cursoTres, 3);*/

    //Curso CursoPADRE = new Curso ("TI9000", "CostosIV", 4, 4);
    //CursoPADRE.anadirCorrequisito("TI1000");
    
    //Prueba envío correo
    
    /*Pdf pdf = new Pdf();
    pdf.crearPdf();
    Email correo = new Email();
    correo.enviarCorreo();*/
 
    
   
    
  }

}
