package com.medical;
import com.formdev.flatlaf.FlatLightLaf;import com.medical.ui.MainFrame;import com.medical.ui.utils.UiStyle;import javax.swing.*;
public class Main{public static void main(String[]args){SwingUtilities.invokeLater(()->{FlatLightLaf.setup();UiStyle.install();new MainFrame().setVisible(true);});}}
