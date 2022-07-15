package frame;

import helpers.koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class kasirInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JTextField jumlahTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private int id_obat;
    private JPanel buttonPanel;
    private JLabel jumlahLabel;
    private JLabel hargaLabel;
    private JTextField hargaTextField;

    public kasirInputFrame(){
        batalButton.addActionListener(e -> {
            dispose();
        });
        simpanButton.addActionListener(e -> {
            String nama_obat = namaTextField.getText();
            String harga = hargaTextField.getText();
            String jumlah= jumlahTextField.getText();
            Connection c = koneksi.getConnection();
            PreparedStatement ps;
            if (nama_obat.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data nama",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }
            try {
                String cekSQL;
                if (this.id_obat == 0) { //jika TAMBAH

                    cekSQL = "SELECT * FROM obat WHERE nama_obat=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_obat);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "nama yang sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO apotik (id_obat,nama_obat,harga,jumlah) VALUES (NULL, ?, ?, ?)";
                    insertSQL = "INSERT INTO `obat` (`id_obat`, `nama_obat`, `harga`, `jumlah`) VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `obat` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO obat (nama_obat,harga,jumlah) VALUES (?)";
                    insertSQL = "INSERT INTO obat SET nama_obat=?, harga=?, jumlah=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama_obat);
                    ps.setString(2, jumlah);
                    ps.setString(3, harga);
                    ps.executeUpdate();
                    dispose();
                } else {
                    cekSQL = "SELECT * FROM apotik WHERE nama_obat=? AND harga=? AND jumlah=? AND id_obat!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_obat);
                    ps.setString(2, jumlah);
                    ps.setString(3, harga);
                    ps.setInt(4,id_obat);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Data sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String updateSQL = "UPDATE obat SET nama_obat=?,harga=?, jumlah=? WHERE id_obat=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama_obat);
                    ps.setString(2, jumlah);
                    ps.setString(3, harga);
                    ps.setInt(4, id_obat);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("input obat");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen(){
        Connection c = koneksi.getConnection();
        String findSQL = "SELECT * FROM obat WHERE id_obat = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id_obat);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                idTextField.setText(String.valueOf(rs.getInt("id_obat")));
                namaTextField.setText(rs.getString("nama_obat"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setId(int id) {
    }
}