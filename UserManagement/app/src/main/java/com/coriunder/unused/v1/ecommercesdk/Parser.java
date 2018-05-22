package com.coriunder.unused.v1.ecommercesdk;

import android.util.Log;

import com.android.volley.VolleyError;
import com.coriunder.unused.v1.basesdk.BaseParser;
import com.coriunder.unused.v1.ecommercesdk.models.Cart;
import com.coriunder.unused.v1.ecommercesdk.models.CartItem;
import com.coriunder.unused.v1.ecommercesdk.models.Merchant;
import com.coriunder.unused.v1.ecommercesdk.models.Product;
import com.coriunder.unused.v1.ecommercesdk.models.ProductCategory;
import com.coriunder.unused.v1.ecommercesdk.models.Property;
import com.coriunder.unused.v1.ecommercesdk.models.Stock;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 1 on 22.02.2016.
 */
public class Parser extends BaseParser {

    protected static Property parseProperty(JSONObject jsonResult) {
        Property property = new Property();
        property.setPropertyId((long) getValue(jsonResult, "ID", ValueType.LONG));
        property.setPropertyText((String) getValue(jsonResult, "Text", ValueType.STRING));
        property.setPropertyValue((String) getValue(jsonResult, "Value", ValueType.STRING));
        property.setPropertyType((String) getValue(jsonResult, "Type", ValueType.STRING));

        try {
            JSONArray values = (JSONArray) getValue(jsonResult, "Values", ValueType.JSONARRAY);
            ArrayList<Property> subproperties = new ArrayList<>();
            if (values != null) {
                for (int i=0; i<values.length(); i++) {
                    Property subproperty = parseProperty((JSONObject)values.get(i));
                    subproperties.add(subproperty);
                }
            }
            property.setSubproperties(subproperties);
        } catch (Exception e) {
            Log.d("EcommerceSDK", "Error when parsing subproperties");
        }
        return property;
    }

    protected static Product parseProduct(JSONObject jsonResult) {
        Product product = new Product();
        product.setProductId((String) getValue(jsonResult, "ID", ValueType.STRING));
        product.setSku((String) getValue(jsonResult, "SKU", ValueType.STRING));
        product.setName((String) getValue(jsonResult, "Name", ValueType.STRING));
        product.setProdDescription((String) getValue(jsonResult, "Description", ValueType.STRING));
        product.setImageUrl((String) getValue(jsonResult, "ImageURL", ValueType.STRING));
        product.setPrice((String) getValue(jsonResult, "Price", ValueType.STRING));
        product.setCurrencyISOCode((String) getValue(jsonResult, "Currency", ValueType.STRING));
        product.setIsDynamicAmount((boolean) getValue(jsonResult, "IsDynamicAmount", ValueType.BOOLEAN));
        product.setType((String) getValue(jsonResult, "Type", ValueType.STRING));
        product.setMinQuantity((String) getValue(jsonResult, "QuantityMin", ValueType.STRING));
        product.setMaxQuantity((String) getValue(jsonResult, "QuantityMax", ValueType.STRING));
        product.setStepQuantity((String) getValue(jsonResult, "QuantityInterval", ValueType.STRING));
        product.setQuantity((String) getValue(jsonResult, "QuantityAvailable", ValueType.STRING));
        product.setProductUrl((String) getValue(jsonResult, "ProductURL", ValueType.STRING));
        product.setMetaTitle((String) getValue(jsonResult, "Meta_Title", ValueType.STRING));
        product.setMetaDescription((String) getValue(jsonResult, "Meta_Description", ValueType.STRING));
        product.setMetaKeywords((String) getValue(jsonResult, "Meta_Keywords", ValueType.STRING));

        //properties
        try {
            JSONArray propArray = (JSONArray) getValue(jsonResult, "Properties", ValueType.JSONARRAY);
            ArrayList<Property> properties = new ArrayList<>();
            if (propArray != null) {
                for (int i=0; i<propArray.length(); i++) {
                    Property property = parseProperty((JSONObject)propArray.get(i));
                    properties.add(property);
                }
            }
            product.setProperties(properties);
        } catch (Exception e) {
            Log.d("EcommerceSDK", "Error when parsing properties");
        }

        //stocks
        try {
            JSONArray stocksArray = (JSONArray) getValue(jsonResult, "Stocks", ValueType.JSONARRAY);
            ArrayList<Stock> stocks = new ArrayList<>();
            if (stocksArray != null) {
                for (int i=0; i<stocksArray.length(); i++) {
                    JSONObject stockObj = (JSONObject)stocksArray.get(i);
                    Stock stock = new Stock();
                    stock.setStockId((long) getValue(stockObj, "ID", ValueType.LONG));
                    stock.setStockQuantity((long) getValue(stockObj, "QuantityAvailable", ValueType.LONG));
                    stock.setSku((String) getValue(stockObj, "SKU", ValueType.STRING));

                    JSONArray propsArray = (JSONArray) getValue(stockObj, "PropertyValues", ValueType.JSONARRAY);
                    ArrayList<Long> propertyIds = new ArrayList<>();
                    for (int j=0; j<propsArray.length(); j++) {
                        propertyIds.add(Long.valueOf((Integer)propsArray.get(j)));
                    }
                    stock.setPropertyIds(propertyIds);

                    stocks.add(stock);
                }
            }
            product.setStocks(stocks);
        } catch (Exception e) {
            Log.d("EcommerceSDK", "Error when parsing stocks");
        }

        try {
            JSONArray categoriesArray = (JSONArray) getValue(jsonResult, "Categories", ValueType.JSONARRAY);
            ArrayList<Integer> categories = new ArrayList<>();
            if (categoriesArray != null) {
                for (int j = 0; j < categoriesArray.length(); j++) {
                    categories.add((Integer) categoriesArray.get(j));
                }
            }
            product.setCategories(categories);
        } catch (Exception e) {
            Log.d("EcommerceSDK", "Error when parsing categories");
        }

        try {
            JSONArray pmArray = (JSONArray) getValue(jsonResult, "PaymentMethods", ValueType.JSONARRAY);
            ArrayList<Integer> paymentMethods = new ArrayList<>();
            if (pmArray != null) {
                for (int j = 0; j < pmArray.length(); j++) {
                    paymentMethods.add((Integer) pmArray.get(j));
                }
            }
            product.setPaymentMethods(paymentMethods);
        } catch (Exception e) {
            Log.d("EcommerceSDK", "Error when parsing payment methods");
        }

        JSONObject merchantObj = (JSONObject) getValue(jsonResult, "Merchant", ValueType.JSONOBJECT);
        if (merchantObj != null) product.setMerchant(parseMerchant(merchantObj));

        return product;
    }

    protected static Merchant parseMerchant(JSONObject jsonResult) {
        Merchant merchant = new Merchant();
        merchant.setNumber((String) getValue(jsonResult, "Number", ValueType.STRING));
        merchant.setName((String) getValue(jsonResult, "Name", ValueType.STRING));
        merchant.setWebsite((String) getValue(jsonResult, "WebsiteUrl", ValueType.STRING));
        merchant.setCountryIso((String) getValue(jsonResult, "CountryIso", ValueType.STRING));
        merchant.setAddress1((String) getValue(jsonResult, "AddressLine1", ValueType.STRING));
        merchant.setCity((String) getValue(jsonResult, "City", ValueType.STRING));
        merchant.setPhone((String) getValue(jsonResult, "PhoneNumber", ValueType.STRING));
        merchant.setEmail((String) getValue(jsonResult, "Email", ValueType.STRING));
        merchant.setBannerUrl((String) getValue(jsonResult, "BannerUrl", ValueType.STRING));
        merchant.setFax((String) getValue(jsonResult, "FaxNumber", ValueType.STRING));
        merchant.setTwitterUrl((String) getValue(jsonResult, "TwitterUrl", ValueType.STRING));
        merchant.setZipCode((String) getValue(jsonResult, "PostalCode", ValueType.STRING));
        merchant.setGooglePlusUrl((String) getValue(jsonResult, "GooglePlusUrl", ValueType.STRING));
        merchant.setBannerLinkUrl((String) getValue(jsonResult, "BannerLinkUrl", ValueType.STRING));
        merchant.setVimeoUrl((String) getValue(jsonResult, "VimeoUrl", ValueType.STRING));
        merchant.setGroup((String) getValue(jsonResult, "Group", ValueType.STRING));
        merchant.setBaseColor((String) getValue(jsonResult, "UIBaseColor", ValueType.STRING));
        merchant.setPinterestUrl((String) getValue(jsonResult, "PinterestUrl", ValueType.STRING));
        merchant.setYoutubeUrl((String) getValue(jsonResult, "YoutubeUrl", ValueType.STRING));
        merchant.setLinkedinUrl((String) getValue(jsonResult, "LinkedinUrl", ValueType.STRING));
        merchant.setAddress2((String) getValue(jsonResult, "AddressLine2", ValueType.STRING));
        merchant.setStateIso((String) getValue(jsonResult, "StateIso", ValueType.STRING));
        merchant.setFacebookUrl((String) getValue(jsonResult, "FacebookUrl", ValueType.STRING));
        merchant.setLogoUrl((String) getValue(jsonResult, "LogoUrl", ValueType.STRING));

        try {
            JSONArray currArray = (JSONArray) getValue(jsonResult, "Currencies", ValueType.JSONARRAY);
            ArrayList<String> currencies = new ArrayList<>();
            if (currArray != null) {
                for (int j=0; j<currArray.length(); j++) {
                    currencies.add((String) currArray.get(j));
                }
            }
            merchant.setCurrencies(currencies);
        } catch (Exception e) {
            Log.d("EcommerceSDK", "Error when parsing currencies");
        }
        return merchant;
    }

    protected static CartItem parseCartItem(JSONObject jsonResult) {
        CartItem item = new CartItem();
        item.setItemId((String) getValue(jsonResult, "ID", ValueType.STRING));
        item.setItemProductId((String) getValue(jsonResult, "ProductId", ValueType.STRING));
        item.setItemProductStockId((long) getValue(jsonResult, "ProductStockId", ValueType.LONG));
        item.setItemImageUrl((String) getValue(jsonResult, "ProductImageUrl", ValueType.STRING));
        item.setItemInsertDate(getReadableDate(jsonResult, "InsertDate"));
        item.setItemName((String) getValue(jsonResult, "Name", ValueType.STRING));
        item.setItemQuantity((String) getValue(jsonResult, "Quantity", ValueType.STRING));
        item.setItemPrice((String) getValue(jsonResult, "Price", ValueType.STRING));
        item.setItemCurrencyIsoCode((String) getValue(jsonResult, "CurrencyISOCode", ValueType.STRING));
        item.setItemCurrencyFxRate((String) getValue(jsonResult, "CurrencyFXRate", ValueType.STRING));
        item.setItemShippingFee((String) getValue(jsonResult, "ShippingFee", ValueType.STRING));
        item.setItemVATPercent((String) getValue(jsonResult, "VATPercent", ValueType.STRING));
        item.setItemTotalProduct((double) getValue(jsonResult, "TotalProduct", ValueType.DOUBLE));
        item.setItemTotal((String) getValue(jsonResult, "Total", ValueType.STRING));
        item.setItemMinQuantity((String) getValue(jsonResult, "MinQuantity", ValueType.STRING));
        item.setItemMaxQuantity((String) getValue(jsonResult, "MaxQuantity", ValueType.STRING));
        item.setItemStepQuantity((String) getValue(jsonResult, "StepQuantity", ValueType.STRING));
        item.setItemTotalShipping((String) getValue(jsonResult, "TotalShipping", ValueType.STRING));
        item.setItemIsAvailable((boolean) getValue(jsonResult, "IsAvailable", ValueType.BOOLEAN));
        item.setItemReceiptLink((String) getValue(jsonResult, "ReceiptLink", ValueType.STRING));
        item.setItemReceiptText((String) getValue(jsonResult, "ReceiptText", ValueType.STRING));
        item.setItemType((String) getValue(jsonResult, "Type", ValueType.STRING));
        item.setItemGuestDownloadUrl((String) getValue(jsonResult, "GuestDownloadUrl", ValueType.STRING));
        item.setItemDownloadMediaType((String) getValue(jsonResult, "DownloadMediaType", ValueType.STRING));
        item.setItemIsChanged((boolean) getValue(jsonResult, "IsChanged", ValueType.BOOLEAN));
        item.setItemChangedPrice((String) getValue(jsonResult, "ChangedPrice", ValueType.STRING));
        item.setItemChangedTotal((String) getValue(jsonResult, "ChangedTotal", ValueType.STRING));
        item.setItemChangedCurrencyIso((String) getValue(jsonResult, "ChangedCurrencyIsoCode", ValueType.STRING));

        try {
            JSONArray propArray = (JSONArray) getValue(jsonResult, "ItemProperties", ValueType.JSONARRAY);
            ArrayList<Property> properties = new ArrayList<>();
            if (propArray != null) {
                for (int j = 0; j < propArray.length(); j++) {
                    Property property = parseProperty((JSONObject)propArray.get(j));
                    properties.add(property);
                }
            }
            item.setItemProperties(properties);
        } catch (Exception e) {
            Log.d("EcommerceSDK", "Error when parsing properties");
        }
        return item;
    }

    protected static Cart parseCart(JSONObject jsonResult) {
        Cart cart = new Cart("", "");
        try {
            JSONArray items = (JSONArray) getValue(jsonResult, "Items", ValueType.JSONARRAY);
            ArrayList<CartItem> cartItems = new ArrayList<>();
            if (items != null) {
                for (int i=0; i<items.length(); i++) {
                    CartItem item = parseCartItem((JSONObject) items.get(i));
                    cartItems.add(item);
                }
            }
            cart.setShopCartItems(cartItems);
        } catch (Exception e) {
            Log.d("EcommerceSDK", "Error when parsing cart items");
        }

        JSONObject merchantObj = (JSONObject) getValue(jsonResult, "Merchant", ValueType.JSONOBJECT);
        if (merchantObj != null) cart.setMerchant(parseMerchant(merchantObj));

        cart.setCartCookie((String) getValue(jsonResult, "Cookie", ValueType.STRING));
        cart.setTotalPrice((String) getValue(jsonResult, "Total", ValueType.STRING));
        cart.setCartCurrencyIso((String) getValue(jsonResult, "CurrencyIso", ValueType.STRING));
        cart.setInstallments((String) getValue(jsonResult, "Installments", ValueType.STRING));
        cart.setMaxInstallments((String) getValue(jsonResult, "MaxInstallments", ValueType.STRING));
        cart.setCartMerchantReference((String) getValue(jsonResult, "MerchantReference", ValueType.STRING));
        cart.setCartCheckoutUrl((String) getValue(jsonResult, "CheckoutUrl", ValueType.STRING));
        cart.setCartIsChanged((boolean) getValue(jsonResult, "IsChanged", ValueType.BOOLEAN));
        cart.setCartChangedTotal((String) getValue(jsonResult, "ChangedTotal", ValueType.STRING));
        if (cart.getMerchant() == null) cart.setMerchant(new Merchant());
        cart.getMerchant().setNumber((String) getValue(jsonResult, "MerchantNumber", ValueType.STRING));

        return cart;
    }

    protected static ProductCategory parseCategory(JSONObject jsonResult) {
        ProductCategory category = new ProductCategory();
        category.setCategoryId((String) getValue(jsonResult, "ID", ValueType.STRING));
        category.setCategoryName((String) getValue(jsonResult, "Name", ValueType.STRING));

        try {
            JSONArray subCategories = (JSONArray) getValue(jsonResult, "SubCategories", ValueType.JSONARRAY);
            ArrayList<ProductCategory> subCategoriesToAdd = new ArrayList<>();
            if (subCategories != null) {
                for (int i=0; i<subCategories.length(); i++) {
                    ProductCategory subCategory = parseCategory((JSONObject) subCategories.get(i));
                    subCategoriesToAdd.add(subCategory);
                }
            }
            category.setSubcategories(subCategoriesToAdd);
        } catch (Exception e) {
            Log.d("EcommerceSDK", "Error when parsing subcategories");
        }

        return category;
    }

    protected static Object getValue(JSONObject resultObject, String key, ValueType type) {
        return getValueBase(resultObject, key, type);
    }

    protected static String getErrorText(VolleyError error) {
        return getErrorTextBase(error);
    }

    protected static Date getReadableDate(JSONObject jsonResult, String key) {
        return getReadableDateBase(jsonResult, key);
    }

    protected static boolean isEmptyOrNull(String value) {
        return isEmptyOrNullBase(value);
    }

    protected static boolean isRequestSuccessful(JSONObject resultObject) {
        return isRequestSuccessfulBase(resultObject);
    }

    protected static String getMessage(JSONObject resultObject) {
        return getMessageBase(resultObject);
    }
}