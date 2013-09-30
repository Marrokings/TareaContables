package sistemacontable;

import java.awt.*;
import java.util.Date;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class Inicio extends JFrame implements ActionListener{

    Conexion co = new Conexion();
    JFrame principal = new JFrame("SISTEMA CONTABLE");
    JInternalFrame secundario = new JInternalFrame("ESCOJA UNA TAREA");
    Container contenedor1 = principal.getContentPane();
    JComboBox acciones = new JComboBox();
    JButton aceptar = new JButton("Aceptar");
    JButton salir = new JButton("Cerrar");
    JMenuBar barra = new JMenuBar();
    JMenu archivo = new JMenu("Archivo");
    JMenu tareas = new JMenu("Tareas");
    JMenu ayuda = new JMenu("Ayuda");
  //  ImageIcon im = new ImageIcon(getClass().getResource("/imagenes/contab.jpg"));
  //  ImageIcon im2 = new ImageIcon(getClass().getResource("/imagenes/contabilidad.jpg"));
    JLabel lbl = new JLabel();
    JLabel lbl2 = new JLabel();
    Date fec= new Date();
    esfinanc ef = new esfinanc();


    public Inicio() {
       principal.setSize(800, 500);
       principal.setLocationRelativeTo(null);
       secundario.setSize(500,200);
       secundario.setLocation(130,80);
       contenedor1.setLayout(null);

       barraprincipal();
 //      lbl.setIcon(im);
 //      lbl2.setIcon(im2);

       acciones.setBounds(25,60,225,20);
       aceptar.setBounds(300,60, 150, 20);
       salir.setBounds(170,90,150,20);
       
       lbl.setBounds(0, 0, 500, 500);
       lbl2.setBounds(0, 0, 500, 500);

       secundario.add(acciones);
       secundario.add(aceptar);
       secundario.add(salir);
       secundario.add(lbl);
       contenedor1.add(secundario);

       principal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       principal.setJMenuBar(barra);
       principal.setResizable(false);
       contenedor1.setBackground(Color.GRAY);
       secundario.setVisible(true);
       principal.setVisible(true);
    }

    //Metodo para la barra principal del programa y la ventana interna
    public void barraprincipal(){
       setJMenuBar(barra);
       //Menu archivo
       archivo.add("Salir del Programa").addActionListener(this);
       barra.add(archivo);
       //Menu tareas
       tareas.add("Registros Diarios").addActionListener(this);
       tareas.addSeparator();
       tareas.add("Ver Catalogo de Cuentas").addActionListener(this);
       tareas.addSeparator();
       tareas.add("Empleados").addActionListener(this);
       tareas.addSeparator();
       tareas.add("Productos").addActionListener(this);
       tareas.addSeparator();
       tareas.add("Generacion de Estados Financieros").addActionListener(this);
       barra.add(tareas);
       //Menu Ayuda
       ayuda.add("Acerca de...").addActionListener(this);
       ayuda.addSeparator();
       barra.add(ayuda);
       //Para ventana interna
       acciones.addItem("Registros Diarios");
       acciones.addItem("Ver Catalogo de Cuentas");
       acciones.addItem("Empleados");
       acciones.addItem("Productos");
       acciones.addItem("Generacion de Estados Financieros");
       aceptar.addActionListener(this);
       salir.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)  {
        String com=e.getActionCommand();
        String m;
        if("Aceptar".equals(com)){
            m = acciones.getSelectedItem().toString();
            if (m.equals("Registros Diarios")){
                secundario.dispose();
                barra.getComponent(1).setEnabled(false);
                LibroDiario l = new LibroDiario();
                
               l.LibroDiari(principal);

            }else if(m.equals("Ver Catalogo de Cuentas")){
                obtenerreporte("catalogo");
            }else if(m.equals("Empleados")){
                secundario.dispose();
                barra.getComponent(1).setEnabled(false);
                Empleados em = new Empleados();
                em.Empleados(principal);
            }else if(m.equals("Productos")){
                secundario.dispose();
                barra.getComponent(1).setEnabled(false);
                Producto pro = new Producto();
                pro.Producto(principal);
            }else if(m.equals("Generacion de Estados Financieros")){
                secundario.dispose();
                barra.getComponent(1).setEnabled(false);
                ef.ver(principal);
            }
        }else if("Salir del Programa".equals(com)){
            System.exit(0);
        }else if("Cerrar".equals(com)){
            secundario.dispose();
        }else if("Tareas".equals(com)){
            secundario.show();
        }else if("Registros Diarios".equals(com)){
            secundario.dispose();
            barra.getComponent(1).setEnabled(false);
            LibroDiario l = new LibroDiario();
            l.LibroDiari(principal);
        }else if("Ver Catalogo de Cuentas".equals(com)){
                obtenerreporte("catalogo");
        }else if("Empleados".equals(com)){
                secundario.dispose();
                barra.getComponent(1).setEnabled(false);
                Empleados em = new Empleados();
                em.Empleados(principal);
        }else if("Productos".equals(com)){
                secundario.dispose();
                barra.getComponent(1).setEnabled(false);
                Producto pro = new Producto();
                pro.Producto(principal);
        }else if("Generacion de Estados Financieros".equals(com)){
            secundario.dispose();
            barra.getComponent(1).setEnabled(false);
            ef.ver(principal);
        }else if("Acerca de...".equals(com)){
            JOptionPane.showMessageDialog(null, "Universidad de El Salvador\nFacultad de Ingenieria y Arquitectura" +
                    "\nTarea de Sistemas Contables (SIC115)" +
                    "\nEntregado: 16 de Enero 2012" +
                    "\nCiclo II-2011");
        }
    }

    public void obtenerreporte(String reporte){
        JasperPrint jPrint;
                try {
                    Map map = new HashMap();
                    map.put("par",'%'+""+'%');
                    jPrint = JasperFillManager.fillReport(this.getClass().getClassLoader().getResourceAsStream("reportes/"+reporte+".jasper"),map,co.rep());
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
}