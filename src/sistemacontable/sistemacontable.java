/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacontable;

import javax.swing.JFrame;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.watermark.SubstanceImageWatermark;

/**
 *
 * @author Doradea
 */
public class sistemacontable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //   Conexion ahhh=new Conexion();
        //   ahhh.actuali();
        Inicio.setDefaultLookAndFeelDecorated(true);//Visual
     //   SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.BusinessBlueSteelSkin");//Visual
    //    SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceLimeGreenTheme");//Visual
     //   SubstanceLookAndFeel.setCurrentWatermark("org.jvnet.substance.watermark.SubstanceBinaryWatermark");//Visual
  // JFrame.setDefaultLookAndFeelDecorated(true);
//SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.CremeSkin");

         JFrame.setDefaultLookAndFeelDecorated(true);
        SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.EmeraldDuskSkin");
    //     SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceRaspberryTheme");
         SubstanceLookAndFeel.setCurrentWatermark("org.jvnet.substance.watermark.SubstanceMetalWallWatermark");
        Inicio ehhh = new Inicio();
       // TwilightSkin
        //SubstanceMetalWallWatermark
        ehhh.setVisible(true);
    }
}
