package com.medsync;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ConsultaScreen extends JFrame {
    private final String token;
    private final DefaultTableModel tableModel;

    public ConsultaScreen(String token) {
        this.token = token;
        setTitle("Medsync - Consultas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(230, 240, 255)); // Light blue background for a medical theme

        JLabel titleLabel = new JLabel("Consultas");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 24)); // Use JetBrains Mono font for the title
        titleLabel.setForeground(new Color(0, 123, 255)); // Blue text for the title

        String[] columnNames = {"ID", "Nome Médico", "Especialidade", "Data", "Hora", "Recorrente", "Observações"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable consultasTable = new JTable(tableModel);
        consultasTable.setFillsViewportHeight(true);
        consultasTable.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for table content
        consultasTable.setRowHeight(25); // Increase row height for better readability
        consultasTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 14)); // Bold header font
        consultasTable.getTableHeader().setBackground(new Color(0, 123, 255)); // Blue header background
        consultasTable.getTableHeader().setForeground(Color.WHITE); // White header text

        // Hide the ID column
        consultasTable.getColumnModel().getColumn(0).setMinWidth(0);
        consultasTable.getColumnModel().getColumn(0).setMaxWidth(0);
        consultasTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(consultasTable);
        scrollPane.setPreferredSize(new Dimension(750, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 2)); // Add border to the table

        JButton newConsultaButton = new JButton("Nova Consulta");
        newConsultaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newConsultaButton.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for buttons
        newConsultaButton.setBackground(new Color(40, 167, 69)); // Green button for new appointment
        newConsultaButton.setForeground(Color.WHITE);
        newConsultaButton.setFocusPainted(false);
        newConsultaButton.addActionListener((ActionEvent e) -> {
            new NovaConsultaScreen(token, this).setVisible(true);
        });

        JButton updateConsultaButton = new JButton("Atualizar Consulta");
        updateConsultaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateConsultaButton.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for buttons
        updateConsultaButton.setBackground(new Color(0, 123, 255)); // Blue button for updating appointment
        updateConsultaButton.setForeground(Color.WHITE);
        updateConsultaButton.setFocusPainted(false);
        updateConsultaButton.addActionListener((ActionEvent e) -> {
            int selectedRow = consultasTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma consulta para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                long consultaId = Long.parseLong(tableModel.getValueAt(selectedRow, 0).toString()); // ID is still retrievable
                new UpdateConsultaScreen(token, this, consultaId).setVisible(true);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao obter o ID da consulta. Certifique-se de que a tabela contém o ID.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton updatePersonalInfoButton = new JButton("Atualizar informações pessoais");
        updatePersonalInfoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updatePersonalInfoButton.setFont(new Font("Inter", Font.PLAIN, 14)); // Use Inter font for buttons
        updatePersonalInfoButton.setBackground(new Color(255, 193, 7)); // Yellow button for updating personal info
        updatePersonalInfoButton.setForeground(Color.WHITE);
        updatePersonalInfoButton.setFocusPainted(false);
        updatePersonalInfoButton.addActionListener((ActionEvent e) -> {
            new UpdatePersonalInfoScreen(token).setVisible(true);
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(10));
        panel.add(newConsultaButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(updateConsultaButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(updatePersonalInfoButton);

        add(panel);

        loadConsultas(tableModel);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void loadConsultas(DefaultTableModel tableModel) {
        try {
            // Clear the table to avoid duplication
            tableModel.setRowCount(0);

            URL url = new URL("http://localhost:8080/consultas/ver-consultas");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            if (conn.getResponseCode() == 200) {
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    String response = scanner.useDelimiter("\\A").next();
                    JSONArray consultasArray = new JSONArray(response);

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

                    for (int i = 0; i < consultasArray.length(); i++) {
                        JSONObject consulta = consultasArray.getJSONObject(i);

                        // Parse and format date and time
                        String formattedDate = dateFormatter.format(Date.from(java.time.LocalDate.parse(consulta.getString("data")).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
                        String formattedTime = timeFormatter.format(Date.from(java.time.LocalTime.parse(consulta.getString("hora")).atDate(java.time.LocalDate.now()).atZone(java.time.ZoneId.systemDefault()).toInstant()));

                        Object[] rowData = {
                            consulta.getLong("id"), // Add ID to the row data
                            consulta.getString("nomeMedico"),
                            consulta.getString("especialidade"),
                            formattedDate,
                            formattedTime,
                            consulta.getBoolean("ehRecorrente") ? "Sim" : "Não",
                            consulta.optString("observacoes", "")
                        };
                        tableModel.addRow(rowData);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao carregar consultas: " + conn.getResponseMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
