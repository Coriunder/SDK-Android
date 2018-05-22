package com.coriunder.international.callbacks;

import com.coriunder.international.models.Country;
import com.coriunder.international.models.Language;
import com.coriunder.international.models.State;

import java.util.ArrayList;

/**
 * Interface definition for a callback to be invoked when server returns response
 */
public interface ReceivedInternationalListsCallback {
    /**
     * Called when server returns response
     *
     * @param isSuccess shows whether request was successful or not
     * @param languages ArrayList containing Language models
     * @see Language
     * @param countries ArrayList containing Country models
     * @see Country
     * @param canadaStates ArrayList containing State models for Canada states
     * @param usaStates ArrayList containing State models for USA states
     * @see State
     * @param message returned error or message if any
     */
    void onResultReceived(boolean isSuccess, ArrayList<Language> languages, ArrayList<Country> countries,
                          ArrayList<State> canadaStates, ArrayList<State> usaStates, String message);
}