package frameworks;// frameworks/SwingUI.java
import controller.DiagnosisController;
import entities.Diagnosis;
import entities.Gejala;
import entities.Solusi;
import entities.User;
import jdk.jshell.Diag;
import usecases.DiagnoseUseCase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static frameworks.SessionManager.getLoggedInUsers;


public class SwingUI {
    private DatabaseConnector databaseConnector;
    private DiagnoseUseCase diagnoseUseCase;

    public SwingUI() {
        try {
            databaseConnector = new DatabaseConnector();
            diagnoseUseCase = new DiagnoseUseCase(databaseConnector);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showLoginPage() {
        JFrame frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 255, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("Login to Smartphone Expert System");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 144, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(lblTitle, gbc);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Roboto", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(lblUsername, gbc);

        JTextField txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Roboto", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginPanel.add(lblPassword, gbc);

        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginPanel.add(txtPassword, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Roboto", Font.BOLD, 16));
        btnLogin.setForeground(new Color(30, 144, 255));
        btnLogin.setBackground(new Color(30, 144, 255));
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(btnLogin, gbc);

        frame.add(loginPanel);
        frame.setVisible(true);

        // Tombol ke halaman Register
        JButton btnGoToRegister = new JButton("Create Account");
        btnGoToRegister.setFont(new Font("Roboto", Font.BOLD, 12));
        btnGoToRegister.setForeground(new Color(30, 144, 255));
        btnGoToRegister.setBackground(Color.WHITE);
        btnGoToRegister.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginPanel.add(btnGoToRegister, gbc);

        frame.add(loginPanel);
        frame.setVisible(true);
        btnGoToRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);  // Menyembunyikan halaman login
                showRegisterPage();  // Pindah ke halaman Register
            }
        });

        // Login Action
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                try {
                    // Verifikasi login dan mendapatkan objek User
                    User user = databaseConnector.verifikasiLogin(username, password);

                    if (user != null) {
                        // Jika berhasil login, periksa role
                        String role = user.getRole();  // Dapatkan role dari objek User
                        if (role.equals("admin")) {
                            JOptionPane.showMessageDialog(frame, "Login berhasil sebagai Admin: " + username);
                            showAdminPage();  // Pindah ke halaman Admin
                        } else {
                            JOptionPane.showMessageDialog(frame, "Login berhasil sebagai User: " + username);
                            showHomePage(username);  // Pindah ke halaman User
                        }
                        frame.setVisible(false);  // Menyembunyikan halaman login
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username atau password salah!");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Terjadi kesalahan saat login!");
                    ex.printStackTrace();
                }
            }
        });
    }


    // Mendapatkan daftar pengguna yang sedang login (simulasi)
    public List<User> getLoggedInUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "johndoe", "password123", "user"));
        users.add(new User(2, "admin", "adminpass", "admin"));
        users.add(new User(3, "alice", "alice123", "user"));
        return users;
    }




    // Menampilkan halaman Register
    public void showRegisterPage() {
        JFrame frame = new JFrame("Register Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setBackground(new Color(255, 255, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("Register New Account");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 144, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerPanel.add(lblTitle, gbc);

        JLabel lblUsername = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registerPanel.add(lblUsername, gbc);

        JTextField txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registerPanel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        registerPanel.add(lblPassword, gbc);

        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        registerPanel.add(txtPassword, gbc);

        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Roboto", Font.BOLD, 16));
        btnRegister.setForeground(new Color(30, 144, 255));
        btnRegister.setBackground(new Color(30, 144, 255));
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        registerPanel.add(btnRegister, gbc);

        frame.add(registerPanel);
        frame.setVisible(true);

        // Register Action
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                try {
                    // Mendaftar user baru
                    boolean isRegistered = databaseConnector.registerUser(username, password);
                    if (isRegistered) {
                        JOptionPane.showMessageDialog(frame, "Register berhasil! Silakan login.");
                        showLoginPage();  // Pindah ke halaman login setelah register
                        frame.setVisible(false);  // Menyembunyikan halaman register
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username sudah terdaftar, coba username lain.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Terjadi kesalahan saat mendaftar!");
                    ex.printStackTrace();
                }
            }
        });
    }

    // Menampilkan halaman Home setelah login
    public void showHomePage(String username) {
        JFrame frame = new JFrame("Home Page");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // Center the frame

        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(new Color(245, 245, 245));

        JLabel lblWelcome = new JLabel("Welcome, " + username);
        lblWelcome.setFont(new Font("Roboto", Font.BOLD, 24));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDescription = new JLabel("<html><center>Selamat datang di Sistem Pakar Smartphone.<br>Silakan pilih diagnosa untuk melanjutkan.</center></html>");
        lblDescription.setFont(new Font("Roboto", Font.PLAIN, 16));
        lblDescription.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnGoToDiagnose = new JButton("Start Diagnosis");
        btnGoToDiagnose.setFont(new Font("Roboto", Font.BOLD, 16));
        btnGoToDiagnose.setForeground(Color.BLUE);
        btnGoToDiagnose.setBackground(new Color(30, 144, 255));
        btnGoToDiagnose.setFocusPainted(false);
        btnGoToDiagnose.setPreferredSize(new Dimension(200, 40));
        btnGoToDiagnose.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Roboto", Font.BOLD, 16));
        btnLogout.setForeground(Color.BLUE);
        btnLogout.setBackground(Color.RED);
        btnLogout.setFocusPainted(false);
        btnLogout.setPreferredSize(new Dimension(200, 40));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Menambahkan komponen ke panel
        homePanel.add(lblWelcome);
        homePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        homePanel.add(lblDescription);
        homePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        homePanel.add(btnGoToDiagnose);
        homePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        homePanel.add(btnLogout);

        frame.add(homePanel);
        frame.setVisible(true);

        // Tombol Start Diagnosis action
        btnGoToDiagnose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDiagnosisPage(username);  // Pindah ke halaman diagnosa
                frame.setVisible(false);  // Menyembunyikan halaman home
            }
        });

        // Tombol Logout action
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPage();  // Arahkan kembali ke halaman login
                frame.setVisible(false);  // Menyembunyikan halaman home
            }
        });
    }


    public void showDiagnosisPage(String username) {
        // Frame utama untuk halaman Diagnosis
        JFrame frame = new JFrame("Diagnosis Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);  // Center the frame

        // Menginisialisasi CardLayout dan panel yang menampung cards
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout); // Menampung panel-panel berbeda

        // Halaman 1: Keluhan
        JPanel keluhanPanel = new JPanel();
        keluhanPanel.setLayout(new BoxLayout(keluhanPanel, BoxLayout.Y_AXIS));
        keluhanPanel.setBackground(new Color(245, 245, 245));

        JLabel lblKeluhan = new JLabel("Masukkan Keluhan Smartphone:");
        lblKeluhan.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblKeluhan.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea keluhanTextArea = new JTextArea(5, 20);
        keluhanTextArea.setLineWrap(true);
        keluhanTextArea.setWrapStyleWord(true);
        keluhanTextArea.setFont(new Font("Roboto", Font.PLAIN, 14));
        keluhanTextArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        JScrollPane keluhanScroll = new JScrollPane(keluhanTextArea);
        keluhanScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton nextButton1 = new JButton("Next");
        nextButton1.setFont(new Font("Roboto", Font.BOLD, 16));
        nextButton1.setForeground(new Color(30, 144, 255));
        nextButton1.setBackground(new Color(30, 144, 255));
        nextButton1.setFocusPainted(false);
        nextButton1.setPreferredSize(new Dimension(200, 40));

        keluhanPanel.add(lblKeluhan);
        keluhanPanel.add(keluhanScroll);
        keluhanPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        keluhanPanel.add(nextButton1);

        // Halaman 2: Diagnosis
        JPanel diagnosisPanel = new JPanel();
        diagnosisPanel.setLayout(new BoxLayout(diagnosisPanel, BoxLayout.Y_AXIS));
        diagnosisPanel.setBackground(new Color(245, 245, 245));

        JLabel lblDiagnosis = new JLabel("Diagnosis Berdasarkan Keluhan Anda:");
        lblDiagnosis.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblDiagnosis.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea diagnosisTextArea = new JTextArea(5, 20);
        diagnosisTextArea.setLineWrap(true);
        diagnosisTextArea.setWrapStyleWord(true);
        diagnosisTextArea.setFont(new Font("Roboto", Font.PLAIN, 14));
        diagnosisTextArea.setEditable(false);
        diagnosisTextArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        JScrollPane diagnosisScroll = new JScrollPane(diagnosisTextArea);
        diagnosisScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton nextButton2 = new JButton("Next");
        nextButton2.setFont(new Font("Roboto", Font.BOLD, 16));
        nextButton1.setForeground(new Color(30, 144, 255));
        nextButton2.setBackground(new Color(30, 144, 255));
        nextButton2.setFocusPainted(false);
        nextButton2.setPreferredSize(new Dimension(200, 40));

        diagnosisPanel.add(lblDiagnosis);
        diagnosisPanel.add(diagnosisScroll);
        diagnosisPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        diagnosisPanel.add(nextButton2);

        // Halaman 3: Hasil dan Solusi
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(245, 245, 245));

        JLabel lblResult = new JLabel("Hasil Diagnosa dan Solusi:");
        lblResult.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea resultTextArea = new JTextArea(5, 20);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setFont(new Font("Roboto", Font.PLAIN, 14));
        resultTextArea.setEditable(false);
        resultTextArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        JScrollPane resultScroll = new JScrollPane(resultTextArea);
        resultScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTextArea solutionTextArea = new JTextArea(5, 20);
        solutionTextArea.setLineWrap(true);
        solutionTextArea.setWrapStyleWord(true);
        solutionTextArea.setFont(new Font("Roboto", Font.PLAIN, 14));
        solutionTextArea.setEditable(false);
        solutionTextArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        JScrollPane solutionScroll = new JScrollPane(solutionTextArea);
        solutionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton finishButton = new JButton("Finish");
        finishButton.setFont(new Font("Roboto", Font.BOLD, 16));
        finishButton.setForeground(new Color(30, 144, 255));
        finishButton.setBackground(new Color(30, 144, 255));
        finishButton.setFocusPainted(false);
        finishButton.setPreferredSize(new Dimension(200, 40));

        resultPanel.add(lblResult);
        resultPanel.add(resultScroll);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        resultPanel.add(new JLabel("Solusi:"));
        resultPanel.add(solutionScroll);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        resultPanel.add(finishButton);

        // Menambahkan halaman ke dalam CardLayout
        cardPanel.add(keluhanPanel, "Keluhan");
        cardPanel.add(diagnosisPanel, "Diagnosis");
        cardPanel.add(resultPanel, "Result");

        frame.add(cardPanel);
        frame.setVisible(true);

        // Aksi tombol Next pada halaman keluhan
        nextButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keluhan = keluhanTextArea.getText();
                if (keluhan.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Silakan masukkan keluhan terlebih dahulu.");
                    return;
                }

                // Menggunakan diagnoseUseCase untuk diagnosa berdasarkan keluhan
                Diagnosis diagnosis = diagnoseUseCase.diagnose(keluhan);
                diagnosisTextArea.setText(diagnosis.getDiagnosisResult());

                cardLayout.show(cardPanel, "Diagnosis");
            }
        });

        // Aksi tombol Next pada halaman diagnosis
        nextButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keluhan = keluhanTextArea.getText();
                if (keluhan.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Silakan masukkan keluhan terlebih dahulu.");
                    return;
                }

                // Menggunakan diagnoseUseCase untuk diagnosa berdasarkan keluhan
                Diagnosis diagnosis = diagnoseUseCase.diagnose(keluhan);
                diagnosisTextArea.setText(diagnosis.getDiagnosisResult());

                // Menampilkan hasil diagnosa
                resultTextArea.setText(diagnosis.getDiagnosisResult());

                // Menampilkan solusi berdasarkan gejala yang ditemukan
                StringBuilder solusi = new StringBuilder();

                // Mendapatkan gejala-gejala berdasarkan keluhan
                List<Gejala> gejalas = diagnoseUseCase.getGejalasForDiagnosis(keluhan);  // Pastikan ini mengembalikan List<Gejala>

                // Menggunakan for-each untuk mengiterasi List<Gejala>
                for (Gejala gejala : gejalas) {
                    // Ambil solusi berdasarkan gejala ID
                    Solusi sol = null;
                    try {
                        sol = databaseConnector.getSolusiByGejalaId(gejala.getId());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    if (sol != null) {
                        solusi.append(sol.tampilkanSolusi()).append("\n");
                    } else {
                        solusi.append("Solusi tidak ditemukan untuk gejala: ").append(gejala.getNama()).append("\n");
                    }
                }

                // Menampilkan solusi di solutionTextArea
                solutionTextArea.setText(solusi.toString());

                // Menampilkan halaman Result
                cardLayout.show(cardPanel, "Result");
            }
        });



        // Aksi tombol Finish
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Terima kasih telah menggunakan sistem diagnosis kami!");
                frame.setVisible(false);  // Menyembunyikan halaman diagnosis
            }
        });
    }
    public void showAdminPage() {
        JFrame frame = new JFrame("Admin Page");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Tabel untuk menampilkan daftar pengguna yang sedang login
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Role");

        // Mendapatkan daftar pengguna yang sedang login dari SessionManager
        Map<Integer, User> loggedInUsers = SessionManager.getLoggedInUsers();
        for (User user : loggedInUsers.values()) {
            tableModel.addRow(new Object[]{user.getId(), user.getUsername(), user.getRole()});
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button untuk melihat lebih lanjut tentang pengguna
        JButton btnViewDetails = new JButton("View User Details");
        btnViewDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int userId = (int) table.getValueAt(selectedRow, 0);
                    showUserDetails(userId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a user to view details.");
                }
            }
        });

        panel.add(btnViewDetails, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);
    }

    public void showUserDetails(int userId) {
        User selectedUser = SessionManager.getUserById(userId);
        if (selectedUser != null) {
            JOptionPane.showMessageDialog(null,
                    "User Details:\n" +
                            "Username: " + selectedUser.getUsername() + "\n" +
                            "Role: " + selectedUser.getRole() + "\n" +
                            "Last Login: [Simulated Date and Time]");  // Simulasi data waktu login
        }
    }
}


