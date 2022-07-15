package frame;

import helpers.koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class kasirViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPane;
    private JPanel buttonPanel;
    private JButton cariButton;
    private JTextField cariTextField;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public kasirViewFrame(){
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        tambahButton.addActionListener(e -> {
            kasirInputFrame inputFrame = new kasirInputFrame();
            inputFrame.setVisible(true);
        });
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
           }
        });
        cariButton.addActionListener(e -> {

            if (cariTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi Kata Kunci Pencarian",
                        "Validasi Kata Kunci kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Connection c = koneksi.getConnection();
            String keyword = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT * FROM apotik WHERE id like ?";
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                viewTable.setModel(dtm);
                Object[] row = new Object[4];
                while (rs.next()) {
                    row[0] = rs.getInt("id_obat");
                    row[1] = rs.getString("nama_obat");
                    row[2] = rs.getString("harga");
                    row[3] = rs.getString("jumlah");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(null, "Pilih data dulu");
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog( null, "Yakin mau hapus?", "Konfirmasi Hapus",JOptionPane.YES_NO_OPTION
            );
            if(pilihan == 0){
                TableModel tm = viewTable.getModel();
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
                Connection c = koneksi.getConnection();
                String deleteSQL = "DELETE FROM apotik WHERE id = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt( 1, id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(null,"Pilih data dulu");
                return;
            }
            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
            kasirInputFrame inputFrame = new kasirInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        isiTable();
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Data apotik");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTable(){
        Connection c = koneksi.getConnection();
        String selectSQL = "SELECT * FROM apotik";

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"id_obat", "nama_obat", "harga", "jumlah"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            viewTable.getColumnModel().getColumn(1).setMaxWidth(150);
            viewTable.getColumnModel().getColumn(2).setMaxWidth(150);
            viewTable.getColumnModel().getColumn(3).setMaxWidth(150);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            Object[] row = new Object[4];
            while (rs.next()) {
                row[0] = rs.getInt("id_obat");
                row[1] = rs.getString("nama_obat");
                row[2] = rs.getString("harga");
                row[3] = rs.getString("jumlah");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
