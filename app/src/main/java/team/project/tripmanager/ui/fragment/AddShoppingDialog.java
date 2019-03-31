package team.project.tripmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.callback.CustomCallback;
import team.project.tripmanager.listener.OnItemAddedListener;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;

public class AddShoppingDialog extends BaseDialogFragment implements View.OnClickListener {
    private EditText itemNameEt;
    private CheckBox boughtCb;
    private TextView cancelBtn, addBtn;
    private int tripId;
    private Logger logger = new Logger(getClass());
    private OnItemAddedListener onItemAddedListener;

    public static AddShoppingDialog newInstance() {
        return new AddShoppingDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            tripId = getArguments().getInt("tripId");
        }
        View v = inflater.inflate(R.layout.fragment_add_shopping, container, false);
        itemNameEt = v.findViewById(R.id.itemNameEdt);
        boughtCb = v.findViewById(R.id.boughtCheck);
        cancelBtn = v.findViewById(R.id.cancelBtn);
        addBtn = v.findViewById(R.id.addBtn);
        cancelBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(cancelBtn)) {
            this.dismiss();
        } else if (v.equals(addBtn)) {
            addShoppingItemServer(itemNameEt.getText().toString(), boughtCb.isChecked());
        }
    }

    private void addShoppingItemServer(String itemName, Boolean bought) {
        environment.getAPIService().addShoppingItem(tripId, itemName, bought).enqueue(new CustomCallback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                super.onResponse(call, response);
                if (response.body() != null) {
                    if (onItemAddedListener != null) {
                        onItemAddedListener.onItemAdded(response.body().getMessage());
                    } else {
                        logger.warn("onItemAddedListener not initialized");
                    }
                } else {
                    logger.warn("response.body() != null False");
                }
            }
        });
        this.dismiss();
    }

    public void setOnItemAddedListener(OnItemAddedListener onItemAddedListener) {
        this.onItemAddedListener = onItemAddedListener;
    }
}
