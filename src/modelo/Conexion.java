package modelo;

import java.sql.*;


public class Conexion {
  
  Connection conectar = null;
  //
  /**
  *<p>Método conectar: A través del conector de Mysql se conecta a la base de datos con Java
  *   usando las credenciales correspondientes.
  *@return conectar: Objeto de tipo Connection, representa la conexion con la base de datos.
 */
  public Connection conectar(){
      
    try{
      Class.forName("com.mysql.cj.jdbc.Driver");
      conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/sgpe","root","XXXLupitaLaRicaXXX");
    }
    catch(ClassNotFoundException | SQLException error){
      System.out.println("NO se pudo conectar a la base de datos, ocurrió el error " + error);
    }
    return conectar;
    
  }
}
