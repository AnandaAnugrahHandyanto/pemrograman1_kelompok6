# 💼 ReservaLink – Aplikasi Booking Aset Sederhana Berbasis Java

**ReservaLink** adalah aplikasi desktop sederhana berbasis **Java (Swing)** yang dirancang untuk memudahkan proses **peminjaman aset** di lingkungan kampus atau organisasi. Aplikasi ini menyajikan antarmuka grafis (GUI) yang intuitif, sehingga pengguna dapat melakukan booking tanpa perlu memahami sistem yang kompleks.

---

## 🎯 Tujuan Aplikasi

- Menyediakan sistem peminjaman aset yang **mudah digunakan** dan **terstruktur**
- Mencegah bentrok jadwal peminjaman dengan sistem pengecekan otomatis
- Memvisualisasikan status aset secara real-time melalui dashboard interaktif

---

## 👥 Target Pengguna

Mahasiswa, dosen, atau staf yang membutuhkan peminjaman aset seperti:
- Proyektor
- Kunci lab
- Kamera dokumentasi
- Ruang kelas

---

## 🛠️ Fitur Utama

| Fitur | Penjelasan |
|-------|------------|
| **Dashboard Ketersediaan** | Menampilkan seluruh aset dan statusnya secara real-time |
| **Pencarian Cepat** | Filter aset berdasarkan nama atau lokasi |
| **Form Booking** | Pemilihan aset, waktu, dan input data peminjam |
| **Validasi Bentrok** | Mengecek apakah aset tersedia di waktu yang dipilih |
| **Pengembalian Aset** | Update status dari "Dipinjam" menjadi "Tersedia" |
| **Feedback Visual** | Warna/peringatan otomatis saat terjadi konflik jadwal |

---

## 🖥️ Teknologi yang Digunakan

- **Java Swing (GUI)**
- **NetBeans IDE**
- **Database**: MySQL
- **JDBC**: Untuk koneksi database
- **JOptionPane & JTable**: Untuk feedback visual dan tampilan data aset

---

## 📐 Desain Antarmuka

Aplikasi dirancang sepenuhnya berbasis **GUI** menggunakan komponen-komponen seperti:

| Komponen | Fungsi |
|----------|--------|
| `JTable` | Menampilkan daftar aset dan statusnya |
| `JComboBox` | Pemilihan aset saat booking |
| `JSpinner` / Date Picker | Pilih tanggal & jam pinjam |
| `JButton` | Booking, pengembalian, cek ketersediaan |
| `JOptionPane` | Notifikasi & peringatan |
| `JTextField` | Input pencarian, nama peminjam |

Desain UI difokuskan agar **mudah digunakan** oleh pengguna awam sekalipun.

---

## 🔁 Alur Penggunaan Singkat

1. Pengguna membuka aplikasi dan melihat ketersediaan aset di dashboard.
2. Menekan tombol **Booking Baru** → mengisi form booking.
3. Sistem mengecek bentrok waktu.
4. Jika tersedia → booking disimpan dan dashboard diperbarui.
5. Aset dikembalikan → status kembali ke "Tersedia".

---

## ✅ Status Implementasi

```bash
[✓] Ide dan fitur utama disusun
[✓] Struktur UI dirancang
[ ] Database disiapkan
[ ] Desain UI selesai
[ ] Coding fitur booking & validasi
[ ] Finalisasi & testing
```