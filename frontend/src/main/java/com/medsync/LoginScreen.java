package com.medsync;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginScreen extends JFrame {
    public LoginScreen() {
        setTitle("Medsync - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(230, 240, 255)); // Light blue background for a medical theme

        JLabel titleLabel = new JLabel("Medsync");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 24)); // Use JetBrains Mono font for the title
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the title
        titleLabel.setIcon(new ImageIcon("resources/medical-icon.png")); // Add a medical-themed icon
        titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        titleLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20)); // Add spacing below the title

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the label
        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, emailField.getPreferredSize().height));
        emailField.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for text fields

        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the label
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        passwordField.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for text fields

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false); // Make button panel blend with the background

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for buttons
        loginButton.setBackground(new Color(0, 123, 255)); // Blue button for login
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener((ActionEvent e) -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try {
                URL url = new URL("http://localhost:8080/users/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonPayload = String.format("{\"email\":\"%s\",\"senha\":\"%s\"}", email, password);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonPayload.getBytes());
                    os.flush();
                }

                if (conn.getResponseCode() == 200) {
                    try (Scanner scanner = new Scanner(conn.getInputStream())) {
                        String response = scanner.useDelimiter("\\A").next();
                        String token = response.replace("{\"token\": \"", "").replace("\"}", "");
                        new ConsultaScreen(token).setVisible(true);
                        this.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for buttons
        registerButton.setBackground(new Color(40, 167, 69)); // Green button for register
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener((ActionEvent e) -> {
            new RegisterScreen().setVisible(true);
            this.dispose();
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(10)); // Add spacing
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(20)); // Add spacing
        panel.add(buttonPanel);

        add(panel);
    }
}
