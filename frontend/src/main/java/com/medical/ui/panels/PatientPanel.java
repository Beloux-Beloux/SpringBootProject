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
    private final JTextField searchField = new JTextField();
    private final JComboBox<String> searchType = new JComboBox<>(new String[]{"Tous", "Code", "Nom", "Prénom", "Sexe", "Adresse"});
    private final JLabel patCount = new JLabel("...");
    private final JLabel visCount = new JLabel("...");
    private final JLabel hommesCount = new JLabel("...");
    private final JLabel femmesCount = new JLabel("...");

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

        loadStats();
        refresh();
    }

    private JPanel topSection() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        // Stats cards
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(0, 0, 24, 0));
        cards.add(createStatCard("Patients", patCount, new Color(139, 92, 246)));
        cards.add(createStatCard("Visites", visCount, UiStyle.SUCCESS));
        cards.add(createStatCard("Hommes", hommesCount, UiStyle.PRIMARY));
        cards.add(createStatCard("Femmes", femmesCount, UiStyle.WARNING));

        // Title + buttons
        JPanel titleRow = new JPanel(new BorderLayout(16, 0));
        titleRow.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(UiStyle.title("Patients"));
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(UiStyle.muted("Gérer les patients du centre médical"));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton addBtn = new JButton("＋ Ajouter");
        JButton refreshBtn = new JButton("↻ Actualiser");
        UiStyle.primaryButton(addBtn);
        UiStyle.secondaryButton(refreshBtn);
        addBtn.addActionListener(e -> add());
        refreshBtn.addActionListener(e -> { refresh(); loadStats(); });
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
        UiStyle.comboBox(searchType);

        JButton searchBtn = new JButton("Rechercher");
        UiStyle.secondaryButton(searchBtn);
        searchBtn.addActionListener(e -> search());

        JButton clearBtn = new JButton("Effacer");
        clearBtn.setForeground(UiStyle.TEXT_SECONDARY);
        clearBtn.setBorder(new EmptyBorder(8, 12, 8, 12));
        clearBtn.setContentAreaFilled(false);
        clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> { searchField.setText(""); refresh(); });

        searchRow.add(searchLabel);
        searchRow.add(searchField);
        searchRow.add(Box.createHorizontalStrut(8));
        searchRow.add(new JLabel("Filtre :"));
        searchRow.add(searchType);
        searchRow.add(Box.createHorizontalStrut(8));
        searchRow.add(searchBtn);
        searchRow.add(clearBtn);

        top.add(cards);
        top.add(titleRow);
        top.add(searchRow);
        return top;
    }

    private JPanel createStatCard(String label, JLabel valueLabel, Color accent) {
        JPanel p = new JPanel(new BorderLayout(8, 4));
        p.setBackground(UiStyle.CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiStyle.BORDER, 1),
            new EmptyBorder(16, 20, 16, 20)
        ));
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 28f));
        valueLabel.setForeground(accent);
        JLabel lbl = new JLabel(label);
        lbl.setForeground(UiStyle.TEXT_SECONDARY);
        lbl.setFont(lbl.getFont().deriveFont(13f));
        p.add(valueLabel, BorderLayout.NORTH);
        p.add(lbl, BorderLayout.SOUTH);
        return p;
    }

    private void loadStats() {
        new SwingWorker<Void, Void>() {
            long pat, vis, h, f;
            protected Void doInBackground() throws Exception {
                pat = api.get("/api/patients/count", Long.class);
                vis = api.get("/api/visites/count", Long.class);
                h   = api.get("/api/patients/count/by-sexe?sexe=M", Long.class);
                f   = api.get("/api/patients/count/by-sexe?sexe=F", Long.class);
                return null;
            }
            protected void done() {
                patCount.setText(String.valueOf(pat));
                visCount.setText(String.valueOf(vis));
                hommesCount.setText(String.valueOf(h));
                femmesCount.setText(String.valueOf(f));
            }
        }.execute();
    }

    private void refresh() { load("/api/patients"); }

    private void search() {
        String q = searchField.getText().trim();
        if (q.isBlank()) { refresh(); return; }
        String type = switch (searchType.getSelectedItem().toString()) {
            case "Code"    -> "code";
            case "Nom"     -> "nom";
            case "Prénom"  -> "prenom";
            case "Sexe"    -> "sexe";
            case "Adresse" -> "adresse";
            default        -> "tous";
        };
        load("/api/patients/search?type=" + type + "&value=" + api.encode(q));
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
        int opt = JOptionPane.showConfirmDialog(this,
            "Modifier le patient « " + o.codePat() + " — " + o.nom() + " " + o.prenom() + " » ?",
            "Confirmer la modification", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;

        PatientDialog d = new PatientDialog(SwingUtilities.getWindowAncestor(this), o);
        d.setVisible(true);
        if (!d.isOk()) return;
        run(() -> api.put("/api/patients/" + api.encode(o.codePat()),
            new Patient(d.code(), d.nom(), d.prenom(), d.sexe(), d.adresse()), Patient.class));
    }

    private void deleteRow(int row) {
        Patient x = model.getAt(row);
        int opt = JOptionPane.showConfirmDialog(this,
            "Supprimer le patient « " + x.codePat() + " — " + x.nom() + " " + x.prenom() + " » ?\n(Blocage si des visites sont associées)",
            "Confirmer la suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                api.delete("/api/patients/" + api.encode(x.codePat()));
                return null;
            }
            protected void done() {
                try { get(); refresh(); loadStats(); status.setText(" ✓ Patient supprimé"); }
                catch (Exception e) { error(e); }
            }
        }.execute();
    }

    private interface Task { Object run() throws Exception; }
    private void run(Task t) {
        new SwingWorker<Object, Void>() {
            protected Object doInBackground() throws Exception { return t.run(); }
            protected void done() {
                try { get(); refresh(); loadStats(); status.setText(" ✓ Opération réussie"); }
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