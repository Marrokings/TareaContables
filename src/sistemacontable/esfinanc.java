
package sistemacontable;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class esfinanc extends JFrame implements ActionListener,ItemListener {
    //Acceso a DB
    Conexion c = new Conexion();

    //Creacion de componentes para el formulario
    JFrame lb = new JFrame();
    Container contenedorLD;
    Container contenedor1;
    JButton salir = new JButton("Cancelar");
    JInternalFrame escoger = new JInternalFrame("ESCOJA PERIODO A GENERAR");
    JComboBox periodo = new JComboBox();
    JLabel lab = new JLabel("Seleccione el mes a evaluar en el estado financiero:");
    ImageIcon im = new ImageIcon(getClass().getResource("/imagenes/contabilidad.jpg"));
    JLabel lbl = new JLabel();
    JLabel lbl2 = new JLabel();
    String per;

    //Para ventana estados financieros
    JInternalFrame estados = new JInternalFrame("ESTADOS FINANCIEROS");
    JComboBox estfinan = new JComboBox();
    JButton mostrar = new JButton("Ver");
    JButton cerrar = new JButton("Cancelar");

    public void ver(JFrame p){
       lb=p;
       lb.setSize(800, 500);
       lb.setLocationRelativeTo(null);
       contenedor1=lb.getContentPane();
       
       escoger.setSize(500,150);
       escoger.setLocation(130,80);
       estados.setSize(500,200);
       estados.setLocation(130,80);
       estfinan.setBounds(25,60,225,20);
       mostrar.setBounds(300,60, 150, 20);
       cerrar.setBounds(170,90,150,20);
       estados.add(estfinan);
       estfinan.addItem("Balanza de Comprobacion");
       estfinan.addItem("Estado de Resultados");
       estfinan.addItem("Balance General");
       estados.add(mostrar);
       mostrar.addActionListener(this);
       estados.add(cerrar);
       cerrar.addActionListener(this);

       lbl.setIcon(im);
       lbl2.setIcon(im);
       contenedorLD=lb.getContentPane();
       contenedorLD.setLayout(null);
       periodo.setBounds(40,50,225,20);
       periodo.addItem("");
       periodo.addItemListener(this);
       salir.setBounds(290,50,150,20);
       salir.addActionListener(this);

       obtenerfecha();
       lbl.setBounds(0, 0, 500, 500);
       lbl2.setBounds(0, 0, 500, 500);
       escoger.add(periodo);
       escoger.add(salir);
       escoger.add(lbl);
       estados.add(lbl2);
       contenedorLD.add(estados);
       estados.setVisible(false);
       contenedorLD.add(escoger);
       lb.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
       lb.setResizable(false);
       escoger.setVisible(true);
       lb.setVisible(true);
    }

    public void obtenerfecha(){
        c.lle="select fecha from transacciones";
        c.llenar();
        String ant1, ant2="";
        int i;
        try{
            while(c.Resultado.next()){
            ant1=c.Resultado.getString("fecha");
            if(!ant1.equals(ant2)){periodo.addItem(c.Resultado.getString("fecha"));}
            c.Resultado.next();
            ant2=ant1;
            }c.Resultado.refreshRow();
        }catch(SQLException e) {}
    }

    public void itemStateChanged(ItemEvent e){
        boolean isSelected;
        isSelected=(e.getStateChange()==ItemEvent.SELECTED);
        if(e.getItemSelectable()==periodo){
           if(isSelected){
               per=periodo.getSelectedItem().toString();
               escoger.setVisible(false);
               estados.setVisible(true);
           }
       }
    }

    public void actionPerformed(ActionEvent l){
        String com=l.getActionCommand();
        String m;
        if("Cancelar".equals(com)){
           Inicio i = new Inicio();
           lb.dispose();
        }else if("Ver".equals(com)){
            m = estfinan.getSelectedItem().toString();
            if (m.equals("Balanza de Comprobacion")){
                obtenerreporte("BalanzaComprobacion");
            }else if(m.equals("Estado de Resultados")){
                estadoresultados();
                obtenerreporte("estadoresultados");
            }else if(m.equals("Balance General")){
                obtenerreporte("BalanceGeneral");
            }
        }
    }
    public void obtenerreporte(String reporte){
        JasperPrint jPrint;
                try {
                    Map map = new HashMap();
                    map.put("per",per);
                    jPrint = JasperFillManager.fillReport(this.getClass().getClassLoader().getResourceAsStream("reportes/"+reporte+".jasper"),map,c.rep());
                    JRViewer jv = new JRViewer(jPrint);
                    contenedor1.add(jv);
                    JFrame report = new JFrame();
                    report.setSize(750, 400);
                    report.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    report.getContentPane().add(jv);
                    report.setLocationRelativeTo(null);
                    report.setVisible(true);
                } catch (JRException ex) {
                        JOptionPane.showMessageDialog(null,ex);
                }
    }


    public void estadoresultados(){
     double saltotal=0;
     boolean res;
     boolean RL=true;
     double otro=0;
     //Ventas netas
     c.lle="select distinct saldodeudor from cuentasmayor, lddebe, ldhaber, transacciones where "+
             "(codigocuenta=codcuentad or codigocuenta=codcuentah) and " +
             "(lddebe.ntransaccion = ldhaber.ntransaccion) in" +
             "(SELECT ntrans FROM transacciones WHERE fecha = '"+per+"' ) and codigocuenta='512'";
     c.llenar();
     try{
        res=c.Resultado.next();
        while(res){
            otro=otro+c.Resultado.getDouble("saldodeudor");
            res=c.Resultado.next();
        }
        c.Resultado.refreshRow();
     }catch(Exception e){}
     c.lle="select distinct saldo from estresultados where indice=0";
     c.llenar();
     try{
        res=c.Resultado.next();
        while(res){
            saltotal=saltotal+c.Resultado.getDouble("saldo");
            res=c.Resultado.next();
        }
        c.Resultado.refreshRow();
     }catch(Exception e){}
     saltotal=saltotal-otro;
     c.upd="update estresultados set saldo="+saltotal +"where indice=1";
     c.actuali();

     //costo de venta
     otro=0;
     String iif;
     c.lle="select distinct saldodeudor from cuentasmayor, lddebe, transacciones " +
             "where codigocuenta='115' or codigocuenta='424' and codigocuenta=codcuentad in " +
             "(SELECT ntrans FROM transacciones WHERE fecha = '"+per+"' )";
     c.llenar();
     try{
        res=c.Resultado.next();
        if(res){
            otro=otro + c.Resultado.getDouble("saldodeudor");
            res=c.Resultado.next();
        }
        c.Resultado.refreshRow();
     }catch(Exception e){}
     c.lle="select distinct saldoacreedor from cuentasmayor, ldhaber, transacciones " +
             "where codigocuenta='425' and codigocuenta=codcuentah in " +
             "(SELECT ntrans FROM transacciones WHERE fecha = '"+per+"' )";
     c.llenar();
     try{
        res=c.Resultado.next();
        if(res){
            otro= otro - c.Resultado.getDouble("saldoacreedor");
            res=c.Resultado.next();
        }
        c.Resultado.refreshRow();
     }catch(Exception e){}
     iif=JOptionPane.showInputDialog("Digite el valor de Inventario Final:");
     otro=-1*otro-Double.parseDouble(iif);
     c.upd="update estresultados set saldo="+otro +"where indice=2";
     c.actuali();
     saltotal=saltotal+otro;
     c.upd="update estresultados set saldo="+saltotal +"where indice=3";
     c.actuali();

     //Primera parte del estado de resultados
     otro=0;
     c.lle="select distinct saldo from estresultados, " +
             "cuentasmayor, lddebe, ldhaber where (indice!=0 and saldo!=0 " +
             "and codigo=codcuentad or codigo=codcuentah) and " +
             "(lddebe.ntransaccion = ldhaber.ntransaccion) in" +
             "(SELECT ntrans FROM transacciones WHERE fecha = '"+per+"' ) " +
             "and (indice=3 or indice=4 or indice=5 or indice=6 or indice=7) " +
             "order by indice";
     c.llenar();
     try{
        res=c.Resultado.next();
        while(res){
            saltotal=saltotal+c.Resultado.getDouble("saldo");
            res=c.Resultado.next();
        }
        c.Resultado.refreshRow();
     }catch(Exception e){}
     c.upd="update estresultados set saldo="+saltotal +"where indice=8";
     c.actuali();

     //segunda parte del estado de resultados
     c.lle="select distinct saldo from estresultados, " +
             "cuentasmayor, lddebe, ldhaber where (indice!=0 and saldo!=0 " +
             "and codigo=codcuentad or codigo=codcuentah) and " +
             "(lddebe.ntransaccion = ldhaber.ntransaccion) in" +
             "(SELECT ntrans FROM transacciones WHERE fecha = '"+per+"' ) " +
             "and (indice between 9 and 18) " +
             "order by indice";
     c.llenar();
     try{
        res=c.Resultado.next();
        while(res){
            saltotal=saltotal+c.Resultado.getDouble("saldo");
            res=c.Resultado.next();
        }
        c.Resultado.refreshRow();
     }catch(Exception e){}
     c.upd="update estresultados set saldo="+saltotal +"where indice=19";
     c.actuali();

     //tercera parte del estado de resultados
     otro=-1;
     c.lle="select distinct saldo from estresultados, " +
             "cuentasmayor, lddebe, ldhaber where (indice!=0 and saldo!=0 " +
             "and codigo=codcuentad or codigo=codcuentah) and " +
             "(lddebe.ntransaccion = ldhaber.ntransaccion) in" +
             "(SELECT ntrans FROM transacciones WHERE fecha = '"+per+"' ) " +
             "and (indice=21 or indice=22)" +
             "order by indice";
     c.llenar();
     otro=otro*0.07*saltotal;
     saltotal=saltotal-(0.07*saltotal);
     c.upd="update estresultados set saldo="+otro+"where indice=20";
     c.actuali();
     try{
        res=c.Resultado.next();
        while(res){
                saltotal=saltotal+c.Resultado.getDouble("saldo");
                res=c.Resultado.next();
        }
        c.Resultado.refreshRow();
     }catch(Exception e){}
     c.upd="update estresultados set saldo="+saltotal +"where indice=23";
     c.actuali();

     //cuarta parte del estado de resultados
     otro=0;
     otro=-1*saltotal*0.15;
     saltotal=saltotal-(saltotal*0.15);
     c.upd="update estresultados set saldo="+otro+"where indice=24";
     c.actuali();
     c.upd="update estresultados set saldo="+saltotal +"where indice=25";
     c.actuali();

     //quinta parte del estado de resultados
     c.lle="select distinct saldo from estresultados, " +
             "cuentasmayor, lddebe, ldhaber where (indice!=0 and saldo!=0 " +
             "and codigo=codcuentad or codigo=codcuentah) and " +
             "(lddebe.ntransaccion = ldhaber.ntransaccion) in" +
             "(SELECT ntrans FROM transacciones WHERE fecha = '"+per+"' ) " +
             "and (indice=26)" +
             "order by indice";
     c.llenar();
     try{
        res=c.Resultado.next();
        while(res){
                saltotal=saltotal+c.Resultado.getDouble("saldo");
                res=c.Resultado.next();
        }
        c.Resultado.refreshRow();
     }catch(Exception e){}
     c.upd="update estresultados set saldo="+saltotal +"where indice=27";
     c.actuali();


     //sexta parte del estado de resultados
     c.lle="select distinct saldo from estresultados, " +
             "cuentasmayor, lddebe, ldhaber where (indice!=0 and saldo!=0 " +
             "and codigo=codcuentad or codigo=codcuentah) and " +
             "(lddebe.ntransaccion = ldhaber.ntransaccion) in" +
             "(SELECT ntrans FROM transacciones WHERE fecha = '"+per+"' ) " +
             "and (indice=28 or indice=29)" +
             "order by indice";
     c.llenar();
     try{
        res=c.Resultado.next();
        while(res){
                saltotal=saltotal+c.Resultado.getDouble("saldo");
                res=c.Resultado.next();
        }
        c.Resultado.refreshRow();
     }catch(Exception e){}
     c.upd="update estresultados set saldo="+saltotal +"where indice=30";
     c.actuali();
    }
}
