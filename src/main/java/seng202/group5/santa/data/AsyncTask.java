package seng202.group5.santa.data;

import javafx.application.Platform;

/**
 * Makes threads work like android threads, with generic callback value and exception handling
 *
 * @author Victor Oliveira
 *         https://github.com/victorlaerte/javafx-asynctask
 *         Modifications by Will Richardson
 *         Modification by group 5
 */

public abstract class AsyncTask<T> {

    private boolean daemon = true;

    /**
     * Run on main thread before the background task
     */
    public abstract void onPreExecute();

    /**
     * Perform a task in background thread
     * @return The result of the task
     * @throws Exception from the task
     */
    public abstract T doInBackground() throws Exception;

    /**
     * Run on main thread after the background task
     * @param result Result of background task
     * @param ex Exception that have been thrown from back ground task
     */
    public abstract void onPostExecute(T result, Exception ex);

    /**
     * Run on main thread updates the gui
     * @param params the thing to update the gui with
     */
    public abstract void progressCallback(Object... params);

    /**
     * Signal that the task has finished
     * @param params passed to progressCallback()
     */
    public void publishProgress(final Object... params) {

        Platform.runLater(new Runnable() {
            public void run() {
                progressCallback(params);
            }
        });

    }
    /**
     * Setup and runs threads
     */
    private final Thread backgroundThread = new Thread(new Runnable() {

        public void run() {
            T r = null;
            Exception ex;
            try {
                r = doInBackground();
                ex = null;
            } catch (Exception e) {
                ex = e;
            }

            final T result = r;
            final Exception exception = ex;


            Platform.runLater(new Runnable() {

                public void run() {
                    onPostExecute(result, exception);
                }
            });
        }
    });

    /**
     * Run this task.
     */
    public void execute() {

        Platform.runLater(new Runnable() {

            public void run() {
                onPreExecute();
                backgroundThread.setDaemon(daemon);
                backgroundThread.start();
            }
        });
    }

    public void setDaemon(boolean daemon) {

        this.daemon = daemon;
    }

    /**
     * Send interrupt to the task thread
     */
    public void interrupt() {
        this.backgroundThread.interrupt();
    }
}