# Ruokapäiväkirja
Ruokapäiväkirjan avulla käyttäjä voi pitää kirjaa päivittäisistä ravintoaineista lisäämällä päivän ruokia sovellukseen. Sovelluksesta näkee valitun päivän ravintoaineiden yhteenvedon. Sovellus käyttää elintarvikkeiden hakemiseen Finelin tarjoamaa API:a (Terveyden ja hyvinvoinnin laitos, Fineli).


## Dokumentaatio
[Käyttöohje](https://github.com/valtterikodisto/food-diary/blob/master/documentation/kayttoohje.md)

[Vaatimusmäärittely](https://github.com/valtterikodisto/food-diary/blob/master/documentation/vaatimusmaarittely.md)

[Työaikakirjanpito](https://github.com/valtterikodisto/food-diary/blob/master/documentation/tuntikirjanpito.md)

[Arkkitehtuurikuvaus](https://github.com/valtterikodisto/food-diary/blob/master/documentation/arkkitehtuuri.md)

[Testausdokumentti](https://github.com/valtterikodisto/food-diary/blob/master/documentation/testaus.md)

## Releaset

[Viikko 5](https://github.com/valtterikodisto/food-diary/releases/tag/viikko5)

[Viikko 6](https://github.com/valtterikodisto/food-diary/releases/tag/1.0)

[Loppupalautus](https://github.com/valtterikodisto/food-diary/releases/tag/1.1)

## Komentorivitoiminnot

### Suoritettavan JAR tiedoston luonti

Jar-tiedoston voi luoda seuraavalla komennolla projektin juuressa:

```bash
mvn package
```

Jar-tiedoston suorittaminen tapahtuu seuraavasti:

```bash
java -jar target/FoodDiary-1.0-SNAPSHOT.jar
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

### JavaDoc
JavaDocin voi luoda ja tarkastella suorittamalla seuraavan komennon juurikansiossa (selaimena firefox):
```bash
mvn javadoc:javadoc && firefox target/site/apidocs/index.html
```

### Checkstyle
Tiedostoon [checkstyle.xml](https://github.com/valtterikodisto/food-diary/blob/master/checkstyle.xml) määrittelemät tarkistukset voi luoda ja tarkastastella seuraavilla komennoilla juurikansiosta (selaimena firefox):
```bash
mvn jxr:jxr checkstyle:checkstyle && firefox target/site/checkstyle.html
```
