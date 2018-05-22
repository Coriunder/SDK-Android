package com.coriunder.international;

import android.util.Log;

import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.base.utils.BaseParser;
import com.coriunder.international.models.Country;
import com.coriunder.international.models.CurrencyRate;
import com.coriunder.international.models.Language;
import com.coriunder.international.models.State;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parser methods for International services
 */
public class Parser extends BaseParser {
    /**
     * Method to parse JSONObject to ArrayList containing CurrencyRate objects
     * @param response JSONObject to parse
     * @return ArrayList containing CurrencyRate objects
     */
    protected static ArrayList<CurrencyRate> parseCurrencyRates(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<CurrencyRate> arrayList = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject requestObj = result.getJSONObject(i);
                    CurrencyRate item = new CurrencyRate();
                    item.setKey(Parser.getString(requestObj, "Key"));
                    item.setValue(Parser.getDouble(requestObj, "Value"));
                    arrayList.add(item);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.international_log),
                            Coriunder.getContext().getString(R.string.international_error_rate) + i);
                }
            }
        }
        return arrayList;
    }

    /**
     * Method to parse JSONObject to ArrayList containing ServiceResult objects
     * @param response JSONObject to parse
     * @return ArrayList containing ServiceResult objects
     */
    protected static ArrayList<ServiceResult> parseErrorCodes(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<ServiceResult> arrayList = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject jsonResult = result.getJSONObject(i);
                    ServiceResult item = parseServiceResult(jsonResult);
                    arrayList.add(item);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.international_log),
                            Coriunder.getContext().getString(R.string.international_error_code) + i);
                }
            }
        }
        return arrayList;
    }

    /**
     * Method to parse JSONObject to ArrayList containing Country objects
     * @param root JSONObject to parse
     * @return ArrayList containing Country objects
     */
    protected static ArrayList<Country> parseCountries(JSONObject root) {
        JSONArray countriesArray = Parser.getJsonArray(root, "Countries");
        ArrayList<Country> countries = new ArrayList<>();
        if (countriesArray != null) {
            for (int i=0; i<countriesArray.length(); i++) {
                try {
                    JSONObject obj = countriesArray.getJSONObject(i);
                    Country country = new Country();
                    country.setKey(Parser.getString(obj, "Key"));
                    country.setName(Parser.getString(obj, "Name"));
                    country.setIcon(Parser.getString(obj, "Icon"));
                    countries.add(country);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.international_log),
                            Coriunder.getContext().getString(R.string.international_error_country)+i);
                }
            }
        }
        return countries;
    }

    /**
     * Method to parse JSONObject to ArrayList containing Language objects
     * @param root JSONObject to parse
     * @return ArrayList containing Language objects
     */
    protected static ArrayList<Language> parseLanguages(JSONObject root) {
        JSONArray languagesArray = Parser.getJsonArray(root, "Languages");
        ArrayList<Language> languages = new ArrayList<>();
        if (languagesArray != null) {
            for (int i=0; i<languagesArray.length(); i++) {
                try {
                    JSONObject obj = languagesArray.getJSONObject(i);
                    Language language = new Language();
                    language.setKey(Parser.getString(obj, "Key"));
                    language.setName(Parser.getString(obj, "Name"));
                    language.setIcon(Parser.getString(obj, "Icon"));
                    languages.add(language);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.international_log),
                            Coriunder.getContext().getString(R.string.international_error_language)+i);
                }
            }
        }
        return languages;
    }

    /**
     * Method to parse JSONObject to ArrayList containing State objects
     * @param root JSONObject to parse
     * @param isCanada boolean to set whether Canada or USA states being parsed
     * @return ArrayList containing State objects
     */
    protected static ArrayList<State> parseStates(JSONObject root, boolean isCanada) {
        JSONArray statesArray = Parser.getJsonArray(root, isCanada ? "CanadaStates" : "UsaStates");
        ArrayList<State> states = new ArrayList<>();
        if (statesArray != null) {
            for (int i=0; i<statesArray.length(); i++) {
                try {
                    JSONObject obj = statesArray.getJSONObject(i);
                    State state = new State();
                    state.setKey(Parser.getString(obj, "Key"));
                    state.setName(Parser.getString(obj, "Name"));
                    state.setIcon(Parser.getString(obj, "Icon"));
                    states.add(state);
                } catch (Exception e) {
                    if (isCanada) Log.e(Coriunder.getContext().getString(R.string.international_log),
                                Coriunder.getContext().getString(R.string.international_error_ca_state)+i);
                    else Log.e(Coriunder.getContext().getString(R.string.international_log),
                                Coriunder.getContext().getString(R.string.international_error_us_state)+i);
                }
            }
        }
        return states;
    }
}