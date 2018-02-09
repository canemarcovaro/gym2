/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import Modelo.Pagos;
import Vista.VistaPagos;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JorgeA
 */
public class ControladorPagos implements ActionListener {

    private Pagos p;
    private VistaPagos pp;
    DefaultTableModel modelo = new DefaultTableModel();
    private boolean band = false;
    private boolean band2 = false;

    public ControladorPagos(Pagos p, VistaPagos pp) {

        this.p = p;
        this.pp = pp;
        this.pp.btnBuscar.addActionListener(this);
        this.pp.btnPago.addActionListener(this);
        this.pp.btnRPago.addActionListener(this);
        this.pp.btnCancelar.addActionListener(this);
        this.pp.comboTar.addActionListener(this);
        this.pp.btnACuenta.addActionListener(this);

    }

    public void iniciar() {

        DefaultTableModel a = new DefaultTableModel();
        pp.setTitle("Pagos");
        pp.setLocationRelativeTo(null);
        pp.setVisible(true);
        pp.panelPago.setVisible(false);
        pp.btnPago.setEnabled(false);
        pp.txtSaldo.setText(null);
        pp.txtCuenta.setText(null);
        pp.panelInfo.setVisible(false);
        pp.btnACuenta.setEnabled(false);
        pp.txtCC.setVisible(false);
        pp.txtDni2.setVisible(false);

        limpiarTabla();
        cargarTablaPagos(a);

        if (band == false) {
            cargarTablaPagos(modelo);
            band = true;

        }
    }

    public void limpiarTabla() {

        int sizeModel = pp.tablaPagos.getRowCount();

        for (int i = 0; i < sizeModel; i++) {
            pp.tablaPagos.removeRowSelectionInterval(0, sizeModel - 1);
        }

    }

    public void cargarTablaPagos(DefaultTableModel table) {

        try {

            PreparedStatement ps = null;
            pp.tablaPagos.setModel(table);
            String dni = pp.txtCuenta.getText();

            ResultSet rs = null;
            Conexion conn = new Conexion();
            Connection con = conn.getConect();

            String sql = "SELECT b.fecha, b.debe, b.haber FROM clientes a, cuentacorriente b WHERE a.dni = '" + dni + "' and a.dni = b.dniCliente";
            String sql2 = "SELECT dni, fechaVto, baja FROM clientes WHERE dni = '" + dni + "'";

            try {
                ps = con.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                rs = ps.executeQuery();

            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResultSetMetaData rsmd = null;
            try {
                rsmd = rs.getMetaData();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            int cantCol = 0;

            try {
                cantCol = rsmd.getColumnCount();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            table.addColumn("FECHA PAGO");
            table.addColumn("DEBE");
            table.addColumn("HABER");

            int debe = 0;
            int haber = 0;

            try {
                while (rs.next()) {

                    debe = debe + rs.getInt(2);
                    haber = haber + rs.getInt(3);

                    Object[] filas = new Object[cantCol];
                    for (int i = 0; i < cantCol; i++) {
                        filas[i] = rs.getObject(i + 1);

                    }

                    table.addRow(filas);

                }

                pp.txtSaldo.setText(String.valueOf(haber - debe));

            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                ps = con.prepareStatement(sql2);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
            }
            rs = ps.executeQuery();

            while (rs.next()) {

                if (rs.getInt(3) == 0) {

                    pp.txtECuenta.setText("ACTIVA");

                } else {

                    pp.txtECuenta.setText("VENCIDA");

                }

                pp.txtPVto.setText(String.valueOf(rs.getDate(2)));
                pp.txtDni2.setText(String.valueOf(rs.getInt(1)));
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nuevoPago() {

        try {
            PreparedStatement ps = null;

            String dni = pp.txtCuenta.getText();

            ResultSet rs = null;
            Conexion conn = new Conexion();
            Connection con = conn.getConect();

            String sql = "SELECT dni, nombre FROM clientes WHERE dni = '" + dni + "'";
            String sql2 = "SELECT id, precio FROM tarifas";

            try {
                ps = con.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                rs = ps.executeQuery();

            } catch (SQLException ex) {
                Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                while (rs.next()) {

                    Calendar fecha = Calendar.getInstance();

                    pp.txtDni.setText(String.valueOf(rs.getObject(1)));
                    pp.txtNombre.setText(String.valueOf(rs.getObject(2)));
                    pp.txtFecha.setText(String.valueOf(new java.sql.Date(fecha.getTimeInMillis())));

                }
            } catch (SQLException ex) {
                Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (band2 == false) {
                try {
                    ps = con.prepareStatement(sql2);
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
                }
                rs = ps.executeQuery();

                while (rs.next()) {

                    pp.comboTar.addItem(rs.getString(2));

                }

                band2 = true;
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void realizarPago() {

        PreparedStatement ps = null;

        String dni = pp.txtCuenta.getText();

        ResultSet rs = null;
        Conexion conn = new Conexion();
        Connection con = null;
        try {
            con = conn.getConect();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "INSERT INTO cuentacorriente (id,dniCliente,fecha,debe,haber) VALUES (NULL,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(pp.txtDni.getText()));
            ps.setString(2, String.valueOf(pp.txtFecha.getText()));
            ps.setDouble(3, (double) Integer.parseInt(pp.txtImporte.getText()));
            ps.setDouble(4, (double) Integer.parseInt(pp.txtMonto.getText()));

            ps.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void activarCuenta() {

        PreparedStatement ps = null;

        String dni = pp.txtCuenta.getText();

        ResultSet rs = null;
        Conexion conn = new Conexion();
        Connection con = null;
        try {
            con = conn.getConect();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "UPDATE clientes SET fechaVal=?, fechaVto=?, baja=? WHERE dni=?";

        try {

            Calendar fechaVal = Calendar.getInstance();
            Calendar fechaVto = Calendar.getInstance();
            fechaVto.add(Calendar.DATE, 30);

            ps = con.prepareStatement(sql);

            ps.setInt(4, Integer.parseInt(pp.txtDni2.getText()));
            ps.setDate(1, new java.sql.Date(fechaVal.getTimeInMillis()));
            ps.setDate(2, new java.sql.Date(fechaVto.getTimeInMillis()));
            ps.setInt(3, 0);

            ps.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == pp.btnBuscar) {

            DefaultTableModel a = new DefaultTableModel();
            limpiarTabla();
            cargarTablaPagos(a);
            pp.btnPago.setEnabled(true);
            pp.panelInfo.setVisible(true);

            if (pp.txtECuenta.getText().equals("VENCIDA")) {

                pp.btnACuenta.setEnabled(true);

            }

        }
        if (e.getSource() == pp.btnPago) {

            pp.panelPago.setVisible(true);
            pp.txtImporte.setText("0");
            pp.txtMonto.setText("0");
            pp.btnPago.setEnabled(false);
            nuevoPago();

        }
        if (e.getSource() == pp.btnRPago) {

            DefaultTableModel b = new DefaultTableModel();

            String botones[] = {"Si", "No"};
            int eleccion = JOptionPane.showOptionDialog(pp, "¿Desea realizar el siguiente pago?", "Confirmación de Pago", 0, 2, null, botones, this);
            if (eleccion == JOptionPane.YES_OPTION) {

                realizarPago();
                pp.btnPago.setEnabled(true);
                pp.panelPago.setVisible(false);
                pp.txtDni.setText(null);
                pp.txtNombre.setText(null);
                pp.txtFecha.setText(null);
                pp.txtImporte.setText(null);
                pp.txtMonto.setText(null);

                cargarTablaPagos(b);

            } else if (eleccion == JOptionPane.NO_OPTION) {

            }

        }
        if (e.getSource() == pp.btnCancelar) {

            pp.btnPago.setEnabled(true);
            pp.panelPago.setVisible(false);
            pp.txtDni.setText(null);
            pp.txtNombre.setText(null);
            pp.txtFecha.setText(null);
            pp.txtImporte.setText(null);
            pp.btnCancelar.setSelected(false);
            pp.txtMonto.setText(null);
        }
        if (e.getSource() == pp.comboTar) {

            if (pp.comboTar.getSelectedItem().toString().equals("Otros")) {

                pp.txtImporte.setEditable(true);
                pp.txtImporte.setText("0");
            } else {
                pp.txtImporte.setEditable(false);
                pp.txtImporte.setText(pp.comboTar.getSelectedItem().toString());

            }

        }
        if (e.getSource() == pp.btnACuenta) {

            DefaultTableModel a = new DefaultTableModel();

            String botones[] = {"Si", "No"};
            int eleccion = JOptionPane.showOptionDialog(pp, "¿Desea activar la cuenta " + pp.txtDni2.getText() + "?", "Renovando Cuenta", 0, 2, null, botones, this);
            if (eleccion == JOptionPane.YES_OPTION) {

                activarCuenta();
                limpiarTabla();
                pp.btnACuenta.setEnabled(false);
                cargarTablaPagos(a);

            } else if (eleccion == JOptionPane.NO_OPTION) {

            }

        }
    }

}
