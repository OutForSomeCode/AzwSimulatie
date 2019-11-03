AzSimulatie
-------------
[![Total alerts](https://img.shields.io/lgtm/alerts/g/AnotherFoxGuy/AzwSimulatie.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/AnotherFoxGuy/AzwSimulatie/alerts/)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/AnotherFoxGuy/AzwSimulatie.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/AnotherFoxGuy/AzwSimulatie/context:java)
[![Language grade: JavaScript](https://img.shields.io/lgtm/grade/javascript/g/AnotherFoxGuy/AzwSimulatie.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/AnotherFoxGuy/AzwSimulatie/context:javascript)

## Required Software

Download en installeer eerst de vereiste software:

* Yarn https://yarnpkg.com/
* Nodejs LTS https://nodejs.org/
* Gradle https://gradle.org/
* RavenDB https://ravendb.net/


## Database setup

- Ga naar waar je het hebt uitgepakt, selecteer het pad(pijl) en typ daar powershell
![install_raven_0](docs/img/install_raven_0.png?raw=true)
- in de powershell window typ je: ./run.ps1
in de browser verschijnt het volgende:
![install_raven_1](docs/img/install_raven_1.png?raw=true)

- kies de unsecured optie
![install_raven_2](docs/img/install_raven_2.png?raw=true)
- vul bij poort 8181 in
![install_raven_3](docs/img/install_raven_3.png?raw=true)
- na de instalatie moet je nog even een DB aanmaken met de volgende naam: AmazonSimulatie
- als je de server stoppen wilt ga je naar de console window die nog open staat
![install_raven_4](docs/img/install_raven_4.png?raw=true)  
typ: exit

## Running

## De backend starten

- Open een cmd window in de root van het project.  
- Run `gradle bootrun` om de spring server te starten.

### Het frontend starten

- Open een cmd window in de root van het project.  
- Run `yarn insttall` om alle benodigde packages te installeren.  
- Run `yarn starto` om de webpack dev server te starten.   
- Als het goed is, zou nu een webbrowser window openen met het addres `localhost:8080`
