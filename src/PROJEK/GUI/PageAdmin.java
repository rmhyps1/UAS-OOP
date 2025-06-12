/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PROJEK.GUI;

import Database.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Class.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import javax.swing.JTextField;

public class PageAdmin extends javax.swing.JFrame {

    public PageAdmin() {
        initComponents();
        ConnectDB.create();
        setupPlaceholder(jTextField1, "Nama");
        setupPlaceholder(jTextField2, "No Tlp");
        setupPlaceholder(jTextField3, "Email");
        setupPlaceholder(jTextField4, "ID Petugas");

        Pengajar pengajar = new Pengajar();
        Staff staff = new Staff();
        Keamanan keamanan = new Keamanan();
        Kebersihan kebersihan = new Kebersihan();

        GuruPNS guruPNS = new GuruPNS();
        GuruHonorer guruHonorer = new GuruHonorer();

        TataUsaha tataUsaha = new TataUsaha();
        PetugasPerpus petugasPerpus = new PetugasPerpus();

        Satpam satpam = new Satpam();

        PetugasKebersihan petugasKebersihan = new PetugasKebersihan();
        PetugasKebun petugasKebun = new PetugasKebun();

        jComboBoxProfesi.removeAllItems();
        jComboBoxProfesi.addItem("Pilih Profesi");
        jComboBoxProfesi.addItem(pengajar.getRole());
        jComboBoxProfesi.addItem(staff.getRole());
        jComboBoxProfesi.addItem(keamanan.getRole());
        jComboBoxProfesi.addItem(kebersihan.getRole());
        jComboBoxProfesi.setSelectedIndex(0);

        jComboBoxPegawai.removeAllItems();
        jComboBoxPegawai.addItem("Pilih Jenis Pegawai");

        jComboBoxProfesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String selectedProfesi = (String) jComboBoxProfesi.getSelectedItem();
                jComboBoxPegawai.removeAllItems();
                if (pengajar.getRole().equals(selectedProfesi)) {
                    jComboBoxPegawai.addItem(guruPNS.getRole());
                    jComboBoxPegawai.addItem(guruHonorer.getRole());
                } else if (staff.getRole().equals(selectedProfesi)) {
                    jComboBoxPegawai.addItem(tataUsaha.getRole());
                    jComboBoxPegawai.addItem(petugasPerpus.getRole());
                } else if (keamanan.getRole().equals(selectedProfesi)) {
                    jComboBoxPegawai.addItem(satpam.getRole());
                } else if (kebersihan.getRole().equals(selectedProfesi)) {
                    jComboBoxPegawai.addItem(petugasKebersihan.getRole());
                    jComboBoxPegawai.addItem(petugasKebun.getRole());
                } else {
                    jComboBoxPegawai.addItem("Pilih Jenis Pegawai");
                }
            }
        });

        loadTable();

        jTable2.getSelectionModel().addListSelectionListener(e -> {
            int row = jTable2.getSelectedRow();
            if (row != -1) {
                jTextField1.setText(jTable2.getValueAt(row, 0).toString());
                jTextField2.setText(jTable2.getValueAt(row, 1).toString());
                jTextField3.setText(jTable2.getValueAt(row, 2).toString());
                jTextField4.setText(jTable2.getValueAt(row, 3).toString());
                jComboBoxProfesi.setSelectedItem(jTable2.getValueAt(row, 4).toString());
                jComboBoxPegawai.setSelectedItem(jTable2.getValueAt(row, 5).toString());
            }
        });

        jButtonTambah.addActionListener(evt -> {
            String nama = jTextField1.getText();
            String noTlp = jTextField2.getText();
            String email = jTextField3.getText();
            String idPegawai = jTextField4.getText();
            String profesi = (String) jComboBoxProfesi.getSelectedItem();
            String jenisPegawai = (String) jComboBoxPegawai.getSelectedItem();

            String sql = "INSERT INTO Pegawai (nama, no_tlp, email, id_pegawai, profesi, jenis_pegawai) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectDB.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nama);
                pstmt.setString(2, noTlp);
                pstmt.setString(3, email);
                pstmt.setString(4, idPegawai);
                pstmt.setString(5, profesi);
                pstmt.setString(6, jenisPegawai);
                pstmt.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
                loadTable();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Gagal menambah data: " + e.getMessage());
            }
        });

        jButtonEdit.addActionListener(evt -> {
            int row = jTable2.getSelectedRow();
            if (row == -1) {
                javax.swing.JOptionPane.showMessageDialog(null, "Pilih data yang ingin diedit!");
                return;
            }
            jTextField1.setText(jTable2.getValueAt(row, 0).toString());
            jTextField2.setText(jTable2.getValueAt(row, 1).toString());
            jTextField3.setText(jTable2.getValueAt(row, 2).toString());
            jTextField4.setText(jTable2.getValueAt(row, 3).toString());
            jComboBoxProfesi.setSelectedItem(jTable2.getValueAt(row, 4).toString());
            jComboBoxPegawai.setSelectedItem(jTable2.getValueAt(row, 5).toString());
        });

        jButtonUpdate.addActionListener(evt -> {
            int row = jTable2.getSelectedRow();
            if (row == -1) {
                javax.swing.JOptionPane.showMessageDialog(null, "Pilih data yang ingin diupdate!");
                return;
            }
            String nama = jTextField1.getText();
            String noTlp = jTextField2.getText();
            String email = jTextField3.getText();
            String idPegawai = jTextField4.getText();
            String profesi = (String) jComboBoxProfesi.getSelectedItem();
            String jenisPegawai = (String) jComboBoxPegawai.getSelectedItem();

            String sql = "UPDATE Pegawai SET nama=?, no_tlp=?, email=?, profesi=?, jenis_pegawai=? WHERE id_pegawai=?";
            try (Connection conn = ConnectDB.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nama);
                pstmt.setString(2, noTlp);
                pstmt.setString(3, email);
                pstmt.setString(4, profesi);
                pstmt.setString(5, jenisPegawai);
                pstmt.setString(6, idPegawai);
                pstmt.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
                loadTable();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Gagal update data: " + e.getMessage());
            }
        });

        jButtonHapus.addActionListener(evt -> {
            int row = jTable2.getSelectedRow();
            if (row == -1) {
                javax.swing.JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus!");
                return;
            }
            String idPegawai = jTable2.getValueAt(row, 3).toString();
            String sql = "DELETE FROM Pegawai WHERE id_pegawai=?";
            try (Connection conn = ConnectDB.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, idPegawai);
                pstmt.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                loadTable();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Gagal hapus data: " + e.getMessage());
            }
        });
    }

    private void loadTable() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        String sql = "SELECT nama, no_tlp, email, id_pegawai, profesi, jenis_pegawai FROM Pegawai";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nama"),
                    rs.getString("no_tlp"),
                    rs.getString("email"),
                    rs.getString("id_pegawai"),
                    rs.getString("profesi"),
                    rs.getString("jenis_pegawai")
                });
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Gagal load data: " + e.getMessage());
        }
    }

    private void setupPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxProfesi = new javax.swing.JComboBox<>();
        jComboBoxPegawai = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButtonTambah = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonHapus = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(960, 540));
        setPreferredSize(new java.awt.Dimension(960, 540));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Menambahkan list data pegawai");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 6, 346, 80));

        jTextField1.setText("Nama");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 104, 150, -1));

        jTextField2.setText("No Tlp");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 144, 150, -1));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    evt.consume();
                }
            }
        });

        jTextField3.setText("Email");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 184, 150, -1));

        jTextField4.setText("ID Petugas");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 218, 150, -1));

        jLabel2.setText("Masukkan Nama");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 107, 111, -1));

        jLabel3.setText("Masukkan No Tlp");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 147, 111, -1));

        jLabel4.setText("Masukkan Email");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 184, 111, -1));

        jLabel5.setText("Masukkan ID Petugas");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 218, 123, -1));

        jLabel6.setText("Masukkan Profesi");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 246, 111, -1));

        jComboBoxProfesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxProfesiActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxProfesi, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 246, 150, -1));

        jPanel1.add(jComboBoxPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 274, 150, -1));

        jLabel7.setText("Tanggal Masuk");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 274, 145, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nama", "No Tlp", "Email", "ID Petugas", "Profesi", "Tanggal Masuk"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(60);
        }

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, 530, 350));

        jButtonTambah.setText("Tambah");
        jPanel1.add(jButtonTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, -1, -1));

        jButtonEdit.setText("Edit");
        jPanel1.add(jButtonEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 310, -1, -1));

        jButtonHapus.setText("Hapus");
        jPanel1.add(jButtonHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 310, -1, -1));

        jButtonUpdate.setText("Update");
        jPanel1.add(jButtonUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 310, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 942, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBoxProfesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxProfesiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxProfesiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PageAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PageAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PageAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PageAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PageAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonHapus;
    private javax.swing.JButton jButtonTambah;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JComboBox<String> jComboBoxPegawai;
    private javax.swing.JComboBox<String> jComboBoxProfesi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
