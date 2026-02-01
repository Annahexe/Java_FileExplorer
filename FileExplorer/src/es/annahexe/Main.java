package es.annahexe;

/**
 * Main: Wires together {@link View}, {@link Utilities}, and {@link Controller} and starts the Swing UI.
 * @author annahexe
 */
public class Main {

    /** Added this for an error generated from the JavaDoc */
    private Main() { }

    /**
     * Launches the application.
     * @param args command-line arguments (unused)
     */
	public static void main(String[] args) {
		View view = new View();
		Utilities utility = new Utilities();
		Controller control = new Controller(utility, view);

	}

}
