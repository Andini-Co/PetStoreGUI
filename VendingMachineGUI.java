import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

//main class
public class VendingMachineGUI extends JFrame {
    private ArrayList<String> products = new ArrayList<>(
            Arrays.asList("Suki Liar", "kungfu panda", "Hamster", "Kelinci", "Burung Hantu",
                    "Suki Liar", "Tupai", "Ular", "Kumbang", "Lebah Ganteng",
                    "mak cik", "Mr ironi", "Si imut", "Badak", "Kucing",
                    "Manuk", "Kodok Berbaju"));
    private ArrayList<Integer> harga = new ArrayList<>(
            Arrays.asList(5000, 6000, 10000, 25000, 12000, 9000, 80000, 45000, 7000, 11000, 15000, 20000, 30000, 35000, 60000, 18000, 22000));
    private ArrayList<Integer> stok = new ArrayList<>(Arrays.asList(5, 6, 8, 10, 15, 20, 12, 9, 7, 4, 11, 3, 9, 14, 6, 13, 8));
    private ArrayList<ImageIcon> productImages;
    private JPanel mainPanel;
    private JPanel productsPanel;
    private JButton adminButton;

    // konstruktor
    public VendingMachineGUI() {
        setTitle("Pet Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 830);
        setResizable(false);

        loadProductImages();

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(0, 0, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("Pet Store", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        productsPanel = new JPanel(new GridLayout(0, 2, 35, 50));
        productsPanel.setBackground(new Color(0, 0, 0));
        productsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 100, 50));

        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(0, 0, 0));

        updateProductCards();

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adminPanel.setBackground(Color.BLACK);
        adminButton = new JButton("Admin");
        styleButton(adminButton);
        adminButton.addActionListener(e -> showAdminLogin());
        adminPanel.add(adminButton);
        mainPanel.add(adminPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    // method untuk load gambar produk dari folder Images (semua foto yang ada akan ditampilkan)
    private void loadProductImages() {
        productImages = new ArrayList<>();
        File imagesDir = new File("Images");
        File[] imageFiles = imagesDir.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                    || lower.endsWith(".png") || lower.endsWith(".gif") || lower.endsWith(".bmp");
        });

        if (imageFiles != null) {
            // urutkan agar urutan tampilan tetap konsisten
            Arrays.sort(imageFiles, (f1, f2) -> f1.getName().compareTo(f2.getName()));

            for (File imageFile : imageFiles) {
                try {
                    ImageIcon originalIcon = new ImageIcon(imageFile.getPath());
                    Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    productImages.add(new ImageIcon(scaledImage));
                } catch (Exception e) {
                    productImages.add(new ImageIcon());
                }
            }
        }

        // pastikan jumlah ikon cukup untuk semua produk (hindari IndexOutOfBounds)
        while (productImages.size() < products.size()) {
            productImages.add(productImages.isEmpty() ? new ImageIcon() : productImages.get(0));
        }
    }

    // method untuk mengatur style button
    private void styleButton(JButton button) {
        button.setBackground(new Color(100, 100, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }

    // method untuk mengupdate product cards
    private void updateProductCards() {
        productsPanel.removeAll();

        for (int i = 0; i < products.size(); i++) {
            JPanel productCard = new JPanel();
            productCard.setLayout(new BoxLayout(productCard, BoxLayout.Y_AXIS));
            productCard.setBackground(new Color(40, 40, 40));
            productCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel imagePanel = new JPanel();
            imagePanel.setBackground(new Color(40, 40, 40));
            imagePanel.add(new JLabel(productImages.get(i)));
            productCard.add(imagePanel);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(new Color(40, 40, 40));

            JLabel nameLabel = new JLabel(products.get(i), SwingConstants.CENTER);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel priceLabel = new JLabel("Rp. " + harga.get(i), SwingConstants.CENTER);
            priceLabel.setForeground(Color.WHITE);
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel stockLabel = new JLabel("Stok: " + stok.get(i), SwingConstants.CENTER);
            stockLabel.setForeground(Color.WHITE);
            stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton buyButton = new JButton("Beli");
            buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            styleButton(buyButton);

            if (stok.get(i) == 0) {
                buyButton.setEnabled(false);
                buyButton.setBackground(new Color(100, 0, 0));
            }

            final int index = i;
            buyButton.addActionListener(e -> handlePurchase(index));

            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            infoPanel.add(priceLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            infoPanel.add(stockLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            infoPanel.add(buyButton);

            productCard.add(infoPanel);
            productsPanel.add(productCard);
        }

        productsPanel.revalidate();
        productsPanel.repaint();
    }

    // method untuk memutar sound effect
    private void playAudio(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing audio: " + e.getMessage());
        }
    }

    // method untuk membeli produk
    private void handlePurchase(int index) {
        if (stok.get(index) == 0) {
            JOptionPane.showMessageDialog(this, "Maaf, produk yang Anda pilih telah habis!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda memilih " + products.get(index) + "\nDengan harga: Rp. " +
                        harga.get(index) + "\nLanjutkan pembelian?",
                "Konfirmasi Pembelian", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            stok.set(index, stok.get(index) - 1);
            updateProductCards();
            playAudio("Images\\vmSound.wav");
            playAudio("Images\\meow.wav");
            JOptionPane.showMessageDialog(this, "Terima kasih atas pembelian Anda!");
        }
    }

    // method untuk menampilkan menu admin
    private void showAdminLogin() {
        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(this, passwordField,
                "Masukkan Password:", JOptionPane.OK_CANCEL_OPTION);
                System.out.println(passwordField.getPassword());
                System.out.println(result);

        if (result == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            if (password.equals("240306")) {
                showMaintenanceMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Password Salah!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // method untuk menampilkan menu maintenance menggunakan JOptionPane
    private void showMaintenanceMenu() {
        String[] options = { "Tambah Stok", "Tambah Jenis Barang", "Ubah Harga", "Kembali" };
        int choice = JOptionPane.showOptionDialog(this, "Pilih Menu Maintenance:",
                "Maintenance Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        switch (choice) {
            case 0:
                showTambahStok();
                break;
            case 1:
                showTambahJenisBarang();
                break;
            case 2:
                showUbahHarga();
                break;
        }
    }

    // method untuk menambah stok
    private void showTambahStok() {
        String[] productNames = products.toArray(new String[0]);
        String selected = (String) JOptionPane.showInputDialog(this,
                "Pilih produk yang ingin ditambah stoknya:",
                "Tambah Stok",
                JOptionPane.QUESTION_MESSAGE,
                null,
                productNames,
                productNames[0]);

        if (selected != null) {
            int index = products.indexOf(selected);
            String input = JOptionPane.showInputDialog(this,
                    "Masukkan jumlah stok yang ingin ditambah untuk " + selected + ":");

            try {
                int tambahan = Integer.parseInt(input);
                if (tambahan < 0) {
                    JOptionPane.showMessageDialog(this, "Jumlah tidak boleh negatif!");
                    return;
                }
                stok.set(index, stok.get(index) + tambahan);
                updateProductCards();
                JOptionPane.showMessageDialog(this, "Stok berhasil ditambah!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Masukkan angka yang valid!");
            }
        }
    }

    // method untuk menambah jenis barang
    private void showTambahJenisBarang() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nama Produk:"));
        panel.add(nameField);
        panel.add(new JLabel("Harga:"));
        panel.add(priceField);
        panel.add(new JLabel("Stok:"));
        panel.add(stockField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Tambah Produk Baru", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                int price = Integer.parseInt(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                if (price < 0 || stock < 0) {
                    JOptionPane.showMessageDialog(this, "Harga dan stok tidak boleh negatif!");
                    return;
                }

                products.add(name);
                harga.add(price);
                stok.add(stock);
                productImages.add(productImages.isEmpty() ? new ImageIcon() : productImages.get(0));
                updateProductCards();
                JOptionPane.showMessageDialog(this, "Produk baru berhasil ditambahkan!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Masukkan angka yang valid untuk harga dan stok!");
            }
        }
    }

    String ixt24 = "twenty four";

    // method untuk mengubah harga
    private void showUbahHarga() {
        String[] productNames = products.toArray(new String[0]);
        String selected = (String) JOptionPane.showInputDialog(this,
                "Pilih produk yang ingin diubah harganya:",
                "Ubah Harga",
                JOptionPane.QUESTION_MESSAGE,
                null,
                productNames,
                productNames[0]);

        if (selected != null) {
            int index = products.indexOf(selected);
            String input = JOptionPane.showInputDialog(this,
                    "Masukkan harga baru untuk " + selected + ":");

            try {
                int hargaBaru = Integer.parseInt(input);
                if (hargaBaru < 0) {
                    JOptionPane.showMessageDialog(this, "Harga tidak boleh negatif!");
                    return;
                }
                harga.set(index, hargaBaru);
                updateProductCards();
                JOptionPane.showMessageDialog(this, "Harga berhasil diubah!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Masukkan angka yang valid!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VendingMachineGUI VM = new VendingMachineGUI();
            VM.setVisible(true);
        });
    }
}
