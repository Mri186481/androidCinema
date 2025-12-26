# androidCinema
Android APP for Multimedia and Mobile Device Programming learning activity.

Se ha implementado el patron MVP en el diseño de la aplicacion Cinemas.

Para ejecutar la aplicacion basta con ejecutar la aplicacion en Android Studio con el emulador integrado. 
Se han realizado principalmente las pruebas con el dispositivo Pixel2XL y con la API 36"Baklava" o Android 16.

La aplicacion consume la Api diseñada en Acceso a Datos. Por motivo de especificaciones y envergadura del proyecto solo
se han implementado dos clases de la Api cinemas. Las clases son Screenings(sesiones) y Movies(peliculas).

Con estas clases se ha diseñado una App Android con funcionalidades limitadas. Con mas tiempo de desarrollo, o en posteriores 
etapas de desarrollo, el objetivo final seria una App completamente funcional para la venta de entradas en una cadena de 
multicines.

Ahora solo estan implementadas las funcionalidades de un CRUD completo de movies, pudiendo dar de alta, ver detalle, modificar y listar movies.
En el alta de movies, aprovechando que se ha forzado el diseño de la clase, donde se han introducido datos geograficos, se hace 
uso de mapas para mostrar informacion sobre la localización principal de filmación de cada movie, y tambien se puede registrar y modificar dicha informacion geografica.
Tambien se ha implementado un CRUD completo de screenings.

Ademas se ha dotado a la App con su propia Bd particular donde guardamos las movies favoritas que queramos guardar. Ademas de 
incluso poder modificar, borrar y listar dichos favoritos.

Se ha dotado tambien a la app con dialogos al modificar o eliminar información.

Se ha dotado a la aplicacion de unas preferencias, nombre del ususario, vista por satelite y habilitar notificaciones de la aplicacion.

Se ha permitido tambien enlazar con las fotos que se tenga en el dispositivo para implementar manejo de imagenes relacionadas
con la Bd local, interactuando de esta manera con otras aplicaciones del dispositivo.

Saludos¡¡.





