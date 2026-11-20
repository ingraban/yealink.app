package name.saak.yealink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

@Component
public class TrayIconManager {

    private static final Logger logger = LoggerFactory.getLogger(TrayIconManager.class);
    private TrayIcon trayIcon;

    public TrayIconManager() {
    }

    public void initializeTrayIcon() {
        // SystemTray-Support prüfen
        if (!SystemTray.isSupported()) {
            logger.warn("⚠ SystemTray wird auf diesem System nicht unterstützt");
            return;
        }

        try {
            SystemTray tray = SystemTray.getSystemTray();

            // Icon laden oder Standard-Icon erstellen
            Image image = loadTrayIcon();

            // PopupMenu erstellen
            PopupMenu popup = createPopupMenu();

            // TrayIcon erstellen
            trayIcon = new TrayIcon(image, "Tel-Handler", popup);
            trayIcon.setImageAutoSize(true);

            // TrayIcon hinzufügen
            tray.add(trayIcon);

            logger.info("✓ Tray-Icon initialisiert");
            showNotification("Tel-Handler gestartet", TrayIcon.MessageType.INFO);

        } catch (AWTException e) {
            logger.error("❌ Fehler beim Initialisieren des Tray-Icons", e);
        }
    }

    /**
     * Icon aus Ressourcen laden oder Standard-Icon erstellen
     */
    private Image loadTrayIcon() {
        try {
            URL iconUrl = getClass().getResource("/icon/tray-icon.png");
            if (iconUrl != null) {
                Image img = ImageIO.read(iconUrl);
                logger.info("✓ Icon geladen: {}", iconUrl);
                return img;
            }
        } catch (Exception e) {
            logger.warn("⚠ Custom Icon nicht gefunden, verwende Standard-Icon", e);
        }

        // Standard-Icon (blaues Quadrat, 16x16)
        return createDefaultIcon();
    }

    /**
     * Standard-Icon erstellen (Fallback)
     */
    private Image createDefaultIcon() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Hintergrund: Weiß
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 16, 16);
        
        // Vordergrund: Blaues "T" für Tel-Handler
        g2d.setColor(Color.BLUE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.drawString("T", 5, 12);
        
        g2d.dispose();
        return image;
    }

    /**
     * PopupMenu mit Einträgen
     */
    private PopupMenu createPopupMenu() {
        PopupMenu popup = new PopupMenu();

        // Status-Eintrag (deaktiviert)
        MenuItem statusItem = new MenuItem("Status: Läuft");
        statusItem.setEnabled(false);
        popup.add(statusItem);

        // Separator
        popup.addSeparator();

        // Einstellungen
        MenuItem settingsItem = new MenuItem("Einstellungen");
        settingsItem.addActionListener(e -> openSettings());
        popup.add(settingsItem);

        // Beenden
        MenuItem quitItem = new MenuItem("Beenden");
        quitItem.addActionListener(e -> {
            logger.info("🛑 App wird beendet...");
            System.exit(0);
        });
        popup.add(quitItem);

        return popup;
    }

    private void openSettings() {
        logger.info("Einstellungen öffnen...");
        JOptionPane.showMessageDialog(
                null,
                "Einstellungen werden noch implementiert",
                "Tel-Handler",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Desktop-Benachrichtigung anzeigen
     */
    public void showNotification(String title, String message, TrayIcon.MessageType type) {
        if (trayIcon != null) {
            trayIcon.displayMessage(title, message, type);
        }
    }

    public void showNotification(String message, TrayIcon.MessageType type) {
        showNotification("Tel-Handler", message, type);
    }
}
