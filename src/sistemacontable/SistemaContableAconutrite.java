package sistemacontableaconutrite;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class SistemaContableAconutrite {
    public static void main(String[] args) {

        
    
       //whistlerthemepack    b0sumiErgothempack
         try{
          JFrame.setDefaultLookAndFeelDecorated(true);
          SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack("temas/roueGreenthemepack.zip"));
          UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
            Inicio ini = new Inicio();
            ini.setVisible(true);
           
           
       }
       catch(Exception e){
           JOptionPane.showMessageDialog(null, e);
       }
        
        
    }
}
