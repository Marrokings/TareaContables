package sistemacontable;
import javax.swing.JOptionPane;
public class abrir{
    Runtime aplicacion;
    public void abrirwamp(){
   	aplicacion = Runtime.getRuntime();
        try{
            aplicacion.exec("cmd.exe /K C:/wamp/wampmanager.exe");
        }
        catch(Exception e){JOptionPane.showMessageDialog(null,e);}
    }
}
