/*
 * Clase que se encarga de almacenar los registros o transacciones diarias
 * de la empresa, en forma de una gran cuenta T imaginaria, ingresando cuentas
 * cargadas y cuentas abonadas
 */

package sistemacontable;

import java.awt.*;
import java.util.Date;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class LibroDiario implements ActionListener, ItemListener{

    //Acceso a DB
    Conexion c = new Conexion();

    //Creacion de componentes para el formulario
    JFrame lb = new JFrame();
    JFrame lbn = new JFrame();
    Container contenedorLD;
    JButton newdeb = new JButton("Cargar otra Cuenta");
    JButton newhab = new JButton("Abonar otra Cuenta");
    JButton atras = new JButton("Terminar");
    JButton insertar = new JButton("Insertar");
    JButton nuevo = new JButton("Nuevo");
    JButton agregarsubd = new JButton("Agregar Subcuenta al Debe");
    JButton agregarsubh = new JButton("Agregar Subcuenta al Haber");
    JLabel debe = new JLabel("DEBE");
    JLabel haber = new JLabel("HABER");
    JComboBox combdeb = new JComboBox();
    JComboBox combhab = new JComboBox();
    JTextField ndebf = new JTextField("");
    JTextField nmontdebf = new JTextField("0");
    JTextField nhabf = new JTextField("");
    JTextField nmonthabf = new JTextField("0");
    JToolBar bb = new JToolBar();
    JToolBar bbd = new JToolBar();
    JToolBar bbh = new JToolBar();
    Font fuente = new Font("Monospaced", Font.BOLD, 20);
    Font fuente2 = new Font("Monospaced",Font.BOLD, 15);
    String [][] aaddb = new String[4][20];
    String [][] aadhb = new String[4][20];
    int d=0, h=0, n;
    Date fe= new Date();
    JLabel fech = new JLabel("Mes: "+fe.toString().substring(4, 7)
            +" "+fe.toString().substring(24,28));
    JLabel ntrans = new JLabel("");
    
    //Creacion de objetos y variables para agregar subcuentas
    JInternalFrame ventanasubcuentas = new JInternalFrame("Sub cuentas");
    JButton iinsertar = new JButton("Agregar al registro");
    JButton iagsubcuenta = new JButton("Agregar otra sub cuenta");
    JButton icancel = new JButton("Cancelar");
    JTextField isubcuenta = new JTextField("");
    JLabel n$i = new JLabel("$");
    JTextField isaldo = new JTextField("0");
    JComboBox subcuentas = new JComboBox();
    JLabel ititulo = new JLabel("Escoja subcuenta a agregar: ");
    ImageIcon im = new ImageIcon(getClass().getResource("/imagenes/contabilidad.jpg"));
    JLabel aux;
    JLabel lbl = new JLabel();
    int did=0, dih=0;

    //Para insercion de nuevos cargos y nuevos abonos
    JTextField nhab;
    JTextField nmonthab;
    JLabel n$b;
    JTextField ndeb;
    JTextField nmontdeb;
    JLabel n$a;

    //Creacion de variables para la posicion de algunos componentes
    //y para el acceso a algunas instrucciones
    int posY1d=150, posY2d=120;
    int posY1h=150, posY2h=120;
    int posYlb=600;
    int id=0, ih=0;


    //Trae como parametro el JFrame y el objeto de librodiario de la ventana principal y crea nuevos
    //componentes para simular el libro diario
    public void LibroDiari(JFrame p){
        //Inicializa objetos para el primer registro
        nhab = new JTextField();
        nmonthab = new JTextField("0");
        n$b = new JLabel("$");
        ndeb = new JTextField();
        nmontdeb = new JTextField("0");
        n$a = new JLabel("$");
        lb=p;   //asigna al JFrame lb las propiedades del JFrame p que viene de la ventana principal
        lbn=p;
        lb.setSize(1005,posYlb);//1005,posYlb
        lb.setLocationRelativeTo(null);
        contenedorLD = lb.getContentPane(); //Obtiene contenedor de lb para almacenar objetos
        //Asigna tamaño al Frame, establece el tipo de contenedor
        contenedorLD.setLayout(null);
        //Inicializa matriz que sevira para almacenar momentaneamente los valores de una transaccion
        for(int i=0;i<20;i++){
            aaddb[0][i]="";
            aaddb[1][i]="0";
            aaddb[2][i]="";
            aaddb[3][i]="nosub";
            aadhb[0][i]="";
            aadhb[1][i]="0";
            aadhb[2][i]="";
            aadhb[3][i]="nosub";
        }
        ventinterna();  //Carga ventanainterna para la insercion de subcuentas
        barratareas();  //Crea la barra de tareas de la ventana actual
        metdebe();  //Metodo que inicializa objetos que serviran para la columna del debe
        methaber(); //Inicializa objetos para la columna del haber
        llenar();   //Llena los JComboBox con cada una de las cuentas de la base de datos
        obtenerultimatransac(); //Obtiene el numero de la ultima transaccion almacenada
        //Define el numero de transaccion actual y la fecha del dia
        ntrans.setBounds(270, 55, 400, 20);
        ntrans.setFont(fuente2);
        fech.setBounds(440, 70, 400, 20);
        fech.setFont(fuente2);
        //inserta objetos al contenedor para que sean visualizados
        contenedorLD.add(bb);
        contenedorLD.add(bbd);
        contenedorLD.add(bbh);
        contenedorLD.add(ventanasubcuentas);
        contenedorLD.add(debe);
        contenedorLD.add(ndeb);
        contenedorLD.add(n$a);
        contenedorLD.add(nmontdeb);
        contenedorLD.add(haber);
        contenedorLD.add(nhab);
        contenedorLD.add(n$b);
        contenedorLD.add(nmonthab);
        contenedorLD.add(ntrans);
        contenedorLD.add(fech);
        lb.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    //inicializa ventana sub cuenta
    public void ventinterna(){
       lbl.setIcon(im);
       lbl.setBounds(0, 0, 700, 200);
       ventanasubcuentas.setSize(700,200);
       ventanasubcuentas.setLocation(300, 353);
       ventanasubcuentas.setLayout(null);
       ititulo.setBounds(10, 10, 400, 20);
       ititulo.setFont(fuente);
       subcuentas.setBounds(10,30,400,20);
       subcuentas.addItemListener(this);
       subcuentas.addItem("");
       AutoCompleteDecorator.decorate(subcuentas);
       subcuentas.setEditable(true);
       iagsubcuenta.setBounds(450, 30, 200, 20);
       iagsubcuenta.addActionListener(this);
       iinsertar.setBounds(450,60,200,20);
       iinsertar.addActionListener(this);
       icancel.setBounds(450, 90, 200, 20);
       icancel.addActionListener(this);
       isubcuenta.setBounds(10, 80, 300, 20);
       isubcuenta.setEditable(false);
       n$i.setBounds(312, 80, 10, 20);
       isaldo.setBounds(323, 80, 100, 20);
       ventanasubcuentas.add(ititulo);
       ventanasubcuentas.add(subcuentas);
       ventanasubcuentas.add(isubcuenta);
       ventanasubcuentas.add(n$i);
       ventanasubcuentas.add(isaldo);
       ventanasubcuentas.add(iinsertar);
       ventanasubcuentas.add(iagsubcuenta);
       ventanasubcuentas.add(icancel);
       ventanasubcuentas.add(lbl);
    }

    //agrega acciones a los botones de la barra de tareas y tambien
    //agrega elementos a la barra
    public void barratareas(){
        nuevo.addActionListener(this);
        nuevo.setToolTipText("Nuevo Registro");
        insertar.addActionListener(this);
        insertar.setToolTipText("Inserta al libro diario");
        atras.addActionListener(this);
        atras.setToolTipText("Regresar a la Ventana principal");
        agregarsubd.addActionListener(this);
        agregarsubd.setToolTipText("Subcuenta a la cuenta actual del debe");
        agregarsubh.addActionListener(this);
        agregarsubh.setToolTipText("Subcuenta a la cuenta actual del haber");
        bb.add(insertar);
        bb.addSeparator();
        bb.add(nuevo);
        bb.addSeparator();
        bb.add(newdeb);
        bb.addSeparator();
        bb.add(agregarsubd);
        bb.addSeparator();
        bb.add(newhab);
        bb.addSeparator();
        bb.add(agregarsubh);
        bb.addSeparator();
        bb.add(atras);
        bbd.add(combdeb);
        bbh.add(combhab);
        bb.setBounds(0, 1, 1130, 25);
        bb.setFloatable(false);
        bbd.setBounds(0, 27, 500, 25);
        bbd.setFloatable(false);
        bbh.setBounds(500, 27, 500, 25);
        bbh.setFloatable(false);
    }

    //Componentes y funcionalidades para la columna del debe
    public void metdebe(){
        newdeb.addActionListener(this);
        debe.setBounds(240, 80, 100, 20);
        ndeb.setBounds(40, 120, 300, 20);
        n$a.setBounds(342, 120, 8, 20);
        nmontdeb.setBounds(352, 120, 100, 20);
        newdeb.setBounds(40,posY1d,200,20);
        combdeb.addItem("");
        AutoCompleteDecorator.decorate(combdeb);
        combdeb.setEditable(true);
        ndeb.setEditable(false);
        combdeb.addItemListener(this);
        debe.setFont(fuente);
    }

    //Componentes y funcionalidades para la columna del haber
    public void methaber(){
        newhab.addActionListener(this);
        haber.setBounds(715, 80, 100,20);
        nhab.setBounds(550, 120, 300, 20);
        n$b.setBounds(852, 120, 8, 20);
        nmonthab.setBounds(862, 120, 100, 20);
        newhab.setBounds(600, posY1h, 200, 20);
        combhab.addItem("");
        AutoCompleteDecorator.decorate(combhab);
        combhab.setEditable(true);
        combhab.addItemListener(this);
        nhab.setEditable(false);
        haber.setFont(fuente);
    }

    //Llena el combobox de la subventana con las subcuentas correspondientes a la cuenta mayor
    //seleccionada para el lado del debe o del haber
    public void mostrarinterna(JTextField jc, JLabel au){
       if(!jc.getText().equals("")){
        aux=au;
        c.lle="select codigocuenta, nombrecuenta from cuentas where cuentamayor='"+jc.getText().substring(0, 3)+"'";
        c.llenar();
        try{
        boolean Res = c.Resultado.next();
        if(Res){
            while(Res){
              subcuentas.addItem(c.Resultado.getString("codigocuenta")+", "+c.Resultado.getString("nombrecuenta"));
              Res = c.Resultado.next();
              ventanasubcuentas.setVisible(true);
            }
            c.Resultado.refreshRow();}
        else{JOptionPane.showMessageDialog(null, "Esta Cuenta no posee Subcuentas");}
        }catch(SQLException e) {}
       }else {JOptionPane.showMessageDialog(null, "Debe elegir una cuenta");}
    }

    //Llena los combobox con las cuentas de mayor del catalogo
    public void llenar(){
        c.lle="select codigocuenta, nombrecuenta from cuentasmayor where codigocuenta!='___'";
        c.llenar();
        try{
        boolean Res = c.Resultado.next();
        while(Res){
              combdeb.addItem(c.Resultado.getString("codigocuenta")+", "+c.Resultado.getString("nombrecuenta"));
              combhab.addItem(c.Resultado.getString("codigocuenta")+", "+c.Resultado.getString("nombrecuenta"));
              Res = c.Resultado.next();
            }
            c.Resultado.refreshRow();
        }catch(SQLException e) {}
    }

    //Obtiene el correlativo de la ultima transaccion realizada
    public void obtenerultimatransac(){
        c.lle="select max(ntrans) from transacciones";
        c.llenar();
        try{
            if(c.Resultado.next()){
            n=c.Resultado.getInt("max(ntrans)");
            ntrans.setText("                  Transaccion # "+n);
            }c.Resultado.refreshRow();
        }catch(SQLException e) {}
    }

    //Asigna a los JComboBox funcionalidades
    public void itemStateChanged(ItemEvent e){
        boolean isSelected;
        isSelected=(e.getStateChange()==ItemEvent.SELECTED);
        //Parte del debe
        if(e.getItemSelectable()==combdeb){
           if(isSelected){
               if(id==0){ ndeb.setText(combdeb.getSelectedItem().toString());
                }else{ ndebf.setText(combdeb.getSelectedItem().toString()); }
                subcuentas.removeAllItems();
                subcuentas.addItem("");
           }
        }
        //Parte del haber
       if(e.getItemSelectable()==combhab){
           if(isSelected){
            if(ih==0){ nhab.setText(combhab.getSelectedItem().toString());
                }else{ nhabf.setText(combhab.getSelectedItem().toString()); }
            subcuentas.removeAllItems();
            subcuentas.addItem("");
           }
       }
        //Parte para las subcuentas
       if(e.getItemSelectable()==subcuentas){
            if(isSelected){
                if(ih==0){ isubcuenta.setText(subcuentas.getSelectedItem().toString()); }
                else{ isubcuenta.setText(subcuentas.getSelectedItem().toString()); }
            }
        }
    }

    //Permite a los botones ejecutar instrucciones
    public void actionPerformed(ActionEvent e){
       String com=e.getActionCommand();
       if("Terminar".equals(com)){
           Inicio i = new Inicio();
           lb.dispose();
       }else if("Cargar otra Cuenta".equals(com)){
           cargarcuenta();
           combdeb.setSelectedItem("");
       }else if("Abonar otra Cuenta".equals(com)){
           abonarcuenta();
           combhab.setSelectedItem("");
       }else if("Nuevo".equals(com)){
           contenedorLD.setVisible(false);
           contenedorLD.removeAll();
           contenedorLD.setVisible(true);
           recargar();
           LibroDiari(lbn);
       }else if("Insertar".equals(com)){
           if(!ndeb.getText().equals("")&&!nmontdeb.getText().equals("0")&&
                   !nhab.getText().equals("")&&!nmonthab.getText().equals("0")){
           temporalhdb(nhab,nmonthab);
           temporalddb(ndeb,nmontdeb);
           insertar();
           contenedorLD.setVisible(false);
           contenedorLD.removeAll();
           contenedorLD.setVisible(true);
           recargar();
           LibroDiari(lbn);
           }else
               JOptionPane.showMessageDialog(null, "Algun campo se encuentra vacio");
       }else if("Agregar Subcuenta al Debe".equals(com)){
           mostrarinterna(ndeb,debe);
       }else if("Agregar Subcuenta al Haber".equals(com)){
           mostrarinterna(nhab,haber);
       }else if("Agregar otra sub cuenta".equals(com)){
           if(!isubcuenta.getText().equals("")&&!isaldo.getText().equals("0"))
                temporalddbi(isubcuenta,isaldo);
           else
               JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
       }else if("Agregar al registro".equals(com)){
           if(!isubcuenta.getText().equals("")&&!isaldo.getText().equals("0"))
                temporalddbi(isubcuenta,isaldo);
           else
               JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
           ventanasubcuentas.dispose();
       }else if("Cancelar".equals(com)){
           for(int i=0;i<20;i++){
            aaddb[0][i]="";
            aaddb[1][i]="0";
            aaddb[2][i]="";
            aaddb[3][i]="nosub";
            aadhb[0][i]="";
            aadhb[1][i]="0";
            aadhb[2][i]="";
            aadhb[3][i]="nosub";
           }
           subcuentas.removeAllItems();
           subcuentas.addItem("");
           subcuentas.setSelectedItem("");
           isubcuenta.setText("");
           isaldo.setText("0");
           ventanasubcuentas.dispose();
       }
    }

    //Se encarga de agregar nuevos componentes para un nuevo registro en el debe
    public void cargarcuenta(){
        if(combdeb.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "Error, debe seleccionar una cuenta");
        }else{
            temporalddb(ndeb,nmontdeb);
            ndeb = new JTextField("");
            nmontdeb = new JTextField("0");
            n$a = new JLabel("$");
            posY1d=posY1d+30;
            posY2d=posY2d+30;
            ndeb.setBounds(40, posY2d, 300, 20);
            n$a.setBounds(342, posY2d, 8, 20);
            nmontdeb.setBounds(352, posY2d, 100, 20);
            ndeb.setEditable(false);
            //Controla la variacion de tamaño del Frame cuando se agregan nuevos campos al debe
            if((posYlb-posY2d)<80){ posYlb=posYlb+40; }
            lb.setSize(1005, posYlb);
            id=id+1;
            contenedorLD.add(ndeb);
            contenedorLD.add(n$a);
            contenedorLD.add(nmontdeb);
            ndebf=ndeb;
            nmontdebf=nmontdeb;
        }
    }

    //Se encarga de agregar nuevos componentes para un nuevo registro en el haber
    public void abonarcuenta(){
        if(combhab.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "Error, debe seleccionar una cuenta");
        }else{
            temporalhdb(nhab,nmonthab);
            nhab = new JTextField("");
            nmonthab = new JTextField("0");
            n$b = new JLabel("$");
            posY2h=posY2h+30;
            nhab.setBounds(550, posY2h, 300, 20);
            n$b.setBounds(852, posY2h, 8, 20);
            nmonthab.setBounds(862, posY2h, 100, 20);
            nhab.setEditable(false);
            //Controla la variacion de tamaño del Frame cuando se agregan nuevos campos al haber
            if((posYlb-posY2h)<80){ posYlb=posYlb+40; }
            lb.setSize(1005, posYlb);
            ih=ih+1;
            contenedorLD.add(nhab);
            contenedorLD.add(n$b);
            contenedorLD.add(nmonthab);
            nhabf=nhab;
            nmonthabf=nmonthab;
        }
    }

    //temporalddb y temporalhdb asignan a una matriz todos los valores
    //que se van insertando en los textfield
    //del debe y del haber, tanto cuentas, subcuentas como montos
    //para luego utilizarlas en la insercion en la base de datos
    public void temporalddb(JTextField a, JTextField b){
        int ind=0;
           ind=a.getText().indexOf(",");
           if(aaddb[3][d].equals("sub")){
           for(int i=0;i<did;i++){
             if(aaddb[0][i].equals("")){
               aaddb[0][i]=a.getText().substring(0, ind);
               d=d+1;}
           }
           }else{
               aaddb[0][d]=a.getText().substring(0, ind);
               aaddb[1][d]=b.getText().toString();
               d=d+1;
               did=did+1;}
    }
    public void temporalhdb(JTextField a, JTextField b){
        int ind=0;
           ind=a.getText().indexOf(",");
           if(aadhb[3][h].equals("sub")){
           for(int i=0;i<dih;i++){
              if(aadhb[0][i].equals("")){
                aadhb[0][i]=a.getText().substring(0,ind);
                h=h+1;}
           }
           }else{
               aadhb[0][h]=a.getText().substring(0,ind);
               aadhb[1][h]=b.getText().toString();
               h=h+1;
               dih=dih+1;}
    }

    //Almacena momentaneamente valores de subcuentas mientras estos son llevados
    //a las funciones temporalhdb y temporalddb
    public void temporalddbi(JTextField a, JTextField b){
        int ind=0;
        ind=a.getText().indexOf(",");
        if(aux.getText().equals("DEBE")){
           aaddb[2][did]=a.getText().substring(0, ind);
           aaddb[1][did]=b.getText().toString();
           aaddb[3][did]="sub";
           did=did+1;
        }
        else if(aux.getText().equals("HABER")){
           aadhb[2][dih]=a.getText().substring(0, ind);
           aadhb[1][dih]=b.getText().toString();
           aadhb[3][dih]="sub";
           dih=dih+1;
        }
        subcuentas.setSelectedItem("");
        a.setText("");
        b.setText("0");
    }
    
    //Por medio del boton insertar, ingresa a la base de datos los valores correspondientes
    //a una transaccion tanto para el debe como para el haber
    private void insertar(){
        double montod, montoh, sumad=0, sumah=0;
        String codigocuentad, codigocuentah;
        String subcuentad, subcuentah;
        String ex1="", ex2="";
        String descrip;

        for(int j=0;j<20;j++){
            if(!aaddb[0][j].equals("")&&!aaddb[1][j].equals("0")){
                if(aaddb[3][j].equals("sub")){
                    montod=Double.parseDouble(aaddb[1][j]);
                    sumad=sumad+montod;
                }else{
                    montod=Double.parseDouble(aaddb[1][j]);
                    sumad=sumad+montod;
                }
            }
            if(!aadhb[0][j].equals("")&&!aadhb[1][j].equals("0")){
                if(aadhb[3][j].equals("sub")){
                    montoh=Double.parseDouble(aadhb[1][j]);
                    sumah=sumah+montoh;
                }else{
                    montoh=Double.parseDouble(aadhb[1][j]);
                    sumah=sumah+montoh;
                }
            }
        }

        String natural="";
        if(sumad==sumah){
        for(int j=0;j<20;j++){  //Inserta en el lado del debe
            if(!aaddb[0][j].equals("")&&!aaddb[1][j].equals("0")){
                if(aaddb[3][j].equals("sub")){
                    codigocuentad=aaddb[0][j];
                    subcuentad=aaddb[2][j];
                    montod=Double.parseDouble(aaddb[1][j]);
                    c.add="insert into lddebe values ("+n+",'"+codigocuentad+"', '"
                            +subcuentad+"', "+montod+")";
                    c.insert();
                ex1="Exito";
                }else{
                    codigocuentad=aaddb[0][j];
                    montod=Double.parseDouble(aaddb[1][j]);
                    c.add="insert into lddebe values ("+n+",'"+codigocuentad+
                            "','____',"+montod+")";
                    c.insert();
                    ex1="Exito";
                }
                    c.lle="select naturaleza from cuentasmayor where codigocuenta='"+codigocuentad+"'";
                    c.llenar();
                    try{
                    if(c.Resultado.next())
                        natural=c.Resultado.getString("naturaleza");
                    c.Resultado.refreshRow();
                    }catch(Exception e){}
                    if(natural.equals("Deudor")){
                        c.upd="update cuentasmayor set saldodeudor = saldodeudor + "+montod+
                            " where codigocuenta='"+codigocuentad+"'";
                        c.actuali();
                    }else{
                        c.upd="update cuentasmayor set saldoacreedor = saldoacreedor - "+montod+
                            " where codigocuenta='"+codigocuentad+"'";
                        c.actuali();
                    }
                    ex1="Exito";
                    montod=montod*(-1);
                    obtenerresul(codigocuentad,montod);
            }
            if(!aadhb[0][j].equals("")&&!aadhb[1][j].equals("0")){
                if(aadhb[3][j].equals("sub")){
                    codigocuentah=aadhb[0][j];
                    subcuentah=aadhb[2][j];
                    montoh=Double.parseDouble(aadhb[1][j]);
                    c.add="insert into ldhaber values ("+n+",'"+codigocuentah
                            +"', '"+subcuentah+"', "+montoh+")";
                    c.insert();
                    ex2="Exito";
                }else{
                    codigocuentah=aadhb[0][j];
                    montoh=Double.parseDouble(aadhb[1][j]);
                    c.add="insert into ldhaber values ("+n+",'"+codigocuentah
                            +"','____',"+montoh+")";
                    c.insert();
                    ex2="Exito";
                }
                    c.lle="select naturaleza from cuentasmayor where codigocuenta='"+codigocuentah+"'";
                    c.llenar();
                    try{
                    if(c.Resultado.next())
                        natural=c.Resultado.getString("naturaleza");
                    c.Resultado.refreshRow();
                    }catch(Exception e){}
                    if(natural.equals("Acreedor")){
                        c.upd="update cuentasmayor set saldoacreedor = saldoacreedor + "+montoh+
                            " where codigocuenta='"+codigocuentah+"'";
                        c.actuali();
                    }else{
                        c.upd="update cuentasmayor set saldodeudor = saldodeudor - "+montoh+
                            " where codigocuenta='"+codigocuentah+"'";
                        c.actuali();
                    }
                    ex2="Exito";
                    obtenerresul(codigocuentah,montoh);
            }
        }
        descrip=JOptionPane.showInputDialog("Agregar comentario sobre la transaccion: ", "Sin comentarios");
        c.upd="update transacciones set descripcion='"+descrip+"', fecha='"
                +fech.getText().substring(5, 13)+"' where ntrans="+n;
        c.actuali();
        n=n+1;
        c.add="insert into transacciones values("+n+", ' ', ' ')";
        c.insert();
        if(ex1.equals("Exito")&&ex2.equals("Exito"))
            JOptionPane.showMessageDialog(null, "Registro Realizado Satisfactoriamente","Mensaje",JOptionPane.INFORMATION_MESSAGE);
        }else{ JOptionPane.showMessageDialog(null, "Sumas del debe y haber diferentes, debe reinsertar el registro"); }
   }

    //Se encarga de reinicializar todos los componentes para crear un nuevo registro
    public void recargar(){
    c = new Conexion();
    lb = new JFrame();
    newdeb = new JButton("Cargar otra Cuenta");
    newhab = new JButton("Abonar otra Cuenta");
    atras = new JButton("Terminar");
    insertar = new JButton("Insertar");
    nuevo = new JButton("Nuevo");
    agregarsubd = new JButton("Agregar Subcuenta al Debe");
    agregarsubh = new JButton("Agregar Subcuenta al Haber");
    debe = new JLabel("DEBE");
    haber = new JLabel("HABER");
    combdeb = new JComboBox();
    combhab = new JComboBox();
    ndebf = new JTextField("");
    nmontdebf = new JTextField("0");
    nhabf = new JTextField("");
    nmonthabf = new JTextField("0");
    bb = new JToolBar();
    bbd = new JToolBar();
    bbh = new JToolBar();
    fuente = new Font("Monospaced", Font.BOLD, 20);
    fuente2 = new Font("Monospaced",Font.BOLD, 15);
    aaddb = new String[4][20];
    aadhb = new String[4][20];
    d=0; h=0; n =1;
    fe= new Date();
    fech = new JLabel("Mes: "+fe.toString().substring(4, 7)
            +" "+fe.toString().substring(24,28));
    ntrans = new JLabel("");

    //Creacion de objetos y variables para agregar subcuentas
    ventanasubcuentas = new JInternalFrame("Sub cuentas");
    iinsertar = new JButton("Agregar al registro");
    iagsubcuenta = new JButton("Agregar otra sub cuenta");
    isubcuenta = new JTextField("");
    n$i = new JLabel("$");
    isaldo = new JTextField("0");
    subcuentas = new JComboBox();
    subcuentas.removeAllItems();
    ititulo = new JLabel("Escoja subcuenta a agregar: ");
    //im = new ImageIcon(getClass().getResource("/imagenes/contabilidad.jpg"));
    lbl = new JLabel();
    did=0; dih=0;

    posY1d=150; posY2d=120;
    posY1h=150; posY2h=120;
    posYlb=600;
    id=0; ih=0;
    }

    //La siguiente funcion se encarga de clasificar
    //las cuentas si son de resultados o no
    public void obtenerresul(String cod, double sald){
        String res="";
        c.lle="select resultado from cuentasmayor where codigocuenta='"+cod+"'";
        c.llenar();
        try{
           if(c.Resultado.next())
              res=c.Resultado.getString("resultado");
           c.Resultado.refreshRow();
        }catch(Exception e){}
        if(res.equals("Si")){
              c.upd="update estresultados set saldo=saldo+"+sald+" where codigo='"+cod+"'";
              c.actuali();
        }
    }
}