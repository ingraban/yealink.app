package name.saak.yealink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class TelephoneHandler {

    private static final Logger logger = LoggerFactory.getLogger(TelephoneHandler.class);
    private final TrayIconManager trayIconManager;

    public TelephoneHandler(TrayIconManager trayIconManager) {
        this.trayIconManager = trayIconManager;
    }

    /**
     * Behandelt eingehende tel:// URL
     * @param phoneNumber Telefonnummer (z.B. +49301234567)
     */
    public void handleCall(String phoneNumber) {
        logger.info("📞 Anruf zu: {}", phoneNumber);

        // Desktop-Benachrichtigung
        trayIconManager.showNotification(
                "Anruf eingegangen",
                "Nummer: " + phoneNumber,
                TrayIcon.MessageType.INFO
        );

        // Hier: Geschäftslogik einfügen (VoIP-App starten, etc.)
        try {
            // Beispiel: macOS Telefon-App öffnen
            openPhoneApp(phoneNumber);
        } catch (Exception e) {
            logger.error("❌ Fehler beim Verarbeiten des Anrufs", e);
            trayIconManager.showNotification(
                    "Fehler",
                    "Anruf konnte nicht verarbeitet werden",
                    TrayIcon.MessageType.ERROR
            );
        }
    }

    /**
     * macOS-Telefon-App öffnen (Beispiel)
     */
    private void openPhoneApp(String phoneNumber) throws Exception {
        // macOS: FaceTime öffnen
        String cleanNumber = phoneNumber.replaceAll("[^0-9+]", "");
        ProcessBuilder pb = new ProcessBuilder(
                "open",
                "facetime://" + cleanNumber
        );
        pb.start();
        logger.info("✓ App geöffnet für: {}", cleanNumber);
    }
}
