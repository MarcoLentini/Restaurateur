package com.example.restaurateur.Reservation;

        import android.graphics.Color;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;
        import android.util.TypedValue;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.example.restaurateur.MainActivity;
        import com.example.restaurateur.R;

        import java.io.Serializable;

public class InProgressDetailsFragment extends Fragment {

    private ReservationModel RM;
    private Integer position;

    public static InProgressDetailsFragment newInstance(ReservationModel RM, int position) {
        Bundle args = new Bundle();
        args.putSerializable("RM", RM);
        args.putInt("position", position);

        InProgressDetailsFragment fragment = new InProgressDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reservation_detail_info_in_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle args = getArguments();
        this.RM = args.containsKey("RM") ? (ReservationModel) args.getSerializable("RM") : null;
        this.position = args.containsKey("position") ? args.getInt("position"): null;

        TextView textViewOrderIdInProgress = view.findViewById(R.id.textViewOrderIdInProgress);
        textViewOrderIdInProgress.setText(Integer.toString(RM.getId()));

        TextView textViewRemainingTimeInProgress = view.findViewById(R.id.textViewRemainingTimeInProgress);
        textViewRemainingTimeInProgress.setText(Integer.toString(RM.getRemainingMinutes()) + " min");

        TextView textViewTotalIncomeInProgress = view.findViewById(R.id.textViewTotalIncomeInProgress);
        textViewTotalIncomeInProgress.setText(Double.toString(RM.getTotalIncome()));

        TextView tvRemarkInProgress = view.findViewById(R.id.tvRemarkInProgress);
        tvRemarkInProgress.setText(RM.getNotes());

        TextView tvNameInProgress = view.findViewById(R.id.tvNameInProgress);
        tvNameInProgress.setText(Integer.toString(RM.getCustomerId()));

        TextView tvPhoneInProgress = view.findViewById(R.id.tvPhoneInProgress);
        tvPhoneInProgress.setText(RM.getCustomerPhoneNumber());

        LinearLayout orderDetailInfoInProgress = view.findViewById(R.id.orderDetailInfoInProgress);

        MainActivity main = (MainActivity) getActivity();

        for(ReservatedDish rd : RM.getReservatedDishes()){
            LinearLayout ll = new LinearLayout(this.getContext());
            // 16dp
            float scale = getResources().getDisplayMetrics().density;
            int _16dp = (int) (16*scale + 0.5f);

            ll.setPadding(_16dp,0,_16dp,0);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params_ll = new LinearLayout.LayoutParams(  ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(params_ll);

            // Name of food
            TextView tv = new TextView(this.getContext());
            tv.setText("▶" + main.offersData.get(rd.getDishId()).getName());

            tv.setTextColor(Color.parseColor("#FF000000"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);


            LinearLayout.LayoutParams params_name = new LinearLayout.LayoutParams(  0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,2f);
            tv.setLayoutParams(params_name);

            // Quantity
            TextView tv1 = new TextView(this.getContext());
            tv1.setText("x" + main.offersData.get(rd.getDishId()).getQuantity());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tv1.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
            tv1.setTextColor(Color.parseColor("#FF000000"));
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            LinearLayout.LayoutParams params_qty = new LinearLayout.LayoutParams(   0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            params_qty.gravity = Gravity.END;

            tv1.setLayoutParams(params_qty);

            // Single price
            TextView tv2 = new TextView(this.getContext());
            tv2.setText(String.format("%.2f", main.offersData.get(rd.getDishId()).getPrice()) + "€" );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tv2.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
            tv2.setTextColor(Color.parseColor("#FF000000"));
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            LinearLayout.LayoutParams params_price = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            params_price.gravity = Gravity.END;

            tv2.setLayoutParams(params_price);

            ll.addView(tv);
            ll.addView(tv1);
            ll.addView(tv2);
            orderDetailInfoInProgress.addView(ll);
        }

    }
}
