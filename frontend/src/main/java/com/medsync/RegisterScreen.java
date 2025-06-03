package com.medsync;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterScreen extends JFrame {
    public RegisterScreen() {
        setTitle("Medsync - Cadastro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(230, 240, 255)); // Light blue background for a medical theme

        JLabel titleLabel = new JLabel("Medsync - Cadastro");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 24)); // Use JetBrains Mono font for the title
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20)); // Add spacing below the title

        JLabel nameLabel = new JLabel("Nome:");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, emailField.getPreferredSize().height));

        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));

        JLabel confirmPasswordLabel = new JLabel("Confirmar Senha:");
        confirmPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmPasswordLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, confirmPasswordField.getPreferredSize().height));

        JLabel birthDateLabel = new JLabel("Data de Nascimento:");
        birthDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        birthDateLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JSpinner birthDateSpinner = new JSpinner(new SpinnerDateModel());
        birthDateSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, birthDateSpinner.getPreferredSize().height));
        birthDateSpinner.setEditor(new JSpinner.DateEditor(birthDateSpinner, "dd/MM/yyyy"));

        JLabel genderLabel = new JLabel("Gênero:");
        genderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        genderLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Masculino", "Feminino", "Outros"});
        genderComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, genderComboBox.getPreferredSize().height));

        JLabel bloodTypeLabel = new JLabel("Tipo Sanguíneo:");
        bloodTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bloodTypeLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JComboBox<String> bloodTypeComboBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodTypeComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, bloodTypeComboBox.getPreferredSize().height));

        JLabel heightLabel = new JLabel("Altura (cm):");
        heightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        heightLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JTextField heightField = new JTextField();
        heightField.setMaximumSize(new Dimension(Integer.MAX_VALUE, heightField.getPreferredSize().height));

        JLabel weightLabel = new JLabel("Peso (kg):");
        weightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        weightLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JTextField weightField = new JTextField();
        weightField.setMaximumSize(new Dimension(Integer.MAX_VALUE, weightField.getPreferredSize().height));

        JLabel chronicDiseasesLabel = new JLabel("Doenças Crônicas:");
        chronicDiseasesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chronicDiseasesLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JTextArea chronicDiseasesArea = new JTextArea();
        chronicDiseasesArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel allergiesLabel = new JLabel("Alergias:");
        allergiesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        allergiesLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JTextArea allergiesArea = new JTextArea();
        allergiesArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel medicationsLabel = new JLabel("Medicamentos:");
        medicationsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicationsLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JTextArea medicationsArea = new JTextArea();
        medicationsArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel emergencyContactNameLabel = new JLabel("Nome do Contato de Emergência:");
        emergencyContactNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emergencyContactNameLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JTextField emergencyContactNameField = new JTextField();
        emergencyContactNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, emergencyContactNameField.getPreferredSize().height));

        JLabel emergencyContactPhoneLabel = new JLabel("Telefone do Contato de Emergência:");
        emergencyContactPhoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emergencyContactPhoneLabel.setFont(new Font("Inter", Font.PLAIN, 16)); // Use Inter font for labels
        JTextField emergencyContactPhoneField = new JTextField();
        emergencyContactPhoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, emergencyContactPhoneField.getPreferredSize().height));

        JButton registerButton = new JButton("Cadastrar");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for buttons
        registerButton.setBackground(new Color(40, 167, 69)); // Green button for register
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener((ActionEvent e) -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String birthDate = new SimpleDateFormat("yyyy-MM-dd").format((Date) birthDateSpinner.getValue());
            String gender = (String) genderComboBox.getSelectedItem();
            String bloodType = (String) bloodTypeComboBox.getSelectedItem();
            String height = heightField.getText();
            String weight = weightField.getText();
            String chronicDiseases = chronicDiseasesArea.getText();
            String allergies = allergiesArea.getText();
            String medications = medicationsArea.getText();
            String emergencyContactName = emergencyContactNameField.getText();
            String emergencyContactPhone = emergencyContactPhoneField.getText();

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                URL url = new URL("http://localhost:8080/users/register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonPayload = String.format(
                    "{\"nome\":\"%s\",\"email\":\"%s\",\"senhaHash\":\"%s\",\"dataNascimento\":\"%s\",\"genero\":\"%s\",\"tipoSanguineo\":\"%s\",\"alturaCm\":%s,\"pesoKg\":%s,\"doencasCronicas\":\"%s\",\"alergias\":\"%s\",\"medicamentos\":\"%s\",\"contatoEmergenciaNome\":\"%s\",\"contatoEmergenciaTelefone\":\"%s\"}",
                    name, email, password, birthDate, gender, bloodType, height, weight, chronicDiseases, allergies, medications, emergencyContactName, emergencyContactPhone
                );

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonPayload.getBytes());
                    os.flush();
                }

                if (conn.getResponseCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + conn.getResponseMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = new JButton("Voltar");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for buttons
        backButton.setBackground(new Color(220, 53, 69)); // Red button for back
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener((ActionEvent e) -> {
            new LoginScreen().setVisible(true);
            this.dispose();
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(birthDateLabel);
        panel.add(birthDateSpinner);
        panel.add(Box.createVerticalStrut(10));
        panel.add(genderLabel);
        panel.add(genderComboBox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(bloodTypeLabel);
        panel.add(bloodTypeComboBox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(heightLabel);
        panel.add(heightField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(weightLabel);
        panel.add(weightField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(chronicDiseasesLabel);
        panel.add(new JScrollPane(chronicDiseasesArea));
        panel.add(Box.createVerticalStrut(10));
        panel.add(allergiesLabel);
        panel.add(new JScrollPane(allergiesArea));
        panel.add(Box.createVerticalStrut(10));
        panel.add(medicationsLabel);
        panel.add(new JScrollPane(medicationsArea));
        panel.add(Box.createVerticalStrut(10));
        panel.add(emergencyContactNameLabel);
        panel.add(emergencyContactNameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(emergencyContactPhoneLabel);
        panel.add(emergencyContactPhoneField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(registerButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(backButton);

        add(panel);
        pack(); // Dynamically adjust the window size
    }
}
