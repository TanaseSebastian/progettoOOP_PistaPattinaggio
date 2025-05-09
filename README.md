# Gestionale Pista di Pattinaggio – Progetto OOP Java SE

### 🎓 Università / Corso: [COMPUTER SCIENCE AND ARTIFICIAL INTELLIGENCE EPICODE INSTITUTE OF TECHNOLOGY]
### 👨‍💻 Studente: [Sebastian Tanase]

---

## 🚀 Obiettivo

Sviluppare un'applicazione Java SE per la gestione di una pista di pattinaggio, rispettando i principi della programmazione a oggetti, l'uso dei design pattern e delle tecnologie core Java.

---

## ✅ Funzionalità principali

- Gestione dei biglietti (singolo, famiglia, abbonamento)
- Prenotazioni per slot orari
- Anagrafica clienti
- Calcolo degli incassi e statistiche
- Interfaccia JavaFX semplice e funzionale
- Logging, salvataggi e gestione eccezioni

---

## 🧠 Design Pattern utilizzati

| Pattern | Descrizione |
|--------|-------------|
| Factory | Creazione dei biglietti |
| Composite | Slot → Prenotazioni giornaliere |
| Iterator | Scorrimento slot disponibili |
| Exception Shielding | Gestione controllata degli errori |
| Singleton | Gestione centralizzata delle risorse |
| Strategy (β) | Strategie di sconto o assegnazione slot |
| Builder (β) | Costruzione guidata delle prenotazioni |
| Observer (β) | Aggiornamento automatico della GUI |

---

## 🔧 Tecnologie Java utilizzate

- JavaFX (GUI)
- Java I/O (salvataggi su file)
- Java Collections & Generics
- Logging (`java.util.logging`)
- JUnit & Mockito (testing)
- Stream API (analisi dati)

---

## 📐 Diagrammi UML

*(Saranno aggiunti a sviluppo concluso – includere almeno il diagramma delle classi principali e uno architetturale)*

---

## 🔐 Sicurezza

- Nessun crash su input errato
- Stack trace nascosti all’utente
- Input sanitizzati
- Nessuna credenziale hardcoded

---

## ⚠ Limitazioni attuali

- Nessun login multiutente
- Nessuna sincronizzazione cloud
- Interfaccia semplificata per progetto didattico

---

## 🔮 Possibili sviluppi futuri

- Versione PRO cloud con Firebase o backend REST
- Stampa scontrino ESC/POS
- Dashboard avanzata per amministratori
