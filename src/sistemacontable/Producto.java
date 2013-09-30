/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sistemacontable;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import java.text.DecimalFormat;
/**
 *
 * @author Rebeca
 */
public class Producto extends JFrame implements ActionListener, ItemListener{
    //Acceso a DB
    Conexion c = new Conexion();

    //Creacion de componentes para el formulario
    JFrame Product = new JFrame();
    Container contenedorProd;
    JButton agreProduc = new JButton("Agregar Producto");
    JButton genKardex = new JButton("Proceso");
    JButton salir = new JButton("Regresar");
    JLabel titulo = new JLabel("Seleccione una opcion: ");
    JLabel imagen = new JLabel();

    //Sirve para insertar sub cuentas
    JInternalFrame Producto = new JInternalFrame("Productos");
//    ImageIcon im = new ImageIcon(getClass().getResource("/imagenes/contabilidad.jpg"));

    //creacion de los componentes de formulario de ingreso de datos
    JTextField tcodP = new JTextField();
    JTextField tnomP = new JTextField();
    JTextArea tdescriP = new JTextArea();

    //componentes a utilizar en kardex
    JButton Ingre = new JButton("Ingreso");
    JButton Salida = new JButton("Salida");
    JButton kardex = new JButton("Ver Kardex");
    JButton Saldo = new JButton("Saldo");
    JButton atras = new JButton("Atras");
    JLabel codPro = new JLabel("Codigo del producto: ");
    JLabel lfecha = new JLabel("Fecha: ");
    JLabel lcant = new JLabel("Cantidad: ");
    JLabel lPrecio = new JLabel("Precio: ");    
    JTextField tcodP1 = new JTextField();
    JTextField tfecha1 = new JTextField();
    JTextField tcantP1 = new JTextField();
    JTextField tprecio1 = new JTextField();
    JComboBox combpro = new JComboBox();
    
    //datos a utilizar en el kardex
    int n;
    int val = 1;
    int tra = 0;
    double sal_anterior = 0;
    double cant_anterior = 0;
    double precio_anterior;
    double sal_nuevo;
    double cant_nuevo;
    double precio_nuevo;
    double saldo_Total = 0;
    double cant_Total = 0;
    double Precio_total = 0;
    static int num = 0;
    //Para vista de saldo
    JLabel lcant_saldo = new JLabel("Cantidad Disponible: ");
    JLabel lprecio_saldo = new JLabel("Costo: ");
    JLabel lmonto_saldo = new JLabel("Monto: ");
    JTextField tmonto = new JTextField();
    JTextField tcantP = new JTextField();
    JTextField tprecio = new JTextField();
    JButton atra = new JButton("Volver");

    public void Producto(JFrame p){
        //asigna al JFrame lb las propiedades del JFrame p que viene de la ventana principal
        Product=p;

        //Obtiene contenedor de lb para almacenar objetos
        contenedorProd = Product.getContentPane();
        //Asigna tamaño al Frame, establece el tipo de contenedor
        Product.setSize(600, 450);
        Product.setLocationRelativeTo(null);
        contenedorProd.setLayout(null);
        ventinterna();
        contenedorProd.add(Producto);
        Producto.setVisible(true);
        Product.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    //inicializa ventana sub cuenta
    public void ventinterna(){
        Producto.setSize(300,200);
        Producto.setLocation(150,50);
        Producto.setLayout(null);
        agreProduc.setBounds(45, 30, 200, 20);
        agreProduc.addActionListener(this);
        genKardex.setBounds(45, 60, 200, 20);
        genKardex.addActionListener(this);
        salir.setBounds(45, 90, 200, 20);
        salir.addActionListener(this);
       // imagen.setIcon(im);
        imagen.setBounds(0, 0, 300, 200);
        Producto.add(agreProduc);
        Producto.add(genKardex);
        Producto.add(salir);
        Producto.add(imagen);
    }

    public void llenarinterpro(){
        c.lle="select codproducto from productos";
        c.llenar();
        combpro.addItem(" ");
        try{
        boolean Res = c.Resultado.next();
        while(Res){
              combpro.addItem(c.Resultado.getString("codproducto"));
              Res = c.Resultado.next();
            }
            c.Resultado.refreshRow();
        }catch(SQLException e) {}
    }

    //Permite que las cuentas sean asignadas a los TextFields al seleccionar
    //un elemento de cada ComboBox
    public void itemStateChanged(ItemEvent e){
        boolean isSelected;
        isSelected=(e.getStateChange()==ItemEvent.SELECTED);
        //Parte del debe
        if(e.getItemSelectable()==combpro){ 
           if(isSelected){
               tcodP.setText(combpro.getSelectedItem().toString());                            
               }
        }       
    }

    //Permite a los botones ejecutar instrucciones
    public void actionPerformed(ActionEvent e){
       String com=e.getActionCommand();
       if("Agregar Producto".equals(com)){
           Producto.setVisible(false);
           Producto.dispose();
           agreProduc();
       }
       else if("Regresar".equals(com)){
           Product.dispose();
           Inicio ini = new Inicio();
       }
       else if("Proceso".equals(com)){
                llenarinterpro();
                Proceso();
            }
            else if("Insertar".equals(com)){
                    Insertar();
                    tcodP.setText("");
                    tnomP.setText("");
                    tdescriP.setText("");
                }
                else if("Buscar".equals(com)){
                        buscar_Product();
                    }
                    else if("Eliminar".equals(com)){
                            borrar();
                            tcodP.setText("");
                            tnomP.setText("");
                            tdescriP.setText("");
                        }
                        else if("Ingreso".equals(com)){
                                Insert_kardex();
                                tfecha1.setText("");
                                tcantP1.setText("");
                                tprecio1.setText("");
                            }
                            else if("Salida".equals(com)){
                                    Salida_kardex();
                                    tfecha1.setText("");
                                    tcantP1.setText("");
                                    tprecio1.setText("");
                                 }
                                else if("Saldo".equals(com)){
                                    Saldo();
                                    tfecha1.setText("");
                                    tcantP1.setText("");
                                    tprecio1.setText("");
                                     }
                                    else if("Atras".equals(com)){
                                        Proceso();
                                    }
                                        else if("Ver Kardex".equals(com)){
                                            kardex();
                                        }
                                        else if("Volver".equals(com)){
                                            Producto.dispose();
                                            Producto = new JInternalFrame();
                                            agreProduc.setVisible(true);
                                            genKardex.setVisible(true);
                                            salir.setVisible(true);
                                            Producto.setSize(300,200);
                                            Producto.setLocation(150,50);
                                            Producto.setLayout(null);
                                            Producto.add(agreProduc);
                                            Producto.add(genKardex);
                                            Producto.add(salir);
                                            Producto.add(imagen);
                                            contenedorProd.add(Producto);
                                            Producto.setVisible(true);
                                        }
    }

    //Formulario de insercion de productos
    public void agreProduc(){
        Color col = Color.WHITE;
        
        //nombre de las labels
        JLabel lcod = new JLabel("Codigo del producto(3 caracteres): ");
        JLabel lnom = new JLabel("Nombre del producto: ");
        JLabel ldescr = new JLabel("Descripcion: ");
        JButton inset = new JButton("Insertar");
        JButton Busc = new JButton("Buscar");
        JButton Eliminar = new JButton("Eliminar");
        inset.addActionListener(this);
        Busc.addActionListener(this);
        Eliminar.addActionListener(this);

        //Para utilizacion del mismo frame oculta funciones anteriores
        agreProduc.setVisible(false);
        genKardex.setVisible(false);
        salir.setVisible(false);

        //da color a las labels
        lcod.setForeground(col);
        lnom.setForeground(col);
        ldescr.setForeground(col);
        
        //Tamanos de los componentes
        lcod.setBounds(30, 30, 200, 20);
        lnom.setBounds(30, 60, 200, 20);
        ldescr.setBounds(30, 90, 200, 20);
        tcodP.setBounds(250, 30, 200, 20);
        tnomP.setBounds(250, 60, 200, 20);
        tdescriP.setBounds(250, 90, 200, 20);
        inset.setBounds(30, 130, 100, 20);
        Busc.setBounds(150, 130, 100, 20);
        Eliminar.setBounds(390, 130, 100, 20);
        atra.setBounds(210, 170, 100, 20);
        atra.addActionListener(this);

        //introduccion de datos al frame
        Producto.setSize(530,250);
        Producto.setLocation(30,50);
        Producto.setLayout(null);
   //     imagen.setIcon(im);
        imagen.setBounds(0, 0, 530, 250);
        Producto.add(lcod);
        Producto.add(lnom);
        Producto.add(ldescr);
        Producto.add(tcodP);
        Producto.add(tnomP);
        Producto.add(tdescriP);
        Producto.add(inset);
        Producto.add(Busc);
        Producto.add(Eliminar);
        Producto.add(atra);
        Producto.add(imagen);
        contenedorProd.add(Producto);
        Producto.setVisible(true);
    }

    //insercion de productos en la base de datos
    private void Insertar(){
        c.add="insert into productos values ('"+tcodP.getText().toUpperCase()+"','"+tnomP.getText().toUpperCase()+
                "','"+tdescriP.getText()+"')";
        c.insert();
    }

    //formulario de los productos
    public void Proceso(){
        Color col = Color.WHITE;
        
        Ingre.addActionListener(this);
        Salida.addActionListener(this);
        Saldo.addActionListener(this);
        kardex.addActionListener(this);

        //Para utilizacion del mismo frame oculta funciones anteriores
        //y muestra las que habian sido ocultas en otra funcion
        agreProduc.setVisible(false);
        genKardex.setVisible(false);
        salir.setVisible(false);
        tcantP.setVisible(false);
        tprecio.setVisible(false);
        tmonto.setVisible(false);
        tcodP.setVisible(false);
        lcant_saldo.setVisible(false);
        lprecio_saldo.setVisible(false);
        lmonto_saldo.setVisible(false);
        atras.setVisible(false);
        combpro.setVisible(true);
        lfecha.setVisible(true);
        tfecha1.setVisible(true);
        lcant.setVisible(true);
        lPrecio.setVisible(true);
        Ingre.setVisible(true);
        Salida.setVisible(true);
        Saldo.setVisible(true);
        kardex.setVisible(true);
        tcantP1.setVisible(true);
        tprecio1.setVisible(true);                        
        
        //da color a las labels
        codPro.setForeground(col);
        lfecha.setForeground(col);
        lcant.setForeground(col);
        lPrecio.setForeground(col);

        //componente de los combo        
        AutoCompleteDecorator.decorate(combpro);
        combpro.setEditable(true);
        combpro.addItemListener(this);
        combpro.setBounds(250, 30, 100, 20);

        //Tamanos de los componentes
        codPro.setBounds(30, 30, 200, 20);
        lfecha.setBounds(30, 60, 200, 20);
        lcant.setBounds(30, 90, 200, 20);
        lPrecio.setBounds(30, 120, 200, 20);
        Ingre.setBounds(30, 160, 100, 20);
        Salida.setBounds(150, 160, 100, 20);
        Saldo.setBounds(270, 160, 100, 20);
        kardex.setBounds(30, 200, 100, 20);
        atra.setBounds(270, 200, 100, 20);
        atra.addActionListener(this);
        tfecha1.setBounds(250, 60, 100, 20);
        tcantP1.setBounds(250, 90, 100, 20);
        tprecio1.setBounds(250, 120, 100, 20);

        //introduccion de datos al frame
        Producto.setSize(400,270);
        Producto.setLocation(50,50);
        Producto.setLayout(null);
     //   imagen.setIcon(im);
        imagen.setBounds(0, 0, 400, 270);
        Producto.add(codPro);
        Producto.add(lfecha);
        Producto.add(lcant);
        Producto.add(lPrecio);
        Producto.add(combpro);
        Producto.add(tfecha1);
        Producto.add(tcantP1);
        Producto.add(tprecio1);
        Producto.add(lPrecio);
        Producto.add(Ingre);
        Producto.add(Salida);
        Producto.add(Saldo);
        Producto.add(kardex);
        Producto.add(atra);
        Producto.add(imagen);
        contenedorProd.add(Producto);
        Producto.setVisible(true);
        Producto.setVisible(true);
    }
           
    //inserta los datos al kardex
    public void Insert_kardex(){        
        //determina el numero de la transaccion y lo entrega a una variable para poderlo incrementar
        c.sel = "select * from transacciones";
        c.select_da();
        try {
            if (c.Resultado.last()) {
                n = Integer.parseInt(c.Resultado.getString("ntrans"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        } 
        tra = n + val;
        c.add = "Insert into transacciones values('"+String.valueOf(tra)+"')";
        c.insert();

        //Buscar el saldo y cantidad anterior para hacer los calculos pero
        //antes verifica si hay cantidades previas
        c.sel = "select count(codproducto) from kardex where codproducto = '"+tcodP.getText().toUpperCase()+"'";
        c.select_da();
        try {
            if (c.Resultado.next()) {
                num = Integer.parseInt(c.Resultado.getString("count(codproducto)"));                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //si hay cantidades previas les da un valor a las variables sino toman el valor de cero
        //por lo tanto sera como el inventario inicial
        if(num>0){
            c.sel = "Select * from kardex where codproducto = '"+tcodP.getText().toUpperCase()+"'";
            c.select_da();
            try {
                if (c.Resultado.last()) {
                    cant_anterior = Double.parseDouble(c.Resultado.getString("cant_exit"));
                    sal_anterior = Double.parseDouble(c.Resultado.getString("monto_exit"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
            }
       }

        //Determina los valores a entrar
        cant_nuevo = Double.parseDouble(tcantP1.getText());
        precio_nuevo = Double.parseDouble(tprecio1.getText());
        sal_nuevo = cant_nuevo * precio_nuevo;
        saldo_Total = sal_nuevo + sal_anterior;
        cant_Total = cant_nuevo + cant_anterior;
        Precio_total = saldo_Total/cant_Total;
        
        //le da formato a los valores que se encontraran en el kardex
        DecimalFormat d = new DecimalFormat(".00");
        
        c.add = "Insert into kardex(ntrans,codproducto,fecha,cantid_entran,cu_entran,monto_entran,cant_exit,"
                + "costo_exit,monto_exit) values('"+String.valueOf(tra)+"','"+tcodP.getText().toUpperCase()+"','"+
                tfecha1.getText()+"','"+String.valueOf(cant_nuevo)+"','"+String.valueOf(d.format(precio_nuevo))+
                "','"+String.valueOf(d.format(sal_nuevo))+"','"+String.valueOf(cant_Total)+"','"+String.valueOf(d.format(Precio_total))+
                "','"+String.valueOf(d.format(saldo_Total))+"')";
        c.insert();
    }

    //Salida los datos del kardex
    public void Salida_kardex(){
        double existencia = 0.00;
        double solicitan = 0.00;
        //determina el numero de la transaccion y lo entrega a una variable para poderlo incrementar
        c.sel = "select * from transacciones";
        c.select_da();
        try {
            if (c.Resultado.last()) {
                n = Integer.parseInt(c.Resultado.getString("ntrans"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        } 
        tra = n + val;
        c.add = "Insert into transacciones values('"+String.valueOf(tra)+"')";
        c.insert();

        //Buscar el saldo y cantidad anterior para realizar la resta
        //antes verifica transacciones previas para poder realizar la descarga
        c.sel = "select count(codproducto) from kardex where codproducto = '"+tcodP.getText().toUpperCase()+"'";
        c.select_da();
        try {
            if (c.Resultado.next()) {
                num = Integer.parseInt(c.Resultado.getString("count(codproducto)"));                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        //verifica si hay existencia del producto y si es menor que lo que solicitan
        c.sel = "Select * from kardex where codproducto = '"+tcodP.getText().toUpperCase()+"'";
        c.select_da();
        try {
            if (c.Resultado.last()) {
               //busca la existencia en saldos
               existencia = Double.parseDouble(c.Resultado.getString("cant_exit"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
            }
        solicitan = Double.parseDouble(tcantP1.getText());
        //si hay cantidades previas les da un valor a las variables sino manda mensaje de error
        if((num > 0)&&(existencia > solicitan)){
            c.sel = "Select * from kardex where codproducto = '"+tcodP.getText().toUpperCase()+"'";
            c.select_da();
            try {
                if (c.Resultado.last()) {
                    //asigna costos y precios del saldo anterior
                    cant_anterior = Double.parseDouble(c.Resultado.getString("cant_exit"));
                    precio_anterior = Double.parseDouble(c.Resultado.getString("costo_exit"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Determina los valores restar y no toma en cuenta el valor del precio
            //como es determinado pero desplega el monto total
            cant_nuevo = solicitan;
            sal_nuevo = cant_nuevo * precio_anterior;
            cant_Total = cant_anterior - cant_nuevo;
            saldo_Total = cant_Total*precio_anterior;
            Precio_total = precio_anterior;

            //le da formato a los valores que se encontraran en el kardex
            DecimalFormat d = new DecimalFormat(".00");

            JOptionPane.showMessageDialog(null, "El monto es: "+sal_nuevo);
        
            c.add = "Insert into kardex(ntrans,codproducto,fecha,cant_sal,cu_sal,monto_sal,cant_exit,"
                    + "costo_exit,monto_exit) values('"+String.valueOf(tra)+"','"+tcodP.getText().toUpperCase()+"','"+
                    tfecha1.getText()+"','"+String.valueOf(cant_nuevo)+"','"+String.valueOf(d.format(precio_anterior))+
                    "','"+String.valueOf(d.format(sal_nuevo))+"','"+String.valueOf(cant_Total)+"','"+
                    String.valueOf(d.format(Precio_total))+"','"+String.valueOf(d.format(saldo_Total))+"')";
            c.insert();
       }
        else{
            //elimina la transaccion antes ingresada por que no se realizo
            c.del = "Delete from transacciones where ntrans = '"+String.valueOf(tra)+"'";
            c.borrar();
            JOptionPane.showMessageDialog(null, "No existen productos previos o \nno hay suficientes en existencia");
         }
    }

    //Verifica los saldos del producto
    public void Saldo(){
        String aux = null;
        c.sel = "select count(codproducto) from kardex where codproducto = '"+tcodP.getText().toUpperCase()+"'";
        c.select_da();
        try {
            if (c.Resultado.next()) {
                num = Integer.parseInt(c.Resultado.getString("count(codproducto)"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        //si hay cantidades previas les da un valor a las variables sino manda mensaje de error
        if(num > 0){
            c.sel = "Select * from kardex where codproducto = '"+tcodP.getText().toUpperCase()+"'";
            c.select_da();
            try {
                if (c.Resultado.last()) {
                    //asigna datos saldo anterior
                    aux = c.Resultado.getString("cant_exit");
                    tcantP.setText(aux);
                    aux = c.Resultado.getString("costo_exit");
                    tprecio.setText(aux);
                    aux = c.Resultado.getString("monto_exit");
                    tmonto.setText(aux);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
            }
            tcodP.setEditable(false);
            tcantP.setEditable(false);
            tprecio.setEditable(false);
            tmonto.setEditable(false);
            impresion();
       }
        else{
            JOptionPane.showMessageDialog(null, "No se encuentra en existencia");
         }
    }

    public void impresion(){        
        Color col = Color.WHITE;
        //oculta los valores utilizados y muestra los que se utilizaran
        lfecha.setVisible(false);
        lcant.setVisible(false);
        lPrecio.setVisible(false);
        Ingre.setVisible(false);
        tfecha1.setVisible(false);
        combpro.setVisible(false);
        Salida.setVisible(false);
        Saldo.setVisible(false);
        tcantP1.setVisible(false);
        tprecio1.setVisible(false);
        tcantP.setVisible(true);
        tprecio.setVisible(true);
        tcodP.setVisible(true);
        tmonto.setVisible(true);
        lcant_saldo.setVisible(true);
        lprecio_saldo.setVisible(true);
        lmonto_saldo.setVisible(true);
        atras.setVisible(true);        

        //da color a las labels
        lcant_saldo.setForeground(col);
        lprecio_saldo.setForeground(col);
        lmonto_saldo.setForeground(col);
        
        //activa el button atras
        atras.addActionListener(this);

        //Tamanos de los componentes
        lcant_saldo.setBounds(30, 60, 200, 20);
        lprecio_saldo.setBounds(30, 90, 200, 20);
        lmonto_saldo.setBounds(30, 120, 200, 20);
        atras.setBounds(30, 160, 100, 20);
        tcodP.setBounds(250, 30, 100, 20);
        tcantP.setBounds(250, 60, 100, 20);
        tprecio.setBounds(250, 90, 100, 20);
        tmonto.setBounds(250, 120, 100, 20);

        //introduccion de datos al frame
        Producto.setSize(400,260);
        Producto.setLocation(50,50);
        Producto.setLayout(null);
      //  imagen.setIcon(im);
        imagen.setBounds(0, 0, 400, 260);
        Producto.add(codPro);
        Producto.add(lcant_saldo);
        Producto.add(lprecio_saldo);
        Producto.add(lmonto_saldo);
        Producto.add(tcodP);
        Producto.add(tcantP);
        Producto.add(tprecio);
        Producto.add(tmonto);
        Producto.add(atras);
        Producto.add(imagen);
        contenedorProd.add(Producto);
        Producto.setVisible(true);
    }
    //vista de la planilla
    public void kardex(){
        String Nombre = "Kardex del producto "+tcodP.getText().toUpperCase();
        String CONSULTA = "Select ntrans 'No.Transc.', fecha 'fecha', cantid_entran 'Cant.Entradas', "
            + "cu_entran 'Precio',monto_entran 'Total',cant_sal 'Cant.Salidas',cu_sal 'Precio',monto_sal 'Total',"
            + "cant_exit 'Cant.Total',costo_exit 'Precio',monto_exit 'Total' from kardex where codproducto = '"+tcodP.getText().toUpperCase()+"'";
        //llama al metodo e imprime
        Mostrar_planilla a = new Mostrar_planilla(CONSULTA,Nombre);
        contenedorProd = a.getContentPane();
        a.setVisible(true);
    }

    //hace la busqueda
    public void buscar_Product(){
        c.sel = "select * from productos where codproducto = '"+tcodP.getText().toUpperCase()+"' ";
        c.select_da();
        busqueda();
    }
   
    //hace la busqueda del producto
    public void busqueda(){
        try {
            if(c.Resultado.next()){
                //variables de apoyo
                String apoyo = null;

                //Para este solo se tomara en cuenta el codigo del empleado
                apoyo = c.Resultado.getString("codproducto");
                tcodP.setText(apoyo);
                apoyo = c.Resultado.getString("nomproducto");
                tnomP.setText(apoyo);
                apoyo = c.Resultado.getString("descripcion");
                tdescriP.setText(apoyo);
                tcodP.setEditable(true);
                tnomP.setEditable(false);
                tdescriP.setEditable(false);
                agreProduc();
            }
        }
       catch (SQLException e) {
                System.out.println("no sale");
        }
    }

    public void borrar(){
        //borra primero los datos del kardex para no tener problemas
        //con la eliminacion de datos del producto
        c.del = "Delete from kardex where codproducto = '"+tcodP.getText().toUpperCase()+"'";
        c.borrar();
        c.del = "Delete from productos where codproducto = '"+tcodP.getText().toUpperCase()+"'";
        c.borrar();
        JOptionPane.showMessageDialog(null, "Los datos del producto "+tcodP.getText().toUpperCase()+" han sido borrados en kardex y base de datos");
    }
}