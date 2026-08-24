package com.medical.ui;

import com.formdev.flatlaf.FlatClientProperties;
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
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle screenBounds = gc.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

        int screenW = screenBounds.width - screenInsets.left - screenInsets.right;
        int screenH = screenBounds.height - screenInsets.top - screenInsets.bottom;


        int winW = (int) screenW;
        int winH = (int) screenH;

        // Appliquer un minimum confortable
        winW = Math.max(winW, 1000);
        winH = Math.max(winH, 700);

        setMinimumSize(new Dimension(1000, 700));
        setSize(winW, winH);
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

        // ── Dashboard ──
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(UiStyle.SURFACE);
        dashboard.setBorder(new EmptyBorder(24, 28, 24, 28));
        dashboard.add(dashboardContent(api), BorderLayout.NORTH);

        JScrollPane dashScroll = new JScrollPane(dashboard);
        dashScroll.setBorder(null); // pas de bordure double
        dashScroll.getVerticalScrollBar().setUnitIncrement(16);
        dashScroll.getHorizontalScrollBar().setUnitIncrement(16);
        dashScroll.getViewport().setBackground(UiStyle.SURFACE);
        contentPanel.add(dashScroll, "dashboard");

        // ── Médecins ──
        JScrollPane medScroll = new JScrollPane(new MedecinPanel(api, status));
        medScroll.setBorder(null);
        medScroll.getVerticalScrollBar().setUnitIncrement(16);
        medScroll.getHorizontalScrollBar().setUnitIncrement(16);
        medScroll.getViewport().setBackground(UiStyle.SURFACE);
        contentPanel.add(medScroll, "medecins");

        // ── Patients ──
        JScrollPane patScroll = new JScrollPane(new PatientPanel(api, status));
        patScroll.setBorder(null);
        patScroll.getVerticalScrollBar().setUnitIncrement(16);
        patScroll.getHorizontalScrollBar().setUnitIncrement(16);
        patScroll.getViewport().setBackground(UiStyle.SURFACE);
        contentPanel.add(patScroll, "patients");

        // ── Visites ──
        JScrollPane visScroll = new JScrollPane(new VisitePanel(api, status));
        visScroll.setBorder(null);
        visScroll.getVerticalScrollBar().setUnitIncrement(16);
        visScroll.getHorizontalScrollBar().setUnitIncrement(16);
        visScroll.getViewport().setBackground(UiStyle.SURFACE);
        contentPanel.add(visScroll, "visites");

        // ── Paramètres ──
        JPanel params = new JPanel(new GridBagLayout());
        params.setBackground(UiStyle.SURFACE);
        JLabel comingSoon = new JLabel("Paramètres — bientôt disponible");
        comingSoon.setForeground(UiStyle.TEXT_SECONDARY);
        comingSoon.setFont(comingSoon.getFont().deriveFont(18f));
        params.add(comingSoon);

        JScrollPane paramScroll = new JScrollPane(params);
        paramScroll.setBorder(null);
        paramScroll.getViewport().setBackground(UiStyle.SURFACE);
        contentPanel.add(paramScroll, "parametres");

        return contentPanel;
    }

    private JPanel dashboardContent(ApiClient api) {
        JPanel dash = new JPanel();
        dash.setBackground(UiStyle.SURFACE);
        dash.setLayout(new BoxLayout(dash, BoxLayout.Y_AXIS));

        // ── Bannière de bienvenue moderne ──
        JPanel welcomeBanner = createWelcomeBanner();
        welcomeBanner.setAlignmentX(Component.LEFT_ALIGNMENT);
        dash.add(welcomeBanner);
        dash.add(Box.createVerticalStrut(24));

        // ── Section titre des statistiques ──
        JLabel statsSectionTitle = UiStyle.sectionTitle("Aperçu des statistiques");
        statsSectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        dash.add(statsSectionTitle);
        dash.add(Box.createVerticalStrut(16));

        // ── Grille des cartes stats (4 cartes) ──
        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsGrid.setOpaque(false);
        cardsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cartes avec labels dynamiques (mis à jour via SwingWorker)
        medCardValue = new JLabel("...");
        visCardValue = new JLabel("...");
        patCardValue = new JLabel("...");
        gradeCardValue = new JLabel("...");

        cardsGrid.add(createModernStatsCard("Médecins", medCardValue,
            UiStyle.PRIMARY, new Color(219, 234, 254), "fa-user-md"));
        cardsGrid.add(createModernStatsCard("Visites", visCardValue,
            UiStyle.SUCCESS, new Color(209, 250, 229), "fa-calendar-check"));
        cardsGrid.add(createModernStatsCard("Patients", patCardValue,
            new Color(139, 92, 246), new Color(237, 232, 254), "fa-users"));
        cardsGrid.add(createModernStatsCard("Grades", gradeCardValue,
            UiStyle.WARNING, new Color(254, 243, 199), "fa-star"));

        dash.add(cardsGrid);
        dash.add(Box.createVerticalStrut(28));

        // ── Panneau d'information "Modules" ──
        JPanel infoPanel = createInfoPanel();
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dash.add(infoPanel);

        // Lancer le chargement asynchrone des vraies statistiques
        loadDashboardStats(api);

        return dash;
    }

    // ── Champs pour les labels de valeurs (déclarés en attribut de classe) ──
    private JLabel medCardValue;
    private JLabel visCardValue;
    private JLabel patCardValue;
    private JLabel gradeCardValue;

    // ═══════════════════════════════════════════════════════════════
    //  1. BANNIÈRE DE BIENVENUE MODERNE
    // ═══════════════════════════════════════════════════════════════

    private JPanel createWelcomeBanner() {
        JPanel banner = new JPanel(new BorderLayout(24, 0));
        banner.setBackground(new Color(239, 246, 255));
        banner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 1),
            new EmptyBorder(28, 32, 28, 32)
        ));
        banner.putClientProperty(FlatClientProperties.STYLE, "arc:16;");

        // ── Contenu texte (West) ──
        JPanel textBlock = new JPanel();
        textBlock.setOpaque(false);
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));

        JLabel badge = new JLabel("●  Module Dashboard — Actif");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(new Color(22, 163, 74));
        badge.setOpaque(true);
        badge.setBackground(new Color(220, 252, 231));
        badge.setBorder(new EmptyBorder(6, 14, 6, 14));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        textBlock.add(badge);
        textBlock.add(Box.createVerticalStrut(12));

        JLabel welcomeTitle = new JLabel("Bienvenue sur MediCenter");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeTitle.setForeground(new Color(30, 41, 59));
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        textBlock.add(welcomeTitle);
        textBlock.add(Box.createVerticalStrut(8));

        JLabel welcomeDesc = new JLabel(
            "<html><div style='width:520px; font-size:15px; line-height:1.6; color:#475569;'>" +
            "Module de gestion médicale complet — Gérez vos <b>médecins</b>, <b>patients</b> " +
            "et <b>visites</b> en toute simplicité. Utilisez la navigation latérale " +
            "pour accéder aux différentes sections de l'application." +
            "</div></html>");
        welcomeDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        textBlock.add(welcomeDesc);

        // ── Icône (East) — approche simple et centrée ──
        // Utiliser directement un JLabel avec un background circulaire FlatLaf
        JLabel iconLabel = new JLabel("⚕");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 154));
        iconLabel.setForeground(new Color(2, 132, 199));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(100, 100));
        iconLabel.setMinimumSize(new Dimension(100, 100));
        // Fond bleu clair arrondi via FlatLaf
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(191, 219, 254));
        iconLabel.putClientProperty(FlatClientProperties.STYLE, "arc:50;");

        banner.add(textBlock, BorderLayout.WEST);
        banner.add(iconLabel, BorderLayout.EAST);

        return banner;
    }

    // ═══════════════════════════════════════════════════════════════
    //  2. CARTE DE STATISTIQUE MODERNE (avec données dynamiques)
    // ═══════════════════════════════════════════════════════════════

    private JPanel createModernStatsCard(String title, JLabel valueLabel,
                                        Color accent, Color accentBg, String iconUnicode) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiStyle.BORDER, 1),
            new EmptyBorder(20, 22, 22, 22)
        ));
        card.putClientProperty(FlatClientProperties.STYLE, "arc:14;");

        // Ligne du haut : icône circulaire + valeur
        JPanel topRow = new JPanel(new BorderLayout(12, 0));
        topRow.setOpaque(false);

        // Icône ronde avec fond coloré
        JPanel iconBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentBg);
                g2.fillOval(0, 0, 44, 44);
                g2.dispose();
            }
        };
        iconBox.setPreferredSize(new Dimension(44, 44));
        iconBox.setOpaque(false);
        iconBox.setLayout(new GridBagLayout());

        String iconChar;
        if (title.equals("Médecins"))       iconChar = "♧";
        else if (title.equals("Visites"))   iconChar = "▤";
        else if (title.equals("Patients"))  iconChar = "♙";
        else if (title.equals("Grades"))    iconChar = "★";
        else                                iconChar = "●";

        JLabel iconLbl = new JLabel(iconChar);
        iconLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        iconLbl.setForeground(accent);
        iconBox.add(iconLbl);

        // Valeur (grande police)
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accent);

        topRow.add(iconBox, BorderLayout.WEST);
        topRow.add(valueLabel, BorderLayout.EAST);

        // Titre en bas
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(UiStyle.TEXT_PRIMARY);

        // Sous-texte
        String subtitle;
        switch (title) {
            case "Médecins": subtitle = "Total des médecins enregistrés"; break;
            case "Visites":  subtitle = "Total des consultations";        break;
            case "Patients": subtitle = "Total des patients";             break;
            case "Grades":   subtitle = "Grades distincts";               break;
            default:         subtitle = "";                               break;
        }
        JLabel subLbl = UiStyle.muted(subtitle);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        card.add(topRow, BorderLayout.NORTH);
        card.add(titleLbl, BorderLayout.CENTER);
        card.add(subLbl, BorderLayout.SOUTH);

        return card;
    }

    // ═══════════════════════════════════════════════════════════════
    //  3. CHARGEMENT ASYNCHRONE DES STATISTIQUES (vraies valeurs API)
    // ═══════════════════════════════════════════════════════════════

    private void loadDashboardStats(ApiClient api) {
        new SwingWorker<Void, Void>() {
            private long medCount, visCount, patCount, gradeCount;
            private String errorMsg = null;

            @Override
            protected Void doInBackground() {
                try {
                    medCount   = api.get("/api/medecins/count", Long.class);
                    patCount   = api.get("/api/patients/count", Long.class);
                    visCount   = api.get("/api/visites/count", Long.class);
                    gradeCount = api.get("/api/medecins/count/distinct-grades", Long.class);
                } catch (Exception e) {
                    errorMsg = "Erreur : " + e.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                if (errorMsg != null) {
                    medCardValue.setText("—");
                    visCardValue.setText("—");
                    patCardValue.setText("—");
                    gradeCardValue.setText("—");
                    status.setText(" " + errorMsg);
                    return;
                }
                medCardValue.setText(String.valueOf(medCount));
                visCardValue.setText(String.valueOf(visCount));
                patCardValue.setText(String.valueOf(patCount));
                gradeCardValue.setText(String.valueOf(gradeCount));
                status.setText(" Prêt — Dernière mise à jour : "
                    + java.time.LocalTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        }.execute();
    }

    // ═══════════════════════════════════════════════════════════════
    //  4. PANNEAU D'INFORMATION MODULES (redesigné)
    // ═══════════════════════════════════════════════════════════════

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiStyle.BORDER, 1),
            new EmptyBorder(22, 26, 22, 26)
        ));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:14;");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 6, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel title = UiStyle.sectionTitle("Modules disponibles");
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(title, gbc);

        // Description avec icônes
        JLabel desc = new JLabel(
            "<html><div style='width:700px; font-size:13px; color:#64748b; line-height:1.8;'>" +
            "➜  <b>Médecins</b> — Ajouter, modifier, supprimer et rechercher des médecins<br/>" +
            "➜  <b>Patients</b> — Gérer les dossiers patients avec recherche avancée<br/>" +
            "➜  <b>Visites</b> — Enregistrer et suivre les consultations médicales<br/>" +
            "</div></html>");
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panel.add(desc, gbc);

        return panel;
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