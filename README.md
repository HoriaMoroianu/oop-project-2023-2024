**Nume: Horia-Valentin MOROIANU**

**Grupa: 324 CA**

# Proiect GlobalWaves  - Etapa 2

---
### Fluxul de executie:

Datele de intrare sunt citite din fisierele json si sunt trimise in `AppControl`  unde o parte sunt incarcate in baza de date a aplicatiei (`Library`) iar restul prelucrate sub forma unui sir de comenzi executabile. Acest sir este parcurs si executat iar rezultatele fiecarei comenzi sunt adaugate la lista de noduri de iesire. Dupa terminarea tuturor comenzilor se elibereaza baza de date si se scrie informatia din noduri inapoi in fisiere json.

---
### Structura:
```
src/
├── app/
│   ├── audio/
│   │   ├── collections/
│   │   │   └── Interfata AudioTrack folosita la simularea MusicPlayer-ului implementata de:
│   │   │       ├── Song
│   │   │       ├── Podcast
│   │   │       ├── Playlist
│   │   │       └── Album
│   │   └── files/
│   │       └── Clasa abstracta AudioFile extinsa de:
│   │           ├── Song
│   │           └── Episode
│   ├── clients/
│   │   ├── Clasa abstracta Client extinsa de:
│   │   │   ├── User
│   │   │   ├── Artist
│   │   │   └── Host
│   │   └── services/
│   │       └── Entitati folosite de clientii aplicatiei:
│   │           ├── SearchBar
│   │           ├── MusicPlayer
│   │           ├── Page
│   │           ├── Event
│   │           ├── Announcement
│   │           └── Merch
│   └── management/
│       ├── Clasa Singleton Library care este trata ca baza de date a aplicatiei
│       └── AppControl care se ocupa de I/O si prelucrarea comenzilor
├── commands/
│   └── Clasa abstracta Command extinsa de toate comenzile aplicatiei:
│       ├── Search
│       ├── Select
│       ├── ... 
│       └── (comenzile sunt grupate in pachete in functie de categoria din care fac parte)
├── fileio.input/
│   └── Clase folosite pentru a citi datele din fisierele json
└── main/
    ├── Main ruleaza mai multe teste cu comenzi pentru aplicatie
    └── Test ruleaza un singur test cu comenzi pentru aplicatie
```
---
### Dificultati intampinate + implementari:
#### Etapa 1:
O mare provocare a fost planificarea relatiilor dintre clase si prelucrarea informatiilor din acestea. Deoarece in `SearchBar` a fost nevoie de pastrarea oricarui tip de colectie audio, am optat pentru crearea interfetei `AudioTrack` ce a ajutat mai tarziu in generalizarea comenzilor pentru `MusicPlayer`.

Pentru obtinerea mai usoara a datelor utilizatorilor, acestia au fost stocati in baza de date intr-un HashMap.

Similar pentru a continua un podcast din momentul in care a ramas, in fiecare MusicPlayer am retinut  intr-un HashMap istoricul (timpul de vizionare de la inceputul podcastului). Fiecare podcast are asociat un vector de sume partiale cu duratele episodelor si astfel se poate obtine usor momentul de timp la care am ramas.

Pentru fiecare 'cerere' a utilizatorilor, starea MusicPlayer-ului este actualizata cu ajutorul unei simulari de PlayQueue care gaseste ce fisier audio ar trebui sa fie redat la momentul respectiv.

---
#### Etapa 2:
Pentru a pastra vechea logica a codului si a-l intelege mai bine am decis ca "user normal" sa ramana `User` iar utilizatorul general al aplicatiei sa fie reprezentat de `Client`.

Pentru sistemul de paginare am considerat ca fiecare client agrega o pagina `Page`, iar navigarea intre acestea se face doar schimband tipul si proprietarul acestora, de unde se vor obtine informatii pentru afisare.

Deoarece a fost nevoie de afisarea clientilor in ordinea in care au fost adaugati in aplicatie, acestia sunt pastrati acum in baza de date intr-un LinkedHashMap cu numele lor drep cheie.

Probabil cea mai controversata comanda de la aceasta etapa a fost DeleteUser, insa am reusit sa o implementez relativ usor retinand pentru fiecare client o lista cu clientii care ii acceseaza continutul. Aceasta lista este actulizata de fiecare data cand se incarca si se scoate ceva din music player sau cand se acceseaza o noua pagina.

---
### Comentarii asupra temei:
Dupa o analiza finala a structurii codului am constatat ca se incarca foarte mult ierarhia avand clase pentru fiecare comanda, care in anumite cazuri au si o implementare restransa, asa ca intentionez pentru urmatoarea etapa sa restructurez partea de executie a comenzilor.

Lucrand la aceasta etapa am ajuns sa ma plictisesc destul de repede si sa simt ca fac acelasi lucru din nou si din nou, si nu consider ca am invatat foarte multe lucruri noi fata de cea precedenta. Asta nu cred neaparat ca e o problema de "task-uri prea usoare" ci mai mult de task-uri prea repetitive si pe alocuri structuri care par a fi adaugate doar de umplutura (Merch, Event, chiar si Host care nu are un impact foarte mare asupra aplicatiei spre deosebire de user sau artist).