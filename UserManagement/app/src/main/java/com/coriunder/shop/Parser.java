package com.coriunder.shop;

import android.util.Log;

import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.utils.BaseParser;
import com.coriunder.shop.models.Cart;
import com.coriunder.shop.models.CartItem;
import com.coriunder.base.common.models.Merchant;
import com.coriunder.shop.models.Product;
import com.coriunder.shop.models.ProductCategory;
import com.coriunder.shop.models.Property;
import com.coriunder.shop.models.ShopLocation;
import com.coriunder.shop.models.Shop;
import com.coriunder.shop.models.Stock;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parser methods for Shop services
 */
public class Parser extends BaseParser {
    /**
     * Method to parse JSONObject to Cart object
     * @param jsonResult JSONObject to parse
     * @return Cart object
     */
    protected static Cart parseCart(JSONObject jsonResult) {
        Cart cart = new Cart("", "");
        cart.setChangedTotal(getDouble(jsonResult, "ChangedTotal"));
        cart.setCheckoutUrl(getString(jsonResult, "CheckoutUrl"));
        cart.setCookie(getString(jsonResult, "Cookie"));
        cart.setCurrencyIso(getString(jsonResult, "CurrencyIso"));
        cart.setInstallments(getString(jsonResult, "Installments"));
        cart.setChanged(getBoolean(jsonResult, "IsChanged"));
        cart.setItems(parseCartItems(getJsonArray(jsonResult, "Items")));
        cart.setMaxInstallments(getString(jsonResult, "MaxInstallments"));
        cart.setMerchant(parseMerchant(getJsonObject(jsonResult, "Merchant"), R.string.shop_log));
        cart.getMerchant().setNumber(getString(jsonResult, "MerchantNumber"));
        cart.setMerchantReference(getString(jsonResult, "MerchantReference"));
        cart.setShopId(getLong(jsonResult, "ShopId"));
        cart.setTotalPrice(getDouble(jsonResult, "Total"));
        return cart;
    }

    /**
     * Method to parse JSONObject to ArrayList containing CartItem objects
     * @param items JSONObject to parse
     * @return ArrayList containing CartItem objects
     */
    protected static ArrayList<CartItem> parseCartItems(JSONArray items) {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        if (items != null) {
            for (int i=0; i<items.length(); i++) {
                try {
                    JSONObject obj = items.getJSONObject(i);
                    CartItem item = Parser.parseCartItem(obj);
                    cartItems.add(item);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_cart_items) + i);
                }
            }
        }
        return cartItems;
    }

    /**
     * Method to parse JSONObject to CartItem object
     * @param jsonResult JSONObject to parse
     * @return CartItem object
     */
    protected static CartItem parseCartItem(JSONObject jsonResult) {
        CartItem item = new CartItem();
        item.setChangedCurrencyIso(getString(jsonResult, "ChangedCurrencyIsoCode"));
        item.setChangedPrice(getDouble(jsonResult, "ChangedPrice"));
        item.setChangedTotal(getDouble(jsonResult, "ChangedTotal"));
        item.setCurrencyFxRate(getDouble(jsonResult, "CurrencyFXRate"));
        item.setCurrencyIsoCode(getString(jsonResult, "CurrencyISOCode"));
        item.setDownloadMediaType(getString(jsonResult, "DownloadMediaType"));
        item.setGuestDownloadUrl(getString(jsonResult, "GuestDownloadUrl"));
        item.setItemId(getLong(jsonResult, "ID"));
        item.setInsertDate(getReadableDate(jsonResult, "InsertDate"));
        item.setAvailable(getBoolean(jsonResult, "IsAvailable"));
        item.setChanged(getBoolean(jsonResult, "IsChanged"));

        // Parse cart item's properties
        JSONArray propArray = getJsonArray(jsonResult, "ItemProperties");
        ArrayList<Property> properties = new ArrayList<>();
        if (propArray != null) {
            for (int i=0; i<propArray.length(); i++) {
                try {
                    JSONObject obj = propArray.getJSONObject(i);
                    Property property = parseCartProperty(obj);
                    properties.add(property);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_properties) + i);
                }
            }
        }
        item.setItemProperties(properties);

        item.setMaxQuantity(getInt(jsonResult, "MaxQuantity"));
        item.setMinQuantity(getInt(jsonResult, "MinQuantity"));
        item.setName(getString(jsonResult, "Name"));
        item.setPrice(getDouble(jsonResult, "Price"));
        item.setProductId(getLong(jsonResult, "ProductId"));
        item.setImageUrl(getString(jsonResult, "ProductImageUrl"));
        item.setProductStockId(getLong(jsonResult, "ProductStockId"));
        item.setQuantity(getInt(jsonResult, "Quantity"));
        item.setReceiptLink(getString(jsonResult, "ReceiptLink"));
        item.setReceiptText(getString(jsonResult, "ReceiptText"));
        item.setShippingFee(getDouble(jsonResult, "ShippingFee"));
        item.setStepQuantity(getInt(jsonResult, "StepQuantity"));
        item.setTotal(getDouble(jsonResult, "Total"));
        item.setTotalProduct(getDouble(jsonResult, "TotalProduct"));
        item.setTotalShipping(getDouble(jsonResult, "TotalShipping"));
        item.setType(getString(jsonResult, "Type"));
        item.setVatPercent(getDouble(jsonResult, "VATPercent"));
        return item;
    }

    /**
     * Method to parse JSONObject to Property object
     * @param jsonResult JSONObject to parse
     * @return Property object
     */
    protected static Property parseCartProperty(JSONObject jsonResult) {
        Property property = new Property();
        property.setText(getString(jsonResult, "Name"));
        property.setPropertyId(getLong(jsonResult, "PropertyID"));
        property.setValue(getString(jsonResult, "Value"));
        return property;
    }

    /**
     * Method to parse JSONObject to ProductCategory object
     * @param jsonResult JSONObject to parse
     * @return ProductCategory object
     */
    protected static ProductCategory parseCategory(JSONObject jsonResult) {
        ProductCategory category = new ProductCategory();
        category.setCategoryId(getInt(jsonResult, "ID"));
        category.setName(getString(jsonResult, "Name"));

        JSONArray subCategories = Parser.getJsonArray(jsonResult, "SubCategories");
        ArrayList<ProductCategory> subCategoriesToAdd = new ArrayList<>();
        if (subCategories != null) {
            for (int i=0; i<subCategories.length(); i++) {
                try {
                    JSONObject obj = subCategories.getJSONObject(i);
                    ProductCategory subCategory = parseCategory(obj);
                    subCategoriesToAdd.add(subCategory);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_subcategories) + i);
                }
            }
        }
        category.setSubCategories(subCategoriesToAdd);
        return category;
    }

    /**
     * Method to parse JSONObject to Product object
     * @param jsonResult JSONObject to parse
     * @return Product object
     */
    protected static Product parseProduct(JSONObject jsonResult) {
        Product product = new Product();

        // Parse product's categories
        JSONArray categoriesArray = getJsonArray(jsonResult, "Categories");
        ArrayList<Integer> categories = new ArrayList<>();
        if (categoriesArray != null) {
            for (int i = 0; i < categoriesArray.length(); i++){
                try {
                    categories.add((Integer) categoriesArray.get(i));
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_categories) + i);
                }
            }
        }
        product.setCategories(categories);

        product.setCategoryId(getLong(jsonResult, "CategoryId"));
        product.setCategoryName(getString(jsonResult, "CategoryName"));
        product.setCheckoutUrl(getString(jsonResult, "CheckoutUrl"));
        product.setCurrencyIso(getString(jsonResult, "Currency"));
        product.setProductDescription(getString(jsonResult, "Description"));
        product.setProductId(getLong(jsonResult, "ID"));
        product.setImageUrl(getString(jsonResult, "ImageURL"));
        product.setDynamicAmount(getBoolean(jsonResult, "IsDynamicAmount"));
        product.setRecurring(getBoolean(jsonResult, "IsRecurring"));
        product.setMerchant(parseMerchant(getJsonObject(jsonResult, "Merchant"), R.string.shop_log));
        product.setMetaDescription(getString(jsonResult, "Meta_Description"));
        product.setMetaKeywords(getString(jsonResult, "Meta_Keywords"));
        product.setMetaTitle(getString(jsonResult, "Meta_Title"));
        product.setName(getString(jsonResult, "Name"));
        product.setNextProductId(getLong(jsonResult, "NetxProductId"));

        // Parse product's payment methods
        JSONArray pmsArray = getJsonArray(jsonResult, "PaymentMethods");
        ArrayList<Integer> paymentMethods = new ArrayList<>();
        if (pmsArray != null) {
            for (int i = 0; i < pmsArray.length(); i++){
                try {
                    paymentMethods.add((Integer) pmsArray.get(i));
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_pms) + i);
                }
            }
        }
        product.setPaymentMethods(paymentMethods);
        
        product.setPreviousProductId(getLong(jsonResult, "PrevProductId"));
        product.setPrice(getDouble(jsonResult, "Price"));
        product.setProductUrl(getString(jsonResult, "ProductURL"));

        // Parse product's properties
        JSONArray propArray = Parser.getJsonArray(jsonResult, "Properties");
        ArrayList<Property> properties = new ArrayList<>();
        if (propArray != null) {
            for (int i=0; i<propArray.length(); i++) {
                try {
                    JSONObject obj = propArray.getJSONObject(i);
                    Property property = parseProperty(obj);
                    properties.add(property);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_properties) + i);
                }
            }
        }
        product.setProperties(properties);

        product.setQuantityAvailable(getInt(jsonResult, "QuantityAvailable"));
        product.setQuantityInterval(getInt(jsonResult, "QuantityInterval"));
        product.setQuantityMax(getInt(jsonResult, "QuantityMax"));
        product.setQuantityMin(getInt(jsonResult, "QuantityMin"));
        product.setRecurringDisplay(getString(jsonResult, "RecurringDisplay"));
        product.setSku(getString(jsonResult, "SKU"));

        // Parse product's stocks
        JSONArray stocksArray = getJsonArray(jsonResult, "Stocks");
        ArrayList<Stock> stocks = new ArrayList<>();
        if (stocksArray != null) {
            for (int i=0; i<stocksArray.length(); i++) {
                try {
                    JSONObject stockObj = stocksArray.getJSONObject(i);
                    Stock stock = new Stock();
                    stock.setStockId(getLong(stockObj, "ID"));

                    // Parse property values for stock
                    JSONArray propsArray = getJsonArray(stockObj, "PropertyValues");
                    ArrayList<Long> propertyIds = new ArrayList<>();
                    if (propsArray != null) {
                        for (int j = 0; j < propsArray.length(); j++){
                            try {
                                propertyIds.add(propsArray.getLong(j));
                            } catch (Exception e) {
                                Log.e(Coriunder.getContext().getString(R.string.shop_log),
                                        Coriunder.getContext().getString(R.string.shop_error_stock_property) + i);
                            }
                        }
                    }
                    stock.setPropertyIds(propertyIds);

                    stock.setQuantityAvailable(getInt(stockObj, "QuantityAvailable"));
                    stock.setSku(getString(stockObj, "SKU"));
                    stocks.add(stock);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_stocks) + i);
                }
            }
        }
        product.setStocks(stocks);

        product.setType(getString(jsonResult, "Type"));
        return product;
    }

    /**
     * Method to parse JSONObject to Property object
     * @param jsonResult JSONObject to parse
     * @return Property object
     */
    protected static Property parseProperty(JSONObject jsonResult) {
        Property property = new Property();
        property.setPropertyId(getLong(jsonResult, "ID"));
        property.setText(getString(jsonResult, "Text"));
        property.setType(getString(jsonResult, "Type"));
        property.setValue(getString(jsonResult, "Value"));

        // Parse subProperties for the property
        JSONArray values = Parser.getJsonArray(jsonResult, "Values");
        ArrayList<Property> subProperties = new ArrayList<>();
        if (values != null) {
            for (int i=0; i<values.length(); i++) {
                try {
                    JSONObject obj = values.getJSONObject(i);
                    Property subProperty = parseProperty(obj);
                    subProperties.add(subProperty);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_subproperties) + i);
                }
            }
        }
        property.setSubProperties(subProperties);

        return property;
    }

    /**
     * Method to parse JSONObject to Shop object
     * @param jsonResult JSONObject to parse
     * @return Shop object
     */
    protected static Shop parseShop(JSONObject jsonResult) {
        Shop shop = new Shop();
        shop.setBannerLinkUrl(getString(jsonResult, "BannerLinkUrl"));
        shop.setBannerUrl(getString(jsonResult, "BannerUrl"));

        // Parse countries for shop
        JSONArray countriesJson = Parser.getJsonArray(jsonResult, "Countries");
        ArrayList<ShopLocation> countries = new ArrayList<>();
        if (countriesJson != null) {
            for (int i=0; i<countriesJson.length(); i++) {
                try {
                    JSONObject obj = countriesJson.getJSONObject(i);
                    ShopLocation country = new ShopLocation();
                    country.setIsoCode(getString(obj, "IsoCode"));
                    country.setName(getString(obj, "Name"));
                    countries.add(country);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_countries) + i);
                }
            }
        }
        shop.setCountries(countries);

        shop.setCurrencyIsoCode(getString(jsonResult, "CurrencyIsoCode"));
        shop.setFacebookUrl(getString(jsonResult, "FacebookUrl"));
        shop.setGooglePlusUrl(getString(jsonResult, "GooglePlusUrl"));
        shop.setLinkedinUrl(getString(jsonResult, "LinkedinUrl"));
        shop.setLocationsString(getString(jsonResult, "LocationsString"));
        shop.setLogoUrl(getString(jsonResult, "LogoUrl"));
        shop.setPinterestUrl(getString(jsonResult, "PinterestUrl"));

        // Parse regions for shop
        JSONArray regionsJson = Parser.getJsonArray(jsonResult, "Regions");
        ArrayList<ShopLocation> regions = new ArrayList<>();
        if (regionsJson != null) {
            for (int i=0; i<regionsJson.length(); i++) {
                try {
                    JSONObject obj = regionsJson.getJSONObject(i);
                    ShopLocation region = new ShopLocation();
                    region.setIsoCode(getString(obj, "IsoCode"));
                    region.setName(getString(obj, "Name"));
                    regions.add(region);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_regions) + i);
                }
            }
        }
        shop.setRegions(regions);

        shop.setShopId(getLong(jsonResult, "ShopId"));
        shop.setTwitterUrl(getString(jsonResult, "TwitterUrl"));
        shop.setUiBaseColor(getString(jsonResult, "UIBaseColor"));
        shop.setVimeoUrl(getString(jsonResult, "VimeoUrl"));
        shop.setYoutubeUrl(getString(jsonResult, "YoutubeUrl"));
        return shop;
    }

    /**
     * Method to parse JSONObject to ArrayList containing Cart objects
     * @param response JSONObject to parse
     * @return ArrayList containing Cart objects
     */
    protected static ArrayList<Cart> parseActiveCarts(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<Cart> carts = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject obj = result.getJSONObject(i);
                    Cart cart = parseCart(obj);
                    carts.add(cart);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_carts) + i);
                }
            }
        }
        return carts;
    }

    /**
     * Method to parse JSONObject to ArrayList containing ProductCategory objects
     * @param response JSONObject to parse
     * @return ArrayList containing ProductCategory objects
     */
    protected static ArrayList<ProductCategory> parseCategories(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<ProductCategory> categories = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject obj = result.getJSONObject(i);
                    ProductCategory category = parseCategory(obj);
                    categories.add(category);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_categories) + i);
                }
            }
        }
        return categories;
    }

    /**
     * Method to parse JSONObject to ArrayList containing Merchant objects
     * @param response JSONObject to parse
     * @return ArrayList containing Merchant objects
     */
    protected static ArrayList<Merchant> parseMerchants(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<Merchant> merchants = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject obj = result.getJSONObject(i);
                    Merchant merchant = parseMerchant(obj, R.string.shop_log);
                    merchants.add(merchant);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_merchants) + i);
                }
            }
        }
        return merchants;
    }

    /**
     * Method to parse JSONObject to ArrayList containing Product objects
     * @param response JSONObject to parse
     * @return ArrayList containing Product objects
     */
    protected static ArrayList<Product> parseProducts(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<Product> products = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject obj = result.getJSONObject(i);
                    Product product = parseProduct(obj);
                    products.add(product);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_products) + i);
                }
            }
        }
        return products;
    }

    /**
     * Method to parse JSONObject to ArrayList containing Shop objects
     * @param response JSONObject to parse
     * @return ArrayList containing Shop objects
     */
    protected static ArrayList<Shop> parseShops(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<Shop> shops = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject obj = result.getJSONObject(i);
                    Shop shop = parseShop(obj);
                    shops.add(shop);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.shop_log),
                            Coriunder.getContext().getString(R.string.shop_error_shops) + i);
                }
            }
        }
        return shops;
    }
}