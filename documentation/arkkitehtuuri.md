# Arkkitehtuurikuvaus
## Sovelluslogiikka
<img src="https://raw.githubusercontent.com/valtterikodisto/food-diary/master/documentation/pictures/pakkauskaavio.png">

## Päätoiminnallisuudet

### Ravintoainepympyrädiagrammin näyttäminen
Kun aloitusnäkymän hakukenttään kirjoitetaan haettava elintarvike, etenee sovelluksen 
kontrolli seuraavasti:

<img src="https://raw.githubusercontent.com/valtterikodisto/food-diary/master/documentation/pictures/sekvenssikaavio.png">

Hakupainikkeen tapahtumankäsittelijä tai tekstikentän merkkejä kuunteleva tapahtumankäsittelijä 
kutsuu Finelin metodia search antaen parametriksi haettavan elintarvikkeen nimen. Metodi hakee 
Finelin API:sta kyseistä elintarviketta ja palauttaa listan sopivista vaihtoehdoista, jotka 
näytetään käyttäjälle. Kun käyttäjä klikkaa vaihtoehtoa, vaihtoehtojen tapahtumakäsittelijä 
asettaa valitun ruoan käyttäen FoodServicen setFood -metodia. Käyttöliittymäluokka vaihtaa 
näkymän NutrientSceneen. Käyttöliittymä hakee FoodServiceltä ruoan käyttäen getFood -metodia. 
Ruoan ravintoarvot käyttöliittymä hakee kutsumalla Food -luokan metodia getBasicNutrients, joka 
palauttaa listan ravintoaineiden prosenttiosuuksia, joista käyttöliittymä rakentaa ympyrädiagrammin.