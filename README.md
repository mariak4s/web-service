<p dir='rtl' align='right'>Maria Toomsalu 155531IABB</p>

# ClubService 

Järgnev dokumentatsioon kirjeldab treeningklubide veebiteenust. Selle kasutajateks võivad olla erinevad inimesed (treenerid-treenijad), kes on huvitatud, millised treeningud on erinevates treeningklubides (edaspidi klubides). Ülemressursiks on _Club_ ((treening)klubi) ja alamressursiks on _Training_ (treening)

Iga päringu kuvamiseks on vaja teada **token**-it. Vale tokeni korral andmeid ei kuvata.

Antud teenuse abil on võimalik sisestada erinevaid klubisid ja treeninguid ning treeninguid klubidesse. Juba lisatud klubisid, treeninguid ja treeninguid klubides ja klubisid saab süsteemist otsida, mille tulemusena need kuvatakse.

Käesolev dokumentatsioon on mõeldud teenuse funktsionaalsuse paremaks mõistmiseks. Lähemalt tutvustatakse SOAP ja REST API-sid. 

## SOAP API

**Teenuse nimetus:** ClubWebService

**API token:** salajane

**Disain:** XSD puhul kasutatakse *Venetian Blind* mustrit: kõik tüübid on globaalsed ja elemendid on lokaalsed. Operatsioonide ja elementide nimetamiseks kasutatakes *lower camelCase* stiili ning need on teineteisega vastavuses (operatsioonile getClub vastab getTrainingClubResponse, mille sisuks on ClubType). Elementide puhul on tegemist XML Schema andmetüüpidega. Lisaks on elementide puhul defineeritud lihttüübid (*simpleType*) ja komplekstüübid (*complexType*).

**Lihttüübid:**

| Parameeter | Andmetüüp | Võimalikud väärtused |
|:--- |:--- |:---: |
| trainingStyleType  | String | "strength", "cardio", "body&mind" |
| hasRelatedTrainingsType | String | "yes", "no" |
| clubStatusType | String | "open", "closed" |
| statusType | String | "active", "inactive" |

**Komplekstüübid:**

* **trainingType**

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| id  | Integer | -  | Treeningu unikaalne identifikaator, mis määratakse süsteemi poolt automaatselt. |
| name | String |+ | Treeningu nimetus. |
| nameOfTheTrainer | String |+ | Treeneri nimi. |
| trainingStyle | String |+ | *trainingStyleType*. Treeningu stiil.|
| durationInMins | Integer |+ | Arvuline väärtus, mis kirjeldab, kui kaua treening kestab.|
| trainingCapacity | Integer |+ | Treeningu kohtade arv. |

* **clubType**

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| id  | Integer | - | Treeningklubi unikaalne identifikaator, mis määratakse süsteemi poolt automaatselt. |
| clubName | String |+ | Treeningklubi nimi. |
| clubCountry | String |+ | Riik, kus treeningklubi asub.|
| clubCity | String |+ | Linn, kus treeningklubi asub.|
| clubStatus | clubStatusType |+ | Kirjeldab, kas klubi on avatud või suletud. |
| clubTrainingList | clubTrainingListType |+ | Nimekiri klubi treeningutest. |

* **clubTrainingType**

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| training  | trainingType | + | Soovitud treeningklubi treening |
| startDate | date |+ | Kuupäev, millest alates on antud klubis treening toimunud. |
| endDate | date |+ | Treeningu lõpukuupäev.|
| status | statusType |+ | Treeningu staatus. Aktiivne, kui treeningud toimuvad klubis ja mitteaktiivne, kui antud klubis antud treeningut ei ole. |

clubTrainingListType - clubTraining clubTrainingType Küsitud treeningklubi treeningute nimekirja kuuluv treening.

### Operatsioonid

NB! *token* on vajalik vaid päringu õnnestumiseks. Response ei kuva *token*-i väärtust.

#### addClub
..võimaldab süsteemi lisada uue treeningklubi koos seda kirjeldavate parameetritega. 

**Sisend:** addClubRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer |- | Päringu unikaalne identifikaator. |
| clubName | String |+ | Treeningklubi nimi. |
| clubCountry | String |+ | Riik, kus treeningklubi asub.|
| clubCity | String |+ | Linn, kus treeningklubi asub.|
| clubStatus | clubStatusType |+ | Kirjeldab, kas klubi on avatud või suletud. |

**Väljund:** addClubResponse

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| responseCode | Integer | + | Päringust saadud unikaalne identifikaator, millega kontrollitakse *Idempotent Capability* mustrit.|
| club | clubType | +  | Päringu põhjal genereeritud treeningklubiobjekt. Sisaldab sisendis antud infot ja klubile antud id väärtust. |

#### getClub
..võimaldab süsteemist küsida treeningklubi sellele määratud ID põhjal. 

**Sisend:** getClubRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| id  | Integer | + | Treeningklubi unikaalne identifikaator. |

**Väljund:** getClubResponse

| Parameeter  | Andmetüüp | Kohustuslik  | Võimalikud väärtused |
|:--- |:--- |:---: |:--- |
| getClubResponse  | clubType | + | Treeningklubi, mille id väärtus vastab sisendis antule. |

#### getClubList
..võimaldab süsteemist küsida treeningklubide nimekirja. Otsingut saab filtreerida riigi, linna ja treeningute olemasolu järgi.

**Sisend:** getClubListRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| clubCity | String |- | Linn, kus treeningklubi asub. |
| clubName | String | - | Treeningklubi nimi. |
| hasRelatedTrainings | hasRelatedTrainingsType | - | Võimalikud väärtused: "yes", "no". |

**Väljund:** getClubListResponse

| Parameeter  | Andmetüüp  | Kirjeldus |
|:--- |:--- |:---: |
| club  | clubType | (0 või rohkem) Treeningklubi, mis on eelnevalt sisestatud, millel on id ja sobib päringus esitatud väärtustega. |

#### addTraining
..võimaldab süsteemi lisada uue treeningu koos seda kirjeldavate parameetritega. 

**Sisend:** addTrainingRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer | + | Päringu unikaalne identifikaator. |
| name | String |+ | Treeningu nimetus. |
| nameOfTheTrainer | String |+ | Treeneri nimi. |
| trainingStyle | String |+ | *trainingStyleType*. Treeningu stiil.|
| durationInMins | Integer |+ | Arvuline väärtus, mis kirjeldab, kui kaua treening kestab.|
| trainingCapacity | Integer |+ | Treeningu kohtade arv. |

**id** genereeritakse süsteemi poolt ja pole seega määratud

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
| name | String | - | Treeningu nimetus. |
| nameOfTheTrainer | String | - | Treeneri nimi. |
| trainingStyle | String | - | *trainingStyleType*. Treeningu stiil.|
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
| startDate | date | + | Kuupäev, alates millest treening toimub. |
| endDate | date | + | Kuupäev, alates milleni treening toimub. |
| status | statusType | + | Kas antud klubis treening toimub või mitte. |

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
