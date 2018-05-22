package com.coriunder.shop;

import android.text.TextUtils;

import com.coriunder.base.Coriunder;
import com.coriunder.shop.models.Cart;
import com.coriunder.shop.models.CartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class contains methods designed to prepare
 * JSONObjects to send with requests to Shop service
 */
public class JsonBuilder {
    /**
     * Prepare JSONObject for GetCart request
     * @param cookie cookie of the required cart
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetCart(String cookie) {
        JSONObject json = new JSONObject();
        try {
            json.put("cookie", TextUtils.isEmpty(cookie) ? "" : cookie);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetCartOfTransaction request
     * @param transactionId transaction id of the required cart
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetCartOfTransaction(long transactionId) {
        JSONObject json = new JSONObject();
        try {
            json.put("transactionId", transactionId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /*
    ToDoV2:
    Installments, MaxInstallments - unsignedByte;
    Merchant - no need;
    Items - most fields not added now
    */
    /**
     * Prepare JSONObject for SetCart request
     * @param cart cart object for the cart you want to update
     * @return prepared JSONObject
     * @see Cart
     */
    protected static JSONObject buildJsonForUpdateCart(Cart cart) {
        if (cart == null || cart.getMerchant() == null) return null;

        JSONObject json = new JSONObject();
        try {
            JSONObject cartJson = new JSONObject();
            cartJson.put("ChangedTotal", cart.getChangedTotal());
            cartJson.put("CheckoutUrl", TextUtils.isEmpty(cart.getCheckoutUrl()) ? "" : cart.getCheckoutUrl());
            cartJson.put("Cookie", TextUtils.isEmpty(cart.getCookie()) ? "" : cart.getCookie());
            cartJson.put("CurrencyIso", TextUtils.isEmpty(cart.getCurrencyIso()) ? "" : cart.getCurrencyIso());
            cartJson.put("Installments", cart.getInstallments());
            cartJson.put("IsChanged", cart.isChanged());

            // Create JSONArray with cart items
            ArrayList<JSONObject> itemsArray = new ArrayList<>();
            if (cart.getItems() != null) {
                for (CartItem item : cart.getItems()) {
                    JSONObject shopCartItem = new JSONObject();
                    shopCartItem.put("ProductId", item.getProductId());
                    if (item.getProductStockId() > 0) shopCartItem.put("ProductStockId", item.getProductStockId());
                    else shopCartItem.put("ProductStockId", JSONObject.NULL);
                    shopCartItem.put("Quantity", item.getQuantity());
                    shopCartItem.put("Price", item.getPrice());
                    itemsArray.add(shopCartItem);
                }
            }
            cartJson.put("Items", new JSONArray(itemsArray));

            cartJson.put("MaxInstallments", cart.getMaxInstallments());
            //cartJson.put("Merchant", );
            cartJson.put("MerchantNumber", TextUtils.isEmpty(cart.getMerchant().getNumber()) ? "" : cart.getMerchant().getNumber());
            cartJson.put("MerchantReference", TextUtils.isEmpty(cart.getMerchantReference()) ? "" : cart.getMerchantReference());
            cartJson.put("ShopId", cart.getShopId());
            cartJson.put("Total", cart.getTotalPrice());
            json.put("cart", cartJson);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for Download request
     * @param itemId id of the item to download
     * @param asPlainData form of downloaded data
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForDownload(long itemId, boolean asPlainData) {
        JSONObject json = new JSONObject();
        try {
            json.put("itemId", itemId);
            json.put("asPlainData", asPlainData);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for DownloadUnauthorized request
     * @param fileKey file key of the item to download
     * @param asPlainData form of downloaded data
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForDownloadUnauthorized(String fileKey, boolean asPlainData) {
        JSONObject json = new JSONObject();
        try {
            json.put("fileKey", TextUtils.isEmpty(fileKey) ? "" : fileKey);
            json.put("asPlainData", asPlainData);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetDownloads request
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForDownloads(int page, int pageSize) {
        JSONObject json = new JSONObject();
        try {
            // Create sortAndPage JSONObject
            JSONObject sortAndPage = new JSONObject();
            sortAndPage.put("PageNumber", page);
            sortAndPage.put("PageSize", pageSize);
            json.put("sortAndPage", sortAndPage);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetMerchant request
     * @param merchantNumber id of the required merchant
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetMerchant(String merchantNumber) {
        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", TextUtils.isEmpty(merchantNumber) ? "" : merchantNumber);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetMerchantCategories request
     * @param merchantNumber id of the required merchant
     * @param language set which language should be included
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetCategories(String merchantNumber, String language) {
        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", TextUtils.isEmpty(merchantNumber) ? "" : merchantNumber);
            json.put("language", TextUtils.isEmpty(language) ? "" : language);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetMerchantContent request
     * @param merchantNumber id of the required merchant
     * @param language language of content to return
     * @param contentName name of content you want to get
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetContent(String merchantNumber, String language, String contentName) {
        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", TextUtils.isEmpty(merchantNumber) ? "" : merchantNumber);
            json.put("language", TextUtils.isEmpty(language) ? "" : language);
            json.put("contentName", TextUtils.isEmpty(contentName) ? "" : contentName);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetMerchants request
     * @param groupId id of the group which merchants should belong to
     * @param status merchant status
     * @param text search text
     * @return prepared JSONObject
     * @see ShopSDKMerchants.MerchantStatus
     */
    protected static JSONObject buildJsonForGetMerchants(long groupId, ShopSDKMerchants.MerchantStatus status, String text) {
        JSONObject json = new JSONObject();
        try {
            // Create filters JSONObject
            JSONObject filters = new JSONObject();
            filters.put("ApplicationToken", Coriunder.getAppToken());
            filters.put("GroupId", groupId);

            // Put int value depending on the merchant status
            if (status != ShopSDKMerchants.MerchantStatus.ALL) {
                int statusInt = 0;
                switch (status) {
                    case ARCHIVED: statusInt = 0; break;
                    case NEW: statusInt = 1; break;
                    case BLOCKED: statusInt = 2; break;
                    case CLOSED: statusInt = 3; break;
                    case LOGIN_ONLY: statusInt = 10; break;
                    case INTEGRATION: statusInt = 20; break;
                    case PROCESSING: statusInt = 30; break;
                    default: break;
                }
                filters.put("MerchantStatus", statusInt);
            }

            filters.put("Text", TextUtils.isEmpty(text) ? "" : text);
            json.put("filters", filters);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetCategorisedProducts request
     * @param category id of the category to load products from
     * @param shopId id of the shop to load products from
     * @param language set language to load products with
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForCategorisedProducts(long category, long shopId, String language) {
        JSONObject json = new JSONObject();
        try {
            json.put("shopId", shopId);
            json.put("itemsPerCategory", category);
            json.put("language", TextUtils.isEmpty(language) ? "" : language);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetProduct request
     * @param merchantNumber id of the merchant which product belongs to
     * @param productId id of the required product
     * @param language language which product data should be returned with
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetProduct(String merchantNumber, long productId, String language) {
        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", TextUtils.isEmpty(merchantNumber) ? "" : merchantNumber);
            json.put("itemId", productId);
            json.put("language", TextUtils.isEmpty(language) ? "" : language);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetProducts request
     * @param categories array of product categories to search in
     * @param countries array of countries to search in
     * @param includeGlobalRegion set whether you need global region products to be included or not
     * @param language set language to get products with
     * @param merchantGroups array of merchant groups to search in
     * @param merchantId specify merchant id to get exact merchant's products
     * @param name name of the product
     * @param promoOnly get only promo products
     * @param regions array of regions to search in
     * @param shopId id of the shop to search products in
     * @param tags tags to search product with
     * @param page number of the page with results, minimum value is 1
     * @param pageSize results amount per page
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetProducts(int[] categories, String[] countries, boolean includeGlobalRegion,
                                String language, int[] merchantGroups, String merchantId, String name, boolean promoOnly,
                                String[] regions, long shopId, String tags, int page, int pageSize) {
        JSONObject json = new JSONObject();
        try {
            // Create filters JSONObject
            JSONObject filters = new JSONObject();

            // Create JSONArray from int[]
            JSONArray categoriesArray = new JSONArray();
            if (categories != null)
                for (int category : categories) {
                    categoriesArray.put(category);
                }
            filters.put("Categories", categoriesArray);

            // Create JSONArray from String[]
            JSONArray countriesArray = new JSONArray();
            if (countries != null)
                for (String country : countries) {
                    countriesArray.put(country);
                }
            filters.put("Countries", countriesArray);

            filters.put("IncludeGlobalRegion", includeGlobalRegion);
            filters.put("Language", TextUtils.isEmpty(language) ? "" : language);

            // Create JSONArray from int[]
            JSONArray merchantGroupsArray = new JSONArray();
            if (merchantGroups != null)
                for (int merchantGroup : merchantGroups) {
                    merchantGroupsArray.put(merchantGroup);
                }
            filters.put("MerchantGroups", merchantGroupsArray);

            filters.put("MerchantNumber", TextUtils.isEmpty(merchantId) ? "" : merchantId);
            filters.put("Name", TextUtils.isEmpty(name) ? "" : name);
            filters.put("PromoOnly", promoOnly);

            // Create JSONArray from String[]
            JSONArray regionsArray = new JSONArray();
            if (regions != null)
                for (String region : regions) {
                    regionsArray.put(region);
                }
            filters.put("Regions", regionsArray);

            filters.put("ShopId", shopId);
            filters.put("Tags", TextUtils.isEmpty(tags) ? "" : tags);
            json.put("filters", filters);

            // Create sortAndPage JSONObject
            JSONObject sortAndPage = new JSONObject();
            sortAndPage.put("PageNumber", page);
            sortAndPage.put("PageSize", pageSize);
            json.put("sortAndPage", sortAndPage);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetShop request
     * @param merchantNumber id of the merchant which shop has to be received
     * @param shopId id of the required shop
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetShop(String merchantNumber, long shopId) {
        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", TextUtils.isEmpty(merchantNumber) ? "" : merchantNumber);
            json.put("shopId", shopId);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetShopIds request
     * @param subDomainName sub-domain which the shop belongs too
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetShopIds(String subDomainName) {
        JSONObject json = new JSONObject();
        try {
            json.put("subDomainName", TextUtils.isEmpty(subDomainName) ? "" : subDomainName);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetShops request
     * @param merchantNumber id of the merchant which shops have to be received
     * @param culture culture of required shops
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetShops(String merchantNumber, String culture) {
        JSONObject json = new JSONObject();
        try {
            json.put("merchantNumber", TextUtils.isEmpty(merchantNumber) ? "" : merchantNumber);
            json.put("culture", TextUtils.isEmpty(culture) ? "" : culture);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for GetShopsByLocation request
     * @param regions array of regions to search in
     * @param countries array of countries to search in
     * @param includeGlobalShops set whether you need global region shops to be included or not
     * @param culture culture of required shops
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForGetShopsByLocation(String[] regions, String[] countries,
                                                         boolean includeGlobalShops, String culture) {
        JSONObject json = new JSONObject();
        try {
            // Create JSONArray from String[]
            JSONArray regionsArray = new JSONArray();
            if (regions != null)
                for (String region : regions) {
                    regionsArray.put(region);
                }
            json.put("regions", regionsArray);

            // Create JSONArray from String[]
            JSONArray countriesArray = new JSONArray();
            if (countries != null)
                for (String country : countries) {
                    countriesArray.put(country);
                }
            json.put("countries", countriesArray);

            json.put("includeGlobalShops", includeGlobalShops);
            json.put("culture", TextUtils.isEmpty(culture) ? "" : culture);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * Prepare JSONObject for SetSession request
     * @param declineUrl set decline URL
     * @param imageHeight desired image height
     * @param imageWidth desired image width
     * @param pendingUrl set pending URL
     * @param successUrl set success URL
     * @return prepared JSONObject
     */
    protected static JSONObject buildJsonForSetSession(String declineUrl, long imageHeight, long imageWidth,
                                                       String pendingUrl, String successUrl) {
        JSONObject json = new JSONObject();
        try {
            json.put("applicationToken", Coriunder.getAppToken());
            // Create options JSONObject
            JSONObject options = new JSONObject();
            options.put("DeclineUrl", TextUtils.isEmpty(declineUrl) ? "" : declineUrl);
            options.put("ImageHeight", imageHeight);
            options.put("ImageWidth", imageWidth);
            options.put("PendingUrl", TextUtils.isEmpty(pendingUrl) ? "" : pendingUrl);
            options.put("SuccessUrl", TextUtils.isEmpty(successUrl) ? "" : successUrl);
            json.put("options", options);
        } catch (JSONException e) {
            return null;
        }
        return json;
    }
}