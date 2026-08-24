package com.medical.ui.panels;

import com.medical.ui.dialogs.VisiteDialog;
import com.medical.ui.models.*;
import com.medical.ui.utils.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

public class VisitePanel extends JPanel {

    private final ApiClient api;
    private final VisiteTableModel model = new VisiteTableModel();
    private final JTable table = new JTable(model);
    private final JLabel status;
    private final JTextField searchField = new JTextField();
    private final JComboBox<String> searchType = new JComboBox<>(new String[]{
        "CodeMédecin", "CodePatient", "Date", "NomMédecin", "NomPatient"
    });
    private final JLabel visCount = new JLabel("...");
    private final JLabel medCount = new JLabel("...");
    private final JLabel patCount = new JLabel("...");
    private final JLabel monthCount = new JLabel("...");

    public VisitePanel(ApiClient a, JLabel s) {
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
        cards.add(createStatCard("Visites", visCount, UiStyle.SUCCESS));
        cards.add(createStatCard("Médecins", medCount, UiStyle.PRIMARY));
        cards.add(createStatCard("Patients", patCount, new Color(139, 92, 246)));
        cards.add(createStatCard("Ce mois", monthCount, UiStyle.WARNING));

        // Title + buttons
        JPanel titleRow = new JPanel(new BorderLayout(16, 0));
        titleRow.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(UiStyle.title("Visites"));
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(UiStyle.muted("Gérer les visites médicales"));

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
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher une visite...");
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
            long vis, med, pat, month;
            protected Void doInBackground() throws Exception {
                vis   = api.get("/api/visites/count", Long.class);
                med   = api.get("/api/medecins/count", Long.class);
                pat   = api.get("/api/patients/count", Long.class);
                month = api.get("/api/visites/count/current-month", Long.class);
                return null;
            }
            protected void done() {
                visCount.setText(String.valueOf(vis));
                medCount.setText(String.valueOf(med));
                patCount.setText(String.valueOf(pat));
                monthCount.setText(String.valueOf(month));
            }
        }.execute();
    }

    private void refresh() { load("/api/visites"); }

    private void search() {
        String q = searchField.getText().trim();
        if (q.isBlank()) { refresh(); return; }
        String type = switch (searchType.getSelectedItem().toString()) {
            case "CodeMédecin" -> "codemedecin";
            case "CodePatient" -> "codepatient";
            case "Date"        -> "date";
            case "NomMédecin"  -> "nommedecin";
            case "NomPatient"  -> "nompatient";
            default            -> "codemedecin";
        };
        load("/api/visites/search?type=" + type + "&value=" + api.encode(q));
    }

    private void load(String path) {
        new SwingWorker<Visite[], Void>() {
            protected Visite[] doInBackground() throws Exception {
                return api.get(path, Visite[].class);
            }
            protected void done() {
                try {
                    model.setData(Arrays.asList(get()));
                    status.setText(" ✓ " + model.getRowCount() + " visite(s) affichée(s)");
                } catch (Exception e) { error(e); }
            }
        }.execute();
    }

    private void add() {
        VisiteDialog d = new VisiteDialog(SwingUtilities.getWindowAncestor(this), null);
        d.setVisible(true);
        if (!d.isOk()) return;
        run(() -> api.post("/api/visites",
            new Visite(d.med(), d.pat(), d.date(), "", ""), Visite.class));
    }

    private void editRow(int row) {
        Visite o = model.getAt(row);

        VisiteDialog d = new VisiteDialog(SwingUtilities.getWindowAncestor(this), o);
        d.setVisible(true);
        if (!d.isOk()) return;

        StringBuilder changes = new StringBuilder();
        if (!d.med().equals(o.codeMed())) changes.append("• Médecin : ").append(o.codeMed()).append(" → ").append(d.med()).append("\n");
        if (!d.pat().equals(o.codePat())) changes.append("• Patient : ").append(o.codePat()).append(" → ").append(d.pat()).append("\n");
        if (!d.date().equals(o.date())) changes.append("• Date : ").append(o.date()).append(" → ").append(d.date()).append("\n");

        String msg = changes.length() > 0
            ? "Confirmez-vous les modifications de la visite du " + o.date() + " ?\n\n" + changes.toString()
            : "Aucune modification détectée. Enregistrer quand même ?";

        int opt = JOptionPane.showConfirmDialog(this, msg,
            "Confirmer la modification", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;

        run(() -> api.put("/api/visites/" + api.encode(o.codeMed()) + "/" + api.encode(o.codePat()) + "/" + o.date(),
            new Visite(d.med(), d.pat(), d.date(), "", ""), Visite.class));
    }

    private void deleteRow(int row) {
        Visite x = model.getAt(row);
        int opt = JOptionPane.showConfirmDialog(this,
            "Supprimer la visite du " + x.date() + " (" + x.medecinNom() + " → " + x.patientNom() + ") ?",
            "Confirmer la suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                api.delete("/api/visites/" + api.encode(x.codeMed()) + "/" + api.encode(x.codePat()) + "/" + x.date());
                return null;
            }
            protected void done() {
                try { get(); refresh(); loadStats(); status.setText(" ✓ Visite supprimée"); }
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