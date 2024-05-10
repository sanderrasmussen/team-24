Beskrivelse og diagrammer

Modellering og systemdesign

● De viktigste funksjonelle kravene til applikasjonen bør beskrives – bruk gjerne use case diagram, samt sekvensdiagram og tekstlig beskrivelse av de viktigste use-casene.

[UseCase.pdf](https://github.uio.no/IN2000-V24/team-24/files/366/UseCase.pdf)


**Use case: Sjekk været** \
Primæraktør: Bruker \
Sekundæraktør: Metrologisk Institutt \
Prebetingelse: Brukeren ønsker å sjekke værmeldingen \
Postbetingelse: Brukeren har fått tilgang til værinformasjon enten for været i dag eller neste 6 dager 

**Hovedflyt:** 
1.	Bruker åpner applikasjonen for å sjekke været 
2.	Bruker trykker på værkort for nåværende timen for å se flere detaljer om været 
3.	Bruker klikker på neste 6 dager for å se været for de neste seks dagene 

**Alternativ flyt:** \
1a. Bruker har ikke tilgang til internett og informeres om at nåværende temperatur er ukjent \
1b. Bruker kobler til internett og åpner applikasjonen på nytt \
1c. Applikasjonen laster inn værinformasjon hentet fra metrologisk Institutt \
1d. Brukeren får tilgang til værmelding for dagen i dag og de neste 6 dagene


**Use case: Sjekk farevarsler** \
Primæraktør: Bruker \
Sekundæraktør: Metrologisk Institutt \
Prebetingelse: Brukeren ønsker å sjekke gjeldende farevarsler for deres lokasjon  \
Postbetingelse: Brukeren har fått tilgang til informasjon om gjeldende farevarsler for deres lokasjon 

**Hovedflyt:**
1.	Brukeren åpner applikasjonen for å sjekke farevarsler 
2.	Brukeren trykker på farevarsel ikonet for å få informasjon om gjeldende farevarsler 
3.	Applikasjonen henter gjeldende farevarsler fra Metrologisk Institutt for brukerens lokasjon 
4.	Applikasjonen viser informasjon om gjeldende farevarsler for brukerens lokasjon til brukeren 

**Alternativ flyt:**\
	3a. Metrologisk Institutt returnerer ingen farevarsler for brukerens lokasjon. \
3b. Applikasjon informerer brukeren med en toast melding om at det er ingen gjeldende farevarsler for deres lokasjon. 



**Use case: Bytt klær på avataren** \
Primæraktør: Bruker \
Sekundæraktør: Metrologisk Institutt \
Prebetingelse: Bruker ønsker å endre klær på avataren\
Postbetingelse: Brukeren får endret klær på avataren 

**Hovedflyt:**
1.	Bruker åpner applikasjonen og trykker på inventory 
2.	Bruker endrer klær på avataren 
3.	Tilfredsheten til avataren beregnes basert på værinformasjon fra Metrologisk Institutt og heat value til klærne 

**Alternativ flyt:** \
	1a. Bruker ønsker å kjøpe nye klær \
	1b. Bruker trykker inn på store screen og kjøper nye klær \
	1c. Nye klær legges inn i inventory og pengene trekkes fra brukerens bank 

1aa. Bruker har ikke nok penger til å kjøpe nye klær \
1ab. Bruker går til quiz og spiller spill for å tjene penger \
1ac. Bruker svarer riktig og tjener penger 



● Modelleringen bør også inneholde klassediagram som reflekterer use-case og sekvensdiagrammene.



● Andre diagrammer bør også være inkludert for å få frem andre perspektiver, for eksempel aktivitetsdiagram (flytdiagram) eller tilstandsdiagram.

