package Class;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TataUsaha extends Staff {
    public TataUsaha(String nama, String no_Tlp, String e_mail, int id_Pegawai, String profesi, LocalDate tanggal_Masuk) {
        super(nama, no_Tlp, e_mail, id_Pegawai, profesi, "Tata Usaha", tanggal_Masuk);
    }

    public TataUsaha() {
        super("", "", "", 0, "", "Tata Usaha", LocalDate.now());
    }

    public static void ListAdminTataUsaha() {
        String link = "jdbc:sqlite:Database.db";
        String sql = "SELECT * FROM Pegawai WHERE profesi = ?";
        try (java.sql.Connection con = java.sql.DriverManager.getConnection(link);
             java.sql.PreparedStatement prstm = con.prepareStatement(sql)) {
            prstm.setString(1, "Tata Usaha");
            java.sql.ResultSet rs = prstm.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("ID: " + rs.getInt("idPegawai"));
                System.out.println("Nama: " + rs.getString("nama"));
                System.out.println("No Telp: " + rs.getString("noTelp"));
                System.out.println("Email: " + rs.getString("email"));
                String tanggalDb = rs.getString("tanggalMasuk");
                LocalDate tanggalMasuk = LocalDate.parse(tanggalDb); 
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                System.out.println("Tanggal Masuk: " + tanggalMasuk.format(formatter));
                System.out.println("Profesi: " + rs.getString("profesi"));
                System.out.println("===============================================================");
            }
            if (count == 0) {
                System.out.println("Tidak ada data Tata Usaha ditemukan di database.");
            }
        } catch (Exception e) {
            System.out.println("Error saat mengambil data Tata Usaha: " + e.getMessage());
        }
    }

    public static void ListUserTataUsaha(int idPegawai, int jumlahHariMasuk) {
        String link = "jdbc:sqlite:Database.db";
        String sql = "SELECT * FROM Pegawai WHERE profesi = ? AND idPegawai = ?";
        try (java.sql.Connection con = java.sql.DriverManager.getConnection(link);
             java.sql.PreparedStatement prstm = con.prepareStatement(sql)) {
            prstm.setString(1, "Tata Usaha");
            prstm.setInt(2, idPegawai);
            java.sql.ResultSet rs = prstm.executeQuery();
            if (rs.next()) {
                System.out.println("===============================================================");
                System.out.println("ID: " + rs.getInt("idPegawai"));
                System.out.println("Nama: " + rs.getString("nama"));
                System.out.println("No Telp: " + rs.getString("noTelp"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Profesi: " + rs.getString("profesi"));
                double gaji = Interfaces.hitungGaji.hitungGaji_tataUsaha();
                double tunjangan = Interfaces.hitungTunjangan.hitungTunjangan_tataUsaha();
                double uangMakan = Interfaces.hitungUangMakan.hitungTotalUangMakan(jumlahHariMasuk);
                double totalGaji = gaji + tunjangan + uangMakan;
                System.out.println("Gaji: " + FormatNominal.rupiah(gaji));
                System.out.println("Tunjangan: " + FormatNominal.rupiah(tunjangan));
                System.out.println("Uang Makan (" + jumlahHariMasuk + " hari): " + FormatNominal.rupiah(uangMakan));
                System.out.println("Total Gaji: " + FormatNominal.rupiah(totalGaji));
                System.out.println("===============================================================");
            } else {
                System.out.println("Data Tata Usaha dengan ID " + idPegawai + " tidak ditemukan.");
            }
        } catch (Exception e) {
            System.out.println("Error saat mengambil data Tata Usaha: " + e.getMessage());
        }
    }

    public static void deleteTataUsaha(String profesi, int idPegawai) {
        String link = "jdbc:sqlite:Database.db";
        String sql = "DELETE FROM Pegawai WHERE profesi = ? AND idPegawai = ?";
        try (java.sql.Connection con = java.sql.DriverManager.getConnection(link);
             java.sql.PreparedStatement prstm = con.prepareStatement(sql)) {
            prstm.setString(1, profesi);
            prstm.setInt(2, idPegawai);
            int affectedRows = prstm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Data Tata Usaha dengan ID " + idPegawai + " berhasil dihapus.");
            } else {
                System.out.println("Data Tata Usaha dengan ID " + idPegawai + " tidak ditemukan.");
            }
        } catch (Exception e) {
            System.out.println("Error saat menghapus data Tata Usaha: " + e.getMessage());
        }
    }
}
