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
| trainingCapacity | Integer |+ | Treeningu kohtade arv. |

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
| trainingCapacity | Integer |+ | Treeningu kohtade arv. |

**Väljund:** addTrainingResponse

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| responseCode | Integer | +  | Päringust saadud unikaalne identifikaator, millega kontrollitakse *Idempotent Capability* mustrit.|
| training | trainingType | +  | Päringu põhjal genereeritud treeninguobjekt. Sisaldab sisendis antud infot ja treeningule antud id väärtust. |

#### getTraining
..võimaldab küsida treeningut koos seda kirjeldavate parameetritega. 

**Sisend:** getTrainingRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| id | Integer | + | Treeningu unikaalne identifikaator. |

**Väljund:** getTrainingResponse

| Andmetüüp | Kirjeldus |
|:--- |:--- |
| trainingType | Päringu põhjal genereeritud treeninguobjekt. Sisaldab sisendis antud infot ja treeningule antud id väärtust.|

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
| trainingCapacity | Integer | - | Treeningu kohtade arv. |

**Väljund:** getTrainingListResponse

| Parameeter  | Andmetüüp  | Kirjeldus |
|:--- |:--- |:---: |
| training  | trainingType | (0 või rohkem) Treeningklubi, mis on eelnevalt sisestatud, millel on id ja sobib päringus esitatud väärtustega. |

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

**Väljund:** addClubTrainingResponse

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| responseCode | Integer | +  | Päringust saadud unikaalne identifikaator, millega kontrollitakse *Idempotent Capability* mustrit.|
| clubTraining | clubTrainingType | + | Kuvab küsitud treeningklubi ja sisaldab selles antavaid treeninguid. |

#### getClubTraining
..võimaldab süsteemist küsida kõiki treeninguid.

**Sisend:** getClubTrainingListRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| clubId | Integer | + | Treeningklubi identifikaator. |

**Väljund:** getClubTrainingListResponse

| Andmetüüp  | Kirjeldus |
|:--- |:--- |
| clubTrainingListType | (0 või rohkem) Treening, mis on eelnevalt sisestatud, millel on id ja on küsitud treeningklubi treeningute seas. |

## REST API

