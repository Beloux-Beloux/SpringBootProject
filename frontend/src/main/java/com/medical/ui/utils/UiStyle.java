package com.medical.ui.utils;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class UiStyle {
    private UiStyle() {}
    public static void install(){UIManager.put("Component.arc",12);UIManager.put("Button.arc",10);UIManager.put("TextComponent.arc",10);UIManager.put("ScrollBar.width",10);UIManager.put("TabbedPane.showTabSeparators",false);UIManager.put("TabbedPane.tabHeight",42);UIManager.put("TabbedPane.selectedBackground",Color.white);UIManager.put("Table.rowHeight",42);UIManager.put("Table.showHorizontalLines",true);UIManager.put("Table.showVerticalLines",false);UIManager.put("Table.intercellSpacing",new Dimension(0,1));}
    public static void primaryButton(JButton b){b.putClientProperty(FlatClientProperties.STYLE,"background:#2563EB;foreground:#FFFFFF;borderWidth:0;focusWidth:0;font:bold;");b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));}
    public static void secondaryButton(JButton b){b.putClientProperty(FlatClientProperties.STYLE,"background:#F3F4F6;foreground:#374151;borderWidth:0;focusWidth:0;");b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));}
    public static void dangerButton(JButton b){b.putClientProperty(FlatClientProperties.STYLE,"background:#FEE2E2;foreground:#B91C1C;borderWidth:0;focusWidth:0;");b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));}
    public static void table(JTable t){t.setFillsViewportHeight(true);t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);t.setShowGrid(false);t.setIntercellSpacing(new Dimension(0,1));t.setRowHeight(44);t.getTableHeader().setReorderingAllowed(false);t.getTableHeader().setFont(t.getTableHeader().getFont().deriveFont(Font.BOLD,13f));t.getTableHeader().setPreferredSize(new Dimension(0,42));}
    public static JPanel card(JComponent c){JPanel p=new JPanel(new BorderLayout());p.setBorder(new EmptyBorder(8,8,8,8));p.add(c);return p;}
    public static JLabel title(String s){JLabel l=new JLabel(s);l.setFont(l.getFont().deriveFont(Font.BOLD,22f));return l;}
    public static JLabel muted(String s){JLabel l=new JLabel(s);l.setForeground(new Color(107,114,128));return l;}
}
