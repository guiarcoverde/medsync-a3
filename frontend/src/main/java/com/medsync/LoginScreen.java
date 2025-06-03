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

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the label
        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, emailField.getPreferredSize().height));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the label
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton loginButton = new JButton("Login");
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
                    JOptionPane.showMessageDialog(this, "Erro ao realizar login: " + conn.getResponseMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton registerButton = new JButton("Register");
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
