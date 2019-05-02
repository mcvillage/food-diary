# Vaatimusmäärittely

## Sovelluksen tarkoitus
Ruokapäiväkirjan avulla käyttäjä voi pitää kirjaa päivittäisistä ravintoaineista lisäämällä päivän ruokia sovellukseen. Sovelluksesta näkee valitun päivän ravintoaineiden yhteenvedon.

## Käyttöliittymäluonnos
Sovellus koostuu neljästä eri näkymästä. Sovellus avautuu hakunäkymään, josta voidaan hakutulokseen tai ruokapäiväkirjaan. 
Ruokapäiväkirjasta voi siirtyä valitun päivän yhteenvetoon.

<img src="https://raw.githubusercontent.com/valtterikodisto/food-diary/master/documentation/pictures/food-diary-ui-sketch.png">

## Toiminnallisuudet
- Käyttäjä voi hakea elintarvikkeita ja tarkastella niiden ravintosisältöä
- Haku tapahtuu Finellin tarjoaman API:n kautta
- Käyttäjä voi lisätä elintarvikkeen ja määrän ruokapäiväkirjaan
- Käyttäjä voi poistaa elintarvikkeen ruokapäiväkirjasta
- Käyttäjä näkee lyhyen yhteenvedon viikosta
- Käyttäjä näkee yksittäisen päivän ruokien ravintosisältöjen yhteenvedon

## Jatkokehitysideoita
- Ravintosisältöyhteenvedoissa näkyy päivittäinen saantisuositus
- Hakupalkkiin autocomplete -tyylinen totetus
- Ruokapäiväkirjassa olevan elintarvikkeen määrän (paino, tilavuus,...) muuttaminen
- Ruokapäiväkirjassa olevien ruokien jaoettelu aamupalaan, lounaaseen, välipalaan, päivälliseen tai iltapalaan
- Enemmän ravintoaineita huomioon