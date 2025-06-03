package com.medsync;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdatePersonalInfoScreen extends JFrame {
    private final String token;

    public UpdatePersonalInfoScreen(String token) {
        this.token = token;

        setTitle("Atualizar Informações Pessoais");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(230, 240, 255)); // Light blue background for a medical theme

        JLabel titleLabel = new JLabel("Atualizar Informações Pessoais");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(0, 123, 255));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        JLabel alturaLabel = new JLabel("Altura (cm):");
        alturaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField alturaField = new JTextField();
        alturaField.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaField.getPreferredSize().height));

        JLabel pesoLabel = new JLabel("Peso (kg):");
        pesoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField pesoField = new JTextField();
        pesoField.setMaximumSize(new Dimension(Integer.MAX_VALUE, pesoField.getPreferredSize().height));

        JLabel doencasLabel = new JLabel("Doenças Crônicas:");
        doencasLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea doencasArea = new JTextArea();
        doencasArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel alergiasLabel = new JLabel("Alergias:");
        alergiasLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea alergiasArea = new JTextArea();
        alergiasArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel medicamentosLabel = new JLabel("Medicamentos:");
        medicamentosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea medicamentosArea = new JTextArea();
        medicamentosArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel contatoNomeLabel = new JLabel("Nome do Contato de Emergência:");
        contatoNomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField contatoNomeField = new JTextField();
        contatoNomeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, contatoNomeField.getPreferredSize().height));

        JLabel contatoTelefoneLabel = new JLabel("Telefone do Contato de Emergência:");
        contatoTelefoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField contatoTelefoneField = new JTextField();
        contatoTelefoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, contatoTelefoneField.getPreferredSize().height));

        JButton updateButton = new JButton("Atualizar");
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateButton.setFont(new Font("Inter", Font.PLAIN, 14));
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener((ActionEvent e) -> {
            String altura = alturaField.getText();
            String peso = pesoField.getText();
            String doencas = doencasArea.getText();
            String alergias = alergiasArea.getText();
            String medicamentos = medicamentosArea.getText();
            String contatoNome = contatoNomeField.getText();
            String contatoTelefone = contatoTelefoneField.getText();

            try {
                URL url = new URL("http://localhost:8080/users/update");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setDoOutput(true);

                String jsonPayload = String.format(
                    "{\"alturaCm\":%s,\"pesoKg\":%s,\"doencasCronicas\":\"%s\",\"alergias\":\"%s\",\"medicamentos\":\"%s\",\"contatoEmergenciaNome\":\"%s\",\"contatoEmergenciaTelefone\":\"%s\"}",
                    altura, peso, doencas, alergias, medicamentos, contatoNome, contatoTelefone
                );

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonPayload.getBytes());
                    os.flush();
                }

                if (conn.getResponseCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Informações atualizadas com sucesso!");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar informações: " + conn.getResponseMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(alturaLabel);
        panel.add(alturaField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(pesoLabel);
        panel.add(pesoField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(doencasLabel);
        panel.add(new JScrollPane(doencasArea));
        panel.add(Box.createVerticalStrut(10));
        panel.add(alergiasLabel);
        panel.add(new JScrollPane(alergiasArea));
        panel.add(Box.createVerticalStrut(10));
        panel.add(medicamentosLabel);
        panel.add(new JScrollPane(medicamentosArea));
        panel.add(Box.createVerticalStrut(10));
        panel.add(contatoNomeLabel);
        panel.add(contatoNomeField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(contatoTelefoneLabel);
        panel.add(contatoTelefoneField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(updateButton);

        add(panel);
        pack(); // Automatically adjust the window size based on content
    }
}
