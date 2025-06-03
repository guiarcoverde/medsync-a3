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

        JLabel titleLabel = new JLabel("Consultas");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        String[] columnNames = {"ID", "Nome Médico", "Especialidade", "Data", "Hora", "Recorrente", "Observações"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable consultasTable = new JTable(tableModel);
        consultasTable.setFillsViewportHeight(true);

        // Hide the ID column
        consultasTable.getColumnModel().getColumn(0).setMinWidth(0);
        consultasTable.getColumnModel().getColumn(0).setMaxWidth(0);
        consultasTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(consultasTable);
        scrollPane.setPreferredSize(new Dimension(750, 400));

        JButton newConsultaButton = new JButton("Nova Consulta");
        newConsultaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newConsultaButton.addActionListener((ActionEvent e) -> {
            new NovaConsultaScreen(token, this).setVisible(true);
        });

        JButton updateConsultaButton = new JButton("Atualizar Consulta");
        updateConsultaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(10));
        panel.add(newConsultaButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(updateConsultaButton);

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
