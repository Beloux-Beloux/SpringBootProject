package com.medical.ui.panels;

import com.medical.ui.dialogs.MedecinDialog;
import com.medical.ui.models.*;
import com.medical.ui.utils.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

public class MedecinPanel extends JPanel {

    private final ApiClient api;
    private final MedecinTableModel model = new MedecinTableModel();
    private final JTable table = new JTable(model);
    private final JLabel status;
    private final JTextField searchField = new JTextField();
    private final JComboBox<String> gradeFilter = new JComboBox<>(new String[]{"Tous", "Professeur", "Médecin", "Chirurgien", "Généraliste"});

    public MedecinPanel(ApiClient a, JLabel s) {
        api = a;
        status = s;
        setLayout(new BorderLayout(0, 0));
        setBackground(UiStyle.SURFACE);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // Top content
        add(topSection(), BorderLayout.NORTH);

        // Table card
        UiStyle.table(table);
        table.setAutoCreateRowSorter(true);
        RowActionCell.install(table, 4, this::editRow, this::deleteRow);
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

        // Stats cards
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(0, 0, 24, 0));
        cards.add(UiStyle.statsCard("Médecins", "12", UiStyle.PRIMARY));
        cards.add(UiStyle.statsCard("Visites", "8", UiStyle.SUCCESS));
        cards.add(UiStyle.statsCard("Grades", "4", UiStyle.WARNING));
        cards.add(UiStyle.statsCard("Actifs", "10", new Color(139, 92, 246)));

        // Title + actions
        JPanel titleRow = new JPanel(new BorderLayout(16, 0));
        titleRow.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(UiStyle.title("Médecins"));
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(UiStyle.muted("Gérer les médecins et leurs informations"));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton addBtn = new JButton("＋ Ajouter un médecin");
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
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher un médecin...");

        UiStyle.comboBox(gradeFilter);

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
        searchRow.add(new JLabel("Grade :"));
        searchRow.add(gradeFilter);
        searchRow.add(Box.createHorizontalStrut(8));
        searchRow.add(searchBtn);
        searchRow.add(clearBtn);

        top.add(cards);
        top.add(titleRow);
        top.add(searchRow);
        return top;
    }

    private void refresh() {
        new SwingWorker<Medecin[], Void>() {
            protected Medecin[] doInBackground() throws Exception {
                return api.get("/api/medecins", Medecin[].class);
            }
            protected void done() {
                try {
                    model.setData(Arrays.asList(get()));
                    status.setText(" ✓ " + model.getRowCount() + " médecin(s) affiché(s)");
                } catch (Exception e) { error(e); }
            }
        }.execute();
    }

    private void search() {
        // Filter locally from already loaded data
        // (backend search endpoint can be added later)
        refresh();
    }

    private void add() {
        MedecinDialog d = new MedecinDialog(SwingUtilities.getWindowAncestor(this), null);
        d.setVisible(true);
        if (!d.isOk()) return;
        run(() -> api.post("/api/medecins",
            new Medecin(d.code(), d.nom(), d.prenom(), d.grade()), Medecin.class));
    }

    private void editRow(int row) {
        Medecin o = model.getAt(row);
        MedecinDialog d = new MedecinDialog(SwingUtilities.getWindowAncestor(this), o);
        d.setVisible(true);
        if (!d.isOk()) return;
        run(() -> api.put("/api/medecins/" + api.encode(o.codeMed()),
            new Medecin(d.code(), d.nom(), d.prenom(), d.grade()), Medecin.class));
    }

    private void deleteRow(int row) {
        Medecin x = model.getAt(row);
        int opt = JOptionPane.showConfirmDialog(this,
            "Supprimer le médecin « " + x.codeMed() + " — " + x.nom() + " » ?",
            "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt != JOptionPane.YES_OPTION) return;
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                api.delete("/api/medecins/" + api.encode(x.codeMed()));
                return null;
            }
            protected void done() {
                try { get(); refresh(); status.setText(" ✓ Médecin supprimé"); }
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