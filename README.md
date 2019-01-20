#Projekt grafu by Andrzey Dynamics©®
Aplikacja **klient** (wizualizacja grafu) - **serwer** (obliczenia, przechowywanie stanu grafu)
***
####Serwer
REST API w Spring'u. Endpoint'y do obsługi modyfikacji na wierzchołkach i krawędziach
***
####Klient
Biblioteka w `D3.js` z `JQuery`

Funkcje pomocnicze dla wysyłania zapytań http: `utils.js`

Główny plik aplikacji: `grapham.js`, obecnie utworzone metody:
+ `onChangeFileInput` - wczytywanie pliku grafu i wysłanie go na serwer
+ `onLoadGraphSuccess` - po udanym wczytaniu grafu, inicjalizuje wartości, resetuje stan aplikacji i rysuje graf
+ `showManipulationOptions` - decyduje, który blok z opcjami pokazać (dla pojedynczego wierzchołka, dwóch wierzchołoków itd.)
+ `loadGraph` - rysowanie grafu, dodanie listener'ów na `click, mouse/drag over` itp.
+ `onClickNode` - zapisanie wybranego wierzchołka, wywołanie `showManipulationOptions`
+ `onClickEdge` - zapisanie wybranej krawędzi, wywołanie `showManipulationOptions`