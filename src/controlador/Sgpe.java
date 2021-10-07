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
    controladores.cargarBaseDatos();
    controladores.iniciar();
    
    nuevaVista.setVisible(true);

  }

}
