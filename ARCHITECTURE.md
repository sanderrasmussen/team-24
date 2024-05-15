■ Beskriver arkitekturen som er benyttet i appen.
![Architecture](https://media.github.uio.no/user/9649/files/2eb25934-7818-4747-be2d-837260cd8c2d)


UI Elements --> State Holders --> Repositories --> DataSources \
HomeScreen --> HomeScreenViewModel --> LoationForecastRepository --> LocationForecastDataSource

■ Beskrivelse av hvordan viktige objektorienterte prinsipper som lav kobling og høy kohesjon samt design patterns som MVVM
og UDF er ivaretatt i løsningen burde også være med.



UDF: unidirectional dataflow: dataen går kun i én retning gjennom appens komponenter, og endringer i dataene propageres nedover gjennom lagene for å oppdatere grensesnittene. \
      - brukt mest i homescreen der vi viser informasjonen som hentes fra vm som henter fra repository, som henter fra datasource og som henter fra met.  \
      - Dataene begynner fra sentral kilden - MET Api-endepunktene.\
      Sendes videre til datasource som håndterer henting- sender til repository som håndterer formattering \
      ViewModellene håndterers hvordan de skal vises \
      Screen viser til brukere. 
      \
MVVM: Viewmodels som separerer brukergrensesnittet fra forretningslogikken og datahåndteringen. Håndterer presentasjonslogikken og separerer den fra visningslogikken. 

Lav kobling: objektet samarbeider med et lite antall andre objekter\
Bruker databasen for delte ressurser som f.eks klær og penger 

Høy kohesjon: objekt som har moderat ansvar og utfører et begrenset antall oppgaver innenfor et funksjonell områd\

■ Beskriv løsningen beregnet på lesere som skal jobbe med drift, vedlikehold og videreutvikling av løsningen. Beskriv hvilke teknologier og arkitektur som brukes i løsningen. Beskriv hvilket API-nivå (Android versjon) dere har valgt, og hvorfor.

Backend, Frontend, Database Logcat for logging og feilsøking, 
Utviklet med Kotlin \
Var API nivå 24, men siden mange av funksjonene våre krever høyere API nivå, så endret vi den til å være 26 \
@RequiresApi(Build.VERSION_CODES.O) : Noen funksjoner som krever API 26 \
Velge versjon som gir oss de funksjonalitetene vi trenger for appen vår. \ 
Velge en versjon som gir tilgang til de viktigste funksjonene for brukerne våre. Tenke på brukere, hvilke versjon er de målgruppen vår best kjent med? 


