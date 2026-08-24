package com.medical.ui.dialogs;

import com.medical.ui.models.Visite;
import com.medical.ui.utils.UiStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class VisiteDialog extends JDialog {

    private final JTextField med = new JTextField();
    private final JTextField pat = new JTextField();
    private final JTextField date = new JTextField();
    private boolean ok;

    public VisiteDialog(Window owner, Visite v) {
        super(owner, "Visite médicale", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 24, 16, 24));

        add(form(), BorderLayout.CENTER);
        add(buttons(), BorderLayout.SOUTH);

        if (v != null) {
            med.setText(v.codeMed());
            pat.setText(v.codePat());
            date.setText(v.date().toString());
        }

        pack();
        setMinimumSize(new Dimension(460, 300));
        setLocationRelativeTo(owner);
    }

    private JPanel form() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        String[] labels = {"Code médecin", "Code patient", "Date (AAAA-MM-JJ)"};
        JTextField[] fields = {med, pat, date};

        for (int i = 0; i < 3; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 12f));
            UiStyle.searchField(fields[i]);
            fields[i].setPreferredSize(new Dimension(280, 36));

            p.add(lbl, gbc(0, i, GridBagConstraints.WEST));
            p.add(fields[i], gbc(1, i, GridBagConstraints.HORIZONTAL));
        }
        return p;
    }

    private JPanel buttons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton cancel = new JButton("Annuler");
        UiStyle.secondaryButton(cancel);
        cancel.addActionListener(e -> dispose());

        JButton save = new JButton("Enregistrer");
        UiStyle.primaryButton(save);
        save.addActionListener(e -> {
            if (valid()) { ok = true; dispose(); }
        });

        p.add(cancel);
        p.add(save);
        return p;
    }

    private GridBagConstraints gbc(int x, int y, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y;
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = fill;
        c.weightx = x == 1 ? 1 : 0;
        return c;
    }

    private boolean valid() {
        if (med.getText().isBlank() || pat.getText().isBlank() || date.getText().isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Tous les champs sont obligatoires.", "Formulaire incomplet",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            LocalDate.parse(date.getText().trim());
            return true;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Date invalide. Format attendu : AAAA-MM-JJ", "Erreur de format",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean isOk() { return ok; }
    public String med() { return med.getText().trim(); }
    public String pat() { return pat.getText().trim(); }
    public LocalDate date() { return LocalDate.parse(date.getText().trim()); }
}