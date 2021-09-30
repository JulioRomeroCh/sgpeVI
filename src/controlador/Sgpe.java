package controlador;

import modelo.Curso;
import modelo.Escuela;
import modelo.PlanEstudios;
import vista.Formulario;

public class Sgpe {
    
  public static void main (String[] args){

    modelo.Curso modeloUno = new modelo.Curso();
    modelo.Escuela modeloDos = new modelo.Escuela();
    modelo.PlanEstudios modeloTres = new modelo.PlanEstudios();
    vista.Formulario nuevaVista = new vista.Formulario();
  
    Controlador controladores = new Controlador(modeloUno, modeloDos, modeloTres, nuevaVista);
    controladores.iniciar();
    nuevaVista.setVisible(true);
    
  }

}
