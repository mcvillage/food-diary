# Testausdokumentti
Ohjelmaa on testattu sekä automatisoiduin yksikkö- ja integraatiotestein 
JUnitilla sekä manuaalisesti tapahtunein järjestelmätason testein.

## Yksikkö- ja integraatiotestaus

### Sovelluslogiikka
Automatisoitujen testien pääpaino on sovelluslogiikassa eli domain pakkauksen 
alla olevien luokkien toiminnallisuudessa. Testit testaavat luokkien FoodService 
ja Food lisäksi Fineli luokkaa, jossa testataan API:n toimivuutta.

Integraatiotestien testidatan tallennuksessa käytetään FoodDao luokan kautta luotua 
food-test.db tiedostoa.

### Testauskattavuus
Käyttöliittymäkerrosta lukuunottamatta sovelluksen testauksen rivi- ja haarautumakattavuus 
on 75%.

<img src="https://raw.githubusercontent.com/valtterikodisto/food-diary/master/documentation/pictures/jacoco.png">

## Järjestelmätestaus
Sovelluksen järjestelmätestaus on suoritettu manuaalisesti.

### Toiminnallisuudet
Kaikki määrittelydokumentin ja käyttöohjeen listaamat toiminnallisuudet on käyty läpi. Kaikkien 
toiminnallisuuksien yhteydessä on myös testattu virheellisten syötteiden syöttäminen kenttään 
(esimerkiksi kirjaimet ruoan painoon).