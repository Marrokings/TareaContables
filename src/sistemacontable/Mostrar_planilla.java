/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sistemacontable;
import java.awt.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.*;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
/**
 *
 * @author Rebeca
 */
public class Mostrar_planilla extends JFrame{
    Conexion c = new Conexion();
    String driver = "com.mysql.jdbc.Driver";
     private ResultSetTableModel modelo;
    private JTextArea area;

    public Mostrar_planilla(String consulta,String Name){
        super(Name);
        try{
            modelo = new ResultSetTableModel(driver,c.StringConect,consulta);
            JScrollPane scroll = new JScrollPane(area,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            Box boxnorte = Box.createHorizontalBox();
            boxnorte.add(scroll);
            JTable tablaResult = new JTable(modelo);
            add(new JScrollPane(tablaResult),BorderLayout.CENTER);
            final TableRowSorter<TableModel>sorter = new TableRowSorter<TableModel>(modelo);
            tablaResult.setRowSorter(sorter);
            setSize(1000,250);
            //setVisible(true);
        }
        catch(ClassNotFoundException no){
            System.out.println("Nose encontro el controlador");
            System.exit(1);
        }
        catch(SQLException ex){
            System.out.println("no se encontro la base");
            System.exit(1);
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
