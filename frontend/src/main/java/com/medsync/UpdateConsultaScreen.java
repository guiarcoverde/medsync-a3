package com.medsync;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateConsultaScreen extends JFrame {
    private final String token;
    private final ConsultaScreen consultaScreen;
    private final long consultaId;

    public UpdateConsultaScreen(String token, ConsultaScreen consultaScreen, long consultaId) {
        this.token = token;
        this.consultaScreen = consultaScreen;
        this.consultaId = consultaId;

        setTitle("Atualizar Consulta");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel dataLabel = new JLabel("Data (dd/MM/yyyy):");
        JSpinner dataSpinner = new JSpinner(new SpinnerDateModel());
        dataSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, dataSpinner.getPreferredSize().height));
        dataSpinner.setEditor(new JSpinner.DateEditor(dataSpinner, "dd/MM/yyyy"));

        JLabel horaLabel = new JLabel("Hora (HH:mm):");
        JSpinner horaSpinner = new JSpinner(new SpinnerDateModel());
        horaSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, horaSpinner.getPreferredSize().height));
        horaSpinner.setEditor(new JSpinner.DateEditor(horaSpinner, "HH:mm"));

        JButton updateButton = new JButton("Atualizar Consulta");
        updateButton.addActionListener((ActionEvent e) -> {
            String data = new SimpleDateFormat("yyyy-MM-dd").format((Date) dataSpinner.getValue());
            String hora = new SimpleDateFormat("HH:mm").format((Date) horaSpinner.getValue());

            try {
                URL url = new URL("http://localhost:8080/consultas/" + consultaId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setDoOutput(true);

                String jsonPayload = String.format(
                    "{\"data\":\"%s\",\"hora\":\"%s\"}",
                    data, hora
                );

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonPayload.getBytes());
                    os.flush();
                }

                if (conn.getResponseCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Consulta atualizada com sucesso!");
                    consultaScreen.loadConsultas(consultaScreen.getTableModel()); // Refresh the table
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar consulta: " + conn.getResponseMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(dataLabel);
        panel.add(dataSpinner);
        panel.add(Box.createVerticalStrut(10));
        panel.add(horaLabel);
        panel.add(horaSpinner);
        panel.add(Box.createVerticalStrut(20));
        panel.add(updateButton);

        add(panel);
    }
}
