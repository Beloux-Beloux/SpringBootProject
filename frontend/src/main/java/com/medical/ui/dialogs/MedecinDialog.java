package com.medical.ui.dialogs;

import com.medical.ui.models.Medecin;
import com.medical.ui.utils.UiStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MedecinDialog extends JDialog {

    private final JTextField code = new JTextField();
    private final JTextField nom = new JTextField();
    private final JTextField prenom = new JTextField();
    private final JTextField grade = new JTextField();
    private boolean ok;

    public MedecinDialog(Window owner, Medecin value) {
        super(owner, "Médecin", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 24, 16, 24));

        JPanel form = form();
        add(form, BorderLayout.CENTER);
        add(buttons(), BorderLayout.SOUTH);

        if (value != null) {
            code.setText(value.codeMed());
            nom.setText(value.nom());
            prenom.setText(value.prenom());
            grade.setText(value.grade());
        }

        pack();
        setMinimumSize(new Dimension(460, 320));
        setLocationRelativeTo(owner);
    }

    private JPanel form() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        String[] labels = {"Code médecin", "Nom", "Prénom", "Grade"};
        JTextField[] fields = {code, nom, prenom, grade};

        for (int i = 0; i < 4; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 12f));
            UiStyle.searchField(fields[i]);
            fields[i].setPreferredSize(new Dimension(300, 36));

            p.add(lbl, gbc(0, i, 0, 0, GridBagConstraints.WEST));
            p.add(fields[i], gbc(1, i, 1, 0, GridBagConstraints.HORIZONTAL));
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
        c.weighty = wy;
        return c;
    }

    private boolean valid() {
        for (JTextField f : new JTextField[]{code, nom, prenom, grade})
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
    public String grade() { return grade.getText().trim(); }
}