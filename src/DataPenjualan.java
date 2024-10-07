import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataPenjualan extends JFrame {
    private JTable tablePenjualan;

    public DataPenjualan() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Data Pembeli");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createEmptyBorderPanel(), BorderLayout.CENTER);
    }

    private JPanel createEmptyBorderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tablePenjualan = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablePenjualan);
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
        JButton inputButton = new JButton("Input Data Pembelian");
        inputButton.addActionListener(e -> {
            InputDataPenjualan indata = new InputDataPenjualan();
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
        String sqlQuery = "SELECT * FROM Data_Penjualan";
        ResultSet result = ((java.sql.Statement) statement).executeQuery(sqlQuery);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Sales ID");
        tableModel.addColumn("Tanggal Penjualan");
        tableModel.addColumn("Nama Pembeli");
        tableModel.addColumn("Jenis Motor");
        tableModel.addColumn("Kuantitas");
        tableModel.addColumn("Harga");

        while (result.next()) {
            String salesid = result.getString("sales_id");
            String tanggal = result.getString("tanggal_penjualan");
            String namapembeli = result.getString("namapembeli");
            String jenismotor = result.getString("jenismotor");
            String kuantitas = result.getString("kuantitas");
            String harga = result.getString("harga");
            tableModel.addRow(new Object[]{salesid, tanggal, namapembeli, jenismotor, kuantitas, harga});
        }

        tablePenjualan.setModel(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablePenjualan.setDefaultRenderer(Object.class, centerRenderer);

        tablePenjualan.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablePenjualan.getColumnModel().getColumn(1).setPreferredWidth(50);
        tablePenjualan.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablePenjualan.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablePenjualan.getColumnModel().getColumn(4).setPreferredWidth(40);
        tablePenjualan.getColumnModel().getColumn(5).setPreferredWidth(100);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataPembeli frame = new DataPembeli();
            frame.setVisible(true);
        });
    }
}