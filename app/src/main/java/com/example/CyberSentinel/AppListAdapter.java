package com.example.CyberSentinel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private List<App> appItemData;
    private Context context;

    public AppListAdapter(Context context, List<App> appItemData) {
        this.context = context;
        this.appItemData = appItemData;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        App appItem = appItemData.get(position);

        // Set icon & name
        holder.icon.setImageDrawable(appItem.getAppIcon(context));
        holder.text.setText(appItem.getName());

        // Set action icon
        if (appItem.getIcon() == 0) {
            holder.trash.setImageResource(R.drawable.add);
        } else if (appItem.getIcon() == 1) {
            holder.trash.setImageResource(R.drawable.trash);
        } else {
            holder.trash.setImageResource(R.drawable.next);
        }

        // ✅ CLICK ON FULL CARD (MAIN FIX)
        holder.itemView.setOnClickListener(v -> openThreatScreen(appItem));

        // ✅ CLICK ON ACTION ICON
        holder.trash.setOnClickListener(v -> {

            // Add app
            if (appItem.getIcon() == 0) {
                SelectAppActivity.setSelectedApps(
                        appItem.getPackageName(),
                        appItem.getName(),
                        0
                );
                appItem.setIcon(1);
                holder.trash.setImageResource(R.drawable.trash);
            }

            // Remove app
            else if (appItem.getIcon() == 1) {
                SelectAppActivity.setSelectedApps(
                        appItem.getPackageName(),
                        appItem.getName(),
                        1
                );
                appItem.setIcon(0);
                holder.trash.setImageResource(R.drawable.add);
            }

            // Go to remediation
            else {
                openThreatScreen(appItem);
            }
        });
    }

    // ✅ COMMON METHOD TO OPEN NEXT SCREEN
    private void openThreatScreen(App appItem) {

        Intent intent = new Intent(context, ThreatRemediation.class);
        ArrayList<App> appList = new ArrayList<>();
        appList.add(appItem);

        if (appItem.getMaliciousIPs() != null && !appItem.getMaliciousIPs().isEmpty()) {
            intent.putParcelableArrayListExtra("MaliciousIP", appList);
        } else {
            intent.putParcelableArrayListExtra("MaliciousApp", appList);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return appItemData.size();
    }
}
