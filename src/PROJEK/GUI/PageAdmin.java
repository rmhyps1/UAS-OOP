/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PROJEK.GUI;

import Database.Create_Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import javax.swing.JTextField;

public class PageAdmin extends javax.swing.JFrame {

    public PageAdmin() {
        initComponents();
        Create_Connect.create();
        setupPlaceholder(jTextField1, "Nama");
        setupPlaceholder(jTextField2, "No Tlp");
        setupPlaceholder(jTextField3, "Email");

        jTextField4.setEditable(false); // ID hanya tampil, tidak bisa diubah

        jComboBoxProfesi.removeAllItems();
        jComboBoxProfesi.addItem("Guru PNS");
        jComboBoxProfesi.addItem("Guru Honorer");
        jComboBoxProfesi.addItem("Petugas Kebersihan");
        jComboBoxProfesi.addItem("Petugas Kebun");
        jComboBoxProfesi.addItem("Satpam");
        jComboBoxProfesi.addItem("Tata Usaha");
        jComboBoxProfesi.addItem("Petugas Perpustakaan");
        jComboBoxProfesi.setSelectedIndex(0);

        // Pastikan tabel Pegawai sudah ada sebelum loadTable
        createTableIfNotExists();

        loadTable();

        jTable2.getSelectionModel().addListSelectionListener(e -> {
            int row = jTable2.getSelectedRow();
            if (row != -1) {
                jTextField4.setText(jTable2.getValueAt(row, 0).toString()); // idPegawai
                jTextField1.setText(jTable2.getValueAt(row, 1).toString()); // nama
                jTextField2.setText(jTable2.getValueAt(row, 2).toString()); // noTelp
                jTextField3.setText(jTable2.getValueAt(row, 3).toString()); // email
                jComboBoxProfesi.setSelectedItem(jTable2.getValueAt(row, 5).toString()); // profesi
            }
        });

        jButtonTambah.addActionListener(evt -> {
            String nama = jTextField1.getText();
            String noTlp = jTextField2.getText();
            String email = jTextField3.getText();
            String profesi = (String) jComboBoxProfesi.getSelectedItem();

            String inputTanggal = javax.swing.JOptionPane.showInputDialog(this, "Masukkan Tanggal Masuk (DD-MM-YYYY):");
            LocalDate tanggalMasuk = null;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                tanggalMasuk = LocalDate.parse(inputTanggal, formatter);
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Format tanggal salah!");
                return;
            }

            String sql = "INSERT INTO Pegawai (nama, noTelp, email, tanggalMasuk, profesi) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = Create_Connect.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nama);
                pstmt.setString(2, noTlp);
                pstmt.setString(3, email);
                pstmt.setString(4, tanggalMasuk.toString());
                pstmt.setString(5, profesi);
                pstmt.executeUpdate();

                // Ambil data terakhir yang baru di-insert
                String sqlLast = "SELECT * FROM Pegawai ORDER BY idPegawai DESC LIMIT 1";
                try (PreparedStatement pstmtLast = conn.prepareStatement(sqlLast);
                     ResultSet rs = pstmtLast.executeQuery()) {
                    if (rs.next()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String tanggalTampil = "";
                        try {
                            tanggalTampil = LocalDate.parse(rs.getString("tanggalMasuk")).format(formatter);
                        } catch (Exception ex) {
                            tanggalTampil = rs.getString("tanggalMasuk");
                        }
                        String info = "ID: " + rs.getInt("idPegawai") +
                                "\nNama: " + rs.getString("nama") +
                                "\nNo Telp: " + rs.getString("noTelp") +
                                "\nEmail: " + rs.getString("email") +
                                "\nTanggal Masuk: " + tanggalTampil +
                                "\nProfesi: " + rs.getString("profesi");
                        javax.swing.JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!\n" + info);
                    }
                }

                loadTable();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Gagal menambah data: " + e.getMessage());
            }
        });

        jButtonEdit.addActionListener(evt -> {
            int row = jTable2.getSelectedRow();
            if (row == -1) {
                javax.swing.JOptionPane.showMessageDialog(null, "Pilih data yang ingin diedit!");
                return;
            }
            jTextField4.setText(jTable2.getValueAt(row, 0).toString());
            jTextField1.setText(jTable2.getValueAt(row, 1).toString());
            jTextField2.setText(jTable2.getValueAt(row, 2).toString());
            jTextField3.setText(jTable2.getValueAt(row, 3).toString());
            jComboBoxProfesi.setSelectedItem(jTable2.getValueAt(row, 5).toString());
        });

        jButtonUpdate.addActionListener(evt -> {
            int row = jTable2.getSelectedRow();
            if (row == -1) {
                javax.swing.JOptionPane.showMessageDialog(null, "Pilih data yang ingin diupdate!");
                return;
            }
            String idPegawai = jTextField4.getText();
            String nama = jTextField1.getText();
            String noTlp = jTextField2.getText();
            String email = jTextField3.getText();
            String profesi = (String) jComboBoxProfesi.getSelectedItem();

            String inputTanggal = javax.swing.JOptionPane.showInputDialog(this, "Masukkan Tanggal Masuk (DD-MM-YYYY):");
            LocalDate tanggalMasuk = null;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                tanggalMasuk = LocalDate.parse(inputTanggal, formatter);
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Format tanggal salah!");
                return;
            }

            String sql = "UPDATE Pegawai SET nama=?, noTelp=?, email=?, tanggalMasuk=?, profesi=? WHERE idPegawai=?";
            try (Connection conn = Create_Connect.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nama);
                pstmt.setString(2, noTlp);
                pstmt.setString(3, email);
                pstmt.setString(4, tanggalMasuk.toString());
                pstmt.setString(5, profesi);
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
            String idPegawai = jTextField4.getText();
            String sql = "DELETE FROM Pegawai WHERE idPegawai=?";
            try (Connection conn = Create_Connect.connect();
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

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Pegawai ("
                + "idPegawai INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nama TEXT,"
                + "noTelp TEXT,"
                + "email TEXT,"
                + "tanggalMasuk TEXT,"
                + "profesi TEXT"
                + ")";
        try (Connection conn = Create_Connect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal membuat tabel: " + e.getMessage());
        }
    }

    private void loadTable() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        String sql = "SELECT idPegawai, nama, noTelp, email, tanggalMasuk, profesi FROM Pegawai";
        try (Connection conn = Create_Connect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            while (rs.next()) {
                String tanggal = "";
                try {
                    tanggal = LocalDate.parse(rs.getString("tanggalMasuk")).format(formatter);
                } catch (Exception e) {
                    tanggal = rs.getString("tanggalMasuk");
                }
                model.addRow(new Object[]{
                        rs.getInt("idPegawai"),
                        rs.getString("nama"),
                        rs.getString("noTelp"),
                        rs.getString("email"),
                        tanggal,
                        rs.getString("profesi")
                });
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxProfesi = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButtonTambah = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonHapus = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();

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
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 150, -1));

        jLabel2.setText("Masukkan Nama");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 107, 111, -1));

        jLabel3.setText("Masukkan No Tlp");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 147, 111, -1));

        jLabel4.setText("Masukkan Email");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 184, 111, -1));

        jLabel6.setText("Masukkan Profesi");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 246, 111, -1));

        jComboBoxProfesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxProfesiActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxProfesi, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 246, 150, -1));

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
                "id", "nama", "noTelp", "email", "tanggalMasuk", "profesi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, 530, 350));

        jButtonTambah.setText("Tambah");
        jPanel1.add(jButtonTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, -1, -1));

        jButtonEdit.setText("Edit");
        jPanel1.add(jButtonEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 310, -1, -1));

        jButtonHapus.setText("Hapus");
        jPanel1.add(jButtonHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 310, -1, -1));

        jButtonUpdate.setText("Update");
        jPanel1.add(jButtonUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 310, -1, -1));

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 220, 150, -1));

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

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

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
    private javax.swing.JComboBox<String> jComboBoxProfesi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
