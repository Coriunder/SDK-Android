package com.coriunder.unused.xtestappx;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coriunder.account.AccountSDK;
import com.coriunder.account.callbacks.ReceivedLoginCallback;
import com.coriunder.account.models.LoginResult;
import com.coriunder.audio.DownloadsListFragment;
import com.coriunder.base.common.callbacks.ReceivedBasicCallback;
import com.coriunder.base.common.callbacks.ReceivedServiceCallback;
import com.coriunder.base.common.models.ServiceResult;
import com.coriunder.customer.CustomerSDKCustomers;
import com.coriunder.customer.models.ShippingAddress;
import com.coriunder.international.InternationalSDK;
import com.coriunder.international.callbacks.ReceivedErrorCodesCallback;
import com.coriunder.merchant.MerchantSDK;
import com.coriunder.merchant.models.RegistrationMerchant;
import com.coriunder.paymentmethods.models.PaymentMethod;
import com.coriunder.shop.ShopSDKCarts;
import com.coriunder.shop.callbacks.ReceivedCartCallback;
import com.coriunder.shop.models.Cart;

import java.util.ArrayList;

/**
 * Created by 1 on 15.02.2016.
 */
public class MainActivity extends AppCompatActivity {

    //email for password reset sdkblererer@test.test
    //sdk@test.test / aaa111aaa / 2647597 / 1 friend
    //lev1@test.test / aaa111aaa / 7808338 / 3 friends
    //lev2@test.test / aaa111aaa / 3705306 / 1 friend
    //lev3@test.test / aabb1122 / 5641263 / 1 friend

    // for money tests
    // blablabla@sapdan.com / aaa111aaa
    // blablabla1@sapdan.com / aaa111aaa
    PaymentMethod pm;
    ShippingAddress address;
    LinearLayout layout;
    ImageView imageView;
    TextView textView;
    int j = 0;

    ArrayList<RegistrationMerchant> list;
    private Button generateButton(String name) {
        Button button = new Button(this);
        button.setText(name);
        layout.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return button;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new LinearLayout(this);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imageView = new ImageView(this);
        layout.addView(imageView, new LinearLayout.LayoutParams(200, 200));
        textView = new TextView(this);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(60);
        layout.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));

        Button button1 = generateButton("Login");
        Button button2 = generateButton("Click2");
        Button button3 = generateButton("Click3");
        Button button4 = generateButton("Click4");
        Button button5 = generateButton("Click5");
        Button button6 = generateButton("Click6");
        Button button7 = generateButton("Click7");
        Button button8 = generateButton("Click8");
        Button button9 = generateButton("Click9");
        Button button10 = generateButton("Click10");
        Button button11 = generateButton("Click11");
        Button button12 = generateButton("Click12");
        Button button13 = generateButton("Click13");
        Button button14 = generateButton("Click14");
        setContentView(layout);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(new AccountSDK()).login("sdk@test.test", "", "aaa111aaa", "appname", "", false, new ReceivedLoginCallback() {
                //(new AccountSDK()).login("lev@sapdan.com", "", "8Gj7lC8R", "appname", "", false, new ReceivedLoginCallback() {
                (new AccountSDK()).login("blablabla1@sapdan.com", "", "aaa111aaa", "appname", "", false, new ReceivedLoginCallback() {
                    @Override
                    public void onResultReceived(boolean isSuccess, LoginResult loginResult, String message) {
                        if (isSuccess) {
                        }
                    }
                });
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new CustomerSDKCustomers()).registerCustomer("aaa111aaa", "1234", "blablabla1@sapdan.com", "Lev", "Surname", new ReceivedServiceCallback() {
                    @Override
                    public void onResultReceived(boolean isSuccess, ServiceResult serviceResult, String message) {
                        Log.d("tag", "success1: " + isSuccess);
                    }
                });
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AccountSDK().setNewPassword("aabb1122", "1Kk6zL1D", new ReceivedServiceCallback() {
                    @Override
                    public void onResultReceived(boolean isSuccess, ServiceResult serviceResult, String message) {
                        if (isSuccess) {

                        }
                    }
                });
                //REGISTER MERCHANT
                /*MerchantSDK.registerMerchant("", 0, 0, 0, "", "", "", new Date(), "", "", "", "merchant@test.test", "",
                        "name", 0, "surname", "", "", new Date(), "", 0, 0, 0, 0, "", "", "", "", "", "", "", 0, "", "",
                        new ReceivedBasicCallback() {
                            @Override
                            public void onResultReceived(boolean isSuccess, String message) {
                                if (isSuccess) Log.d("VolleyTest", "Great");
                                else Log.d("VolleyTest", message);
                            }
                        });*/
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AccountSDK().setNewPinCode("1234", "aabb1122", new ReceivedServiceCallback() {
                    @Override
                    public void onResultReceived(boolean isSuccess, ServiceResult serviceResult, String message) {
                        if (isSuccess) {

                        }
                    }
                });
                /*UserManagementSDK.getImageForUser(getApplicationContext(), "7808338", new ReceivedImageCallback() {
                    @Override
                    public void onResultReceived(boolean isSuccess, Bitmap image, String message) {
                        if (isSuccess) Log.d("VolleyTest", "Great");
                        else Log.d("VolleyTest", message);
                        imageView.setImageBitmap(image);
                    }
                });*/
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AccountSDK().logOff(new ReceivedBasicCallback() {
                    @Override
                    public void onResultReceived(boolean isSuccess, String message) {

                    }
                });
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeAllViews();
                RelativeLayout view = new RelativeLayout(MainActivity.this);
                int idInt = View.generateViewId();
                view.setId(idInt);
                layout.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                fTrans.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fTrans.replace(view.getId(), new DownloadsListFragment());
                fTrans.commit();
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new InternationalSDK()).getInstance().getErrorCodes("en", null, new ReceivedErrorCodesCallback() {
                    @Override
                    public void onResultReceived(boolean isSuccess, ArrayList<ServiceResult> serviceResults, String message) {
                        Log.d("tag", "success1: " + isSuccess);
                    }
                });
            }
        });

        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}

/*
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(R.drawable.pic)
                        + '/' + getResources().getResourceTypeName(R.drawable.pic) + '/' + getResources().getResourceEntryName(R.drawable.pic) );
                new CustomerSDKCustomers().saveCustomerInfo("testAddress", null, null, null, null, null, null, null, "lev@sapdan.com", "name", "surname",
                        null, null, null/*imageUri* /, new ReceivedCustomerCallback() {
@Override
public void onResultReceived(boolean isSuccess, Customer customer, String message) {

        }
        });
*/