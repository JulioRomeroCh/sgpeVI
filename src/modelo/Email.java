
package modelo;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;

public class Email {
     private final String usuarioAdmi = "sgpepoojjk@gmail.com";
    private final String contraseña = "sgpe1234";
    
    
    public void enviarCorreo () throws AddressException, MessagingException {
        
// Instanciar un objeto de tipo properties (propiedades del correo)
        Properties props = new  Properties();
        //Setea al host al TTLS y a un puerto
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        //Autenticación
        props.setProperty("mail.smtp.user", usuarioAdmi);
        props.setProperty("mail.smtp.clave", contraseña);
        
        //Se crea una sesion, vincula con gmail
        Session sesion = Session.getDefaultInstance(props);
        
        
        //Redacción del mensaje
        MimeMessage mensaje = new MimeMessage(sesion);
        
        //Envía el correo al destinatario
        mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress ("KevRjs172@gmail.com"));
        //mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress ("joseblanco1313@gmail.com"));
        //Escribe el asunto
        mensaje.setSubject("Información del plan de estudios");
 
 
        //Archivo PDF
         BodyPart mensajeTexto = new MimeBodyPart();
         mensaje.setText("Se adjunta el archivo PDF con las consultas realizadas");
         mensajeTexto.setText("Consultas realizadas");
        //Archivo adjunto
        BodyPart adjunto = new MimeBodyPart();
        
        
        //Ruta archivo Kevin: C:\\Users\\KevRj\\OneDrive\\Documents\\NetBeansProjects\\sgpeVI\\planEstudios.pdf
        //Ruta archivo Jose: C:\\Users\\Jose Blanco\\Documents\\NetBeansProjects\\sgpe\\planEstudios.pdf 
        adjunto.setDataHandler (new DataHandler (new FileDataSource("C:\\Users\\KevRj\\OneDrive\\Documents\\NetBeansProjects\\sgpeVI\\planEstudios.pdf")));
        adjunto.setFileName("planEstudios.pdf");
        //Se une texto con PDF
        MimeMultipart multiparte = new MimeMultipart ();
        multiparte.addBodyPart(mensajeTexto);
        multiparte.addBodyPart(adjunto);
        
        mensaje.setContent(multiparte);
        
        
        
        //Servidor de salida del correo
        Transport transport = sesion.getTransport("smtp");
        
        transport.connect(usuarioAdmi, contraseña);
        transport.sendMessage(mensaje, mensaje.getRecipients(Message.RecipientType.TO));
        transport.close();
        System.out.println("Correo enviado");
    }

}
