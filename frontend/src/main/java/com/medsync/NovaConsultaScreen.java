package com.medsync;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class NovaConsultaScreen extends JFrame {
    private final String token;
    private final ConsultaScreen consultaScreen;

    public NovaConsultaScreen(String token, ConsultaScreen consultaScreen) {
        this.token = token;
        this.consultaScreen = consultaScreen;

        setTitle("Nova Consulta");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nomeMedicoLabel = new JLabel("Nome do Médico:");
        JTextField nomeMedicoField = new JTextField();
        nomeMedicoField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nomeMedicoField.getPreferredSize().height));

        JLabel especialidadeLabel = new JLabel("Especialidade:");
        JTextField especialidadeField = new JTextField();
        especialidadeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, especialidadeField.getPreferredSize().height));

        JLabel dataLabel = new JLabel("Data (dd/MM/yyyy):");
        dataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JSpinner dataSpinner = new JSpinner(new SpinnerDateModel());
        dataSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, dataSpinner.getPreferredSize().height));
        dataSpinner.setEditor(new JSpinner.DateEditor(dataSpinner, "dd/MM/yyyy"));

        JLabel horaLabel = new JLabel("Hora (HH:mm):");
        horaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JSpinner horaSpinner = new JSpinner(new SpinnerDateModel());
        horaSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, horaSpinner.getPreferredSize().height));
        horaSpinner.setEditor(new JSpinner.DateEditor(horaSpinner, "HH:mm"));

        JLabel ehRecorrenteLabel = new JLabel("Recorrente:");
        JCheckBox ehRecorrenteCheckBox = new JCheckBox();

        JLabel observacoesLabel = new JLabel("Observações:");
        JTextArea observacoesArea = new JTextArea();
        observacoesArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JButton createButton = new JButton("Criar Consulta");
        createButton.addActionListener((ActionEvent e) -> {
            String nomeMedico = nomeMedicoField.getText();
            String especialidade = especialidadeField.getText();
            String data = new SimpleDateFormat("yyyy-MM-dd").format((Date) dataSpinner.getValue());
            String hora = new SimpleDateFormat("HH:mm").format((Date) horaSpinner.getValue());
            boolean ehRecorrente = ehRecorrenteCheckBox.isSelected();
            String observacoes = observacoesArea.getText();

            try {
                URL url = new URL("http://localhost:8080/consultas/create");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setDoOutput(true);

                String jsonPayload = String.format(
                    "{\"nomeMedico\":\"%s\",\"especialidade\":\"%s\",\"data\":\"%s\",\"hora\":\"%s\",\"ehRecorrente\":%b,\"observacoes\":\"%s\"}",
                    nomeMedico, especialidade, data, hora, ehRecorrente, observacoes
                );

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonPayload.getBytes());
                    os.flush();
                }

                if (conn.getResponseCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Consulta criada com sucesso!");
                    consultaScreen.loadConsultas(consultaScreen.getTableModel());
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao criar consulta: " + conn.getResponseMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(nomeMedicoLabel);
        panel.add(nomeMedicoField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(especialidadeLabel);
        panel.add(especialidadeField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(dataLabel);
        panel.add(dataSpinner);
        panel.add(Box.createVerticalStrut(10));
        panel.add(horaLabel);
        panel.add(horaSpinner);
        panel.add(Box.createVerticalStrut(10));
        panel.add(ehRecorrenteLabel);
        panel.add(ehRecorrenteCheckBox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(observacoesLabel);
        panel.add(new JScrollPane(observacoesArea));
        panel.add(Box.createVerticalStrut(20));
        panel.add(createButton);

        add(panel);
    }
}
