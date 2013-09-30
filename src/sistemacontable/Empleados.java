package sistemacontable;

import java.awt.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;


public class Empleados extends JFrame implements ActionListener {
    //Acceso a DB
    Conexion c = new Conexion();

    //Creacion de componentes para el formulario
    JFrame emple = new JFrame();
    JFrame visib = new JFrame();
    Container contenedorEmple;
    JButton agreEmple = new JButton("Agregar Empleado");
    JButton genplanilla = new JButton("Generar planilla");
    JButton salir = new JButton("Regresar");
    JLabel titulo = new JLabel("Seleccione una opcion: ");
    JLabel imagen = new JLabel();

    JInternalFrame Empleado = new JInternalFrame("Empleados");
   // ImageIcon im = new ImageIcon(getClass().getResource("/imagenes/contabilidad.jpg"));

    //creacion de los componentes de formulario de ingreso de salario
    JTextField tcod = new JTextField();
    JTextField tnom = new JTextField();
    JTextField tsal = new JTextField();
    JTextField tdep = new JTextField();
    JTextField ttime = new JTextField();
    JTextField tafp = new JTextField();

    //datos a utilizar para la planilla
    final double CR_AFP = 0.065;
    final double CN_AFP = 0.0675;
    final double INSF = 0.01;
    final double ISSS = 0.075;
    double salnom = 0;
    double vacaciones;
    double aguinaldo = 0;
    double isss = 0;
    double afp = 0;
    double insafor = 0;
    double salario = 0;
    static int num = 10;

    public void Empleados(JFrame p){
        //asigna al JFrame lb las propiedades del JFrame p que viene de la ventana principal
        emple=p;
        //Obtiene contenedor de lb para almacenar objetos
        contenedorEmple = emple.getContentPane();
        //Asigna tamaño al Frame, establece el tipo de contenedor
        emple.setSize(600, 450);
        emple.setLocationRelativeTo(null);
        contenedorEmple.setLayout(null);
        ventinterna();
        contenedorEmple.add(Empleado);
        Empleado.setVisible(true);
        emple.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    //inicializa ventana sub cuenta
    public void ventinterna(){
        Empleado.setSize(300,200);
        Empleado.setLocation(150,50);
        Empleado.setLayout(null);
        agreEmple.setBounds(45, 30, 200, 20);
        agreEmple.addActionListener(this);
        genplanilla.setBounds(45, 60, 200, 20);
        genplanilla.addActionListener(this);
        salir.setBounds(45, 90, 200, 20);
        salir.addActionListener(this);
      //  imagen.setIcon(im);
        imagen.setBounds(0, 0, 300, 200);
        Empleado.add(agreEmple);
        Empleado.add(genplanilla);
        Empleado.add(salir);
        Empleado.add(imagen);
    }

    //Permite a los botones ejecutar instrucciones
    public void actionPerformed(ActionEvent e){
       String com=e.getActionCommand();
       if("Agregar Empleado".equals(com)){
           Empleado.dispose();
           agreEmple();
       }
       else if("Regresar".equals(com)){
           emple.dispose();
           Inicio i = new Inicio();
       }
       else{
           if("Generar planilla".equals(com)){
                planilla();
            }
            else{
                if("Insertar".equals(com)){
                    Insertar();
                    Insert_planilla();
                    tcod.setText("");
                    tnom.setText("");
                    tsal.setText("");
                    tdep.setText("");
                    ttime.setText("");
                    tafp.setText("");                    
                }
                else{
                    if("Buscar".equals(com)){
                        buscar();
                    }
                    else{
                        if("Actualizar".equals(com)){

                        }
                        else if("Eliminar".equals(com)){
                            borrar_Emple();
                        }
                        else if("Atras".equals(com)){
                            Empleado.dispose();
                            Empleado = new JInternalFrame();
                            agreEmple.setVisible(true);
                            genplanilla.setVisible(true);
                            salir.setVisible(true);
                            Empleado.setSize(300,200);
                            Empleado.setLocation(150,50);
                            Empleado.setLayout(null);
                            Empleado.add(agreEmple);
                            Empleado.add(genplanilla);
                            Empleado.add(salir);
                            Empleado.add(imagen);
                            contenedorEmple.add(Empleado);
                            Empleado.setVisible(true);
                        }
                    }
                }
        }
    }
}
    //Formulario de insercion de datos
    public void agreEmple(){
        Color col = Color.WHITE;

        //nombre de las labels
        JLabel lcod = new JLabel("Codigo de empleado: ");
        JLabel lnom = new JLabel("Nombre del empleado: ");
        JLabel lsal = new JLabel("Salario: ");
        JLabel ldep = new JLabel("Departamento: ");
        JLabel ltime = new JLabel("Meses de trabajar en la empresa: ");
        JLabel lafp = new JLabel("Tipo de AFP : ");

        JButton inset = new JButton("Insertar");
        JButton Busc = new JButton("Buscar");
        JButton Actua = new JButton("Actualizar");
        JButton Eliminar = new JButton("Eliminar");
        JButton atra = new JButton("Atras");
        inset.addActionListener(this);
        Busc.addActionListener(this);
        Actua.addActionListener(this);
        Eliminar.addActionListener(this);

        //Para utilizacion del mismo frame oculta funciones anteriores
        agreEmple.setVisible(false);
        genplanilla.setVisible(false);
        salir.setVisible(false);

        //da color a las labels
        lcod.setForeground(col);
        lnom.setForeground(col);
        lsal.setForeground(col);
        ldep.setForeground(col);
        ltime.setForeground(col);
        lafp.setForeground(col);

        //Tamanos de los componentes
        lcod.setBounds(30, 30, 200, 20);
        lnom.setBounds(30, 60, 200, 20);
        lsal.setBounds(30, 90, 200, 20);
        ldep.setBounds(30, 120, 200, 20);
        ltime.setBounds(30, 150, 200, 20);
        lafp.setBounds(30, 180, 200, 20);
        tcod.setBounds(250, 30, 200, 20);
        tnom.setBounds(250, 60, 200, 20);
        tsal.setBounds(250, 90, 200, 20);
        tdep.setBounds(250, 120, 200, 20);
        ttime.setBounds(250, 150, 200, 20);
        tafp.setBounds(250, 180, 200, 20);
        inset.setBounds(30, 220, 100, 20);
        Busc.setBounds(150, 220, 100, 20);
        Actua.setBounds(270, 220, 100, 20);
        Eliminar.setBounds(390, 220, 100, 20);
        atra.setBounds(220, 250, 100, 20);
        atra.addActionListener(this);
        //introduccion de datos al frame
        Empleado.setSize(530,325);
        Empleado.setLocation(40,30);
        Empleado.setLayout(null);
      //  imagen.setIcon(im);
        imagen.setBounds(0, 0, 530, 325);
        Empleado.add(lcod);
        Empleado.add(lnom);
        Empleado.add(lsal);
        Empleado.add(ldep);
        Empleado.add(ltime);
        Empleado.add(lafp);
        Empleado.add(tcod);
        Empleado.add(tnom);
        Empleado.add(tsal);
        Empleado.add(tdep);
        Empleado.add(ttime);
        Empleado.add(tafp);
        Empleado.add(inset);
        Empleado.add(Busc);
        Empleado.add(Actua);
        Empleado.add(Eliminar);
        Empleado.add(atra);
        Empleado.add(imagen);
        contenedorEmple.add(Empleado);
        Empleado.setVisible(true);
    }

    //insercion de datos en la base de datos
    private void Insertar(){
        c.add="insert into empleados values ('"+tcod.getText().toUpperCase()+"','"+tnom.getText().toUpperCase()+
                "','"+tsal.getText()+"','"+tdep.getText()+"','"+ttime.getText()+"','"+tafp.getText().toUpperCase()+"')";
        c.insert();
    }

    //inserta los datos a la planilla
    public void Insert_planilla(){
        //variables a utilizar
        int n;
        int val = 0;
        double mes;
        double afps = 0;
        double prestaciones = ISSS;
        double vacans = 0;
        double valor_vacans = 0;

        //le da formato a los valores que se encontraran en la planilla
        DecimalFormat a = new DecimalFormat(".00");
        
        //inicia algunas variable
        vacaciones = 0;
        aguinaldo = 0;

        //determina los calculos que conformaran la planilla al mes
        String ntiempo = ttime.getText();
        n = Integer.parseInt(ntiempo);
        salnom = Double.parseDouble(tsal.getText());
        mes = salnom/30;

        //determinacion de afp y prestaciones a pagar en vacaciones
        if((tafp.getText().toUpperCase()).equals("CR")){
            afps = CR_AFP;
        }
        else
            afps = CN_AFP;
        prestaciones += afps;

        //determinacion de las vacaciones asumiendo que el tiempo son meses
        if(n>=12)
            valor_vacans += 15*mes*1.3;
        vacans = valor_vacans/12;
        vacaciones += (vacans*prestaciones);
        vacaciones += vacans;

        //determinacion de aguinaldo
        if( n >= 12){
            if(n > 120)
                val = 18;
            else
                val = (n <= 36)? 10 : 15;            
        }
        aguinaldo = ((mes*val)/12);

        //determinacion del las prestaciones
        isss = salnom * ISSS;
        afp = salnom * afps;
        
        //determina el numero de empleados revisar logica
        //c.sel = "select count(codigoemple) from empleados ";
        //c.select_da();
        /*try {
            //num = c.select();
            if (c.Resultado.next()) {
                num = Integer.parseInt(c.Resultado.getString("count(codigoemple)"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        insafor = salnom*0.01;

        //determinacion del salario
        salario = salnom + vacaciones + aguinaldo + isss + afp + insafor;

        //insercion a la base con formato .00
        c.add = "Insert into planilla values ('"+tcod.getText().toUpperCase()+"','"+String.valueOf(a.format(salnom))+"','"+
                String.valueOf(a.format(vacaciones))+"','"+String.valueOf(a.format(aguinaldo))+"','"+
                String.valueOf(a.format(isss))+"','"+String.valueOf(a.format(afp))+"','"+String.valueOf(a.format(insafor))+
                "','"+String.valueOf(a.format(salario))+"')";
        c.insert();
    }

    //vista de la planilla
    public void planilla(){
        String Nombre = "Planilla de empleados";
        String CONSULTA = "Select codigoemple 'Codigo de empleado',salarionomin 'Salario nominal', vacaciones 'Vacaciones', "
            + "aguinaldo 'Aguinaldo',isss 'ISSS',afp 'AFP',insaford 'INSAFORD',salarioreal 'SALARIO REAL' from planilla";
        Mostrar_planilla a = new Mostrar_planilla(CONSULTA,Nombre);
        contenedorEmple = a.getContentPane();
        a.setVisible(true);
    }
    
    //hace la busqueda
    public void buscar(){
        c.sel = "select * from empleados where codigoemple = '"+tcod.getText().toUpperCase()+"' ";
        c.select_da();
        busqueda();
    }
    //hace la busqueda de los empleados
    public void busqueda(){
        try { 
            if(c.Resultado.next()){
                //variables de apoyo
                String apoyo = null;

                //Para este solo se tomara en cuenta el codigo del empleado
                apoyo = c.Resultado.getString("codigoemple");
                tcod.setText(apoyo);
                apoyo = c.Resultado.getString("nombreemple");
                tnom.setText(apoyo);
                apoyo = c.Resultado.getString("salario_nom");
                tsal.setText(apoyo);
                apoyo = c.Resultado.getString("departemple");
                tdep.setText(apoyo);
                apoyo = c.Resultado.getString("tiempotrabajo");
                ttime.setText(apoyo);
                apoyo = c.Resultado.getString("tipo_afp");
                tafp.setText(apoyo);
                tcod.setEditable(true);
                tnom.setEditable(false);
                tsal.setEditable(false);
                tdep.setEditable(false);
                ttime.setEditable(false);
                tafp.setEditable(false);
                agreEmple();
            }
        }
       catch (SQLException e) {
                System.out.println("no sale");
        }
    }

     //Eliminacion de datos
    public void borrar_Emple(){
        //borra primero los datos de la planilla para no tener problemas
        //con la eliminacion de datos del empleado
        c.del = "Delete from planilla where codigoemple = '"+tcod.getText().toUpperCase()+"'";
        c.borrar();
        c.del = "Delete from empleados where codigoemple = '"+tcod.getText().toUpperCase()+"'";
        c.borrar();
        JOptionPane.showMessageDialog(null, "Los datos del empleado "+tcod.getText().toUpperCase()+" han sido borrados en planilla y base de datos");
    }
}