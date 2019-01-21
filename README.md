# Projekt grafu by Andrzey Dynamics©®
Aplikacja **klient** (wizualizacja grafu) - **serwer** (obliczenia, przechowywanie stanu grafu)

## Uruchomienie aplikacji
1) Pobranie z repozytorium kodu, adres: https://github.com/andrzej-dynamics/grapham.git
2) Uruchomienie serwer z głowengo katalogu komendą: `java -jar grapham-1.0-SNAPSHOT.jar`
3) Uruchomienie przez przeglądarkę: http://localhost:9888/ (zalecany Chrome)
4) Wczytać przykładowy plik z głównego katalogu aplikacji (graph.txt)

## Podstawowe funkcjonalności
1. Wybranie wierzchołka operacje GET
 - zapytanie do `/vertices/can-perform-single` czy można go usunąć (włączenie guzika)
 - dodanie wierchołka zapytanie `/vertices/add?source=wierchołek&direction=true/false`
 - usunięcie wierzchołka `/vertices/remove?vertex=wierchołek`

 2. Wybranie dwóch wierchołków GET
  - zapytanie do `/vertices/can-perform-double` czy można zrobić zamianę czy można dodać krawędź
  - dodanie krawędzi zapytanie `/vertices/add-edge?source=wierchołek&direction=true/false`
  - zamiana podgrafów `/vertices/switch`

3. Wybranie krawędzi GET
    - zapytanie do `/edges/can-remove` czy można usunąć
    - usunięcie krawędzi zapytanie `/edges/remove`

3. Zamiana krawędzi do wierchołka GET (wybranie krawedzi, dwa wierzchołki)
    - zapytanie do `/edges/can-move`
    - zamiana krawędzi zapytanie `/edges/move`
#### Serwer
REST API w Spring'u. Endpoint'y do obsługi modyfikacji na wierzchołkach i krawędziach
***
#### Klient
Biblioteka w `D3.js` z `JQuery`

Funkcje pomocnicze dla wysyłania zapytań http: `utils.js`

Główny plik aplikacji: `grapham.js`, obecnie utworzone metody:
+ `onChangeFileInput` - wczytywanie pliku grafu i wysłanie go na serwer
+ `onLoadGraphSuccess` - po udanym wczytaniu grafu, inicjalizuje wartości, resetuje stan aplikacji i rysuje graf
+ `showManipulationOptions` - decyduje, który blok z opcjami pokazać (dla pojedynczego wierzchołka, dwóch wierzchołoków itd.)
+ `loadGraph` - rysowanie grafu, dodanie listener'ów na `click, mouse/drag over` itp.
+ `onClickNode` - zapisanie wybranego wierzchołka, wywołanie `showManipulationOptions`
+ `onClickEdge` - zapisanie wybranej krawędzi, wywołanie `showManipulationOptions`
