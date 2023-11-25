
**Nume: MOROIANU Horia-Valentin**

**Grupa: 324 CA**

# Proiect GlobalWaves  - Etapa 1

### Fluxul de executie:
Datele de intrare sunt citite din fisierele .json si sunt trimise in `AppControl`  unde o parte sunt incarcate in baza de date a aplicatiei (`Library`) iar restul prelucrate sub forma unui sir de comenzi executabile. Acest sir este parcurs si executat iar rezultatele fiecarei comenzi sunt adaugate la lista de noduri de iesire. Dupa terminarea tuturor comenzilor se elibereaza baza de date si se scrie informatia din noduri inapoi in fisiere .json .

### Structura:
* pachetul `commands`: contine clasa abstracta `Command` ce reprezinta clasa-parinte pentru restul comenzilor. Celalte clase sunt specifice pentru fiecare comanda din aplicatie.
* pachetul `entities` contine: 
	* clasele Singleton `AppControl` si `Library` si clasa `User` ce are asociate celelate doua entitati `MusicPlayer`, in care este simulata rularea de fisiere audio si `SearchBar` pentru salvarea cautarilor efectuate de utilizator.  
	* pachetul `audio_collections`: contine interfata `AudioFile`implementata de `Song` si `Episode` si interfata `AudioTrack` (pentru colectii de fisiere audio ce pot fi rulate in MusicPlayer) implementata de `Song`, `Podcast` si `Playlist`.

### Dificultati intampinate + implementari:
Pe langa magnitudinea temei, o mare provocare a fost planificarea relatiilor dintre clase si prelucrarea informatiilor din acestea. Deoarece in `SearchBar` a fost nevoie de pastrarea oricarui tip de colectie audio, am optat pentru crearea interfetei `AudioTrack` ce a ajutat mai tarziu in generalizarea comenzilor pentru `MusicPlayer`. In mod similar a fost creata si `AudioFile` pentru compatibiliatea dintre `Song` si `Episode`, ulterior ajungand la concluzia ca poate era mai bine sa fi fost o clasa abstracta. Am pastrat implementarea in vederea unei viitoare mosteniri multiple.
 
 - Pentru obtinerea mai usoara a datelor utilizatorilor, acestia au fost stocati in baza de date intr-un HashMap;
 - Similar pentru a continua un podcast din momentul in care a ramas, in fiecare MusicPlayer am retinut  intr-un HashMap istoricul (timpul de vizionare de la inceputul podcastului). Fiecare podcast are asociat un vector de sume partiale cu duratele episodelor si astfel se poate obtine usor momentul de timp la care am ramas.
 - Pentru fiecare 'cerere' a utilizatorilor, starea MusicPlayer-ului este actualizata cu ajutorul unei simulari de PlayQueue care gaseste ce fisier audio ar trebui sa fie redat la momentul respectiv.

### Comentarii asupra temei:
Din punctul meu de vedere, aceata a fost una dintre cele mai grele si laborioase teme primite pana acum. Fiind prima tema in stil OOP, proiectarea codului nu a fost atat de usoara si a dus adesea in punctul " eu cu ce incep/ce fac acum? ".
Inafara de asta, ideea temei mi-a placut si doresc **cat mai mult feedback** posibil pe cod si abordarea problemei.
