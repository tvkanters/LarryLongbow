package com.tvkdevelopment.larrylongbow;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

/**
 * The LarryLongbow advice widget.
 */
public class Widget extends AppWidgetProvider {

    /** The random number generator */
    private final static Random RNG = new Random();
    /** The amount of ms between each period showing up */
    private final static int TYPE_SPEED = 500;
    /** The name of the app's package */
    private final static String PACKAGE_NAME = "com.tvkdevelopment.larrylongbow";
    /** The intent action for requesting advice */
    private final static String REQUEST_ACTION = "com.tvkdevelopment.larrylongbow.REQUEST_ACTION";
    /** The intent action for the thanks action */
    private final static String THANKS_ACTION = "com.tvkdevelopment.larrylongbow.THANKS_ACTION";

    /** Whether or not this static instance is initialised */
    private static boolean sInitialised = false;

    /** The possible requests to show in the button */
    private static String[] sRequests;
    /** The possible advice Larry can give */
    private static String[] sAdvice;

    /** The timer used while Larry is typing */
    private static Timer sTypingTimer;
    /** The amount of dots shown while Larry is typing */
    private static int sDotCount;
    /** The last set advice */
    private static String sLastAdviceText;
    /** The remote views */
    private static RemoteViews sViews;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] widgetIds) {
        super.onUpdate(context, appWidgetManager, widgetIds);

        initialise(context);

        // Prepare request button
        final Intent requestIntent = new Intent(context, getClass());
        requestIntent.setAction(REQUEST_ACTION);
        final PendingIntent requestPendingIntent = PendingIntent.getBroadcast(context, 0, requestIntent, 0);
        sViews.setOnClickPendingIntent(R.id.requestButton, requestPendingIntent);

        // Prepare thanks button
        final Intent thanksIntent = new Intent(context, getClass());
        thanksIntent.setAction(THANKS_ACTION);
        final PendingIntent thanksPendingIntent = PendingIntent.getBroadcast(context, 0, thanksIntent, 0);
        sViews.setOnClickPendingIntent(R.id.advice, thanksPendingIntent);

        // Push the changes
        appWidgetManager.updateAppWidget(new ComponentName(context, Widget.class), sViews);
    }

    /**
     * {@inheritDoc}
     *
     * Handles click actions from the widget.
     */
    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);

        // Don't do anything while Larry is typing
        if (sTypingTimer != null) {
            return;
        }

        final String action = intent.getAction();

        if (REQUEST_ACTION.equals(action)) {
            // Request advice from Larry
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            initialise(context);

            // Update the request button
            sViews.setTextColor(R.id.requestButton, Color.GRAY);

            // Update the advice field
            sLastAdviceText = context.getString(R.string.typing);
            sViews.setTextViewText(R.id.advice, sLastAdviceText);

            // Make Larry start typing
            sDotCount = 0;
            sTypingTimer = new Timer();
            sTypingTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    if (sDotCount++ == 3) {
                        // When there are enough dots, show the advice
                        sViews.setTextViewText(R.id.advice, "<LarryLongbow> " + sAdvice[RNG.nextInt(sAdvice.length)]);

                        // Update the buttons
                        sViews.setTextColor(R.id.requestButton, Color.BLACK);
                        sViews.setTextViewText(R.id.requestButton, sRequests[RNG.nextInt(sRequests.length)]);

                        // Cancel the timer
                        sTypingTimer.cancel();
                        sTypingTimer = null;

                    } else {
                        // Add periods while Larry is typing
                        sLastAdviceText += ".";
                        sViews.setTextViewText(R.id.advice, sLastAdviceText);
                    }

                    // Push the changes
                    appWidgetManager.updateAppWidget(new ComponentName(context, Widget.class), sViews);
                }
            }, TYPE_SPEED, TYPE_SPEED);

            // Push the changes
            appWidgetManager.updateAppWidget(new ComponentName(context, Widget.class), sViews);

        } else if (THANKS_ACTION.equals(action)) {
            // Clear the advice
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            sViews.setTextViewText(R.id.advice, "");
            appWidgetManager.updateAppWidget(new ComponentName(context, Widget.class), sViews);
        }
    }

    /**
     * Initialise the widget so that it shows the correct stuff and can be interacted with.
     *
     * @param context
     *            The context to use
     */
    private void initialise(final Context context) {
        if (!sInitialised) {
            sInitialised = true;

            // Prepare the view
            sViews = new RemoteViews(PACKAGE_NAME, R.layout.widget);

            // Get requests and advice
            sRequests = context.getResources().getStringArray(R.array.requests);
            sAdvice = context.getResources().getStringArray(R.array.advice);

            // Set request button text
            sViews.setTextViewText(R.id.requestButton, sRequests[RNG.nextInt(sRequests.length)]);
        }
    }
}
