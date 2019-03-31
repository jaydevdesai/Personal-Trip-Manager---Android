package team.project.tripmanager.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.adapter.ShopListAdapter;
import team.project.tripmanager.callback.CustomCallback;
import team.project.tripmanager.listener.OnItemAddedListener;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.Shopping;
import team.project.tripmanager.ui.fragment.AddShoppingDialog;

public class ShoppingListActivity extends TripBaseActivity {

    RecyclerView recyclerView;
    private Logger logger = new Logger(getClass());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Shopping-List");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.shopListView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fetchShoppingListFromServer();
    }

    private void fetchShoppingListFromServer() {
        environment.getAPIService().getShoppingList(tripId).enqueue(new CustomCallback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                super.onResponse(call, response);
                if (response.body() != null && response.body().getShoppingList() != null) {
                    List<Shopping> shoppingList = response.body().getShoppingList();
                    ShopListAdapter shopListAdapter = new ShopListAdapter(shoppingList);
                    recyclerView.setAdapter(shopListAdapter);
                } else {
                    logger.debug("response.body() != null && response.body().getShoppingList() != null False");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.addBtn:
                FragmentManager fm = getSupportFragmentManager();
                AddShoppingDialog addShoppingDialog = AddShoppingDialog.newInstance();
                addShoppingDialog.setOnItemAddedListener(new OnItemAddedListener() {
                    @Override
                    public void onItemAdded(String message) {
                        showToast(message);
                        fetchShoppingListFromServer();
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putInt("tripId", tripId);
                addShoppingDialog.setArguments(bundle);
                addShoppingDialog.show(fm, "add_shopping_item");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new, menu);
        return true;
    }

}
