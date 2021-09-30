package controlador;

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
    
    //Prueba envío correo
    
    Pdf pdf = new Pdf();
    pdf.crearPdf();
    Email correo = new Email();
    correo.enviarCorreo();
 
    nuevaVista.setVisible(true);
   
    
  }

}
