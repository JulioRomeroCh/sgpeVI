
package modelo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Pdf {
  
    public void crearPdf (int numeroPlan) throws Exception {
         try (PDDocument documento = new PDDocument ()) {
         PDPage pagina = new PDPage(PDRectangle.LETTER);
         documento.addPage(pagina);
         //Texto
         PDPageContentStream contenido = new PDPageContentStream (documento, pagina);
         
         int pixelesXColumnas = 80;
         int pixelesYColumnas = 750;
         ArrayList <String> columnas = new ArrayList<String>();
         columnas.add("Código");
         columnas.add("Nombre");
         columnas.add("Bloque");
         columnas.add("Horas");
         columnas.add("Créditos");
         
         for (int contador = 1; contador <= 5; contador++){
             contenido.beginText();
                 contenido.setFont(PDType1Font.TIMES_BOLD, 11);
                 contenido.moveTextPositionByAmount(pixelesXColumnas, pixelesYColumnas);
                 contenido.showText(" " + columnas.get(contador-1));
                 contenido.endText();
                 pixelesXColumnas += 120;
         }
         
         //Ciclo for para imprimir los datos en PDF
         ResultSet datosPlan = seleccionarDatosDePlanEstudios(numeroPlan); //Cambiar por JTextfield
         ResultSet datosTotalesCursosYCreditos = selecionarCursosYCreditosTotalesPlanEstudios(numeroPlan); //Cambiar por JTextfield
             int pixelesX = 80;
             int pixelesY = 700;
         while(datosPlan.next()){ 
             pixelesX = 80;
            for(int indice = 1; indice<=5; indice++){  
                 contenido.beginText();
                 contenido.setFont(PDType1Font.TIMES_ROMAN, 11);
                 contenido.moveTextPositionByAmount(pixelesX, pixelesY);
                 contenido.showText(" " + datosPlan.getObject(indice));
                 contenido.endText();
                 pixelesX += 120;
            }
            
             pixelesY -= 50;
         }
               //Total de créditos
               if (datosTotalesCursosYCreditos.next()){
               contenido.beginText();
               contenido.setFont(PDType1Font.TIMES_BOLD, 12);
               contenido.moveTextPositionByAmount(100, pixelesY-50);
               contenido.showText("Total de créditos: " + datosTotalesCursosYCreditos.getObject(1));
               contenido.endText();
               
               contenido.beginText();
               contenido.setFont(PDType1Font.TIMES_BOLD, 12);
               contenido.moveTextPositionByAmount(100, pixelesY-100);
               contenido.showText("Total de cursos: " + datosTotalesCursosYCreditos.getObject(2));
               contenido.endText();
               }
               
               contenido.close();
             documento.save("planEstudios.pdf");
             
         }              
        }
    
    public ResultSet seleccionarDatosDePlanEstudios(int pNumeroPlan) throws Exception{ //Cambiar por JTextField
      ResultSet resultado;
      CallableStatement consultarPlanEstudios;
      Conexion nuevaConexion = new Conexion();
      Connection conectar = nuevaConexion.conectar();
      
      consultarPlanEstudios = conectar.prepareCall("{CALL selectPlanEstudiosParaPDF(?)}");
      consultarPlanEstudios.setInt(1, pNumeroPlan);
      resultado = consultarPlanEstudios.executeQuery(); 
       return resultado;
   }
    
      public ResultSet selecionarCursosYCreditosTotalesPlanEstudios(int pNumeroPlan) throws Exception{ //Cambiar por JTextField
      ResultSet resultado;
      CallableStatement consultarPlanEstudios;
      Conexion nuevaConexion = new Conexion();
      Connection conectar = nuevaConexion.conectar();
      
      consultarPlanEstudios = conectar.prepareCall("{CALL selectTotalesCursosYCreditosPDF(?)}");
      consultarPlanEstudios.setInt(1, pNumeroPlan);
      resultado = consultarPlanEstudios.executeQuery(); 
       return resultado;
   }
    

}
