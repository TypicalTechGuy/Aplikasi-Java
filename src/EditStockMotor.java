import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class EditStockMotor extends JFrame {
    private final Font mainFont = new Font("Consolas", Font.BOLD, 14);
    private final JComboBox<String> motorCB = new JComboBox<>();

    public EditStockMotor() {
        setTitle("Edit Stock Motor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 230);
        setResizable(false);
        setLocationRelativeTo(null);

        createEmptyBorderPanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createEmptyBorderPanel(), BorderLayout.CENTER);
    }

    private JPanel createEmptyBorderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 20, 2, 20);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel motorlb = new JLabel("Jenis Motor:");
        motorlb.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(motorlb, gbc);

        populateMotorCB();

        JLabel kuantitaslb = new JLabel("Kuantitas");
        kuantitaslb.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(kuantitaslb, gbc);

        JTextField kuantitastf = new JTextField();
        kuantitastf.setFont(mainFont);
        kuantitastf.setPreferredSize(new Dimension(158, 20));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(kuantitastf, gbc);

        JLabel hargalb = new JLabel("Harga");
        hargalb.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(hargalb, gbc);

        JTextField hargatf = new JTextField(20);
        hargatf.setFont(mainFont);
        hargatf.setPreferredSize(new Dimension(158, 20));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(hargatf, gbc);

        motorCB.setMinimumSize(new Dimension(158, 20));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(motorCB, gbc);

        JButton submitbutton = new JButton("Submit");
        submitbutton.setFont(mainFont);
        submitbutton.addActionListener(e -> {
            String Motor = (String) motorCB.getSelectedItem();
            int quantity = Integer.parseInt(kuantitastf.getText());
            double price = Double.parseDouble(hargatf.getText());

            updateStock(Motor, quantity, price);
        });

        getContentPane().add(submitbutton, BorderLayout.SOUTH);

        return panel;
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

    private void updateStock(String motor, int quantity, double price) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;", "Fikhri", "Fikhri12")) {
            String updateQuery = "UPDATE Stock_Motor SET Kuantitas = ?, Harga = ? WHERE JenisMotor = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, quantity);
                preparedStatement.setDouble(2, price);
                preparedStatement.setString(3, motor);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data saved successfully!");
            }
        }   catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving data to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EditStockMotor frame = new EditStockMotor();
            frame.setVisible(true);
        });
    }
}

