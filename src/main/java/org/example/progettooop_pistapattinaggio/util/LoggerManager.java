package org.example.progettooop_pistapattinaggio.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@code LoggerManager} fornisce un'istanza singleton del {@link Logger} centralizzato per l'applicazione.
 * <p>Utilizza un {@link ConsoleHandler} configurato per stampare tutti i livelli di log sulla console,
 * disattivando l'uso degli handler predefiniti del sistema.</p>
 *
 * <p>Il logger è accessibile tramite il metodo {@link #getLogger()} e può essere usato per loggare
 * eventi, errori e messaggi di debug all'interno del progetto SkateZone.</p>
 */
public class LoggerManager {

    private static final Logger logger = Logger.getLogger("SkateZoneLogger");

    // Blocco statico per configurare il logger
    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
    }

    /**
     * Restituisce l'istanza condivisa del logger configurato.
     *
     * @return logger configurato per SkateZone
     */
    public static Logger getLogger() {
        return logger;
    }
}
