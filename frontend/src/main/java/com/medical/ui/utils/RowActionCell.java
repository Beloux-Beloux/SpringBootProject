package com.medical.ui.utils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.function.Consumer;

public final class RowActionCell {

    private RowActionCell() {
    }

    public static void install(
            JTable table,
            int column,
            Consumer<Integer> editAction,
            Consumer<Integer> deleteAction
    ) {
        table.getColumnModel()
                .getColumn(column)
                .setCellRenderer(new ActionRenderer());

        table.getColumnModel()
                .getColumn(column)
                .setCellEditor(new ActionEditor(editAction, deleteAction));

        table.getColumnModel()
                .getColumn(column)
                .setPreferredWidth(190);

        table.getColumnModel()
                .getColumn(column)
                .setMinWidth(190);

        table.getColumnModel()
                .getColumn(column)
                .setMaxWidth(190);
    }

    private static JButton createButton(String text, boolean danger) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 12f));
        button.setMargin(new Insets(6, 14, 6, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (danger) {
                button.setBackground(new Color(254, 226, 226));
                button.setForeground(new Color(185, 28, 28));
        } else {
                button.setBackground(UiStyle.PRIMARY);
                button.setForeground(Color.WHITE);
        }
        return button;
        }

    private static JPanel createPanel() {

        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        6,
                        5
                )
        );

        panel.setOpaque(true);

        return panel;
    }

    /**
     * Apparence de la cellule lorsqu'elle n'est pas en édition.
     */
    private static class ActionRenderer
            implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focused,
                int row,
                int column
        ) {

            JPanel panel = createPanel();

            if (selected) {
                panel.setBackground(
                        table.getSelectionBackground()
                );
            } else {
                panel.setBackground(
                        table.getBackground()
                );
            }

            JButton edit = createButton(
                    "Modifier",
                    false
            );

            JButton delete = createButton(
                    "Supprimer",
                    true
            );

            /*
             * Les boutons sont volontairement non fonctionnels
             * dans le renderer.
             *
             * Les vrais clics sont traités par ActionEditor.
             */
            panel.add(edit);
            panel.add(delete);

            return panel;
        }
    }

    /**
     * Éditeur réel de la cellule.
     *
     * C'est ici que les clics de souris sont capturés.
     */
    private static class ActionEditor
            extends AbstractCellEditor
            implements TableCellEditor {

        private final JPanel panel;

        private final JButton editButton;
        private final JButton deleteButton;

        private final Consumer<Integer> editAction;
        private final Consumer<Integer> deleteAction;

        private int modelRow = -1;

        ActionEditor(
                Consumer<Integer> editAction,
                Consumer<Integer> deleteAction
        ) {

            this.editAction = editAction;
            this.deleteAction = deleteAction;

            panel = createPanel();

            editButton = createButton(
                    "Modifier",
                    false
            );

            deleteButton = createButton(
                    "Supprimer",
                    true
            );

            panel.add(editButton);
            panel.add(deleteButton);

            editButton.addActionListener(e -> {

                int row = modelRow;

                fireEditingStopped();

                if (row >= 0) {
                    editAction.accept(row);
                }
            });

            deleteButton.addActionListener(e -> {

                int row = modelRow;

                fireEditingStopped();

                if (row >= 0) {
                    deleteAction.accept(row);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean selected,
                int row,
                int column
        ) {

            /*
             * JTable peut être triée.
             *
             * On convertit donc la ligne visuelle
             * vers la ligne réelle du modèle.
             */
            modelRow = table.convertRowIndexToModel(row);

            if (selected) {
                panel.setBackground(
                        table.getSelectionBackground()
                );
            } else {
                panel.setBackground(
                        table.getBackground()
                );
            }

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}