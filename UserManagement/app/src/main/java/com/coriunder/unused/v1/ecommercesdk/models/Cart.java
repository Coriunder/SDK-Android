package com.coriunder.unused.v1.ecommercesdk.models;

import java.util.ArrayList;

/**
 * Created by 1 on 22.02.2016.
 */
public class Cart {

    private String cartId;
    private String cartCookie;
    private ArrayList<CartItem> shopCartItems;
    private String totalPrice;
    private String cartCurrencyIso;
    private String cartCheckoutUrl;
    private String cartMerchantReference;
    private String cartWalletCredentials;
    private Merchant merchant;
    private String installments;
    private String maxInstallments;
    private boolean cartIsChanged;
    private String cartChangedTotal;

    public Cart(String merchantId, String currencyIso) {
        cartId = "";
        cartCookie = null;
        shopCartItems = new ArrayList<>();
        totalPrice = "0";
        cartCurrencyIso = currencyIso;
        cartCheckoutUrl = "";
        cartMerchantReference = "";
        cartWalletCredentials = "";
        merchant = new Merchant();
        merchant.setNumber(merchantId);
        installments = "0";
        maxInstallments = "0";
        cartIsChanged = false;
        cartChangedTotal = "0";
    }

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getCartCookie() { return cartCookie; }
    public void setCartCookie(String cartCookie) { this.cartCookie = cartCookie; }
    
    public ArrayList<CartItem> getShopCartItems() { return shopCartItems; }
    public void setShopCartItems(ArrayList<CartItem> shopCartItems) { this.shopCartItems = shopCartItems; }

    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public String getCartCurrencyIso() { return cartCurrencyIso; }
    public void setCartCurrencyIso(String cartCurrencyIso) { this.cartCurrencyIso = cartCurrencyIso; }

    public String getCartCheckoutUrl() { return cartCheckoutUrl; }
    public void setCartCheckoutUrl(String cartCheckoutUrl) { this.cartCheckoutUrl = cartCheckoutUrl; }

    public String getCartMerchantReference() { return cartMerchantReference; }
    public void setCartMerchantReference(String cartMerchantReference) { this.cartMerchantReference = cartMerchantReference; }

    public String getCartWalletCredentials() { return cartWalletCredentials; }
    public void setCartWalletCredentials(String cartWalletCredentials) { this.cartWalletCredentials = cartWalletCredentials; }

    public Merchant getMerchant() { return merchant; }
    public void setMerchant(Merchant merchant) { this.merchant = merchant; }

    public String getInstallments() { return installments; }
    public void setInstallments(String installments) { this.installments = installments; }

    public String getMaxInstallments() { return maxInstallments; }
    public void setMaxInstallments(String maxInstallments) { this.maxInstallments = maxInstallments; }

    public boolean isCartIsChanged() { return cartIsChanged; }
    public void setCartIsChanged(boolean cartIsChanged) { this.cartIsChanged = cartIsChanged; }

    public String getCartChangedTotal() { return cartChangedTotal; }
    public void setCartChangedTotal(String cartChangedTotal) { this.cartChangedTotal = cartChangedTotal; }

    /**
     * @param product - product to add
     * @param stockId - stock id (0 in case product doesn't have stocks)
     * @param price - price of the product
     * @param amount - amount of pieces of this product to order
     * @return error text if any or "Success" message in case the product was successfully added to the cart
     */
    public String addProduct(Product product, long stockId, String price, long amount) {
        if (product == null) return "Attempt to add an empty product";
        if (amount < 1) return "Amount is too small. There is nothing to add.";
        if (product.getMerchant()!=null && merchant!=null && !product.getMerchant().getNumber().equals(merchant.getNumber()))
            return "Attempt to add product to the cart of another merchant";

        //Check whether stock is correct
        Stock currentStock = null;
        if (product.getStocks() != null && product.getStocks().size() > 0) {
            for (Stock stock : product.getStocks()) {
                if (stock.getStockId() == stockId) {
                    currentStock = stock;
                    break;
                }
            }
            if (currentStock == null) return "Attempt to send wrong stock";
        }

        if (shopCartItems == null) shopCartItems = new ArrayList<>();

        //Check for duplicates and different dynamic prices
        CartItem duplicatedItem = null;
        int duplicatedItemPosition = 0;
        for (int i = 0; i < shopCartItems.size(); i++) {
            CartItem shopCartItem = shopCartItems.get(i);
            if (shopCartItem.getItemProductId().equals(product.getProductId()) &&
                (!product.isDynamicAmount() || (product.isDynamicAmount() &&
                      Float.valueOf(price) - Float.valueOf(shopCartItem.getItemPrice()) == 0)) &&
                (stockId == shopCartItem.getItemProductStockId())) {
                //same item with same price detected
                duplicatedItem = shopCartItem;
                duplicatedItemPosition = i;
                break;
            }
        }

        long previousQuantity = 0;
        if (duplicatedItem != null) previousQuantity = Long.valueOf(duplicatedItem.getItemQuantity());
        long resultQuantity = previousQuantity + amount;

        if (resultQuantity < Long.valueOf(product.getStepQuantity()) ||
                (resultQuantity > Long.valueOf(product.getStepQuantity()) && (resultQuantity%Long.valueOf(product.getStepQuantity())) != 0)) {
            ///Wrong step for quantity
            return "Wrong product quantity. Product quantity should be divisible by step quantity ("+product.getStepQuantity()+" items for this product)";
        }

        if (Long.valueOf(product.getMaxQuantity()) < resultQuantity) {
            //Maximum quantity exceeded.
            long availableQuantityToAdd = (Long.valueOf(product.getMaxQuantity()) - previousQuantity);
            return "Maximum quantity for the product "+product.getName()+" is "+product.getMaxQuantity()+". "+
                    "You already have "+previousQuantity+" such products in the cart. Can't add "+amount+
                    " more such products. Maximum quantity to add is "+availableQuantityToAdd;
        }

        if (Long.valueOf(product.getQuantity()) < resultQuantity) {
            //Available quantity exceeded.
            long availableQuantityToAdd = (Long.valueOf(product.getQuantity()) - previousQuantity);
            return "Available quantity for the product "+product.getName()+" is "+product.getQuantity()+
                    ". You already have "+previousQuantity+" such products in the cart. Can't add "+amount+
                    " more such products. Available quantity to add is "+availableQuantityToAdd;
        }

        if (currentStock != null && currentStock.getStockQuantity() < resultQuantity) {
            //Available stock quantity exceeded.
            long availableQuantityToAdd = currentStock.getStockQuantity() - previousQuantity;
            return "Available quantity for this stock of the product "+product.getName()+" is "+currentStock.getStockQuantity()+
                    ". You already have "+previousQuantity+" such products in the cart. Can't add "+amount+
                    " more such products. Available quantity to add is "+availableQuantityToAdd;
        }


        if (duplicatedItem != null) {
            //This item is already in the cart, update it
            duplicatedItem.setItemQuantity(String.valueOf(resultQuantity));
            duplicatedItem.setItemTotalProduct(duplicatedItem.getItemTotalProduct() + Double.valueOf(price) * amount);
            shopCartItems.remove(duplicatedItemPosition);
            shopCartItems.add(duplicatedItemPosition, duplicatedItem);

        } else {
            //This item is new, add it
            CartItem itemToAdd = new CartItem();
            itemToAdd.setItemProductId(product.getProductId());
            if (currentStock != null) itemToAdd.setItemProductStockId(stockId);
            itemToAdd.setItemName(product.getName());
            itemToAdd.setItemQuantity(String.valueOf(amount));
            itemToAdd.setItemPrice(price);
            itemToAdd.setItemCurrencyIsoCode(product.getCurrencyISOCode());
            itemToAdd.setItemTotalProduct(Double.valueOf(price)*amount);
            itemToAdd.setItemMinQuantity(product.getMinQuantity());
            itemToAdd.setItemMaxQuantity(product.getMaxQuantity());
            itemToAdd.setItemStepQuantity(product.getStepQuantity());
            itemToAdd.setItemImageUrl(product.getImageUrl());
            shopCartItems.add(itemToAdd);
        }

        cartMerchantReference = product.getMerchant().getWebsite();
        cartCurrencyIso = product.getCurrencyISOCode();
        totalPrice = String.valueOf(Double.valueOf(totalPrice) + Double.valueOf(price)*amount);

        return "Success";
    }
}