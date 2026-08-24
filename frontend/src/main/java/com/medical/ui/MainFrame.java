package com.medical.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.medical.ui.panels.*;
import com.medical.ui.utils.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private final JLabel status = new JLabel(" Prêt");
    private final JLabel clock = new JLabel();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private JButton activeNav = null;

    // Sidebar buttons
    private final JButton navDashboard = UiStyle.sidebarButton("  ▣  Tableau de bord");
    private final JButton navMedecins = UiStyle.sidebarButton("  ♧  Médecins");
    private final JButton navPatients = UiStyle.sidebarButton("  ♙  Patients");
    private final JButton navVisites  = UiStyle.sidebarButton("  ▤  Visites");
    private final JButton navParam    = UiStyle.sidebarButton("  ⚙  Paramètres");

    public MainFrame() {
        super("MediCenter — Gestion Médicale");
        FlatLightLaf.setup();
        UiStyle.install();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 780));
        setSize(1400, 860);
        setLocationRelativeTo(null);

        ApiClient api = new ApiClient();

        // ── Root ──
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiStyle.SURFACE);

        // ── Header ──
        root.add(header(), BorderLayout.NORTH);

        // ── Body: Sidebar + Content ──
        JPanel body = new JPanel(new BorderLayout());
        body.add(sidebar(), BorderLayout.WEST);
        body.add(buildContent(api), BorderLayout.CENTER);
        root.add(body, BorderLayout.CENTER);

        // ── Footer status bar ──
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            new EmptyBorder(10, 24, 10, 24)
        ));
        status.setForeground(UiStyle.TEXT_SECONDARY);
        status.setFont(status.getFont().deriveFont(12f));
        clock.setForeground(UiStyle.TEXT_SECONDARY);
        clock.setFont(clock.getFont().deriveFont(12f));
        bottom.add(status, BorderLayout.WEST);
        bottom.add(clock, BorderLayout.EAST);
        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);

        // Activer par défaut
        activateNav(navMedecins);
        updateClock();
        new Timer(1000, e -> updateClock()).start();
    }

    private JPanel header() {
        JPanel h = new JPanel(new BorderLayout(16, 0));
        h.setBackground(Color.WHITE);
        h.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(16, 28, 16, 28),
            BorderFactory.createMatteBorder(0, 0, 1, 0, UiStyle.BORDER)
        ));

        // Brand
        JLabel brand = new JLabel("✦ MediCenter");
        brand.setFont(brand.getFont().deriveFont(Font.BOLD, 24f));
        brand.setForeground(UiStyle.PRIMARY_DARK);

        JLabel tagline = new JLabel("Gestion des médecins, patients et visites");
        tagline.setForeground(UiStyle.TEXT_SECONDARY);
        tagline.setFont(tagline.getFont().deriveFont(13f));

        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.add(brand);
        brandPanel.add(Box.createVerticalStrut(2));
        brandPanel.add(tagline);

        // Badge + clock
        /*JLabel badge = new JLabel("● API locale");
        badge.setOpaque(true);
        badge.setBackground(new Color(220, 252, 231));
        badge.setForeground(new Color(21, 128, 61));
        badge.setBorder(new EmptyBorder(6, 14, 6, 14));
        badge.setFont(badge.getFont().deriveFont(Font.BOLD, 12f));

        JLabel adminBadge = new JLabel("Admin");
        adminBadge.setOpaque(true);
        adminBadge.setBackground(new Color(239, 246, 255));
        adminBadge.setForeground(UiStyle.PRIMARY_DARK);
        adminBadge.setBorder(new EmptyBorder(6, 14, 6, 14));
        adminBadge.setFont(adminBadge.getFont().deriveFont(Font.BOLD, 12f));*/

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        //right.add(badge);
        //right.add(adminBadge);

        h.add(brandPanel, BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    private JPanel sidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(UiStyle.SIDEBAR_BG);
        side.setPreferredSize(new Dimension(220, 0));
        side.setBorder(new EmptyBorder(8, 0, 8, 0));

        // Nav items
        JButton[] navs = {navDashboard, navMedecins, navPatients, navVisites, navParam};
        for (JButton btn : navs) {
            btn.addActionListener(e -> {
                String cmd = e.getActionCommand();
                if (cmd.contains("Tableau")) cardLayout.show(contentPanel, "dashboard");
                else if (cmd.contains("Médecins")) cardLayout.show(contentPanel, "medecins");
                else if (cmd.contains("Patients")) cardLayout.show(contentPanel, "patients");
                else if (cmd.contains("Visites")) cardLayout.show(contentPanel, "visites");
                else if (cmd.contains("Paramètres")) cardLayout.show(contentPanel, "parametres");
                activateNav(btn);
            });
            side.add(btn);
            side.add(Box.createVerticalStrut(2));
        }

        // Spacer + footer in sidebar
        side.add(Box.createVerticalGlue());
        JLabel version = new JLabel("v1.0.0");
        version.setForeground(UiStyle.SIDEBAR_TEXT);
        version.setFont(version.getFont().deriveFont(11f));
        version.setBorder(new EmptyBorder(8, 20, 8, 20));
        side.add(version);

        return side;
    }

    private JPanel buildContent(ApiClient api) {
        contentPanel.setBackground(UiStyle.SURFACE);

        // Dashboard panel
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(UiStyle.SURFACE);
        dashboard.setBorder(new EmptyBorder(24, 28, 24, 28));
        dashboard.add(dashboardContent(api), BorderLayout.NORTH);

        contentPanel.add(dashboard, "dashboard");
        contentPanel.add(new MedecinPanel(api, status), "medecins");
        contentPanel.add(new PatientPanel(api, status), "patients");
        contentPanel.add(new VisitePanel(api, status), "visites");

        // Paramètres placeholder
        JPanel params = new JPanel(new GridBagLayout());
        params.setBackground(UiStyle.SURFACE);
        JLabel comingSoon = new JLabel("Paramètres — bientôt disponible");
        comingSoon.setForeground(UiStyle.TEXT_SECONDARY);
        comingSoon.setFont(comingSoon.getFont().deriveFont(18f));
        params.add(comingSoon);
        contentPanel.add(params, "parametres");

        return contentPanel;
    }

    private JPanel dashboardContent(ApiClient api) {
        JPanel dash = new JPanel();
        dash.setBackground(UiStyle.SURFACE);
        dash.setLayout(new BoxLayout(dash, BoxLayout.Y_AXIS));

        // Welcome
        JLabel welcome = UiStyle.title("Tableau de bord");
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = UiStyle.muted("Aperçu général de l'activité médicale");
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Stats cards row
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(24, 0, 24, 0));
        cards.add(UiStyle.statsCard("Médecins", "12", UiStyle.PRIMARY));
        cards.add(UiStyle.statsCard("Visites", "8", UiStyle.SUCCESS));
        cards.add(UiStyle.statsCard("Grades", "4", UiStyle.WARNING));
        cards.add(UiStyle.statsCard("Patients", "24", new Color(139, 92, 246)));

        dash.add(welcome);
        dash.add(Box.createVerticalStrut(4));
        dash.add(sub);
        dash.add(cards);

        // Info panel
        JPanel info = new JPanel(new GridBagLayout());
        info.setBackground(UiStyle.CARD_BG);
        info.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiStyle.BORDER, 1),
            new EmptyBorder(20, 24, 20, 24)
        ));
        JLabel infoTitle = UiStyle.sectionTitle("Modules de gestion");
        JLabel infoDesc = UiStyle.muted(
            "<html>Utilisez la navigation latérale pour gérer<br/>" +
            "les médecins, les patients et les visites médicales.</html>");
        info.add(infoTitle, new GridBagConstraints(0,0,1,1,0,0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0,8,0),0,0));
        info.add(infoDesc, new GridBagConstraints(0,1,1,1,0,0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));

        dash.add(info);
        return dash;
    }

    private void activateNav(JButton active) {
        JButton[] all = {navDashboard, navMedecins, navPatients, navVisites, navParam};
        for (JButton b : all) {
            UiStyle.setInactive(b);
        }
        UiStyle.setActive(active);
    }

    private void updateClock() {
        clock.setText(java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss")));
    }
}