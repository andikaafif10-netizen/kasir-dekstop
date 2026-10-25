package com.mycompany.kasirdesktop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class KasirApp extends JFrame {

    private final JComboBox<Produk> pilihanProduk;
    private final JSpinner jumlahProduk;
    private final JTable tabel;
    private final DefaultTableModel model;
    private final JLabel labelTotal;
    private final JTextField inputBayar;
    private final JLabel labelKembalian;

    private final List<ItemKeranjang> keranjang = new ArrayList<>();

    private final NumberFormat rupiah =
            NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public KasirApp() {
        setTitle("Aplikasi Kasir - KasirDesktop");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(
        new java.awt.Color(235, 243, 252));

        getContentPane().setForeground(
        new java.awt.Color(20, 40, 70));

        rupiah.setMaximumFractionDigits(0);

        JLabel judul = new JLabel(
    "<html><div style='text-align:center;'>"
    + "🛒 APLIKASI KASIR TOKO"
    + "<br><span style='font-size:14px;'>Mudah • Cepat • Akurat</span>"
    + "</div></html>",
    SwingConstants.CENTER
);

judul.setFont(new java.awt.Font(
    "Arial", java.awt.Font.BOLD, 28));

judul.setForeground(new java.awt.Color(255, 255, 255));

judul.setOpaque(true);

judul.setBackground(new java.awt.Color(25, 100, 190));

judul.setBorder(
    BorderFactory.createEmptyBorder(25, 10, 25, 10));
judul.setPreferredSize(new java.awt.Dimension(100, 140));
        judul.setFont(new java.awt.Font("Arial",
                java.awt.Font.BOLD, 24));
        judul.setBorder(BorderFactory.createEmptyBorder(
                15, 10, 15, 10));
        add(judul, BorderLayout.NORTH);

        // Daftar produk
    pilihanProduk = new JComboBox<>();
    muatProdukDariDatabase();
        jumlahProduk = new JSpinner(
                new SpinnerNumberModel(1, 1, 1000, 1));

        JButton tombolTambah = new JButton("Tambah Barang");
        JButton tombolHapus = new JButton("Hapus Barang");
        JButton tombolRiwayat = new JButton("Riwayat Transaksi");
        tombolTambah.setBackground(new java.awt.Color(25, 160, 85));
tombolTambah.setForeground(java.awt.Color.WHITE);
tombolTambah.setFocusPainted(false);
tombolTambah.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));

tombolHapus.setBackground(new java.awt.Color(220, 55, 55));
tombolHapus.setForeground(java.awt.Color.WHITE);
tombolHapus.setFocusPainted(false);
tombolHapus.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));

tombolRiwayat.setBackground(new java.awt.Color(25, 125, 220));
tombolRiwayat.setForeground(java.awt.Color.WHITE);
tombolRiwayat.setFocusPainted(false);
tombolRiwayat.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));

        JPanel panelInput = new JPanel(new FlowLayout(
                FlowLayout.LEFT, 10, 10));
        panelInput.add(new JLabel("Produk:"));
        panelInput.add(pilihanProduk);
        panelInput.add(new JLabel("Jumlah:"));
        panelInput.add(jumlahProduk);
        panelInput.add(tombolTambah);
        panelInput.add(tombolHapus);
        panelInput.add(tombolRiwayat);
        
        tombolRiwayat.addActionListener(e -> tampilkanRiwayatTransaksi());

        model = new DefaultTableModel(
                new String[]{"Kode", "Nama Barang", "Harga",
                    "Jumlah", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        tabel.setRowHeight(35);

tabel.setFont(new java.awt.Font(
    "Arial", java.awt.Font.PLAIN, 14));

tabel.getTableHeader().setFont(new java.awt.Font(
    "Arial", java.awt.Font.BOLD, 14));

tabel.getTableHeader().setBackground(
    new java.awt.Color(40, 130, 220));

tabel.getTableHeader().setForeground(
    java.awt.Color.WHITE);

tabel.setSelectionBackground(
    new java.awt.Color(190, 220, 250));

tabel.setSelectionForeground(
    new java.awt.Color(20, 40, 70));

tabel.setGridColor(
    new java.awt.Color(210, 220, 230));
        tabel.setRowHeight(28);

        JPanel panelTengah = new JPanel(new BorderLayout(5, 5));
        panelTengah.add(panelInput, BorderLayout.NORTH);
        panelTengah.add(new JScrollPane(tabel),
                BorderLayout.CENTER);
        panelTengah.setBorder(BorderFactory.createEmptyBorder(
                0, 10, 0, 10));

        add(panelTengah, BorderLayout.CENTER);

        // Panel pembayaran
        labelTotal = new JLabel(formatRupiah(0));
        labelTotal.setFont(
    new java.awt.Font("Arial", java.awt.Font.BOLD, 28)
);

labelTotal.setForeground(
    new java.awt.Color(20, 75, 170)
);

        labelTotal.setFont(new java.awt.Font(
    "Arial", java.awt.Font.BOLD, 28));

labelTotal.setForeground(
    new java.awt.Color(20, 75, 170));
        labelTotal.setFont(new java.awt.Font("Arial",
                java.awt.Font.BOLD, 22));

        inputBayar = new JTextField(12);
        inputBayar.setFont(
    new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));

inputBayar.setPreferredSize(
    new java.awt.Dimension(250, 40));
        labelKembalian = new JLabel(formatRupiah(0));

        JButton tombolBayar = new JButton("Bayar");
        tombolBayar.setBackground(
    new java.awt.Color(25, 160, 85));

tombolBayar.setForeground(
    java.awt.Color.WHITE);

tombolBayar.setFont(
    new java.awt.Font("Arial", java.awt.Font.BOLD, 16));

tombolBayar.setFocusPainted(false);
        JButton tombolReset = new JButton("Transaksi Baru");
        tombolReset.setBackground(
    new java.awt.Color(245, 155, 25));

tombolReset.setForeground(
    java.awt.Color.WHITE);

tombolReset.setFont(
    new java.awt.Font("Arial", java.awt.Font.BOLD, 16));

tombolReset.setFocusPainted(false);

        JPanel panelBawah = new JPanel(new GridLayout(0, 2, 10, 8));
        panelBawah.setBorder(BorderFactory.createEmptyBorder(
                10, 15, 15, 15));

       JLabel teksTotal = new JLabel("TOTAL BELANJA:");
teksTotal.setFont(new java.awt.Font(
    "Arial", java.awt.Font.BOLD, 16));
teksTotal.setForeground(new java.awt.Color(20, 75, 170));
panelBawah.add(teksTotal);
        panelBawah.add(labelTotal);
        panelBawah.add(new JLabel("Uang Pembayaran (Rp):"));
        panelBawah.add(inputBayar);
        panelBawah.add(new JLabel("Kembalian:"));
        panelBawah.add(labelKembalian);
        panelBawah.add(tombolBayar);
        panelBawah.add(tombolReset);

        add(panelBawah, BorderLayout.SOUTH);

        // Aksi tombol tambah barang
        tombolTambah.addActionListener(e -> {
            Produk p = (Produk) pilihanProduk.getSelectedItem();
            int jumlah = (Integer) jumlahProduk.getValue();

            if (p == null) {
                return;
            }

            ItemKeranjang itemDitemukan = null;

            for (ItemKeranjang item : keranjang) {
                if (item.produk.kode.equals(p.kode)) {
                    itemDitemukan = item;
                    break;
                }
            }

            if (itemDitemukan == null) {
                keranjang.add(new ItemKeranjang(p, jumlah));
            } else {
                itemDitemukan.jumlah += jumlah;
            }

            perbaruiTabel();
        });

        // Aksi tombol hapus barang
        tombolHapus.addActionListener(e -> {
            int baris = tabel.getSelectedRow();

            if (baris == -1) {
                JOptionPane.showMessageDialog(this,
                        "Pilih barang pada tabel terlebih dahulu.");
                return;
            }

            keranjang.remove(baris);
            perbaruiTabel();
        });

        // Aksi pembayaran
        tombolBayar.addActionListener(e -> prosesPembayaran());

        // Aksi transaksi baru
        tombolReset.addActionListener(e -> {
            keranjang.clear();
            inputBayar.setText("");
            labelKembalian.setText(formatRupiah(0));
            perbaruiTabel();
        });
    }

    private void perbaruiTabel() {
        model.setRowCount(0);
        long total = 0;

        for (ItemKeranjang item : keranjang) {
            long subtotal = item.produk.harga * item.jumlah;
            total += subtotal;

            model.addRow(new Object[]{
                item.produk.kode,
                item.produk.nama,
                formatRupiah(item.produk.harga),
                item.jumlah,
                formatRupiah(subtotal)
            });
        }

        labelTotal.setText(formatRupiah(total));
        labelKembalian.setText(formatRupiah(0));
    }

    private void prosesPembayaran() {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Keranjang belanja masih kosong.");
            return;
        }

        String teksBayar = inputBayar.getText().trim();

        long bayar;
        try {
            if (!teksBayar.matches("\\d+")) {
                throw new NumberFormatException();
            }
            bayar = Long.parseLong(teksBayar);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Masukkan uang pembayaran berupa angka bulat, "
                    + "contoh: 100000.");
            return;
        }

        long total = 0;
        for (ItemKeranjang item : keranjang) {
            total += item.produk.harga * item.jumlah;
        }

        if (bayar < total) {
            JOptionPane.showMessageDialog(this,
                    "Uang pembayaran kurang "
                    + formatRupiah(total - bayar));
            return;
        }

        try {
    simpanTransaksiKeDatabase(total, bayar, bayar - total);
} catch (SQLException e) {
    JOptionPane.showMessageDialog(this,
            "Gagal menyimpan transaksi: " + e.getMessage());
    return;
}
        long kembalian = bayar - total;
        labelKembalian.setText(formatRupiah(kembalian));

        StringBuilder struk = new StringBuilder();
        struk.append("TRANSAKSI BERHASIL\n\n");

        for (ItemKeranjang item : keranjang) {
            struk.append(item.produk.nama)
                    .append(" x ").append(item.jumlah)
                    .append(" = ")
                    .append(formatRupiah(
                            item.produk.harga * item.jumlah))
                    .append("\n");
        }

        struk.append("\nTotal: ").append(formatRupiah(total));
        struk.append("\nBayar: ").append(formatRupiah(bayar));
        struk.append("\nKembalian: ").append(formatRupiah(kembalian));

        JOptionPane.showMessageDialog(this, struk.toString(),
                "Struk Pembayaran",
                JOptionPane.INFORMATION_MESSAGE);

        keranjang.clear();
        inputBayar.setText("");
        perbaruiTabel();
    }

    private String formatRupiah(long angka) {
        return rupiah.format(angka);
    }

    private static class Produk {
        String kode;
        String nama;
        long harga;

        Produk(String kode, String nama, long harga) {
            this.kode = kode;
            this.nama = nama;
            this.harga = harga;
        }

        @Override
        public String toString() {
            return nama + " - " + formatHarga(harga);
        }

        private static String formatHarga(long harga) {
            return NumberFormat.getCurrencyInstance(
                    new Locale("id", "ID")).format(harga);
        }
    }

    private static class ItemKeranjang {
        Produk produk;
        int jumlah;

        ItemKeranjang(Produk produk, int jumlah) {
            this.produk = produk;
            this.jumlah = jumlah;
        }
    }

    private void muatProdukDariDatabase() {
    String sql = "SELECT kode_produk, nama_produk, harga, stok FROM produk";

    try (Connection conn = koneksi.getKoneksi();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        pilihanProduk.removeAllItems();

        while (rs.next()) {
            String kode = rs.getString("kode_produk");
            String nama = rs.getString("nama_produk");
            long harga = rs.getLong("harga");
            int stok = rs.getInt("stok");

            if (stok > 0) {
                pilihanProduk.addItem(
                    new Produk(kode, nama, harga)
                );
            }
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(
            this,
            "Gagal memuat produk: " + e.getMessage()
        );
    }
}
    private void tampilkanRiwayatTransaksi() {
    javax.swing.JFrame frame =
            new javax.swing.JFrame("Riwayat Transaksi");
    
    String[] kolom = {
    "ID Transaksi", "Tanggal", "Total", "Bayar", "Kembalian"
};

javax.swing.table.DefaultTableModel model =
    new javax.swing.table.DefaultTableModel(kolom, 0);

javax.swing.JTable tabelRiwayat =
        new javax.swing.JTable(model);
String sql = "SELECT id_transaksi, tanggal, total, bayar, kembalian "
           + "FROM transaksi ORDER BY id_transaksi DESC";

try (java.sql.Connection conn = koneksi.getKoneksi();
     java.sql.PreparedStatement ps = conn.prepareStatement(sql);
     java.sql.ResultSet rs = ps.executeQuery()) {

    while (rs.next()) {
        model.addRow(new Object[]{
            rs.getInt("id_transaksi"),
            rs.getTimestamp("tanggal"),
            rs.getLong("total"),
            rs.getLong("bayar"),
            rs.getLong("kembalian")
        });
    }

} catch (java.sql.SQLException e) {
    javax.swing.JOptionPane.showMessageDialog(
        this, "Gagal memuat riwayat: " + e.getMessage()
    );
    return;
}
frame.add(new javax.swing.JScrollPane(tabelRiwayat));
frame.setSize(750, 400);
frame.setLocationRelativeTo(this);
frame.setVisible(true);
} 
  public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(() -> {
        new KasirApp().setVisible(true);
    });
}  
  
  private void simpanTransaksiKeDatabase(
        long total, long bayar, long kembalian) throws SQLException {

    String sqlTransaksi =
        "INSERT INTO transaksi (total, bayar, kembalian) VALUES (?, ?, ?)";

    String sqlDetail =
        "INSERT INTO detail_transaksi "
        + "(id_transaksi, id_produk, jumlah, harga, subtotal) "
        + "VALUES (?, ?, ?, ?, ?)";

    String sqlStok =
        "UPDATE produk SET stok = stok - ? "
        + "WHERE id_produk = ? AND stok >= ?";

    try (Connection conn = koneksi.getKoneksi()) {
        conn.setAutoCommit(false);

        try {
            int idTransaksi;

            try (PreparedStatement ps = conn.prepareStatement(
                    sqlTransaksi,
                    java.sql.Statement.RETURN_GENERATED_KEYS)) {

                ps.setLong(1, total);
                ps.setLong(2, bayar);
                ps.setLong(3, kembalian);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException(
                            "ID transaksi tidak berhasil dibuat.");
                    }
                    idTransaksi = rs.getInt(1);
                }
            }

            try (PreparedStatement psDetail =
                    conn.prepareStatement(sqlDetail);
                 PreparedStatement psStok =
                    conn.prepareStatement(sqlStok)) {

                for (ItemKeranjang item : keranjang) {
                    int idProduk = cariIdProduk(conn, item.produk.kode);
                    int jumlah = item.jumlah;
                    long harga = item.produk.harga;
                    long subtotal = harga * jumlah;

                    psStok.setInt(1, jumlah);
                    psStok.setInt(2, idProduk);
                    psStok.setInt(3, jumlah);

                    if (psStok.executeUpdate() != 1) {
                        throw new SQLException(
                            "Stok tidak cukup untuk " + item.produk.nama);
                    }

                    psDetail.setInt(1, idTransaksi);
                    psDetail.setInt(2, idProduk);
                    psDetail.setInt(3, jumlah);
                    psDetail.setLong(4, harga);
                    psDetail.setLong(5, subtotal);
                    psDetail.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException | RuntimeException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}

private int cariIdProduk(Connection conn, String kodeProduk)
        throws SQLException {

    String sql = "SELECT id_produk FROM produk WHERE kode_produk = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, kodeProduk);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id_produk");
            }
        }
    }

    throw new SQLException("Produk tidak ditemukan: " + kodeProduk);
}
}