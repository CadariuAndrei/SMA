#  CourtConnect Mobile — Rezervări Terenuri de Tenis & Matchmaking (Android)

Aplicație nativă **Android** dezvoltată în **Kotlin** cu **Jetpack Compose**, destinată jucătorilor de tenis amatori și avansați pentru rezervarea terenurilor în timp real, găsirea partenerilor de joc în funcție de nivelul valoric (**NTRP**) și actualizarea clasamentului local.

Aplicația mobilă face parte dintr-un ecosistem complet (Web + Mobile) și comunică în timp real printr-un API REST cu un backend centralizat (**Node.js + Express**) și o bază de date relațională (**PostgreSQL**).


##  Funcționalități Principale & Business Logic

### 1.  Matchmaking Inteligent ("Caut Partener" & Compatibilitate NTRP)
* **Filtrare după Nivel Valoric (NTRP 2.0 – 5.0):** Jucătorii pot crea meciuri deschise (*Open Matches*) pentru **Simplu (2 jucători)** sau **Dublu (4 jucători)**.
* **Algoritm de Fair-Play ($\pm 0.5$ NTRP):** Sistemul verifică compatibilitatea dintre nivelul organizatorului și cel al jucătorului care dorește să se alăture, prevenind meciurile dezechilibrate.
* **Auto-Lock la Capacitate Maximă:** În momentul în care toate locurile disponibile sunt ocupate (2/2 la simplu sau 4/4 la dublu), anunțul se închide automat în baza de date printr-o tranzacție ACID (`SELECT ... FOR UPDATE`).

### 2.  Motor de Tarifare Dinamică & "Split the Bill"
* **Calcul Dinamic pe Server:** Prețul unei ore este calculat automat în funcție de intervalul orar (*Off-Peak* vs. *Peak Hours*), adăugarea taxei de **nocturnă** după ora configurată de club și tipul terenului (**Indoor/Balon** vs. **Exterior**).
* **Cost per Jucător:** În lista de meciuri deschise, aplicația afișează direct costul individual împărțit la numărul de participanți (1/2 pentru simplu, 1/4 pentru dublu).

### 3.  Reguli Anti-Abuz & Concurrency Control
* **Prevenire Double-Booking (Race Conditions):** Sincronizare instantanee între aplicația Web și cea Mobilă. Dacă doi utilizatori încearcă să rezerve același teren la aceeași oră simultan, constrângerile `UNIQUE` și tranzacțiile SQL garantează integritatea datelor.
* **Quota Zilnică & Fereastră de Anulare (24h Rule):** Limitarea numărului de ore active rezervate de același cont pentru a preveni blocarea abuzivă a intervalelor de seară, plus restricționarea anulărilor cu mai puțin de 24 de ore înainte de meci.
* **Regula "Orphan Match" (Transfer de Proprietate):** Dacă organizatorul unui meci deschis se retrage după ce alți jucători s-au alăturat deja, rezervarea nu se anulează, ci proprietatea este transferată automat următorului jucător înscris.

### 4.  Scor Post-Meci, Dinamică ELO & Recenzii
* **Înregistrare Scor & Ajustare NTRP:** La finalizarea meciului, jucătorii pot introduce scorul direct de pe telefon, ajustând dinamic coeficientul NTRP al participanților.
* **Rating Terenuri:** Jucătorii care au finalizat o rezervare pot evalua calitatea suprafeței de joc (`Zgură`, `Hard`, `Iarbă`).

---

## 🛠️ Arhitectură

Aplicația respectă arhitectura oficială **MVVM (Model - View - ViewModel)** și principiile *Clean Architecture*:

* **Limbaj:** Kotlin
* **UI Framework:** Jetpack Compose (Material Design 3)
* **State Management & Asincronism:** Kotlin Coroutines, `StateFlow` / `MutableState`, `ViewModel`
* **Networking:** Retrofit2 + OkHttp + Gson Converter
* **Autentificare & Securitate:** JSON Web Tokens (JWT) cu atașare în header-ul `Authorization: Bearer <token>`
* **Backend & DB (Shared Ecosystem):** Node.js, Express.js, PostgreSQL (rulat în Docker / local)
