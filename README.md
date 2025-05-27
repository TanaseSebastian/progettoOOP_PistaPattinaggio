# ⛸ Gestionale Pista di Pattinaggio – Progetto OOP Java SE

### 🏫 Università / Corso
**COMPUTER SCIENCE AND ARTIFICIAL INTELLIGENCE – EPICODE INSTITUTE OF TECHNOLOGY**

### 👨‍💻 Studente
**Sebastian Tanase**

---

## 🚀 Obiettivo del Progetto

Sviluppare un’applicazione desktop con **Java SE e JavaFX** per la gestione di una pista di pattinaggio, implementando i principali concetti OOP e Design Pattern, oltre a funzionalità pratiche per il noleggio, prenotazione e vendita biglietti.

---

## ✅ Funzionalità implementate

- Emissione biglietti (singoli, famiglia, personalizzati)
- Prenotazione slot orari con visione settimanale
- Gestione anagrafica clienti
- Noleggio pattini con gestione taglie
- Calcolo incassi giornalieri
- Persistenza dati su file `.ser`
- Interfaccia utente semplice in JavaFX
- Logging interno
- Test unitari su componenti core

---

## 🧠 Design Pattern usati

| Pattern              | Classe / Utilizzo                                                       |
|----------------------|-------------------------------------------------------------------------|
| **Factory**          | `TicketFactory` per la creazione dinamica di biglietti                  |
| **Iterator**         | `SlotIterator` per scorrere gli slot disponibili                        |
| **Builder** (β)      | `TicketBuilder` per costruire step-by-step biglietti complessi          |
| **Singleton** (β)    | `CashRegister` per mantenere un’unica istanza degli incassi             |
| **Exception Shielding** | Gestione errori in `BookingService`, `DashboardController`, `DataManager` |
| **(Da integrare)** Composite | Menzione nel design iniziale, **non presente nel codice** attuale |
| **(Assenti)** Strategy / Observer | Non implementati – candidati ideali per sviluppo futuro      |

---

## ⚙️ Tecnologie e concetti Java usati

- **Java SE** (17+)
- **JavaFX** per l’interfaccia grafica
- **Java Collections & Generics**
- **Java I/O** con `ObjectOutputStream`/`ObjectInputStream`
- **Logging** con `java.util.logging`
- **JUnit** per test unitari
- **Architettura MVC semplificata**
- **Uso base di Lambda/Stream API** (da potenziare)

---

## 📐 Diagramma UML – Struttura Classi Principali

![UML Diagramma](/mnt/data/UMLDiagram.png)

> Rappresenta le principali classi, i pattern core (Factory, Singleton, Iterator) e le dipendenze tra componenti centrali.

---

## 📁 Struttura del codice

├── controller/
│ └── DashboardController.java
├── factory/
│ ├── TicketFactory.java
│ └── TicketBuilder.java
├── iterator/
│ └── SlotIterator.java
├── model/
│ ├── Booking.java, Customer.java, Ticket.java, Slot.java, ...
├── service/
│ └── BookingService.java
├── util/
│ ├── DataManager.java, CashRegister.java, LoggerManager.java, ...
├── test/
│ ├── BookingServiceTest.java, TestDataManager.java

---

## 🧪 Testing

- ✅ **JUnit** utilizzato per unit test
- ✅ Test presenti per `BookingService` e `DataManager`
- 🔜 Da estendere a `TicketFactory`, `Slot`, `CashRegister`, ecc.

---

## 🔐 Sicurezza e stabilità

| Aspetto                          | Stato   |
|----------------------------------|---------|
| Stack trace visibili all’utente  | ❌ Evitati |
| Crash su input errato            | ⚠️ Alcuni casi gestiti, altri da migliorare |
| Sanitizzazione input             | ⚠️ Minima, da migliorare |
| Password/credenziali hardcoded   | ✅ Nessuna |
| Propagazione eccezioni           | ⚠️ Alcune presenti, da schermare |

---

## ⚠ Limitazioni note

- Nessun supporto multiutente o autenticazione
- Nessuna sincronizzazione cloud o database remoto
- GUI non responsive né ottimizzata per dispositivi touch
- Assenza di pattern avanzati (Observer, Strategy, ecc.)
- Salvataggio non automatico su eventi

---

## 🔮 Sviluppi futuri

- 💼 Integrazione login e ruoli (admin/operatori)
- ☁️ Connessione a backend REST o Firebase
- 🧾 Stampa scontrino via stampanti ESC/POS
- 📈 Dashboard visuale per l’amministrazione
- 🎨 Interfaccia più moderna e responsive
- 🧠 Inserimento Observer e Strategy pattern (es. aggiornamento UI automatico, logiche promozionali)
- 🧵 Supporto multithreading per la gestione concorrente di prenotazioni

---

## ▶️ Istruzioni per l'esecuzione

1. Clona il progetto o scaricalo come `.zip`
2. Aprilo in **IntelliJ IDEA** o **Eclipse** (Java 17+)
3. Costruisci il progetto con Maven (`pom.xml`)
4. Esegui `HelloApplication.java`
5. Tutti i dati vengono salvati nei file `.ser` all’interno della cartella radice

---

## 📎 Note finali

> Questo progetto nasce come esercizio accademico, ma è strutturato per essere facilmente estendibile a un prodotto reale.  
> L’obiettivo è dimostrare una buona padronanza dell’OOP, del ciclo di vita di un’app Java e dei principali pattern software.

---