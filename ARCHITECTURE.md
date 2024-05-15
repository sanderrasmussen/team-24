
![Arkitektur](https://media.github.uio.no/user/9649/files/808208ab-37b4-4961-980e-9089bca96305)

I utviklingen av appen vår har vi brukt Kotlin som hovedspråket og Android Studio som utviklingsverktøy. For å sikre en moderne og intuitiv brukeropplevelse for våre brukere har vi i stor grad benyttet oss av Jetpack Compose for å designe UI-elementene. Gjennom Android Studio har vi også brukt Gradle for å blant annet administrere avhengigheter, automatisere byggeprosessen og konfigurere innstillinger. 

Arkitekturen i appen vår er hovedsakelig basert på MVVM (Model-View-ViewModel) designmønsteret. Vi valgte dette for å oppnå en klar og tydelig separasjon av ansvar mellom de ulike komponentene i appen vår. Vi har derfor sørget for at hver skjerm i appen har sin egen ViewModel. Dette har bidratt til en tydelig struktur og gjort det enklere for oss å vedlikeholde og teste koden vår. Vi har også oppnådd høy kohesjon ved å ha separate ViewModeller, siden hver modell er dedikert til en tilhørende view. 

Modellene i appen vår representerer dataene og forretningslogikken, og inkluderer elementer som klær, bankinformasjon, kategorier, spørsmål, databasen og datasources for innhenting av data fra endepunktene. 

Når det kommer til view laget, har vi brukt Jetpack Compose for å designe UI-elementene. Dette laget er ansvarlig for å vise dataene til brukeren på en forståelig og intuitiv måte, men også for å fange opp brukerinteraksjoner. 

ViewModel-laget fungerer som et bindeledd mellom modellene og viewene. Her har hver skjerm i appen sin egen ViewModel som nevnt tidligere, som er ansvarlig for å hente data fra relevante repositories, som ClothesRepository, LocationForecastRepository osv. Dette sikrer at visningen har tilgang til riktig data på en effektiv måte, uten å måtte håndtere komplekse datahentingsoperasjoner direkte. ViewModellene er også der vi håndterer brukerinteraksjoner og utfører handlinger basert på dem. 

Når det kommer til prinsippene om høy kohesjon har vi som nevnt tidligere gitt hver ViewModel et klart og spesifikt ansvarsområde. Hver ViewModel er ansvarlig for å håndtere data og brukerinteraksjoner knyttet til en bestemt skjerm i appen. Dette gjør at det blir lettere å forstå koden og vedlikeholde den over tid, da hver ViewModel har begrenset funksjonalitet å ta seg av. 

Vi har sikret lav kobling ved å sørge for at ViewModellen kun kommuniserer med relevante repositories for å hente data. Dette betyr at ViewModellen ikke har direkte avhengighet av hverandre eller av andre deler av appen. Dette gjør det lettere å endre eller erstatte en del av koden uten å påvirke resten av systemet, samt at det reduserer kompleksiteten i koden betydelig. 

I tillegg til MVVM-mønsteret har vi også implementert unidirectional flow i vår løsning. Det betyr at dataen kun går i en retning gjennom appens komponenter, og endringer i dataene propageres nedover gjennom lagene for å oppdatere grensesnittene. Dette har også bidratt til å gjøre koden vår mer forutsigbar og enklere å vedlikeholde. 

Vi har også repositories som er ansvarlige for å abstrahere datakildene fra resten av appen. Disse repositories håndterer datahenting fra lokale databaser, som Room, og eksterne kilder som nettverksforespørsler.  

Når det gjelder databehandling, har vi benyttet oss av en enkel lokal databaseløsning basert på Room. Dette har gjort det mulig for oss å lagre og behandle data lokalt på enheten noe som spesielt er nyttig for funksjoner som krever rask tilgang til dataen uten noe behov for nettverkstilgang. 

I forhold til API-nivå valgte vi først å støtte minimum API-nivå 26. Vi valgte dette nivået for å sikre at vi kunne dra nytte av de nyeste funksjonene og forbedringene i Android-plattformen, samtidig som vi kunne nå ut til et bredt spektrum av brukere. I tillegg til de tekniske aspektene tenkte vi også brukernes preferanser, og vi ønsket å velge en versjon som ga oss funksjonaliteten vi trengte for appen, samtidig som den ga tilgang til de viktigste funksjonene for brukerne våre. Dette innebar å tenkte på hvilken versjon målgruppen vår var mest kjent med og komfortabel med å bruke. 
