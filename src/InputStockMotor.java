import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InputStockMotor extends JFrame{
    final private Font mainFont = new Font("Consolas", Font.BOLD, 14);
    private Connection connection;
    
    public InputStockMotor() {
        setTitle("Input Stock Motor");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new FlowLayout());
        panel.setLayout(new GridLayout(20,2));
        
        JLabel jmlabel = new JLabel("Jenis Motor: ");
        jmlabel.setFont(mainFont);
        JTextField jmtf = new JTextField(20);
        jmtf.setFont(mainFont);

        JLabel ktLabel = new JLabel("Kuantitas: ");
        ktLabel.setFont(mainFont);
        JTextField kttf = new JTextField(20);
        kttf.setFont(mainFont);

        JLabel hlabel = new JLabel("Harga: ");
        hlabel.setFont(mainFont);
        JTextField htf = new JTextField(20);
        htf.setFont(mainFont);

        JLabel spacingLabel = new JLabel("");
        spacingLabel.setPreferredSize(new Dimension(10, 10));

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(mainFont);
        submitButton.addActionListener(e -> {
            saveDataToDatabase(jmtf.getText(), kttf.getText(), htf.getText());
        });
        
        panel.add(jmlabel);
        panel.add(jmtf);
        panel.add(ktLabel);
        panel.add(kttf);
        panel.add(hlabel);
        panel.add(htf);
        panel.add(spacingLabel);
        panel.add(submitButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        
    }

        private void saveDataToDatabase(String JenisMotor, String Kuantitas, String Harga) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;", "Fikhri", "Fikhri12");
            }

            String sql = "INSERT INTO Stock_Motor (JenisMotor, Kuantitas, Harga) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, JenisMotor);
            statement.setString(2, Kuantitas);
            statement.setString(3, Harga);

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