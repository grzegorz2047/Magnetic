package pl.grzegorz2047.magnetic;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.grzegorz2047.magnetic.window.ConfigurationWindow;
import pl.grzegorz2047.magnetic.window.MainChart;

import static java.lang.Thread.sleep;

/**
 * Plik stworzony przez grzegorz2047 27.04.2017.
 */
public class Main extends Application {

    /*
        Wykonujesz obliczenia dla modelu (50,1, 50000), (50, 2, 50000),(50, 3, 50000),(50, 4, 50000),(50, 5, 50000)
        oraz wartości wokół 2.4 czyli np. 2.1, 2.2, 2.3 ,2.4, 2.5
        Dla każdego modelu robisz wykres przedstawiający magnetyzację do czasu MCS co dziesięć MCS

        Potem z tych wszystkich średnich wyliczonych z modelu  oraz ich temperatur robisz wykres pokazujący
        jak zachowuje się magnetyzacja dla danych temperatur.

        Całość możesz robić na raz używając osobnych wątków po skończeniu przy okazji zapisz magnetyzację do pliku.
        Najlepiej w pliku dawaj magnetyzację co linię.

        Ewentualnie możesz robić czas MCS : magnetyzacja, żeby widzieć co i jak

        Po zapisaniu do plików wartości wrzuconych z wykresów, bierzesz od której linii ma brać wartości linia = DANY MCS
        a w linii jest dana magnetyzacja w danym MCS.

        Robisz sobie jeszcze jedno okno, gdzie podajesz nazwę pliku, z którego ma brać wartości, podajesz od którego MCSa
        ma brać magnetyzacje i jeszcze przycisk, żeby rozpoczył przetwarzanie pliku. Po zakończeniu przetwarzania w labelu
        wyświetla się średnia.

        Potem tę średnia bierzesz do innego okna, gdzie dodajesz sobie średnia magnetyzacja dla danej temperatury.
        Potrzebujesz przycisk dodaj, który dokłada punkt do wykresu, ewentualnie przycisk do pokazywania wykresu i chowania

        

     */


    @Override
    public void start(Stage stage) {
        ConfigurationWindow configurationWindow = new ConfigurationWindow();
        configurationWindow.show();

    }

    @Override
    public void stop() throws Exception {

    }

    public static void main(String[] args) {
        launch(args);
    }

}