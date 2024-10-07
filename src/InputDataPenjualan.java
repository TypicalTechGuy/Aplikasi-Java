import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class InputDataPenjualan extends JFrame{
    final private Font mainFont = new Font("Consolas", Font.BOLD, 14);
    private final JComboBox<String> motorCB = new JComboBox<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Connection connection;

    private JTextField kttf;
    private JTextField hargatf;
    
    public InputDataPenjualan() {
        setTitle("Input Data Penjualan");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new FlowLayout());    
        panel.setLayout(new GridLayout(20,2));
        
        JLabel tgllabel = new JLabel("Tanggal Pembelian (YYYY-MM-DD): ");
        tgllabel.setFont(mainFont);
        JFormattedTextField tgltf = new JFormattedTextField(dateFormat);
        tgltf.setFont(mainFont);

        JLabel NamaLabel = new JLabel("Nama Pembeli: ");
        NamaLabel.setFont(mainFont);
        JTextField Namatf = new JTextField(20);
        Namatf.setFont(mainFont);

        JLabel mtrlabel = new JLabel("Jenis Motor: ");
        mtrlabel.setFont(mainFont);

        populateMotorCB();

        JLabel ktlabel = new JLabel("Kuantitas: ");
        ktlabel.setFont(mainFont);
        JTextField kttf = new JTextField();
        kttf.setFont(mainFont);

        kttf.addActionListener(e -> {
            multiplyPriceAndSetTotal();
        });
        
        JLabel hargalabel = new JLabel("Harga ");
        hargalabel.setFont(mainFont);
        JTextField hargatf = new JTextField(20);
        hargatf.setFont(mainFont);

        JLabel spacingLabel = new JLabel("");
        spacingLabel.setPreferredSize(new Dimension(10, 10));

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(mainFont);
        submitButton.addActionListener(e -> {
            saveDataToDatabase(dateFormat.format(tgltf.getValue()), Namatf.getText(), motorCB.getSelectedItem().toString(),     kttf.getText(), hargatf.getText());
        });

        motorCB.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedMotor = motorCB.getSelectedItem().toString();
                    int price = getPriceForMotor(selectedMotor);
                    hargatf.setText(String.valueOf(price));
                }
            }
        });

        panel.add(tgllabel);
        panel.add(tgltf);
        panel.add(NamaLabel);
        panel.add(Namatf);
        panel.add(mtrlabel);
        panel.add(motorCB);
        panel.add(ktlabel);
        panel.add(kttf);
        panel.add(hargalabel);
        panel.add(hargatf);
        panel.add(spacingLabel);
        panel.add(submitButton);

        getContentPane().add(panel, BorderLayout.CENTER);
    }
    
    private int getPriceForMotor(String motor) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;", "Fikhri", "Fikhri12");
            }
    
            String sql = "SELECT Harga FROM Stock_Motor WHERE JenisMotor = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, motor);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Harga");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void multiplyPriceAndSetTotal() {
        String quantityText = kttf.getText();
        if (!quantityText.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityText);
                int price = Integer.parseInt(hargatf.getText());
                int total = quantity * price;
                hargatf.setText(String.valueOf(total));
            } catch (NumberFormatException ex) {

            }
        }
    }

    private void saveDataToDatabase(String tanggal_penjualan, String namapembeli, String jenismotor, String kuantitas, String harga) {
    try {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;", "Fikhri", "Fikhri12");
        }

        String checknama = "SELECT COUNT(*) AS count FROM Data_Pembeli WHERE Nama = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checknama);
        checkStatement.setString(1, namapembeli);
        ResultSet resultSet = checkStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt("count");
        if (count > 0) {
            String getMaxSalesIDSQL = "SELECT MAX(sales_id) AS max_sales_id FROM Data_Penjualan";
            Statement maxSalesIDStatement = connection.createStatement();
            ResultSet maxSalesIDResultSet = maxSalesIDStatement.executeQuery(getMaxSalesIDSQL);
            maxSalesIDResultSet.next();
            int maxSalesID = maxSalesIDResultSet.getInt("max_sales_id");

            int salesID = maxSalesID + 1;

            String sql = "INSERT INTO Data_Penjualan (sales_id, tanggal_penjualan, namapembeli, jenismotor, kuantitas, harga) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, salesID);
            statement.setString(2, tanggal_penjualan);
            statement.setString(3, namapembeli);
            statement.setString(4, jenismotor);
            statement.setString(5, kuantitas);
            statement.setString(6, harga);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data tersimpan!");

            String updatestockSQL = "UPDATE Stock_Motor SET Kuantitas = Kuantitas - ? WHERE JenisMotor = ?";
            PreparedStatement updateStockStatement = connection.prepareStatement(updatestockSQL);
            updateStockStatement.setString(1, kuantitas);
            updateStockStatement.setString(2, jenismotor);
            updateStockStatement.executeUpdate();
    
            int totalHarga = Integer.parseInt(kuantitas) * Integer.parseInt(harga);
            String updateBalanceSql = "UPDATE balance_perusahaan SET balance = balance + ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateBalanceSql);
            updateStatement.setInt(1, totalHarga);
            updateStatement.executeUpdate();
        } else {
            JOptionPane.showMessageDialog(this, "Nama pembeli tidak terdaftar! Harap daftarkan terlebih dahulu!","Informasi" ,JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error saving data to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void populateMotorCB() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;", "Fikhri", "Fikhri12");
        Statement statement = connection.createStatement();
        ResultSet resultset = statement.executeQuery("SELECT JenisMotor FROM Stock_Motor")) {

        Vector<String> dataMotor = new Vector<>();
        while (resultset.next()) {
            dataMotor.add(resultset.getString("JenisMotor"));
        }
        motorCB.setModel(new DefaultComboBoxModel<>(dataMotor));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InputDataPenjualan inputDataPenjualan = new InputDataPenjualan();
            inputDataPenjualan.setVisible(true);
        });
    }
}
