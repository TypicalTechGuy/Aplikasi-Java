import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalTime;

public class Dashboard extends JFrame{
 
    public void initialize(User user) {
        String greeting;
        LocalTime currTime = LocalTime.now();
        Font mainFont = new Font("Consolas", Font.BOLD, 14);

        if (currTime.isAfter(LocalTime.MIDNIGHT) && currTime.isBefore(LocalTime.NOON)) {
            greeting = "Selamat Pagi";
        } else if (currTime.isAfter(LocalTime.NOON) && currTime.isBefore(LocalTime.of(18, 0))) {
            greeting = "Selamat Siang";
        } else {
            greeting = "Selamat Malam";
        }

        JLabel complabel = new JLabel("PT. XYZ Otomotif");
        complabel.setBounds(254, 156, 141, 19);
        complabel.setFont(mainFont);
        this.add(complabel);

        JLabel greetingLabel = new JLabel("<html>" + greeting + ", " + user.getNama() + "<br>Silahkan klik \"Menu\" untuk mengakses menu</html>");
        greetingLabel.setBounds(174, 172, 301, 55);
        greetingLabel.setFont(mainFont);
        this.add(greetingLabel);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem balance = new JMenuItem("Cek Balance Perusahaan");
        JMenuItem motor = new JMenuItem("Cek Stock Motor");
        JMenuItem pembeli = new JMenuItem("Cek/Input Data Pembeli");
        JMenuItem inPenjualan = new JMenuItem("Cek/Input Data Penjualan Motor");
        JMenuItem exit = new JMenuItem("Exit");

        balance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DatabaseConnection.getConnection();
                    Statement stmt = conn.createStatement()) {
                        String sql = "SELECT balance FROM balance_perusahaan";
                        ResultSet rs = stmt.executeQuery(sql);
                        if (rs.next()) {
                            double balance = rs.getDouble("balance");
                            DecimalFormat df = new DecimalFormat("#,###");
                            String formattedBalance = df.format(balance);
                            JOptionPane.showMessageDialog(Dashboard.this, "Balance Perusahaan: Rp. " + formattedBalance , "Informasi", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(Dashboard.this, "Data not found", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Dashboard.this, "Failed to retrieve balance", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            }
        });

        motor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableMotor tableMotor = new TableMotor();
                tableMotor.setVisible(true);
            }   
        });

        pembeli.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataPembeli dataPembeli = new DataPembeli();
                dataPembeli.setVisible(true);
            }
        });

        inPenjualan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataPenjualan dataPenjualan = new DataPenjualan();
                dataPenjualan.setVisible(true);
            }
            
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
            
        });

        menu.add(balance);
        menu.add(motor);
        menu.add(pembeli);
        menu.add(inPenjualan);
        menu.addSeparator();
        menu.add(exit);

        menuBar.add(menu);

        this.setJMenuBar(menuBar);

        setTitle("Dashboard");
        setSize(650,450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    
        try {
            BufferedImage img = ImageIO.read(new File("src/main/resources/dashBack2.jpg"));
            BufferedImage transparentImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = transparentImg.createGraphics();
            float alpha = 0.75f;
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2d.setComposite(alphaComposite);
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();

            JLabel background = new JLabel(new ImageIcon(transparentImg));
            background.setBounds(0, 0, 650, 450);
            add(background);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setVisible(true);
    }
}
