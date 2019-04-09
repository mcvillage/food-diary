# Ruokapäiväkirja
Ruokapäiväkirjan avulla käyttäjä voi pitää kirjaa päivittäisistä ravintoaineista lisäämällä päivän ruokia sovellukseen. Sovelluksesta näkee valitun päivän ravintoaineiden yhteenvedon. Sovellus käyttää elintarvikkeiden hakemiseen Finelin tarjoamaa API:a (Terveyden ja hyvinvoinnin laitos, Fineli).


## Dokumentaatio
[Vaatimusmäärittely](https://github.com/valtterikodisto/food-diary/blob/master/documentation/vaatimusmaarittely.md)

[Työaikakirjanpito](https://github.com/valtterikodisto/food-diary/blob/master/documentation/tuntikirjanpito.md)

[Arkkitehtuurikuvaus](https://github.com/valtterikodisto/food-diary/blob/master/documentation/arkkitehtuuri.md)

## Komentorivitoiminnot

### Suoritettavan JAR tiedoston luonti

Jar-tiedoston voi luoda seuraavalla komennolla projektin juuressa:

```bash
mvn assembly:assembly -DdescriptorId=jar-with-dependencies
```

Jar-tiedoston suorittaminen tapahtuu seuraavasti:

```bash
java -jar target/FoodDiary-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Testaus
Testaus suoritetaan juurikansiossa komennolla:
```bash
mvn test
```

Testikattavuusraportin voi luoda ja tarkastella seuraavilla komennoilla (selaimena firefox):
```bash
mvn test jacoco:report && firefox target/site/jacoco/index.html
```

### Checkstyle
Tiedostoon [checkstyle.xml](https://github.com/valtterikodisto/food-diary/blob/master/checkstyle.xml) määrittelemät tarkistukset voi luoda ja tarkastastella seuraavilla komennoilla juurikansiosta (selaimena firefox):
```bash
mvn jxr:jxr checkstyle:checkstyle && firefox target/site/checkstyle.html
```