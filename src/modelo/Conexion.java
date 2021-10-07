package modelo;

import java.sql.*;
//SI QUITO EL * SE DESPICHA TERESA, LUEGO VEMOS ESTO!!!
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;


public class Conexion {
  
  Connection conectar = null;
  
  public Connection conectar(){
      
    try{
      Class.forName("com.mysql.cj.jdbc.Driver");
      conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/sgpe","root","Xx_FEDERICOTORRES_xX");
    }
    catch(ClassNotFoundException | SQLException error){
      System.out.println("NO se pudo conectar a la base de datos, ocurrió el error " + error);
    }
    return conectar;
    
  }
}
