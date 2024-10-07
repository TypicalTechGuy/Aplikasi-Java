import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InputDataPembeli extends JFrame{
    final private Font mainFont = new Font("Consolas", Font.BOLD, 14);
    private Connection connection;
    public InputDataPembeli() {
        setTitle("Input Data Pembeli");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new FlowLayout());
        panel.setLayout(new GridLayout(20,2));
        
        JLabel NIKlabel = new JLabel("NIK (16 Angka): ");
        NIKlabel.setFont(mainFont);
        JTextField NIKtf = new JTextField(20);
        NIKtf.setFont(mainFont);

        JLabel NamaLabel = new JLabel("Nama Lengkap: ");
        NamaLabel.setFont(mainFont);
        JTextField Namatf = new JTextField(20);
        Namatf.setFont(mainFont);

        JLabel TTLlabel = new JLabel("Tempat Tanggal Lahir: ");
        TTLlabel.setFont(mainFont);
        JTextField TTLtf = new JTextField(20);
        TTLtf.setFont(mainFont);

        JLabel JenisKelaminlabel = new JLabel("Jenis Kelamin: ");
        JenisKelaminlabel.setFont(mainFont);
        String[] genders = {"Laki-Laki", "Perempuan"};
        JComboBox<String> JenisKelamincb = new JComboBox<>(genders);

        JLabel Alamatlabel = new JLabel("Alamat: ");
        Alamatlabel.setFont(mainFont);
        JTextField Alamattf = new JTextField(20);
        Alamattf.setFont(mainFont);

        JLabel Agamalabel = new JLabel("Agama: ");
        Agamalabel.setFont(mainFont);
        String[] agama = {"Islam","Kristen", "Katolik", "Hindu", "Buddha", "Khonghucu"};
        JComboBox<String> Agamacb = new JComboBox<>(agama);
        Agamacb.setFont(mainFont);

        JLabel Nikahlabel = new JLabel("Status Nikah: ");
        Nikahlabel.setFont(mainFont);
        String[] nikah = {"Belum Kawin", "Kawin", "Cerai Hidup", "Cerai Mati"};
        JComboBox<String> nikahcb = new JComboBox<>(nikah);
        nikahcb.setFont(mainFont);

        JLabel Pekerjaanlabel = new JLabel("Pekerjaan: ");
        Pekerjaanlabel.setFont(mainFont);
        JTextField Pekerjaantf = new JTextField(20);
        Pekerjaantf.setFont(mainFont);

        JLabel Kewarganegaraanlabel = new JLabel("Kewarganegaraan: ");
        Kewarganegaraanlabel.setFont(mainFont);
        String[] kewarganegaraan = {"WNI", "WNA"};
        JComboBox<String> Kewarganegaraancb = new JComboBox<>(kewarganegaraan);
        Kewarganegaraancb.setFont(mainFont);

        JLabel spacingLabel = new JLabel("");
        spacingLabel.setPreferredSize(new Dimension(10, 10));

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(mainFont);
        submitButton.addActionListener(e -> {
            saveDataToDatabase(NIKtf.getText(), Namatf.getText(), TTLtf.getText(), (String) JenisKelamincb.getSelectedItem(),
            Alamattf.getText(), (String) Agamacb.getSelectedItem(), (String) nikahcb.getSelectedItem(),
            Pekerjaantf.getText(), (String) Kewarganegaraancb.getSelectedItem());
        });
        
        panel.add(NIKlabel);
        panel.add(NIKtf);
        panel.add(NamaLabel);
        panel.add(Namatf);
        panel.add(TTLlabel);
        panel.add(TTLtf);
        panel.add(JenisKelaminlabel);
        panel.add(JenisKelamincb);
        panel.add(Alamatlabel);
        panel.add(Alamattf);
        panel.add(Agamalabel);
        panel.add(Agamacb);
        panel.add(Nikahlabel);
        panel.add(nikahcb);
        panel.add(Pekerjaanlabel);
        panel.add(Pekerjaantf);
        panel.add(Kewarganegaraanlabel);
        panel.add(Kewarganegaraancb);
        panel.add(spacingLabel);
        panel.add(submitButton);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

        private void saveDataToDatabase(String nik, String nama, String ttl, String jenisKelamin, String alamat,
                                    String agama, String statusNikah, String pekerjaan, String kewarganegaraan) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;", "Fikhri", "Fikhri12");
            }

            String sql = "INSERT INTO Data_Pembeli (NIK, Nama, TTL, Jenis_Kelamin, Alamat, Agama, Status_Nikah, Pekerjaan, Kewarganegaraan) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nik);
            statement.setString(2, nama);
            statement.setString(3, ttl);
            statement.setString(4, jenisKelamin);
            statement.setString(5, alamat);
            statement.setString(6, agama);
            statement.setString(7, statusNikah);
            statement.setString(8, pekerjaan);
            statement.setString(9, kewarganegaraan);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data saved successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving data to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InputDataPembeli inputDataPembeli = new InputDataPembeli();
            inputDataPembeli.setVisible(true);
        });
    }
}
