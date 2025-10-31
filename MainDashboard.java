/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pemrograman1_kelompok5;

/**
 *
 * @author vynix-linux
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainDashboard extends JFrame {

    private JTable tabelAset;
    private DefaultTableModel modelTabel;
    private JScrollPane scrollPane;
    private JComboBox<String> comboAset;
    private JSpinner spinnerWaktuPinjam, spinnerWaktuKembali;
    private JTextField fieldNamaPeminjam, fieldSearch;
    private JButton btnBooking, btnKembalikan, btnSearch; // Kita kembalikan btnSearch
    
    private final Color WARNA_HEADER = new Color(76, 175, 80); // Hijau

    public MainDashboard() {
        setTitle("ReservaLink - Dashboard Booking Aset");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Tetap fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelAtas = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JLabel lblJudul = new JLabel("  Dashboard Ketersediaan Aset");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        fieldSearch = new JTextField(20);
        btnSearch = new JButton("Cari"); // Tombol Cari kembali
        
        panelSearch.add(new JLabel("Cari Aset:"));
        panelSearch.add(fieldSearch);
        panelSearch.add(btnSearch);
        
        panelAtas.add(lblJudul, BorderLayout.CENTER);
        panelAtas.add(panelSearch, BorderLayout.SOUTH);

        modelTabel = new DefaultTableModel(new String[]{"ID Aset", "Nama Aset", "Lokasi", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        tabelAset = new JTable(modelTabel);
        tabelAset.setRowHeight(25);
        tabelAset.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabelAset.getTableHeader().setBackground(WARNA_HEADER);
        tabelAset.getTableHeader().setForeground(Color.WHITE);
        scrollPane = new JScrollPane(tabelAset);
        
        JPanel panelBawah = new JPanel(new GridLayout(2, 1, 10, 10));
        
        JPanel panelBooking = new JPanel(new GridLayout(5, 2, 10, 10)); 
        panelBooking.setBorder(BorderFactory.createTitledBorder("Formulir Booking"));
        
        comboAset = new JComboBox<>();
        fieldNamaPeminjam = new JTextField(15);
        
        spinnerWaktuPinjam = new JSpinner(new SpinnerDateModel());
        spinnerWaktuKembali = new JSpinner(new SpinnerDateModel());
        spinnerWaktuPinjam.setEditor(new JSpinner.DateEditor(spinnerWaktuPinjam, "dd/MM/yyyy HH:mm"));
        spinnerWaktuKembali.setEditor(new JSpinner.DateEditor(spinnerWaktuKembali, "dd/MM/yyyy HH:mm"));
        
        btnBooking = new JButton("Booking Aset");
        
        panelBooking.add(new JLabel("Aset:"));
        panelBooking.add(comboAset);
        panelBooking.add(new JLabel("Nama:"));
        panelBooking.add(fieldNamaPeminjam);
        panelBooking.add(new JLabel("Waktu Pinjam:"));
        panelBooking.add(spinnerWaktuPinjam);
        panelBooking.add(new JLabel("Waktu Kembali:"));
        panelBooking.add(spinnerWaktuKembali);
        panelBooking.add(new JLabel()); // Spacer
        panelBooking.add(btnBooking);
        
        JPanel panelKembali = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelKembali.setBorder(BorderFactory.createTitledBorder("Opsi Pengembalian"));
        btnKembalikan = new JButton("Kembalikan Aset Terpilih (di Tabel)");
        
        panelKembali.add(btnKembalikan);
        
        panelBawah.add(panelBooking);
        panelBawah.add(panelKembali);

        add(panelAtas, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBawah, BorderLayout.SOUTH);

        muatDataAset();
        muatDataCombo();

        btnBooking.addActionListener(e -> {
            btnBooking.setEnabled(false);
            btnBooking.setText("Memproses...");

            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return prosesBooking();
                }
                @Override
                protected void done() {
                    try {
                        String hasil = get();
                        if (hasil.startsWith("Booking Berhasil!")) {
                            JOptionPane.showMessageDialog(MainDashboard.this, hasil, "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            muatDataAset();
                            muatDataCombo();
                            fieldNamaPeminjam.setText("");
                        } else {
                            JOptionPane.showMessageDialog(MainDashboard.this, hasil, "Peringatan", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        JOptionPane.showMessageDialog(MainDashboard.this, "Error saat booking: " + ex.getCause().getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
                    }
                    btnBooking.setEnabled(true);
                    btnBooking.setText("Booking Aset");
                }
            };
            worker.execute();
        });

        btnKembalikan.addActionListener(e -> {
            int barisTerpilih = tabelAset.getSelectedRow();
            if (barisTerpilih == -1) {
                JOptionPane.showMessageDialog(this, "Pilih aset yang ingin dikembalikan dari tabel", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            btnKembalikan.setEnabled(false);
            btnKembalikan.setText("Memproses...");

            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return prosesPengembalian(barisTerpilih);
                }
                @Override
                protected void done() {
                    try {
                        String hasil = get();
                        JOptionPane.showMessageDialog(MainDashboard.this, hasil, "Info", JOptionPane.INFORMATION_MESSAGE);
                        if (hasil.equals("Aset berhasil dikembalikan!")) {
                            muatDataAset();
                            muatDataCombo();
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                         JOptionPane.showMessageDialog(MainDashboard.this, "Error saat pengembalian: " + ex.getCause().getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
                    }
                    btnKembalikan.setEnabled(true);
                    btnKembalikan.setText("Kembalikan Aset Terpilih (di Tabel)");
                }
            };
            worker.execute();
        });
        
        btnSearch.addActionListener(e -> {
            btnSearch.setEnabled(false);
            btnSearch.setText("Mencari...");
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    muatDataAset(); // muatDataAset() melakukan query DB
                    return null;
                }
                @Override
                protected void done() {
                    btnSearch.setEnabled(true);
                    btnSearch.setText("Cari");
                }
            };
            worker.execute();
        });
    }

    private void muatDataAset() {
        Connection conn = KoneksiDB.getKoneksi();
        if (conn == null) return;
        
        String query = "SELECT * FROM assets WHERE nama_aset LIKE ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + fieldSearch.getText() + "%");
            ResultSet rs = ps.executeQuery();
            
            java.util.List<Object[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getInt("asset_id"),
                    rs.getString("nama_aset"),
                    rs.getString("lokasi"),
                    rs.getString("status")
                });
            }
            
            SwingUtilities.invokeLater(() -> {
                modelTabel.setRowCount(0);
                for (Object[] row : rows) {
                    modelTabel.addRow(row);
                }
            });
            
        } catch (SQLException e) {
            System.err.println("Gagal reload table yang kosong: " + e.getMessage());
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Gagal memuat data aset: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    private void muatDataCombo() {
        Connection conn = KoneksiDB.getKoneksi();
        if (conn == null) return;

        String query = "SELECT asset_id, nama_aset FROM assets WHERE status = 'Tersedia'";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            java.util.List<String> items = new java.util.ArrayList<>();
            while (rs.next()) {
                items.add(rs.getInt("asset_id") + " - " + rs.getString("nama_aset"));
            }
            
            SwingUtilities.invokeLater(() -> {
                comboAset.removeAllItems();
                for (String item : items) {
                    comboAset.addItem(item);
                }
            });
            
        } catch (SQLException e) {
            System.err.println("Gagal memuat data combo box: " + e.getMessage());
        }
    }

    private String prosesBooking() throws SQLException {
        String itemTerpilih = (String) comboAset.getSelectedItem();
        String namaPeminjam = fieldNamaPeminjam.getText();
        Date waktuPinjam = (Date) spinnerWaktuPinjam.getValue();
        Date waktuKembali = (Date) spinnerWaktuKembali.getValue();

        if (itemTerpilih == null) return "Pilih aset terlebih dahulu";
        int assetId = Integer.parseInt(itemTerpilih.split(" - ")[0]);
        if (namaPeminjam.isEmpty()) return "Nama peminjam tidak boleh kosong";
        if (waktuKembali.before(waktuPinjam)) return "Waktu kembali tidak boleh sebelum waktu pinjam";
        
        Timestamp tsPinjam = new Timestamp(waktuPinjam.getTime());
        Timestamp tsKembali = new Timestamp(waktuKembali.getTime());

        if (cekBentrok(assetId, tsPinjam, tsKembali)) {
            return "JADWAL BENTROK! Aset ini sudah dibooking pada rentang waktu tersebut.";
        }
        
        Connection conn = KoneksiDB.getKoneksi();
        String queryBooking = "INSERT INTO bookings (asset_id, nama_peminjam, waktu_pinjam, waktu_kembali, status_booking) VALUES (?, ?, ?, ?, 'Aktif')";
        String queryUpdateAset = "UPDATE assets SET status = 'Dipinjam' WHERE asset_id = ?";
        
        try (PreparedStatement psBooking = conn.prepareStatement(queryBooking);
             PreparedStatement psUpdate = conn.prepareStatement(queryUpdateAset)) {
            psBooking.setInt(1, assetId);
            psBooking.setString(2, namaPeminjam);
            psBooking.setTimestamp(3, tsPinjam);
            psBooking.setTimestamp(4, tsKembali);
            psBooking.executeUpdate();
            psUpdate.setInt(1, assetId);
            psUpdate.executeUpdate();
            return "Booking Berhasil!";
        }
    }

    private boolean cekBentrok(int assetId, Timestamp pinjamBaru, Timestamp kembaliBaru) throws SQLException {
        Connection conn = KoneksiDB.getKoneksi();
        String query = "SELECT COUNT(*) FROM bookings "
                     + "WHERE asset_id = ? AND status_booking = 'Aktif' "
                     + "AND ( (waktu_pinjam < ? AND waktu_kembali > ?) )";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, assetId);
            ps.setTimestamp(2, kembaliBaru);
            ps.setTimestamp(3, pinjamBaru);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    private String prosesPengembalian(int barisTerpilih) throws SQLException {
        int assetId = (int) modelTabel.getValueAt(barisTerpilih, 0);
        String statusAset = (String) modelTabel.getValueAt(barisTerpilih, 3);
        
        if (statusAset.equals("Tersedia")) {
            return "Aset ini sudah berstatus 'Tersedia'";
        }
        
        Connection conn = KoneksiDB.getKoneksi();
        String queryUpdateAset = "UPDATE assets SET status = 'Tersedia' WHERE asset_id = ?";
        String queryUpdateBooking = "UPDATE bookings SET status_booking = 'Selesai' "
                                 + "WHERE asset_id = ? AND status_booking = 'Aktif' "
                                 + "ORDER BY waktu_kembali DESC LIMIT 1";
        try (PreparedStatement psUpdateAset = conn.prepareStatement(queryUpdateAset);
             PreparedStatement psUpdateBooking = conn.prepareStatement(queryUpdateBooking)) {
            psUpdateAset.setInt(1, assetId);
            psUpdateAset.executeUpdate();
            psUpdateBooking.setInt(1, assetId);
            psUpdateBooking.executeUpdate();
            return "Aset berhasil dikembalikan!";
        }
    }

    public static void main(String[] args) {
        
        if (KoneksiDB.getKoneksi() == null) {
            JOptionPane.showMessageDialog(null, "Aplikasi tidak dapat terhubung ke database. Program akan ditutup.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        SwingUtilities.invokeLater(() -> {
            new MainDashboard().setVisible(true);
        });
    }
}