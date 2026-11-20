package name.saak.yealink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import javax.swing.*;

@SpringBootApplication
public class PhoneHandlerApplication {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        // Headless deaktivieren (Tray-Icon benötigt AWT)
        System.setProperty("java.awt.headless", "false");

        // Spring Boot starten
        applicationContext = SpringApplication.run(PhoneHandlerApplication.class, args);

        // Tray-Icon initialisieren (nach Spring-Start)
        SwingUtilities.invokeLater(() -> {
            TrayIconManager trayManager = applicationContext.getBean(TrayIconManager.class);
            trayManager.initializeTrayIcon();
        });

        // App läuft im Hintergrund
        System.out.println("✓ Tel-Handler läuft. Verfügbar unter http://localhost:8080");
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
