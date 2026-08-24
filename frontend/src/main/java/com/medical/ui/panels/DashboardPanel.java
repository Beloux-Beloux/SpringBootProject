package com.medical.ui.panels;

import com.medical.ui.utils.ApiClient;
import com.medical.ui.utils.UiStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private final ApiClient api;
    private final JLabel medecinsVal = new JLabel("...");
    private final JLabel patientsVal = new JLabel("...");
    private final JLabel visitesVal = new JLabel("...");
    private final JLabel gradesVal = new JLabel("...");

    public DashboardPanel(ApiClient a) {
        api = a;
        setLayout(new BorderLayout(0, 0));
        setBackground(UiStyle.SURFACE);
        setBorder(new EmptyBorder(28, 32, 28, 32));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(UiStyle.title("Tableau de bord"));
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(UiStyle.muted("Aperçu général du centre médical"));
        header.add(titleBlock, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Stats cards grid
        JPanel cardsGrid = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsGrid.setOpaque(false);
        cardsGrid.setBorder(new EmptyBorder(28, 0, 0, 0));

        cardsGrid.add(createStatsCard("Médecins", medecinsVal, UiStyle.PRIMARY, "Total des médecins enregistrés"));
        cardsGrid.add(createStatsCard("Patients", patientsVal, new Color(139, 92, 246), "Total des patients enregistrés"));
        cardsGrid.add(createStatsCard("Visites", visitesVal, UiStyle.SUCCESS, "Total des consultations"));
        cardsGrid.add(createStatsCard("Grades distincts", gradesVal, UiStyle.WARNING, "Nombre de grades différents"));
        cardsGrid.add(createBlankCard());
        cardsGrid.add(createBlankCard());

        add(cardsGrid, BorderLayout.CENTER);

        loadStats();
    }

    private JPanel createStatsCard(String label, JLabel valueLabel, Color accent, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(8, 6));
        p.setBackground(UiStyle.CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiStyle.BORDER, 1),
            new EmptyBorder(20, 24, 20, 24)
        ));

        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 36f));
        valueLabel.setForeground(accent);

        JLabel lbl = new JLabel(label);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 15f));
        lbl.setForeground(UiStyle.TEXT_PRIMARY);

        JLabel sub = UiStyle.muted(subtitle);

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(valueLabel, BorderLayout.WEST);

        p.add(north, BorderLayout.NORTH);
        p.add(lbl, BorderLayout.CENTER);
        p.add(sub, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createBlankCard() {
        JPanel p = new JPanel();
        p.setBackground(UiStyle.CARD_BG);
        p.setBorder(BorderFactory.createLineBorder(UiStyle.BORDER, 1));
        return p;
    }

    private void loadStats() {
        new SwingWorker<Void, Void>() {
            private long med, pat, vis, grades;
            @Override
            protected Void doInBackground() throws Exception {
                med    = api.get("/api/medecins/count", Long.class);
                pat    = api.get("/api/patients/count", Long.class);
                vis    = api.get("/api/visites/count", Long.class);
                grades = api.get("/api/medecins/count/distinct-grades", Long.class);
                return null;
            }
            @Override
            protected void done() {
                medecinsVal.setText(String.valueOf(med));
                patientsVal.setText(String.valueOf(pat));
                visitesVal.setText(String.valueOf(vis));
                gradesVal.setText(String.valueOf(grades));
            }
        }.execute();
    }
}