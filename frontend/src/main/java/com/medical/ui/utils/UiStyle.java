package com.medical.ui.utils;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class UiStyle {

    private UiStyle() {}

    // ── Couleurs ──
    public static final Color PRIMARY       = new Color(14, 165, 233);
    public static final Color PRIMARY_DARK  = new Color(2, 132, 199);
    public static final Color SIDEBAR_BG    = new Color(15, 23, 42);
    public static final Color SIDEBAR_HOVER = new Color(30, 41, 59);
    public static final Color SIDEBAR_ACTIVE= new Color(30, 41, 59);
    public static final Color SIDEBAR_TEXT  = new Color(148, 163, 184);
    public static final Color SIDEBAR_ACCENT= new Color(14, 165, 233);
    public static final Color CARD_BG       = Color.WHITE;
    public static final Color SURFACE       = new Color(248, 250, 252);
    public static final Color BORDER        = new Color(226, 232, 240);
    public static final Color TEXT_PRIMARY   = new Color(30, 41, 59);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color SUCCESS       = new Color(16, 185, 129);
    public static final Color DANGER        = new Color(239, 68, 68);
    public static final Color WARNING       = new Color(245, 158, 11);

    public static void install() {
        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollBar.thumbArc", 10);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("TabbedPane.showTabSeparators", false);
        UIManager.put("TabbedPane.tabHeight", 42);
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
        UIManager.put("Table.rowHeight", 48);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
        UIManager.put("Table.gridColor", BORDER);
    }

    // ── Boutons ──
    public static void primaryButton(JButton b) {
        b.putClientProperty(FlatClientProperties.STYLE,
            "background:" + colorHex(PRIMARY) + ";" +
            "foreground:#FFFFFF;borderWidth:0;focusWidth:0;font:bold;"
        );
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void secondaryButton(JButton b) {
        b.putClientProperty(FlatClientProperties.STYLE,
            "background:#F1F5F9;foreground:" + colorHex(TEXT_PRIMARY) + ";borderWidth:0;focusWidth:0;"
        );
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void dangerButton(JButton b) {
        b.putClientProperty(FlatClientProperties.STYLE,
            "background:#FEF2F2;foreground:" + colorHex(DANGER) + ";borderWidth:0;focusWidth:0;font:bold;"
        );
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void outlineButton(JButton b) {
        b.putClientProperty(FlatClientProperties.STYLE,
            "background:#FFFFFF;foreground:" + colorHex(PRIMARY) + ";" +
            "borderColor:" + colorHex(PRIMARY) + ";borderWidth:1;focusWidth:0;"
        );
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ── Sidebar ──
    public static JButton sidebarButton(String text) {
        JButton b = new JButton(text);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(12, 20, 12, 20));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 14f));
        b.setForeground(SIDEBAR_TEXT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void setActive(JButton b) {
        b.setOpaque(true);
        b.setBackground(SIDEBAR_ACTIVE);
        b.setForeground(Color.WHITE);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 14f));
    }

    public static void setInactive(JButton b) {
        b.setOpaque(false);
        b.setBackground(SIDEBAR_BG);
        b.setForeground(SIDEBAR_TEXT);
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 14f));
    }

    // ── Table ──
    public static void table(JTable t) {
        t.setFillsViewportHeight(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setRowHeight(48);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setFont(t.getTableHeader().getFont().deriveFont(Font.BOLD, 13f));
        t.getTableHeader().setPreferredSize(new Dimension(0, 44));
        t.getTableHeader().setBackground(SURFACE);
        t.getTableHeader().setForeground(TEXT_SECONDARY);
    }

    // ── Cards ──
    public static JPanel card(JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        p.add(c);
        return p;
    }

    public static JPanel statsCard(String label, String value, Color accent) {
        JPanel p = new JPanel(new BorderLayout(8, 4));
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(16, 20, 16, 20)
        ));

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(valLabel.getFont().deriveFont(Font.BOLD, 28f));
        valLabel.setForeground(accent);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setForeground(TEXT_SECONDARY);
        lblLabel.setFont(lblLabel.getFont().deriveFont(13f));

        p.add(valLabel, BorderLayout.NORTH);
        p.add(lblLabel, BorderLayout.SOUTH);
        return p;
    }

    // ── Labels ──
    public static JLabel title(String s) {
        JLabel l = new JLabel(s);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 22f));
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel sectionTitle(String s) {
        JLabel l = new JLabel(s);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 16f));
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel muted(String s) {
        JLabel l = new JLabel(s);
        l.setForeground(TEXT_SECONDARY);
        l.setFont(l.getFont().deriveFont(13f));
        return l;
    }

    // ── Helpers ──
    private static String colorHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    public static JPanel wrap(JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    public static void rounded(JComponent c) {
        c.putClientProperty(FlatClientProperties.STYLE,
            "arc:12;"
        );
    }

    // ── Text field search style ──
    public static void searchField(JTextField f) {
        f.putClientProperty(FlatClientProperties.STYLE,
            "arc:10;borderColor:" + colorHex(BORDER) + ";"
        );
        f.setPreferredSize(new Dimension(220, 36));
    }

    public static void comboBox(JComboBox<?> c) {
        c.putClientProperty(FlatClientProperties.STYLE,
            "arc:10;borderColor:" + colorHex(BORDER) + ";"
        );
        c.setPreferredSize(new Dimension(120, 36));
    }
}