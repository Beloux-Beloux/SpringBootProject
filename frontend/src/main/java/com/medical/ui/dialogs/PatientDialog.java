package com.medical.ui.dialogs;

import com.medical.ui.models.Patient;
import com.medical.ui.utils.UiStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PatientDialog extends JDialog {

    private final JTextField code = new JTextField();
    private final JTextField nom = new JTextField();
    private final JTextField prenom = new JTextField();
    private final JTextField adresse = new JTextField();
    private final JComboBox<String> sexe = new JComboBox<>(new String[]{"F", "M", "Autre"});
    private boolean ok;

    public PatientDialog(Window owner, Patient v) {
        super(owner, "Patient", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 24, 16, 24));

        add(form(), BorderLayout.CENTER);
        add(buttons(), BorderLayout.SOUTH);

        if (v != null) {
            code.setText(v.codePat());
            nom.setText(v.nom());
            prenom.setText(v.prenom());
            sexe.setSelectedItem(v.sexe());
            adresse.setText(v.adresse());
        }

        pack();
        setMinimumSize(new Dimension(480, 360));
        setLocationRelativeTo(owner);
    }

    private JPanel form() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        Object[][] rows = {
            {"Code patient", code},
            {"Nom", nom},
            {"Prénom", prenom},
            {"Sexe", sexe},
            {"Adresse", adresse}
        };

        for (int i = 0; i < 5; i++) {
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 12f));

            Component field = (Component) rows[i][1];
            if (field instanceof JTextField) {
                UiStyle.searchField((JTextField) field);
                ((JTextField) field).setPreferredSize(new Dimension(280, 36));
            } else if (field instanceof JComboBox) {
                UiStyle.comboBox((JComboBox<?>) field);
            }

            p.add(lbl, gbc(0, i, 0, 0, GridBagConstraints.WEST));
            p.add(field, gbc(1, i, 1, 0, GridBagConstraints.HORIZONTAL));
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

    private GridBagConstraints gbc(int x, int y, double wx, double wy, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y;
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = fill;
        c.weightx = wx;
        return c;
    }

    private boolean valid() {
        for (JTextField f : new JTextField[]{code, nom, prenom, adresse})
            if (f.getText().isBlank()) {
                JOptionPane.showMessageDialog(this,
                    "Tous les champs sont obligatoires.", "Formulaire incomplet",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        return true;
    }

    public boolean isOk() { return ok; }
    public String code() { return code.getText().trim(); }
    public String nom() { return nom.getText().trim(); }
    public String prenom() { return prenom.getText().trim(); }
    public String sexe() { return sexe.getSelectedItem().toString(); }
    public String adresse() { return adresse.getText().trim(); }
}