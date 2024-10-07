import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataPembeli extends JFrame {
    private JTable tablePembeli;

    public DataPembeli() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Data Pembeli");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createEmptyBorderPanel(), BorderLayout.CENTER);
    }

    private JPanel createEmptyBorderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tablePembeli = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablePembeli);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            try {
                populateTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        JButton inputButton = new JButton("Input Data Pembeli");
        inputButton.addActionListener(e -> {
            InputDataPembeli indata = new InputDataPembeli();
            indata.setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(okButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(inputButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        try {
            populateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        } return panel;
    }


    private void populateTable() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;";
        String username = "Fikhri";
        String password = "Fikhri12";
        Connection connection = DriverManager.getConnection(url, username, password);

        Statement statement = (Statement) connection.createStatement();
        String sqlQuery = "SELECT * FROM Data_Pembeli";
        ResultSet result = ((java.sql.Statement) statement).executeQuery(sqlQuery);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("NIK");
        tableModel.addColumn("Nama");
        tableModel.addColumn("TTL");
        tableModel.addColumn("Jenis Kelamin");
        tableModel.addColumn("Alamat");
        tableModel.addColumn("Agama");
        tableModel.addColumn("Status Nikah");
        tableModel.addColumn("Pekerjaan");
        tableModel.addColumn("Kewarganegaraan");

        while (result.next()) {
            String NIK = result.getString("NIK");
            String Nama = result.getString("Nama");
            String TTL = result.getString("TTL");
            String JenisKelamin = result.getString("Jenis_Kelamin");
            String Alamat = result.getString("Alamat");
            String Agama = result.getString("Agama");
            String StatusNikah = result.getString("Status_Nikah");
            String Pekerjaan = result.getString("Pekerjaan");
            String Kewarganegaraan = result.getString("Kewarganegaraan");
            tableModel.addRow(new Object[]{NIK, Nama, TTL, JenisKelamin, Alamat, Agama, StatusNikah, Pekerjaan, Kewarganegaraan});
        }

        tablePembeli.setModel(tableModel);

        tablePembeli.getColumnModel().getColumn(0).setPreferredWidth(70);
        tablePembeli.getColumnModel().getColumn(1).setPreferredWidth(70);
        tablePembeli.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablePembeli.getColumnModel().getColumn(3).setPreferredWidth(10);
        tablePembeli.getColumnModel().getColumn(4).setPreferredWidth(150);
        tablePembeli.getColumnModel().getColumn(5).setPreferredWidth(10);
        tablePembeli.getColumnModel().getColumn(6).setPreferredWidth(70);
        tablePembeli.getColumnModel().getColumn(7).setPreferredWidth(70);
        tablePembeli.getColumnModel().getColumn(8).setPreferredWidth(70);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tablePembeli.getColumnCount(); i++) {
            tablePembeli.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataPembeli frame = new DataPembeli();
            frame.setVisible(true);
        });
    }
}
