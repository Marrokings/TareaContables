package sistemacontable;

import javax.swing.*;
import java.sql.*;

public class Conexion{

    //Declaraciones para conexiones y manejo de base de datos
    String StringConect="jdbc:mysql://localhost:3306/sistemacontable?user=root";
    ResultSet Resultado;
    Connection conexion;
    Statement Sentencia;
    String lle, add, upd,sel,del;

    //Para reportes
    public Connection rep(){
        try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conexion=DriverManager.getConnection(StringConect);
        }
        catch(Exception e){JOptionPane.showMessageDialog(null, e.getMessage());}
        return conexion;
    }

    //Obtiene sentencia para conexion a las DB
    public Statement obsentenc(String SC){
        try{
        conexion=DriverManager.getConnection(SC);
        Sentencia=conexion.createStatement();
        }
        catch(SQLException e){JOptionPane.showMessageDialog(null, e.getMessage());}
        return Sentencia;
    }


    //Devuelve el resultado de la consulta para posteriormente llenar los
    //combobox de cada formulario donde se requiera y tener un mejor acceso a
    //las llaves foraneas de cada tabla
    public void llenar(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Resultado=obsentenc(StringConect).executeQuery(lle);
        }catch(ClassNotFoundException e){JOptionPane.showMessageDialog(null, e.getMessage());}
        catch(SQLException e) {JOptionPane.showMessageDialog(null, e.getMessage());}
    }


            //Inserta un registro a las db
    public void insert(){
        try{
                Class.forName("com.mysql.jdbc.Driver");     //Para mysql
                obsentenc(StringConect).executeUpdate(add);
        }catch(ClassNotFoundException e){JOptionPane.showMessageDialog(null,e.getMessage());}
        catch(SQLException e){JOptionPane.showMessageDialog(null,e.getMessage());}
    }

        //Actualiza un registro en las db
    public void actuali(){
        try{
                Class.forName("com.mysql.jdbc.Driver");     //Para mysql
                obsentenc(StringConect).executeUpdate(upd);
        }catch(ClassNotFoundException e){JOptionPane.showMessageDialog(null,e.getMessage());}
        catch(SQLException e){JOptionPane.showMessageDialog(null,e.getMessage());}
    }
        //Selecciona datos de la bd para determinar los valores a buscar
    public void select_da(){
        try{
            Class.forName("com.mysql.jdbc.Driver");     //Para mysql
            Resultado = obsentenc(StringConect).executeQuery(sel);
        }catch(ClassNotFoundException e){JOptionPane.showMessageDialog(null,e.getMessage());}
        catch(SQLException e){JOptionPane.showMessageDialog(null,e.getMessage());}
    }

    //Selecciona datos de la bd para determinar los valores a buscar
    public void borrar(){
        try{
            Class.forName("com.mysql.jdbc.Driver");     //Para mysql
            obsentenc(StringConect).executeUpdate(del);
        }catch(ClassNotFoundException e){JOptionPane.showMessageDialog(null,e.getMessage());}
        catch(SQLException e){JOptionPane.showMessageDialog(null,e.getMessage());}
    }
}