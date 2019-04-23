# Arkkitehtuurikuvaus

## Rakenne
Ohjelman rakenne noudattelee kolmitasoista kerrosarkkitehtuuria, ja koodin pakkausrakenne on seuraava:

<img src="https://raw.githubusercontent.com/valtterikodisto/food-diary/master/documentation/pictures/pakkaukset.png">

Pakkaus ui sisältää JavaFX:llä toteutetun käyttöliittymän domain sovelluslogiikan, dao tietojen 
pysyväistallennuksesta vastaavan koodin ja api Finelin API:sta hakemiseen tarvittavan koodin.

## Käyttöliittymä
Käyttöliittymä sisältää neljä erillistä näkymää:
- Haku
- Hakutulos
- Viikko
- Päivä

Jokainen näkymä on toteutettu omana Scene-oliona. Näkymät ovat esillä yksi kerrallaan. 
Käyttöliittymä on rakennettu ohjelmallisesti luokassa ui.FoodDiaryUI.
Käyttöliittymä kutsuu foodServicen kautta sovelluslogiikkaa toteuttavia metodeja, jolloin 
käyttöliittymäluokassa on hyvin vähän sovelluslogiikkaa.

## Sovelluslogiikka
Sovelluksen esittämiä ruokia kuvaa Food -luokka, joka pitää sisällään nimen ja määrän lisäksi 
myös ravintoaineiden määriä grammoina.

Toiminnallisista kokonaisuuksista vastaa luokka FoodService. FoodService pitää yllä mm. valitun 
ruoan ja valitun päivän. FoodService tarjoaa monet käyttöliittymän tarvitsemat metodit kuten:

- void save(Food food, LocalDate date, int amount)
- void removeEntry(Food food, LocalDate date)
- long getTotalCaloriesByDate(LocalDate date)
- LocalDate getFirstDayOfWeek()

FoodService pääsee käsiksi tallennettuihin ruoka-annoksiin luokan FoodDao avulla.

FoodServicen ja ohjelman muiden osien suhdetta kuvaava luokka/pakkauskaavio: 

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