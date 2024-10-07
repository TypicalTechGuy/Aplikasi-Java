import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends JFrame{
    final private Font mainFont = new Font("Consolas", Font.BOLD, 14);
    JTextField tfNIP, tfPassword;
    
    public void runFrame() {
        //Login Page Panel
        JLabel lbNIP = new JLabel("Nomor Induk Pegawai (NIP)");
        lbNIP.setFont(mainFont);

        tfNIP = new JTextField();
        tfNIP.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(mainFont);

        tfPassword = new JPasswordField();
        tfPassword.setFont(mainFont);

        JLabel lbForgotPass = new JLabel("Lupa Password?");
        lbForgotPass.setFont(mainFont);
        lbForgotPass.setForeground(Color.BLUE);
        lbForgotPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lbForgotPass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginPage.this, "Hubungi administrasi untuk mengubah password anda!",
                 "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(5, 5));
        loginPanel.setOpaque(false);

        loginPanel.add(lbNIP);
        loginPanel.add(tfNIP);
        loginPanel.add(lbPassword);
        loginPanel.add(tfPassword);
        loginPanel.add(lbForgotPass);

        JButton btnOK = new JButton("OK");
        btnOK.setFont(mainFont);
        btnOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String NIP = tfNIP.getText();
                String Password = String.valueOf(((JPasswordField) tfPassword).getPassword());

                User user = getAuthenticatedUser(NIP, Password);

                if (user != null) {
                    JOptionPane.showMessageDialog(LoginPage.this,
                            "Login Berhasil!","Informasi",JOptionPane.INFORMATION_MESSAGE);
                    Dashboard dashBoard = new Dashboard();
                    dashBoard.initialize(user);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this,
                            "NIP/Password Salah!", "Silahkan coba lagi!", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setOpaque(true);
        buttonsPanel.add(btnOK);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(loginPanel, BorderLayout.NORTH);
        mainPanel.add(btnOK, BorderLayout.SOUTH);

        add(mainPanel);

        
        setTitle("Login Page");
        setSize(500,285);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private User getAuthenticatedUser(String NIP, String Password) {
        User user = null;

        final String DB_url = "jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;";
        final String username = "Fikhri";
        final String password = "Fikhri12";

        try (Connection conn = DriverManager.getConnection(DB_url, username, password)) {
            String sql = "SELECT * FROM Data_Pegawai WHERE NIP=? AND Password=?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, NIP);
                preparedStatement.setString(2, Password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        user = new User();
                        user.setNama(resultSet.getString("Nama"));
                        user.setNIP(resultSet.getString("NIP"));
                        user.setPassword(resultSet.getString("Password"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return user;
    }

    public static void main(String[] args) {
        LoginPage myFrame = new LoginPage();
        myFrame.runFrame();
    }
}