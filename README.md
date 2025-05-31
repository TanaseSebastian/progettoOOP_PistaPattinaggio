
# ⛸ Gestionale Pista di Pattinaggio – Progetto OOP Java SE

### 🏫 Università / Corso
**COMPUTER SCIENCE AND ARTIFICIAL INTELLIGENCE – EPICODE INSTITUTE OF TECHNOLOGY**

### 👨‍💻 Studente
**Sebastian Tanase**

---

## 🚀 Obiettivo del Progetto
Realizzare un gestionale desktop in **Java SE 17** e **JavaFX** che supporti la vendita di biglietti, la prenotazione di slot orari e la gestione del noleggio pattini per una pista di pattinaggio.  
Il codice è scritto seguendo i principi **OOP** e fa largo uso di **Design Pattern** per garantire chiarezza, riusabilità ed estendibilità.

---

## ✅ Funzionalità implementate
- Emissione biglietti pre‑configurati e personalizzati (`TicketFactory`)
- Prenotazione slot orari con generazione automatica giornaliera (`SlotFactory`)
- Noleggio pattini con inventario taglie in tempo reale
- Calcolo incassi con **cassa centralizzata** (`CashRegister`)
- **Stampa scontrino ESC/POS** con QR‑Code e taglio carta
- Stampa **report giornaliero** degli incassi
- Persistenza automatica dati su file `.ser`
- Logging centralizzato (`LoggerManager`)
- Notifica vocale (Observer) a fine corsa
- Interfaccia JavaFX (FXML) semplice e intuitiva
- Test JUnit su componenti core

---

## 🧠 Design Pattern utilizzati

| Pattern        | Classe / Scopo                                                       |
|----------------|----------------------------------------------------------------------|
| **Factory**    | `TicketFactory`, `SlotFactory` – creazione biglietti / slot          |
| **Builder**    | `PistaBuilder` – costruzione piste e configurazione slot             |
| **Singleton**  | `CashRegister`, `LoggerManager` – istanza unica di cassa / logger    |
| **Iterator**   | `SlotIterator` – iterazione sugli slot disponibili                   |
| **Observer**   | `BookingObserver` + `VoiceNotifier` – notifica fine prenotazione     |
| **Strategy**   | `BookingSortStrategy` – ordinamento prenotazioni in UI               |
| **Exception Shielding** | Try‑catch mirati in `BookingService`, `DataManager`, `DashboardController` |

---

## ⚙️ Tecnologie e librerie

- **Java SE 17**
- **JavaFX 18** (FXML, CSS)
- **Collections & Generics**
- **Stream / Lambda API**
- **Serialization I/O** (`ObjectOutputStream` / `ObjectInputStream`)
- **ZXing** per generare QR‑Code
- **javax.print** per invio ESC/POS
- **java.util.logging**
- **JUnit 5** per il testing

---

## 📐 Diagramma UML (classi principali)

![UML Diagramma](/UMLDiagram.png)

> Rappresenta Controller, Service, Model, Factory, Builder, Iterator, Singleton e Observer.

---

## 📁 Struttura del progetto

```
src/
├── controller/
│   └── DashboardController.java
├── factory/
│   ├── TicketFactory.java
│   ├── SlotFactory.java
│   └── PistaBuilder.java
├── iterator/
│   └── SlotIterator.java
├── model/
│   ├── Booking.java
│   ├── Customer.java
│   ├── Slot.java
│   ├── PistaBase.java / PistaMultipla.java
│   └── Ticket (interface) + CustomTicket.java
├── observer/
│   ├── BookingObserver.java
│   └── VoiceNotifier.java
├── service/
│   └── BookingService.java
├── util/
│   ├── CashRegister.java
│   ├── DataManager.java
│   ├── Inventory.java + InventoryItem.java
│   ├── PrinterManager.java
│   └── LoggerManager.java
└── test/
    ├── BookingServiceTest.java
    └── DataManagerTest.java
```

---

## 🖨️ Stampa ESC/POS
Alla conferma di ogni prenotazione viene generato uno scontrino compatibile **ESC/POS** con:
- Dati cliente, tipo biglietto, prezzo e metodo di pagamento
- Elenco taglie pattini noleggiati
- **QR‑Code** con ID prenotazione
- Cut‑command finale

La stampante predefinita può essere salvata in `~/.pista_printer.properties`.

---

## 🧾 Report giornaliero
Da interfaccia è possibile produrre un report di fine giornata con:
- Numero prenotazioni
- Elenco clienti e biglietti
- Incasso totale  
  Il report può essere stampato su stampante termica o visualizzato in dialog.

---

## 🚫 Limitazioni attuali
- Nessuna autenticazione o gestione ruoli
- Le prenotazioni **non sono modificabili** dopo la vendita
- Anagrafica clienti solo in memoria (nome, età, telefono, tessera)
- Persistenza solo locale (file `.ser`)
- GUI non responsive per mobile
- Assenza concorrenza multi‑thread

---

## 🔮 Sviluppi futuri
| Priorità | Feature | Descrizione |
|----------|---------|-------------|
| 🔒 Alta  | Login & Ruoli | Separare permessi admin / operatore |
| ☁️ Media | DB / Backend | Salvataggio su PostgreSQL o Firebase |
| 📲 Media | UI Responsive | Migliorare layout per tablet/kiosk |
| ♻️ Media | CRUD Prenotazioni | Modifica / annulla prenotazione |
| 🧠 Bassa | Strategy Pricing | Promozioni e sconti dinamici |
| 🔔 Bassa | Notifiche Push | Observer → notifiche desktop/mobile |

---

## ▶️ Come eseguire

1. Clona il repo o scarica lo .zip
2. Importa in **IntelliJ IDEA** (o Eclipse) con **JDK 17**
3. Lancia `HelloApplication.java`
4. Assicurati che **JavaFX** sia configurato (VM args)
5. (Facoltativo) Collega una stampante termica ESC/POS (USB o Network)  
   Il software rileva automaticamente le stampanti disponibili e consente la selezione.

I file di persistenza (`bookings.ser`, `inventory.ser`, `cashRegister.ser`) vengono generati automaticamente nella cartella principale al primo avvio se non presenti.

---

## 📌 Note finali
Questo progetto dimostra l’applicazione concreta di OOP, design pattern e interazione hardware in un contesto gestionale.  
Non è ancora una base solida per un prodotto commerciale, ma con impegno e una ristrutturazione mirata può evolversi in una soluzione completa e professionale.

---

