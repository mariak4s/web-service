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

### Operatsioonid

#### addClub
..võimaldab süsteemi lisada uue treeningklubi koos seda kirjeldavate parameetritega. Peale lisamist kuvatakse lisatud andmed peale *token*-i.

**Sisend:** addClubRequest
**Väljund:** addClubResponse

#### getClub
..võimaldab süsteemist küsida treeningklubi sellele määratud ID põhjal. Peale lisamist kuvatakse lisatud andmed peale *token*-i.

**Sisend:** getClubRequest
**Väljund:** getClubResponse

#### getClubList
..võimaldab süsteemist küsida treeningklubide nimekirja. Otsingut saab filtreerida riigi, linna ja treeningute olemasolu järgi.

**Sisend:** getClubListRequest
**Väljund:** getClubListResponse

#### addTraining
..võimaldab süsteemi lisada uue treeningu koos seda kirjeldavate parameetritega. Peale lisamist kuvatakse lisatud andmed peale *token*-i.

**Sisend:** addTrainingRequest

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| token  | String | +  | Kliendi autentimiseks kasutatav kood. |
| requestCode | Integer |- | Päringu unikaalne identifikaator. |
| id | Integer |- | Treeningu unikaalne identifikaator, mis määratakse süsteemi poolt automaatselt. |
| name | String |+ | Treeningu nimetus. |
| nameOfTheTrainer | String |+ | Treeneri nimi. |
| trainingStyle | String |+ | *trainingStyleType*. Treeningu stiil.|
| durationInMins | Integer |+ | Arvuline väärtus, mis kirjeldab, kui kaua treening kestab.|
| trainingCapacity | Integer |+ | Treeningu kohtade arv. |

**Väljund:** addTrainingResponse

| Parameeter  | Andmetüüp | Kohustuslik  | Kirjeldus |
|:--- |:--- |:---: |:--- |
| responseCode | Integer | +  | Päringust saadud unikaalne identifikaator, millega kontrollitakse *Idempotent Capability* mustrit.|
| training | trainingType | +  | Päringu põhjal genereeritud treeninguobjekt. Sisaldab sisendis antud infot ja treeningule antud id väärtust. |

#### addClubTraining
..võimaldab süsteemist küsida treeningute nimekirja ühes treeningklubis.

**Sisend:** addClubTrainingRequest
**Väljund:** addClubTrainingResponse

#### getClubTraining
..võimaldab süsteemist küsida kõiki treeninguid.

**Sisend:** getClubTrainingRequest
**Väljund:** getClubTrainingResponse
