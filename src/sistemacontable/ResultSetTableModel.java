/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sistemacontable;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Rebeca
 */
class ResultSetTableModel extends AbstractTableModel{
        private Connection conexion;
        private Statement instruccion;
        private ResultSet Resultado;
        private ResultSetMetaData meta;
        private int numeroDeFilas;

        public ResultSetTableModel(String drive,String url,String consul) throws SQLException, ClassNotFoundException{
            Class.forName(drive);
            conexion = DriverManager.getConnection(url);
            instruccion = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            establecerConsulta(consul);
        }
        public Class getColumnClass(int colum) throws IllegalStateException{
            try{
                String nombreClase = meta.getColumnClassName(colum+1);
                return Class.forName(nombreClase);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return Object.class;
        }
        public int getColumnCount() throws IllegalStateException{
            try{
                return meta.getColumnCount();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return 0;
        }
        public String getColumnName(int col) throws IllegalStateException{
            try{
                return meta.getColumnName(col+1);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return "";
        }
        public int getRowCount() throws IllegalStateException{
            return numeroDeFilas;
        }
        public Object getValueAt(int fila,int colum) throws IllegalStateException{
            try{
                Resultado.absolute(fila+1);
                return Resultado.getObject(colum+1);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return "";
        }
        public void establecerConsulta(String consul) throws SQLException,IllegalStateException{
            Resultado = instruccion.executeQuery(consul);
            meta = Resultado.getMetaData();
            Resultado.last();
            numeroDeFilas = Resultado.getRow();
            fireTableStructureChanged();
        }
}
