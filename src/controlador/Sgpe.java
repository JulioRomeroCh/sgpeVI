
package controlador;
import modelo.Conexion;

public class Sgpe {
  
  public static void main (String[] args){
    Conexion mysql = new Conexion();
    mysql.conectar();
  }
}
