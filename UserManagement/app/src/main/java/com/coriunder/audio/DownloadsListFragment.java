package com.coriunder.audio;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.coriunder.R;
import com.coriunder.shop.ShopSDKDownloads;
import com.coriunder.shop.callbacks.ReceivedCartItemsCallback;
import com.coriunder.shop.models.CartItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DownloadsListFragment extends Fragment {
    private TextView emptyView;
    private RecyclerView recyclerView;

    private DownloadsAdapter adapter;
    private boolean isLoading;
    private int currentPage;
    private int productsOnPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.downloads_fragment, container, false);

        // setup recycler view
        adapter = new DownloadsAdapter(new ArrayList<HashMap<String, String>>());
        emptyView = (TextView)rootView.findViewById(R.id.emptyView);
        emptyView.setVisibility(View.GONE);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.setAdapter(adapter);

        // set data
        currentPage = 0;
        isLoading = false;
        loadNextPage();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Downloads");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            if (!isLoading && lastVisibleItemPosition == adapter.getRealDataSize() && adapter.isProgressIndicatorVisible()) {
                // New page is not loading now, last visible position is greater than products' amount,
                // because it is an additional cell for spinner, spinner is shown, so we'd load next page
                loadNextPage();
            }
        }
    };

    private void loadNextPage() {
        isLoading = true;
        new ShopSDKDownloads().getDownloads(currentPage + 1, productsOnPage, new ReceivedCartItemsCallback() {
            @Override
            public void onResultReceived(boolean isSuccess, ArrayList<CartItem> cartItems, String message) {
                if (isSuccess) {

                    if (currentPage == 0 && cartItems.size() == 0) {
                        // Returned empty result while loading first page; means that the user doesn't have downloads
                        isLoading = false;
                        recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                        emptyView.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);

                    } else if (cartItems.size() == 0) {
                        // Returned empty result while loading NOT first page; means that all downloads were loaded
                        adapter.hideSpinner();
                        isLoading = false;

                    } else {
                        // Returned downloadable item
                        currentPage = currentPage + 1;

                        ArrayList<HashMap<String, String>> tempDataArray = new ArrayList<>();
                        for (CartItem item : cartItems) {
                            // Add product data to a temporary array
                            if (item.getDownloadMediaType().toLowerCase().equals("mp3")) {
                                HashMap<String, String> map = new HashMap<>();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
                                map.put(AudioConstants.KEY_PRODUCT_NAME, item.getName());
                                map.put(AudioConstants.KEY_DATE, formatter.format(item.getInsertDate()));
                                map.put(AudioConstants.KEY_PRODUCT_ID, String.valueOf(item.getProductId()));
                                tempDataArray.add(map);
                            }
                        }

                        isLoading = false;
                        if (!tempDataArray.isEmpty()) {
                            adapter.addData(tempDataArray, adapter.getRealDataSize() == 0);
                            recyclerView.stopScroll();
                            tempDataArray.clear();
                            cartItems.clear();
                        } else {
                            cartItems.clear();
                            loadNextPage();
                        }
                    }

                } else {
                    isLoading = false;
                    processErrorWithMessage(message);
                }
            }
        });
    }

    private void processErrorWithMessage(String message) {
        recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        emptyView.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }
}