/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import Vista.VistaReportes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author JorgeA
 */
public class ControladorReportes implements ActionListener {

    private VistaReportes vr;

    public ControladorReportes(VistaReportes vr) {
        this.vr = vr;
        this.vr.reporte1.addActionListener(this);
    }

    public void iniciar() {
        vr.setTitle("Reportes");
        vr.setLocationRelativeTo(null);
        vr.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vr.reporte1) {
            try {
                Conexion con = new Conexion();
                
                Connection conn = null;
                try {
                    conn = con.getConect();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ControladorReportes.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                JasperReport reporte = null;
                String path = "src\\Reportes\\reporteArqueo.jasper";
                
                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                
                JasperPrint jprint = JasperFillManager.fillReport(reporte, null,conn);
                JasperViewer view = new JasperViewer(jprint,false);
                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                
                view.setVisible(true);
                
            } catch (JRException ex) {
                Logger.getLogger(ControladorReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

}
