import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableMotor extends JFrame {
    private JTable motorTable;

    public TableMotor() {
        setTitle("Stock Motor");
        setSize(580, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createEmptyBorderPanel(), BorderLayout.CENTER);
    }

    private JPanel createEmptyBorderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        motorTable = new JTable();
        panel.add(new JScrollPane(motorTable), BorderLayout.CENTER);

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
        JButton inputButton = new JButton("Input Stock Motor");
        inputButton.addActionListener(e -> {
            InputStockMotor inMotor = new InputStockMotor();
            inMotor.setVisible(true);
        });
        JButton editMotor = new JButton("Edit Stock Motor");
        editMotor.addActionListener(e -> {
           EditStockMotor editDataMotor = new EditStockMotor(); 
           editDataMotor.setVisible(true);
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(okButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(inputButton);
        buttonPanel.add(editMotor);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        try {
            populateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return panel;
    }

    private void populateTable() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;";
        String username = "Fikhri";
        String password = "Fikhri12";
        Connection connection = DriverManager.getConnection(url, username, password);

        Statement statement = (Statement) connection.createStatement();
        String sqlQuery = "SELECT * FROM Stock_Motor";
        ResultSet result = ((java.sql.Statement) statement).executeQuery(sqlQuery);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("No.");
        tableModel.addColumn("Jenis Motor");
        tableModel.addColumn("Kuantitas");
        tableModel.addColumn("Harga");
        int rowNum = 1;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        while (result.next()) {
            String jenismotor = result.getString("JenisMotor");
            int kuantitas = result.getInt("Kuantitas");
            double harga = result.getDouble("Harga");

            tableModel.addRow(new Object[]{rowNum++,jenismotor, kuantitas, decimalFormat.format(harga)});
        }

        motorTable.setModel(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        motorTable.setDefaultRenderer(Object.class, centerRenderer);

        TableColumnModel columnModel = motorTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);

    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TableMotor tableMotor = new TableMotor();
            tableMotor.setVisible(true);
        });
    }
}
