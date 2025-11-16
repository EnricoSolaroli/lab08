 package it.unibo.mvc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.controller.DrawNumberControllerImpl;
import it.unibo.mvc.model.DrawNumberImpl;
import it.unibo.mvc.view.DrawNumberStandardOutputView;
import it.unibo.mvc.view.DrawNumberSwingView;

/**
 * Application entry-point.
 */
public final class LaunchApp {

    private LaunchApp() { }

    /**
     * hardcoded implementation.
     */
    static void hardcodedMain() {
        final var model = new DrawNumberImpl();
        final DrawNumberController app = new DrawNumberControllerImpl(model);
        app.addView(new DrawNumberSwingView());
        app.addView(new DrawNumberSwingView());
        app.addView(new DrawNumberStandardOutputView());
    }

    /**
     * reflection implementation.
     */
    static void reflectionMain() {
        try {
            final var model = new DrawNumberImpl();
            final DrawNumberController app = new DrawNumberControllerImpl(model);

            final Class<? extends DrawNumberView> graphicalView = 
                Class.forName("it.unibo.mvc.view.DrawNumberSwingView").asSubclass(DrawNumberView.class);
            final Class<? extends DrawNumberView> consoleView =
                Class.forName("it.unibo.mvc.view.DrawNumberStandardOutputView").asSubclass(DrawNumberView.class);
            final List<Class<? extends DrawNumberView>> viewClasses = List.of(graphicalView, consoleView);

            for (final Class<? extends DrawNumberView> clazz : viewClasses) {
                final Constructor<? extends DrawNumberView> constructor = clazz.getConstructor();
                for (int i = 0; i < 3; i++) {
                    final DrawNumberView view = constructor.newInstance();
                    app.addView(view);
                }
            }
        } catch (
            InstantiationException
          | IllegalAccessException
          | IllegalArgumentException
          | InvocationTargetException
          | NoSuchMethodException
          | SecurityException
          | ClassNotFoundException
           e) {
            System.out.println(e.getMessage()); // NOPMD
        }
    }

    /**
     * this is the main.
     * 
     * @param args ignored parameters
     */
    public static void main(final String... args) {
        // hardcodedMain();
        reflectionMain();
    }
}
