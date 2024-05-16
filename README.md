## Team members
Halvor Njåsad  
Ingrid Ljosland Waale  
Sander Rasmussen  
Hifza Mehmood  
Uma Shivali Kumar  
Mila Toneff  



## README

Brukerdokumentasjonen, Produktokumentasjonen og Prosessdokumentasjonen ligger i Rapport filen:[IN2000 - Rapport.pdf](https://github.uio.no/IN2000-V24/team-24/files/383/IN2000.-.Rapport.pdf)
  \
Applikasjonen VærBuddy er utviklet for Android mobilenheter med minimum API nivå 26. \
For å kunne bruke appen gjennom en emulator i f.eks Android Studio må det sørges for at operativsystemet tillater emulering. Emulering vil mest sannsynlig fungere uten vesentlige endringer i systeminnstillingene. 

Det anbefales å emulere gjennom Android Studio for å kunne bruke emulator. Fysiske Android enheter med tilstrekkelig API-nivå kan også brukes til å kjøre appen. 

Siden appen ennå ikke er tilgjengelig for offentlig bruk, kan den fås gjennom filene i mappen "Team24_app" på Devilry. 
For å installere og komme i gang med appen: 
- Last ned zip filen fra Devilry og pakk den ut. 
- Åpne filen og bruke Android Studio til å åpne team_24 mappen som ligger inne i filen. 
- Tillat synkronisering av Android SDK. 
- Åpne prosjektmappen i Android Studio og synkroniser projektet med grade filer. Dette gjøres ved å trykke "Sync now" knappen øverst til høyre. 
- Etter ferdig gradle build kan appen kjøres på en emulator med Google tjenester og API-nivå 26 eller høyere. 

Biblioteker som er brukt: \
Ktor \
Coil \
Google Play Services \
Room \
Jetpack Compose \
Jetpack Compose Navigation \

Redegjøring av Warnings \
Vi får primært følgende warnings i vår applikasjon:
Remove empty primary constructor- Ligger i Geometry \
Noen metoder og variabler kan være private - Dette er noe som kommer opp i de fleste filene som f.eks MainActivity \
Noen variabler er redundante- variabler som ikke brukes, kommer opp i flere filer \
'smallTopAppBarColors(Color = ..., Color = ..., Color = ..., Color = ..., Color = ...): TopAppBarColors' is deprecated. Use topAppBarColors instead - Denne får vi .eks QuestionsScreen \
xxx is deprecated. Use yyy instead - kommer opp i en del filer. Kode som fungerer men som blir fjernet i framtidige versjoner\
Unnecessary non-null assertion (!!) on a non-null receiver of type Int - kommer opp i StoreScreen.kt. Int kan ikke være null \

