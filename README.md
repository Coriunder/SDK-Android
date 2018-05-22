# SDK-Android

Our SDK was built to assist you with building your application using the Coriunder BAAS or working with any one our clients, the implmentation will remain the same besides the service URL that will change.

Be sure, before you start, to get your access token and hash to allow you to integrate to the services.

more information can be found at: https://devcenter.coriunder.cloud/website/sdk-Intro.aspx#AddingtheSDK



##com.coriunder.base

This package is included to SDK by default. It deals with internet connection, users' sessions, base models, base responses' parsing and other commonly used things.

##com.coriunder.account

This package is designed to make calls to the Account service. All service's methods can be accessed using AccountSDK class 

##com.coriunder.appidentity

This package is designed to make calls to the AppIdentity service. All service's methods can be accessed using AppIdentitySDK class

## com.coriunder.customer

This package is designed to make calls to the Customer service. All service's methods are divided by groups depending on their purposes. Methods which deal with customer data can be accessed using CustomerSDKCustomers class, methods which deal with friends can be accessed using CustomerSDKFriends class and methods which deal with user's shipping addresses can be accessed using CustomerSDKShippingAddresses class 

## com.coriunder.shop

This package is designed to make calls to the Shop service. All service's methods are divided by groups depending on their purposes. Methods which deal with carts can be accessed using ShopSDKCarts class, methods which deal with downloads can be accessed using ShopSDKDownloads class, methods which deal with merchants can be accessed using ShopSDKMerchants class, methods which deal with products can be accessed using ShopSDKProducts class and methods which deal with shops can be accessed using ShopSDKShops class

## com.coriunder.balance

This package is designed to make calls to the Balance service. All service's methods can be accessed using BalanceSDK class

## com.coriunder.merchant

This package is designed to make calls to the Merchant service. All service's methods can be accessed using MerchantSDK class

## com.coriunder.paymentmethods

This package is designed to make calls to the PaymentMethods service. All service's methods can be accessed using PaymentMethodsSDK class

## com.coriunder.international

This package is designed to make calls to the International service. All service's methods can be accessed using InternationalSDK class

## com.coriunder.transactions

This package is designed to make calls to the Transactions service. All service's methods can be accessed using TransactionsSDK class

