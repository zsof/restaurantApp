# restaurantApp

### Lokális futtatás
Az alkalmazás elérhető a Google Play-en: https://play.google.com/store/apps/details?id=hu.zsof.restaurantappjetpacknew

Ehhez biztosítva van a szerver folyamatos működése. 

Azonban lokális futtatásra is lehetőség van, ehhez szükséges egy saját adatbázis biztosítása. Az útmutatóban egy H2 adatbázis létrehozását, és a Backend-del való összekapcsolást mutatom be:

1. lépés: Projekt klónozása

2. lépés: *build.gradle* fájlban a dependency-k között a kommentet ki kell szedni előle: <b>runtimeOnly("com.h2database:h2")</b>, majd szinkronizálás

3. lépés: ***application.properties*** fájl létrehozása a *resources* mappába

4. lépés: *application.properties* fájl konfigurálása:

    Az email szerverhez tartozó *email-cím* és *jelszó* egyéni kitöltése szükséges, létező adatokkal, valamint az email-cím megfelelő konfigurálásával a Gmail-en belül (https://mailmeteor.com/blog/gmail-smtp-settings#how-to-use-the-gmail-smtp-settings)

   ```
   server.port=8080
   
   #H2
   spring.datasource.url=jdbc:h2:mem:restaurant
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=h2
   spring.datasource.password=password
   spring.jpa.hibernate.ddl-auto=create
   
   rsa.private-key=classpath:private.pem
   rsa.public-key=classpath:public.pem
   
   #email server
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=email@gmail.com
   spring.mail.password=password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

5. lépés - Opcionális: *RestaurantAppApplication* fájlban a kikomentezett rész használatával (és szükséges importok megadásával) érvényes teszt felhasználók jönnek létre, az ott megadott adatokkal. Így könnyebb a továbbiakban tesztelni az appot.

6. Backend alkalmazás futtatása

Androidon a következő lépések szükségesek:

1. lépés: Android alkalmazás klónozása: https://github.com/zsof/RestaurantAppJetpackNew
2. lépés: A *Constants.kt* fájlban a **BASE_URL** helyére a lokális Backend elérhetőséget kell írni
3. lépés: A *Manifest* fájlban az **android:usesCleartextTraffic="false"**-t át kell írni *"true"*-ra
4. lépés: Futtatás után az alkalmazás értelemszerűen használható. Regisztrációt követően az email-címre érkező verifikációs email-en a linkre kattintás kötelező, ezzel válik érvényessé a regisztrálás.
### About

This project is being prepared as thesis work of the BME master's degree in Computer Science.
The application consists of an Android project and a Spring Boot backend written in Kotlin. The Android part is completed with XML description language and in Jetpack Compose. 

The Jetpack Compose codebase can be found here: https://github.com/zsof/RestaurantAppJetpackNew

The codebase  with XML can be found here: https://github.com/zsof/AndroidRestaurantApplication

### The Application

The App is a Restaurant/Bakery/Patessiere... registration app, where users can filter for the right conditions, such as whether lactose-free food is prepared, the place is family-friendly, dogs can be brought in, or whether there is free parking. 
They can also see the places on the map, which one is closest to them, or they can also save the given place in a list of favorites.

Other information about the places is also available in the detailed view of the place, such as opening hours, website/phone number, additional photos or reviews.

The backend ensures the management and storage of the data.

### Techstack

- Written in Kotlin
- Spring Security
- JPA
- PostgreSQL
