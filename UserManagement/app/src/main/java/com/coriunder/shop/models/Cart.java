package com.coriunder.shop.models;

import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.common.models.Merchant;

import java.util.ArrayList;

/**
 * Information about exact cart
 */
@SuppressWarnings("unused")
public class Cart {
    /*
    ToDoV2:
    installments and maxInstallments are String but should be unsignedByte
     */
    private double changedTotal;
    private String checkoutUrl;
    private String cookie;
    private String currencyIso;
    private String installments;
    private boolean isChanged;
    private ArrayList<CartItem> items;
    private String maxInstallments;
    private Merchant merchant;
    private String merchantReference;
    private long shopId;
    private double totalPrice;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public Cart(String merchantId, String currencyIso) {
        changedTotal = 0;
        checkoutUrl = "";
        cookie = null;
        this.currencyIso = currencyIso;
        installments = "0";
        isChanged = false;
        items = new ArrayList<>();
        maxInstallments = "0";
        merchant = new Merchant();
        merchant.setNumber(merchantId);
        merchantReference = "";
        shopId = 0;
        totalPrice = 0;
    }

    public long getShopId() { return shopId; }
    public void setShopId(long shopId) { this.shopId = shopId; }

    public String getCookie() { return cookie; }
    public void setCookie(String cookie) { this.cookie = cookie; }

    public ArrayList<CartItem> getItems() { return items; }
    public void setItems(ArrayList<CartItem> items) { this.items = items; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getCurrencyIso() { return currencyIso; }
    public void setCurrencyIso(String currencyIso) { this.currencyIso = currencyIso; }

    public String getCheckoutUrl() { return checkoutUrl; }
    public void setCheckoutUrl(String checkoutUrl) { this.checkoutUrl = checkoutUrl; }

    public String getMerchantReference() { return merchantReference; }
    public void setMerchantReference(String merchantReference) { this.merchantReference = merchantReference; }

    public Merchant getMerchant() { return merchant; }
    public void setMerchant(Merchant merchant) { this.merchant = merchant; }

    public String getInstallments() { return installments; }
    public void setInstallments(String installments) { this.installments = installments; }

    public String getMaxInstallments() { return maxInstallments; }
    public void setMaxInstallments(String maxInstallments) { this.maxInstallments = maxInstallments; }

    public boolean isChanged() { return isChanged; }
    public void setChanged(boolean changed) { this.isChanged = changed; }

    public double getChangedTotal() { return changedTotal; }
    public void setChangedTotal(double changedTotal) { this.changedTotal = changedTotal; }

    /**
     * Call this method to add product to the cart
     *
     * @param product - product to add
     * @param stockId - stock id or 0 in case product doesn't have stocks
     * @param price - price of the product
     * @param amount - amount of pieces of this product to order
     * @return error text if any or "Success" message in case the product was successfully added to the cart
     */
    public String addProduct(Product product, long stockId, double price, int amount) {
        // Check whether data sent to the method is correct
        if (product == null) return Coriunder.getContext().getString(R.string.cart_empty_product);
        if (amount < 1) return Coriunder.getContext().getString(R.string.cart_small_amount);

        // Check whether product and the cart have same merchant
        if (product.getMerchant()!=null && merchant!=null && !product.getMerchant().getNumber().equals(merchant.getNumber()))
            return Coriunder.getContext().getString(R.string.cart_wrong_merchant_cart);

        // In case product has stocks check whether stock is correct
        Stock currentStock = null;
        if (product.getStocks() != null && product.getStocks().size() > 0) {
            for (Stock stock : product.getStocks()) {
                if (stock.getStockId() == stockId) {
                    // Stock was found
                    currentStock = stock;
                    break;
                }
            }

            // (currentStock == null) means that such stock wasn't found
            if (currentStock == null) return Coriunder.getContext().getString(R.string.cart_wrong_stock);
        }

        // Creating new items' array in case cart's items' array is null
        if (items == null) items = new ArrayList<>();

        // Check for duplicates and different dynamic prices
        CartItem duplicatedItem = null;
        int duplicatedItemPosition = 0;
        for (int i = 0; i < items.size(); i++) {
            CartItem shopCartItem = items.get(i);
            if (shopCartItem.getProductId() == product.getProductId() &&
                (!product.isDynamicAmount() || (product.isDynamicAmount() &&
                      price - shopCartItem.getPrice() == 0)) &&
                (stockId == shopCartItem.getProductStockId())) {
                // Same item with same price detected
                duplicatedItem = shopCartItem;
                duplicatedItemPosition = i;
                break;
            }
        }

        // This is how many items similar to our product we have in the cart
        int previousQuantity = 0;
        if (duplicatedItem != null) previousQuantity = duplicatedItem.getQuantity();
        // This is how many such items will be in the cart after we will add our product
        int resultQuantity = previousQuantity + amount;

        if (resultQuantity < product.getQuantityInterval() ||
                (product.getQuantityInterval() > 0 && resultQuantity > product.getQuantityInterval()
                        && resultQuantity%product.getQuantityInterval() != 0)) {
            // Wrong step quantity
            return Coriunder.getContext().getString(R.string.cart_wrong_quantity, product.getQuantityInterval());
        }

        if (product.getQuantityMax() < resultQuantity) {
            // Maximum quantity exceeded.
            int availableQuantityToAdd = product.getQuantityMax() - previousQuantity;
            return Coriunder.getContext().getString(R.string.cart_max_quantity_exceeded, product.getName(),
                    product.getQuantityMax(), previousQuantity, amount, availableQuantityToAdd);
        }

        if (product.getQuantityAvailable() < resultQuantity) {
            // Available quantity exceeded.
            int availableQuantityToAdd = product.getQuantityAvailable() - previousQuantity;
            return Coriunder.getContext().getString(R.string.cart_available_quantity_exceeded, product.getName(),
                    product.getQuantityAvailable(), previousQuantity, amount, availableQuantityToAdd);
        }

        if (currentStock != null && currentStock.getQuantityAvailable() < resultQuantity) {
            // Available stock quantity exceeded.
            int availableQuantityToAdd = currentStock.getQuantityAvailable() - previousQuantity;
            return Coriunder.getContext().getString(R.string.cart_available_stock_quantity_exceeded, product.getName(),
                    currentStock.getQuantityAvailable(), previousQuantity, amount, availableQuantityToAdd);
        }


        if (duplicatedItem != null) {
            // We already have similar product in the cart; update it
            duplicatedItem.setQuantity(resultQuantity);
            duplicatedItem.setTotalProduct(duplicatedItem.getTotalProduct() + price*(double)amount);
            items.remove(duplicatedItemPosition);
            items.add(duplicatedItemPosition, duplicatedItem);

        } else {
            // We don't have similar products in the cart; add product
            CartItem itemToAdd = new CartItem();
            itemToAdd.setProductId(product.getProductId());
            if (currentStock != null) itemToAdd.setProductStockId(stockId);
            itemToAdd.setName(product.getName());
            itemToAdd.setQuantity(amount);
            itemToAdd.setPrice(price);
            itemToAdd.setCurrencyIsoCode(product.getCurrencyIso());
            itemToAdd.setTotalProduct(price*(double)amount);
            itemToAdd.setMinQuantity(product.getQuantityMin());
            itemToAdd.setMaxQuantity(product.getQuantityMax());
            itemToAdd.setStepQuantity(product.getQuantityInterval());
            itemToAdd.setImageUrl(product.getImageUrl());
            items.add(itemToAdd);
        }

        // Update other cart's data
        merchantReference = product.getMerchant().getWebsite();
        currencyIso = product.getCurrencyIso();
        totalPrice = totalPrice + price*(double)amount;

        return Coriunder.getContext().getString(R.string.cart_success);
    }
}