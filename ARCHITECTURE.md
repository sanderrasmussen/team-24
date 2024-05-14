■ Beskriver arkitekturen som er benyttet i appen.


UI Elements --> State Holders --> Repositories --> DataSources \
HomeScreen --> HomeScreenViewModel --> LoationForecastRepository --> LocationForecastDataSource

■ Beskrivelse av hvordan viktige objektorienterte prinsipper som lav kobling og høy kohesjon samt design patterns som MVVM
og UDF er ivaretatt i løsningen burde også være med.

UDF: unidirectional dataflow: dataen går kun i én retning gjennom appens komponenter, og endringer i dataene propageres nedover gjennom lagene for å oppdatere grensesnittene. \
      - brukt mest i homescreen der vi viser informasjonen som hentes fra vm som henter fra repository, som henter fra datasource og som henter fra met.  \
      \
MVVM: Viewmodels som separerer brukergrensesnittet fra forretningslogikken og datahåndteringen. Håndterer presentasjonslogikken og separerer den fra visningslogikken. \

Lav kobling: objektet samarbeider med et lite antall andre objekter\

Høy kohesjon: objekt som har moderat ansvar og utfører et begrenset antall oppgaver innenfor et funksjonell område\

■ Beskriv løsningen beregnet på lesere som skal jobbe med drift, vedlikehold og videreutvikling av løsningen. Beskriv hvilke teknologier og arkitektur som brukes i løsningen. Beskriv hvilket API-nivå (Android versjon) dere har valgt, og hvorfor.

Utviklet med Kotlin \
API nivå 24 \
@RequiresApi(Build.VERSION_CODES.O) : Noen funksjoner som krever API 26 


