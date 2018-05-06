<p dir='rtl' align='right'>Maria Toomsalu 155531IABB</p>

# ClubService 

Järgnev dokumentatsioon kirjeldab treeningklubide veebiteenust. Selle kasutajateks võivad olla erinevad inimesed (treenerid-treenijad), kes on huvitatud, millised treeningud on erinevates treeningklubides (edaspidi klubides). Ülemressursiks on _Club_ ((treening)klubi) ja alamressursiks on _Training_ (treening).

Iga päringu kuvamiseks on vaja teada **token**-it. Vale tokeni korral andmeid ei kuvata.

Antud teenuse abil on võimalik sisestada erinevaid klubisid ja treeninguid ning treeninguid klubidesse. Juba lisatud klubisid, treeninguid ja treeninguid klubides ja klubisid saab süsteemist otsida, mille tulemusena need kuvatakse. Dubleerimise vältimiseks on lisamisoperatsioonid varustatud identifikaatoriga. Kui saata teistkordselt sama päring, kuvatakse eelnevale samalaadsele päringule koostatud vastus uuesti.

Käesolev dokumentatsioon on mõeldud teenuse funktsionaalsuse paremaks mõistmiseks. Lähemalt tutvustatakse SOAP ja REST API-sid. 

## SOAP API

**Teenuse nimetus:** ClubWebService

**API token:** salajane

**Disain:** XSD puhul kasutatakse *Venetian Blind* mustrit: kõik tüübid on globaalsed ja elemendid on lokaalsed. Operatsioonide ja elementide nimetamiseks kasutatakse *lower camelCase* stiili ning need on teineteisega vastavuses (operatsioonile getClub vastab getTrainingClubResponse, mille sisuks on ClubType). Elementide puhul on tegemist XML Schema andmetüüpidega. Lisaks on elementide puhul defineeritud lihttüübid (*simpleType*) ja komplekstüübid (*complexType*).

**Lihttüübid:**

| Parameeter | Andmetüüp | Võimalikud väärtused |
|:--- |:--- |:---: |
| trainingStyleType  | String | "strength", "cardio", "bodyAndMind" |
| hasRelatedTrainingsType | String | "yes", "no" |
| clubStatusType | String | "open", "closed" |
| statusType | String | "active", "inactive" |

**Komplekstüübid:**

* **clubType**

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| id  | Integer | - | Treeningklubi unikaalne identifikaator, mis määratakse süsteemi poolt automaatselt. |
| clubName | String |+ | Treeningklubi nimi. |
| clubCity | String |+ | Linn, kus treeningklubi asub.|
| clubCountry | String |+ | Riik, kus treeningklubi asub.|
| clubStatus | clubStatusType |+ | Kirjeldab, kas klubi on avatud või suletud. |
| clubTrainingList | clubTrainingListType |+ | Nimekiri klubis olevatest treeningutest. |

* **trainingType**

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| id  | Integer | -  | Treeningu unikaalne identifikaator, mis määratakse süsteemi poolt automaatselt. |
| trainingName | String |+ | Treeningu nimetus. |
| trainerName | String |+ | Treeneri nimi. |
| trainingStyle | String |+ | *trainingStyleType*. Treeningu stiil.|
| durationInMins | Integer |+ | Arvuline väärtus, mis kirjeldab, kui kaua treening kestab.|
| totalPlaces | Integer |+ | Treeningu kohtade arv. |

* **clubTrainingType**

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| training  | trainingType | + | Soovitud treeningklubi treening |
| startDate | date |+ | Kuupäev, millest alates on antud klubis treening toimunud. |
| endDate | date |+ | Treeningu lõpukuupäev.|
| status | statusType |+ | Treeningu staatus. Aktiivne, kui treeningud toimuvad klubis ja mitteaktiivne, kui antud klubis antud treeningut ei ole. |

**clubTrainingListType - clubTraining**

| Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:---: |:--- |
| clubTrainingType | - | Küsitud treeningklubi treeningute nimekirja kuuluv treening. |

### Operatsioonid

NB! *token* on vajalik vaid päringu õnnestumiseks. Response ei kuva *token*-i väärtust.

#### addClub
..võimaldab süsteemi lisada uue treeningklubi koos seda kirjeldavate parameetritega. 

**Sisend:** addClubRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer | + | Päringu unikaalne identifikaator. |
| clubName | String |+ | Treeningklubi nimi. |
| clubCity | String |+ | Linn, kus treeningklubi asub.|
| clubCountry | String |+ | Riik, kus treeningklubi asub.|
| clubStatus | clubStatusType |+ | Kirjeldab, kas klubi on avatud või suletud. |

```xml

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:club="http://www.ttu.ee/idu0075/2015/ws/club">
   <soapenv:Header/>
   <soapenv:Body>
      <club:addClubRequest>
         <club:token>salajane</club:token>
         <club:requestCode>1</club:requestCode>
         <club:clubName>MyFitness Viru</club:clubName>
         <club:clubCity>Tallinn</club:clubCity>
         <club:clubCountry>Eesti</club:clubCountry>
         <club:clubStatus>open</club:clubStatus>
      </club:addClubRequest>
   </soapenv:Body>
</soapenv:Envelope>

```

**Väljund:** addClubResponse

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| responseCode | Integer | + | Päringust saadud unikaalne identifikaator, millega kontrollitakse *Idempotent Capability* mustrit.|
| club | clubType | +  | Päringu põhjal genereeritud treeningklubiobjekt. Sisaldab sisendis antud infot ja klubile antud id väärtust. |

```xml

<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <addClubResponse xmlns="http://www.ttu.ee/idu0075/2015/ws/club">
         <responseCode>1</responseCode>
         <club>
            <id>1</id>
            <clubName>MyFitness Viru</clubName>
            <clubCity>Tallinn</clubCity>
            <clubCountry>Eesti</clubCountry>
            <clubStatus>open</clubStatus>
            <clubTrainingList/>
         </club>
      </addClubResponse>
   </S:Body>
</S:Envelope>

```

#### getClub
..võimaldab süsteemist küsida treeningklubi sellele määratud ID põhjal. 

**Sisend:** getClubRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| id  | Integer | + | Treeningklubi unikaalne identifikaator. |

```xml

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:club="http://www.ttu.ee/idu0075/2015/ws/club">
   <soapenv:Header/>
   <soapenv:Body>
      <club:getClubRequest>
         <club:token>salajane</club:token>
         <club:id>1</club:id>
      </club:getClubRequest>
   </soapenv:Body>
</soapenv:Envelope>

```

**Väljund:** getClubResponse

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| getClubResponse  | clubType | + | Treeningklubi, mille id väärtus vastab sisendis antule. |

```xml

<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <getClubResponse xmlns="http://www.ttu.ee/idu0075/2015/ws/club">
         <id>1</id>
         <clubName>MyFitness Viru</clubName>
         <clubCity>Tallinn</clubCity>
         <clubCountry>Eesti</clubCountry>
         <clubStatus>open</clubStatus>
         <clubTrainingList/>
      </getClubResponse>
   </S:Body>
</S:Envelope>

```

#### getClubList
..võimaldab süsteemist küsida treeningklubide nimekirja. Otsingut saab filtreerida linna, riigi, nime ja treeningute olemasolu järgi.

**Sisend:** getClubListRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| clubCity | String |- | Linn, kus treeningklubi asub. |
| clubCountry | String |- | Riik, kus treeningklubi asub. |
| clubName | String | - | Treeningklubi nimi. |
| hasRelatedTrainings | hasRelatedTrainingsType | - | Võimalikud väärtused: "yes", "no". |

```xml

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:club="http://www.ttu.ee/idu0075/2015/ws/club">
   <soapenv:Header/>
   <soapenv:Body>
      <club:getClubListRequest>
         <club:token>salajane</club:token>      
         <club:clubCity>Tallinn</club:clubCity>
      </club:getClubListRequest>
   </soapenv:Body>
</soapenv:Envelope>

```

**Väljund:** getClubListResponse

| Parameeter  | Andmetüüp  | Kirjeldus |
|:--- |:--- |:---: |
| club  | clubType | (0 või rohkem) Treeningklubi, mis on eelnevalt sisestatud, millel on id ja sobib päringus esitatud väärtustega. |

```xml

<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <getClubListResponse xmlns="http://www.ttu.ee/idu0075/2015/ws/club">
         <club>
            <id>1</id>
            <clubName>MyFitness Viru</clubName>
            <clubCity>Tallinn</clubCity>
            <clubCountry>Eesti</clubCountry>
            <clubStatus>open</clubStatus>
            <clubTrainingList/>
         </club>
         <club>
            <id>3</id>
            <clubName>MyFitness Postimaja</clubName>
            <clubCity>Tallinn</clubCity>
            <clubCountry>Eesti</clubCountry>
            <clubStatus>open</clubStatus>
            <clubTrainingList/>
         </club>
      </getClubListResponse>
   </S:Body>
</S:Envelope>

```

#### addTraining
..võimaldab süsteemi lisada uue treeningu koos seda kirjeldavate parameetritega. 

**Sisend:** addTrainingRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer | + | Päringu unikaalne identifikaator. |
| trainingName | String |+ | Treeningu nimetus. |
| trainerName | String |+ | Treeneri nimi. |
| trainingStyle | String |+ | *trainingStyleType*. Treeningu stiil.|
| durationInMins | Integer |+ | Arvuline väärtus, mis kirjeldab, kui kaua treening kestab.|
| totalPlaces | Integer |+ | Treeningu kohtade arv. |

```xml

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:club="http://www.ttu.ee/idu0075/2015/ws/club">
   <soapenv:Header/>
   <soapenv:Body>
      <club:addTrainingRequest>
         <club:token>salajane</club:token>
         <club:requestCode>1</club:requestCode>
         <club:trainingName>Nike Training Club</club:trainingName>
         <club:trainerName>Mari Maasikas</club:trainerName>
         <club:trainingStyle>strength</club:trainingStyle>
         <club:durationInMins>55</club:durationInMins>
         <club:totalPlaces>45</club:totalPlaces>
      </club:addTrainingRequest>
   </soapenv:Body>
</soapenv:Envelope>

```

**Väljund:** addTrainingResponse

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| responseCode | Integer | +  | Päringust saadud unikaalne identifikaator, millega kontrollitakse *Idempotent Capability* mustrit.|
| training | trainingType | +  | Päringu põhjal genereeritud treeninguobjekt. Sisaldab sisendis antud infot ja treeningule antud id väärtust. |

```xml

<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <addTrainingResponse xmlns="http://www.ttu.ee/idu0075/2015/ws/club">
         <responseCode>1</responseCode>
         <training>
            <id>1</id>
            <trainingName>Nike Training Club</trainingName>
            <trainerName>Mari Maasikas</trainerName>
            <trainingStyle>strength</trainingStyle>
            <durationInMins>55</durationInMins>
            <totalPlaces>45</totalPlaces>
         </training>
      </addTrainingResponse>
   </S:Body>
</S:Envelope>

```

#### getTraining
..võimaldab küsida treeningut koos seda kirjeldavate parameetritega. 

**Sisend:** getTrainingRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| id | Integer | + | Treeningu unikaalne identifikaator. |

```xml

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:club="http://www.ttu.ee/idu0075/2015/ws/club">
   <soapenv:Header/>
   <soapenv:Body>
      <club:getTrainingRequest>
         <club:token>salajane</club:token>
         <club:id>1</club:id>
      </club:getTrainingRequest>
   </soapenv:Body>
</soapenv:Envelope>

```

**Väljund:** getTrainingResponse

| Andmetüüp | Kirjeldus |
|:--- |:--- |
| trainingType | Päringu põhjal genereeritud treeninguobjekt. Sisaldab sisendis antud infot ja treeningule antud id väärtust.|

```xml

<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <getTrainingResponse xmlns="http://www.ttu.ee/idu0075/2015/ws/club">
         <id>1</id>
         <trainingName>Nike Training Club</trainingName>
         <trainerName>Mari Maasikas</trainerName>
         <trainingStyle>strength</trainingStyle>
         <durationInMins>55</durationInMins>
         <totalPlaces>45</totalPlaces>
      </getTrainingResponse>
   </S:Body>
</S:Envelope>

```

#### getTrainingList
..võimaldab küsida kõiki treeninguid sisaldavat nimekirja. 

**Sisend:** getTrainingListRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| trainingName | String | - | Treeningu nimetus. |
| trainerName | String | - | Treeneri nimi. |
| trainingStyle | trainingStyleType | - | Treeningu stiil.|
| durationInMins | Integer | - | Arvuline väärtus, mis kirjeldab, kui kaua treening kestab.|
| totalPlaces | Integer | - | Treeningu kohtade arv. |

```xml

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:club="http://www.ttu.ee/idu0075/2015/ws/club">
   <soapenv:Header/>
   <soapenv:Body>
      <club:getTrainingListRequest>
         <club:token>salajane</club:token>
         <club:trainingStyle>strength</club:trainingStyle>
      </club:getTrainingListRequest>
   </soapenv:Body>
</soapenv:Envelope>

```

**Väljund:** getTrainingListResponse

| Parameeter  | Andmetüüp  | Kirjeldus |
|:--- |:--- |:---: |
| training  | trainingType | (0 või rohkem) Treeningklubi, mis on eelnevalt sisestatud, millel on id ja sobib päringus esitatud väärtustega. |

```xml

<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <getTrainingListResponse xmlns="http://www.ttu.ee/idu0075/2015/ws/club">
         <training>
            <id>1</id>
            <trainingName>Nike Training Club</trainingName>
            <trainerName>Mari Maasikas</trainerName>
            <trainingStyle>strength</trainingStyle>
            <durationInMins>55</durationInMins>
            <totalPlaces>45</totalPlaces>
         </training>
         <training>
            <id>2</id>
            <trainingName>Ringtreening</trainingName>
            <trainerName>Mati Maasikas</trainerName>
            <trainingStyle>strength</trainingStyle>
            <durationInMins>50</durationInMins>
            <totalPlaces>12</totalPlaces>
         </training>
      </getTrainingListResponse>
   </S:Body>
</S:Envelope>

```

#### addClubTraining
..võimaldab süsteemist küsida treeningute nimekirja ühes treeningklubis.

**Sisend:** addClubTrainingRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer | + | Päringu unikaalne identifikaator. |
| clubId | Integer | + | Treeningklubi identifikaator. |
| trainingId | Integer | + | Treeningu identifikaator. |
| startDate | date | + | Kuupäev, alates millest treening toimub. |
| endDate | date | + | Kuupäev, alates milleni treening toimub. |
| status | statusType | + | Näitab, kas antud klubis hetkel treening toimub või mitte. |

```xml

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:club="http://www.ttu.ee/idu0075/2015/ws/club">
   <soapenv:Header/>
   <soapenv:Body>
      <club:addClubTrainingRequest>
         <club:token>salajane</club:token>
         <club:requestCode>1</club:requestCode>
         <club:clubId>1</club:clubId>
         <club:trainingId>2</club:trainingId>
         <club:startDate>2015-01-01</club:startDate>
         <club:endDate>2019-12-31</club:endDate>
         <club:status>active</club:status>
      </club:addClubTrainingRequest>
   </soapenv:Body>
</soapenv:Envelope>

```

**Väljund:** addClubTrainingResponse

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| responseCode | Integer | +  | Päringust saadud unikaalne identifikaator, millega kontrollitakse *Idempotent Capability* mustrit.|
| clubTraining | clubTrainingType | + | Kuvab küsitud treeningklubi ja sisaldab selles antavaid treeninguid. |

```xml

<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <addClubTrainingResponse xmlns="http://www.ttu.ee/idu0075/2015/ws/club">
         <responseCode>1</responseCode>
         <clubTraining>
            <training>
               <id>2</id>
               <trainingName>Ringtreening</trainingName>
               <trainerName>Mati Maasikas</trainerName>
               <trainingStyle>strength</trainingStyle>
               <durationInMins>50</durationInMins>
               <totalPlaces>12</totalPlaces>
            </training>
            <startDate>2015-01-01</startDate>
            <endDate>2019-12-31</endDate>
            <status>active</status>
         </clubTraining>
      </addClubTrainingResponse>
   </S:Body>
</S:Envelope>

```

#### getClubTraining
..võimaldab süsteemist küsida kõiki treeninguid.

**Sisend:** getClubTrainingListRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| clubId | Integer | + | Treeningklubi identifikaator. |

```xml

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:club="http://www.ttu.ee/idu0075/2015/ws/club">
   <soapenv:Header/>
   <soapenv:Body>
      <club:getClubTrainingListRequest>
         <club:token>salajane</club:token>
         <club:clubId>1</club:clubId>
      </club:getClubTrainingListRequest>
   </soapenv:Body>
</soapenv:Envelope>

```

**Väljund:** getClubTrainingListResponse

| Andmetüüp  | Kirjeldus |
|:--- |:--- |
| clubTrainingListType | (0 või rohkem) Treening, mis on eelnevalt sisestatud, millel on id ja on küsitud treeningklubi treeningute seas. |

```xml

<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <getClubTrainingListResponse xmlns="http://www.ttu.ee/idu0075/2015/ws/club">
         <clubTraining>
            <training>
               <id>2</id>
               <trainingName>Ringtreening</trainingName>
               <trainerName>Mati Maasikas</trainerName>
               <trainingStyle>strength</trainingStyle>
               <durationInMins>50</durationInMins>
               <totalPlaces>12</totalPlaces>
            </training>
            <startDate>2015-01-01</startDate>
            <endDate>2019-12-31</endDate>
            <status>active</status>
         </clubTraining>
      </getClubTrainingListResponse>
   </S:Body>
</S:Envelope>

```

## REST API

**Teenuse nimetus:** ClubWebService

**API token:** salajane

**Disainipõhimõtted:** Päringu ja vastuse formaadiks on JSON. REST API pöördub päringute tegemiseks SOAP-teenuse poole ja teisendab  andmed vajaminevale kujule. Operatsioonid ja elemendid on samad, mis SOAP API puhul.  

### Operatsioonid

#### addClub
..võimaldab süsteemi lisada uue treeningklubi koos seda kirjeldavate parameetritega. 

**HTTP meetod:** POST

**Ressurss:** /clubs

**Päringu õnnestumiseks vajaminevad parameetrid:**

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer | + | Päringu tuvastamiseks selle unikaalne identifikaator. |

**Näidis URL:** <http://localhost:8080/ClubWebApplication/webresources/clubs/?token=salajane>

**Sisendandmed:**

Sisendiks on **addClubRequest** objekt.

**Näidispäring:**

```json

{
    "clubName": "Fitlife Kuressaare",
    "clubCity": "Kuressaare",
    "clubCountry": "Estonia",
    "clubStatus": "open"
}

```

**Väljundandmed:**

**addClubResponse** objekt, mis sisaldab lisaks:

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| responseCode  | Integer | +  | Päringus antud requestCode. |
| id | Integer | + | Treeningklubi identifikaator, antakse süsteemi poolt. |

**Näidisväljund:**

```json

{
   "responseCode": 3,
   "club":    {
      "id": 3,
      "clubName": "Fitlife Kuressaare",
      "clubCity": "Kuressaare",
      "clubCountry": "Estonia",
      "clubStatus": "open",
      "clubTrainingList": {"clubTraining": []}
   }
}

```

#### getClub
..võimaldab süsteemist küsida treeningklubi sellele määratud ID põhjal. 

**HTTP meetod:** GET

**Ressurss:** /clubs/{id}

**Päringu õnnestumiseks vajaminevad parameetrid:**

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer | + | Päringu tuvastamiseks selle unikaalne identifikaator. |

**Näidis URL:** <http://localhost:8080/ClubWebApplication/webresources/clubs/1/?token=salajane>

**Väljundandmed:**

**ClubType** objekt.

**Näidisväljund:**

```json

{
   "id": 1,
   "clubName": "MyFitness Viru",
   "clubCity": "Tallinn",
   "clubCountry": "Eesti",
   "clubStatus": "open",
   "clubTrainingList": {"clubTraining": [   {
      "training":       {
         "id": 2,
         "trainingName": "Ringtreening",
         "trainerName": "Mati Maasikas",
         "trainingStyle": "strength",
         "durationInMins": 50,
         "totalPlaces": 12
      },
      "startDate": 1420063200000,
      "endDate": 1577743200000,
      "status": "active"
   }]}
}

```

#### getClubList
..võimaldab süsteemist küsida treeningklubide nimekirja. Otsingut saab filtreerida linna, riigi, nime ja treeningute olemasolu järgi.

**HTTP meetod:** GET

**Ressurss:** /clubs/

**Päringu õnnestumiseks vajaminevad parameetrid:**

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| clubCity | String |- | Linn, kus treeningklubi asub. |
| clubCountry | String |- | Riik, kus treeningklubi asub. |
| clubName | String | - | Treeningklubi nimi. |
| hasRelatedTrainings | hasRelatedTrainingsType | - | Võimalikud väärtused: "yes", "no". |

**Näidis URL:** <http://localhost:8080/ClubWebApplication/webresources/clubs?token=salajane&city=Kuressaare>

**Väljundandmed:**

**getClubListResponse** nimekiri klubidest, mis sobivad esitatud parameetritega.

**Näidisväljund:**

```json

{"club": [{
   "id": 3,
   "clubName": "Fitlife Kuressaare",
   "clubCity": "Kuressaare",
   "clubCountry": "Estonia",
   "clubStatus": "open",
   "clubTrainingList": {"clubTraining": []}
}]}

```

#### addTraining
..võimaldab süsteemi lisada uue treeningu koos seda kirjeldavate parameetritega. 

**HTTP meetod:** POST

**Ressurss:** /trainings

**Päringu õnnestumiseks vajaminevad parameetrid:**

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer | + | Päringu tuvastamiseks selle unikaalne identifikaator. |

**Näidis URL:** <http://localhost:8080/ClubWebApplication/webresources/trainings/?token=salajane>

**Sisendandmed:**

Sisendiks on **addTrainingRequest** objekt.

**Näidispäring:**

```json

{
    "trainingName": "Dance",
    "trainerName": "Kati Karu",
    "trainingStyle": "bodyAndMind",
    "durationInMins": 65,
    "totalPlaces": 30
}

```

**Väljundandmed:**

**addTrainingResponse** objekt, mis sisaldab lisaks:

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| responseCode  | Integer | +  | Päringus antud requestCode. |
| id | Integer | + | Treeningklubi identifikaator, antakse süsteemi poolt. |

**Näidisväljund:**

```json

{
   "responseCode": 3,
   "training":    {
      "id": 4,
      "trainingName": "Dance",
      "trainerName": "Kati Karu",
      "trainingStyle": "bodyAndMind",
      "durationInMins": 65,
      "totalPlaces": 30
   }
}

```

#### getTraining
..võimaldab küsida treeningut koos seda kirjeldavate parameetritega. 

**HTTP meetod:** GET

**Ressurss:** /trainings/{id}

**Päringu õnnestumiseks vajaminevad parameetrid:**

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer | + | Päringu tuvastamiseks selle unikaalne identifikaator. |

**Näidis URL:** <http://localhost:8080/ClubWebApplication/webresources/trainings/1/?token=salajane>

**Väljundandmed:**

**TrainingType** objekt.

**Näidisväljund:**

```json

{
   "id": 1,
   "trainingName": "Nike Training Club",
   "trainerName": "Mari Maasikas",
   "trainingStyle": "strength",
   "durationInMins": 55,
   "totalPlaces": 45
}

```

#### getTrainingList
..võimaldab küsida kõiki treeninguid sisaldavat nimekirja. 

**HTTP meetod:** GET

**Ressurss:** /trainings/

**Päringu õnnestumiseks vajaminevad parameetrid:**

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| trainingName | String | - | Treeningu nimetus. |
| trainerName | String | - | Treeneri nimi. |
| trainingStyle | trainingStyleType | - | Treeningu stiil.|
| durationInMins | Integer | - | Arvuline väärtus, mis kirjeldab, kui kaua treening kestab.|
| totalPlaces | Integer | - | Treeningu kohtade arv. |

**Näidis URL:** <http://localhost:8080/ClubWebApplication/webresources/trainings?token=salajane>

**Väljundandmed:**

**getTrainingListResponse** nimekiri treeningutest, mis sobivad esitatud parameetritega.

**Näidisväljund:**

```json

{"training": [{
    "id":1,
    "trainingName":"Nike Training Club",
    "trainerName":"Mari Maasikas",
    "trainingStyle":"strength",
    "durationInMins":55,
    "totalPlaces":45
    }
    ]}

```

#### addClubTraining
..võimaldab süsteemist küsida treeningute nimekirja ühes treeningklubis.

#### getClubTraining
..võimaldab süsteemist küsida kõiki treeninguid.
