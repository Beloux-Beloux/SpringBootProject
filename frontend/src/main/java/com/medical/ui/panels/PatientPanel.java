package com.medical.ui.panels;

import com.medical.ui.dialogs.PatientDialog;
import com.medical.ui.models.*;
import com.medical.ui.utils.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

public class PatientPanel extends JPanel {

    private final ApiClient api;
    private final PatientTableModel model = new PatientTableModel();
    private final JTable table = new JTable(model);
    private final JLabel status;
    private final JComboBox<String> type = new JComboBox<>(new String[]{"Code", "Nom"});
    private final JTextField searchField = new JTextField();

    public PatientPanel(ApiClient a, JLabel s) {
        api = a;
        status = s;
        setLayout(new BorderLayout(0, 0));
        setBackground(UiStyle.SURFACE);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        add(topSection(), BorderLayout.NORTH);

        UiStyle.table(table);
        table.setAutoCreateRowSorter(true);
        RowActionCell.install(table, 5, this::editRow, this::deleteRow);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UiStyle.BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        add(UiStyle.card(scroll), BorderLayout.CENTER);

        refresh();
    }

    private JPanel topSection() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        // Stats
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(0, 0, 24, 0));
        cards.add(UiStyle.statsCard("Patients", "24", new Color(139, 92, 246)));
        cards.add(UiStyle.statsCard("Hommes", "14", UiStyle.PRIMARY));
        cards.add(UiStyle.statsCard("Femmes", "10", new Color(236, 72, 153)));
        cards.add(UiStyle.statsCard("Visites", "8", UiStyle.SUCCESS));

        JPanel titleRow = new JPanel(new BorderLayout(16, 0));
        titleRow.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(UiStyle.title("Patients"));
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(UiStyle.muted("Recherche par code exact ou nom partiel"));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton addBtn = new JButton("＋ Ajouter un patient");
        JButton refreshBtn = new JButton("↻ Actualiser");
        UiStyle.primaryButton(addBtn);
        UiStyle.secondaryButton(refreshBtn);
        addBtn.addActionListener(e -> add());
        refreshBtn.addActionListener(e -> refresh());
        actions.add(addBtn);
        actions.add(refreshBtn);

        titleRow.add(titlePanel, BorderLayout.WEST);
        titleRow.add(actions, BorderLayout.EAST);

        // Search bar
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchRow.setOpaque(false);
        searchRow.setBorder(new EmptyBorder(16, 0, 16, 0));

        JLabel searchLabel = new JLabel("🔍");
        searchLabel.setFont(searchLabel.getFont().deriveFont(14f));

        UiStyle.searchField(searchField);
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher un patient...");
        UiStyle.comboBox(type);

        JButton searchBtn = new JButton("Rechercher");
        UiStyle.secondaryButton(searchBtn);
        searchBtn.addActionListener(e -> search());

        JButton clearBtn = new JButton("Effacer");
        clearBtn.setForeground(UiStyle.TEXT_SECONDARY);
        clearBtn.setBorder(new EmptyBorder(8, 12, 8, 12));
        clearBtn.setContentAreaFilled(false);
        clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            refresh();
        });

        searchRow.add(searchLabel);
        searchRow.add(searchField);
        searchRow.add(Box.createHorizontalStrut(8));
        searchRow.add(new JLabel("Type :"));
        searchRow.add(type);
        searchRow.add(Box.createHorizontalStrut(8));
        searchRow.add(searchBtn);
        searchRow.add(clearBtn);

        top.add(cards);
        top.add(titleRow);
        top.add(searchRow);
        return top;
    }

    private void refresh() { load("/api/patients"); }

    private void search() {
        if (searchField.getText().isBlank()) { refresh(); return; }
        String q = type.getSelectedItem().toString().equals("Code") ? "code" : "nom";
        load("/api/patients/search?type=" + q + "&value=" + api.encode(searchField.getText().trim()));
    }

    private void load(String path) {
        new SwingWorker<Patient[], Void>() {
            protected Patient[] doInBackground() throws Exception {
                return api.get(path, Patient[].class);
            }
            protected void done() {
                try {
                    model.setData(Arrays.asList(get()));
                    status.setText(" ✓ " + model.getRowCount() + " patient(s) affiché(s)");
                } catch (Exception e) { error(e); }
            }
        }.execute();
    }

    private void add() {
        PatientDialog d = new PatientDialog(SwingUtilities.getWindowAncestor(this), null);
        d.setVisible(true);
        if (!d.isOk()) return;
        run(() -> api.post("/api/patients",
            new Patient(d.code(), d.nom(), d.prenom(), d.sexe(), d.adresse()), Patient.class));
    }

    private void editRow(int row) {
        Patient o = model.getAt(row);
        PatientDialog d = new PatientDialog(SwingUtilities.getWindowAncestor(this), o);
        d.setVisible(true);
        if (!d.isOk()) return;
        run(() -> api.put("/api/patients/" + api.encode(o.codePat()),
            new Patient(d.code(), d.nom(), d.prenom(), d.sexe(), d.adresse()), Patient.class));
    }

    private void deleteRow(int row) {
        Patient x = model.getAt(row);
        int opt = JOptionPane.showConfirmDialog(this,
            "Supprimer le patient « " + x.codePat() + " — " + x.nom() + " » ?",
            "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                api.delete("/api/patients/" + api.encode(x.codePat()));
                return null;
            }
            protected void done() {
                try { get(); refresh(); status.setText(" ✓ Patient supprimé"); }
                catch (Exception e) { error(e); }
            }
        }.execute();
    }

    private interface Task { Object run() throws Exception; }
    private void run(Task t) {
        new SwingWorker<Object, Void>() {
            protected Object doInBackground() throws Exception { return t.run(); }
            protected void done() {
                try { get(); refresh(); status.setText(" ✓ Opération réussie"); }
                catch (Exception e) { error(e); }
            }
        }.execute();
    }

    private void error(Exception e) {
        Throwable x = (e instanceof java.util.concurrent.ExecutionException && e.getCause() != null)
            ? e.getCause() : e;
        status.setText(" ✗ Erreur : " + x.getMessage());
        JOptionPane.showMessageDialog(this, x.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}