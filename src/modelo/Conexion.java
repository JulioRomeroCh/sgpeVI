
package modelo;

import java.sql.*;


public class Conexion {
  
  Connection conectar = null;
  
  public Connection conectar(){
      
    try{
      Class.forName("com.mysql.cj.jdbc.Driver");
      //Cambiar contraseña de acceso a la base de datos
      conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/sgpe","root","");
      System.out.println("Se conectó exitosamente a la base de datos");
    }
    catch(ClassNotFoundException | SQLException error){
      System.out.println("NO se pudo conectar a la base de datos, ocurrió el error " + error);
    }
    return conectar;
  }
}
