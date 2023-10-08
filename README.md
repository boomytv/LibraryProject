# LibraryHelper
1. Sprendžiamo uždavinio aprašymas

1.1. Sistemos paskirtis

Projekto tikslas – palengvinti knygyno darbuotojų darbą, kur darbuotojai galės patogiai matyti ir pažymėti rezervuotas knygas.

Veikimo principas – pačią kuriamą platformą sudaro dvi dalys: internetinė aplikacija, kuria naudosis knygyno darbuotojai, administratorius bei aplikacijų programavimo sąsaja (angl. trump. API).
Knygyno darbuotojas, norėdamas naudotis šia platforma, galės sudaryti knygų sąrašus pagal tam tikrus reikalavimus, juos trinti, redaguoti, pažymėti, kurios knygos jau yra rezervuotos. Sudarytus knygų sąrašus galės matyti naudotojas, kuris galės sukurti rezervaciją specialiai knygai, bei ištrinti ją. Administratorius galės atlikti visas funkcijas, paminėtas aukščiau, bei taip pat turės užregistruoti naujus knygynų darbuotojus. Taip pat jis privalės užregistruoti naujus knygynus, kurie nori naudotis sistema.

1.2. Funkciniai reikalavimai

Registruotas sistemos naudotojas galės:

1.	Peržiūrėti platformos pagrindinį puslapį;
2.	Prisijungti prie internetinės aplikacijos;
3.	Atsijungti nuo internetinės aplikacijos;
4.	Peržiūrėti galimų rezervuotų knygų sąrašą;
5.	Peržiūrėti savo asmeninių rezervuotų knygų sąrašą;
6.	Rezervuoti knygą;
7.	Atšaukti knygos rezervaciją;

Registruotas sistemos knygyno darbuotojas galės:

1.	Peržiūrėti rezervuotų knygų sąrašą;
2.	Patvirtinti/Atmesti naudotojo rezervaciją;
3.	Pridėti naują knygą į sistemą;
4.	Ištrinti knygą iš sistemos;
5.	Redaguoti knygą sistemoje.

Administratorius galės:

1.	Sistemoje sukurti naują knygyną;
2.	Užregistruoti/Panaikinti knygyno darbuotojus;
3.	Užregistruoti/Panaikinti naudotojus.

2.	Sistemos architektūra

Sistemos sudedamosios dalys:

•	Kliento pusė (ang. Front-End) – naudojant Angular;

•	Serverio pusė (angl. Back-End) – naudojant Java Spring Boot. Duomenų bazė – MySQL. 
Sistemos diegimo diagrama: https://ibb.co/RcMsCyL
