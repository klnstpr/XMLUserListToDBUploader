# XMLUserListToDBUploader
Do działania aplikacji potrzebne jest połączenie z bazą danych MySQL (np Xampp).

Aplikacja działa domyślnie pod adresem http://localhost:8080/ port 8080 można zmienić w pliku
application.properties.

Następne linijki konfiguracyjne w tym pliku to:

1. spring.datasource.url=jdbc:mysql://localhost:3306/userapp?useSSL=false

Zdefiniowane połączenie do mojej bazy danych, w przypadku zmiany nazwy bazy danych
(w tym przypadku jest to "userapp") lub innych parametrów konfigurację należy zmienić.

2. spring.datasource.username=root

Nazwa użytkownika, w przypadku zmiany należy również zmienić ją w pliku konfiguracyjnym.

3. spring.datasource.password=root


Hasło użytkownika, w przypadku zmiany należy również zmienić ją w pliku konfiguracyjnym.

Wczytywany plik zapisywany jest do katalogu "uploads", przy wczytywaniu kolejnych plików
poprzedni plik nie będzie już dostępny. Każdy wczytany plik do katalogu "uploads" zmienia
nazwę na "file". Podczas startu programu plik nie jest wymagany, aplikacja może wystartować
bez danych xml.

Niektóre parametry, takie jak długość listy użytkowników na 1 stronę zostały ustawione
domyślnie (w tym przypadku 20) i można je zmienić w kodzie programu. 

Ze względu na wielkość listy użytkowników dodałem także możliwość bezpośredniego
przejścia na daną stronę (poprzez wpisanie jej numeru przez użytkownika).
Pod tabelą znajdują się także odnośniki do pierwszej, następnej, poprzedniej
i ostatniej strony.

Funkcjonalność szyfrowania md5 zrealizowana została z wykorzystaniem Javy,
dane w bazie nie są zmieniane.

Przy wczytywaniu pliku i zapisie do bazy danych w konsoli wyświetlany 
jest stan zapisu (który rekord aktualnie jest zapisywany), funkcjonalność
tę można zakomentować w celu przyspieszenia działania.

Stworzyłem również przykładowy generator XML dla tej aplikacji: https://github.com/klnstpr/xml_generator
