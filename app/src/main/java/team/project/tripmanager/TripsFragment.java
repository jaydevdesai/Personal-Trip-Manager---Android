package team.project.tripmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;


public class TripsFragment extends Fragment {

    AppCompatEditText placePickerEdt;
    int PLACE_PICKER_REQUEST = 1;

    AppCompatImageView tripCoverImage;
    AppCompatImageButton createTripBtn;
    RecyclerView tripsListView;
    TripsListHolder tripsListHolder;
    ArrayList<TripDetails> tripDetailsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_trips,container,false);

        tripDetailsList = new ArrayList<>();
        tripsListView = v.findViewById(R.id.tripsList);
        tripCoverImage = v.findViewById(R.id.tripCoverImage);
        createTripBtn = v.findViewById(R.id.createTripBtn);
        tripsListHolder = new TripsListHolder();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        tripsListView.setLayoutManager(layoutManager);
        tripsListView.setItemAnimator(new DefaultItemAnimator());


        createTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CreateTripFragment createTripFragment = CreateTripFragment.newInstance();
                createTripFragment.setCancelable(false);
                createTripFragment.show(getActivity().getSupportFragmentManager().beginTransaction(),"createTrip");
                getActivity().getSupportFragmentManager().executePendingTransactions();
                createTripFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(createTripFragment).commit();
                        new GetTrips().execute();
                    }
                });
            }
        });
        new GetTrips().execute();
        return v;
    }

    private class GetTrips extends AsyncTask<String, Void ,String> {

        JSONObject jsonObject;

        @Override
        protected String doInBackground(String... strings) {
            String aUrl = Objects.requireNonNull(getActivity()).getString(R.string.WS_Url) +"GetTrips.php";
            String result = null;
            try {
                WebService webService = new WebService(aUrl,createJson());
                result = webService.PostJSon();
                jsonObject = new JSONObject(result);
                result = jsonObject.getString("result");
                //Log.d("result",jsonObject.toString());
            } catch (JSONException | IOException e) {
                result = "conn";
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            switch (result) {
                case "true":
                    try {
                        tripDetailsList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        for(int i = 0;i<jsonArray.length();i++){
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            TripDetails tripDetails = new TripDetails();
                            tripDetails.setTripId(object.getString("tripId"));
                            tripDetails.setTripName(object.getString("tripName"));
                            tripDetails.setPlaceName(object.getString("placeName"));
                            tripDetails.setStartDate(object.getString("startDate"));
                            tripDetails.setEndDate(object.getString("endDate"));
                            tripDetailsList.add(tripDetails);
                        }
                        tripsListView.setAdapter(tripsListHolder);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "null":
                    Toast.makeText(getActivity(), "No Trips.", Toast.LENGTH_SHORT).show();
                    break;
                case "conn":
                    Toast.makeText(getActivity(), "Check Internet Connection.", Toast.LENGTH_SHORT).show();
                default:
                    Toast.makeText(getActivity(), "Error! Try Again.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private JSONObject createJson() throws JSONException{
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("email", sharedPreferences.getString("email",""));
        return jsonObject;
    }

    public class TripsListHolder extends RecyclerView.Adapter<TripsListHolder.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder{

            AppCompatImageView tripCoverImage;
            AppCompatTextView tripTitleTv, tripDatesTv;
            CardView tripCard;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tripTitleTv = itemView.findViewById(R.id.tripTitle);
                tripDatesTv = itemView.findViewById(R.id.tripDates);
                tripCoverImage = itemView.findViewById(R.id.tripCoverImage);
                tripCard = itemView.findViewById(R.id.tripCard);
            }
        }

        @NonNull
        @Override
        public TripsListHolder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.trips_list_row, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TripsListHolder.MyViewHolder holder, int position) {
            final TripDetails details = tripDetailsList.get(position);
            holder.tripTitleTv.setText(details.getTripName());
            try {
                String tripdates = details.getFormattedStartDate() + " - " + details.getFormattedEndDate();
                holder.tripDatesTv.setText(tripdates);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tripCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),TripDetailsActivity.class);
                    intent.putExtra("TripDetails", details);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return tripDetailsList.size();
        }
    }

}
