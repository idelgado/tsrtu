package org.idelgado.tstu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.GridView;

import org.idelgado.tstu.AppsLoader;
import org.idelgado.tstu.service.TSTUService;
import org.idelgado.tstu.activity.HomeScreenActivity;
import org.idelgado.tstu.adapter.AppListAdapter;
import org.idelgado.tstu.model.AppModel;

import java.util.ArrayList;

import hugo.weaving.DebugLog;

/**
 * Created by Arnab Chakraborty
 */
public class AppsGridFragment extends GridFragment implements LoaderManager.LoaderCallbacks<ArrayList<AppModel>> {

    AppListAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No Applications");

        mAdapter = new AppListAdapter(getActivity());
        setGridAdapter(mAdapter);

        // till the data is loaded display a spinner
        setGridShown(false);

        // create the loader to load the apps list in background
        getLoaderManager().initLoader(0, null, this);
    }

    @DebugLog
    @Override
    public void onStart() {
        super.onStart();

        // Notify service to hide heads up display
        Intent serviceIntent = new Intent(getActivity().getApplication(), TSTUService.class);
        serviceIntent.putExtra(TSTUService.HIDE_HUD, true);
        getActivity().startService(serviceIntent);
    }

    @Override
    public Loader<ArrayList<AppModel>> onCreateLoader(int id, Bundle bundle) {
        return new AppsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AppModel>> loader, ArrayList<AppModel> apps) {
        mAdapter.setData(apps);

        if (isResumed()) {
            setGridShown(true);
        } else {
            setGridShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AppModel>> loader) {
        mAdapter.setData(null);
    }

    @Override
    public void onGridItemClick(GridView g, View v, int position, long id) {
        AppModel app = (AppModel) getGridAdapter().getItem(position);
        if (app != null) {
            HomeScreenActivity homeScreenActivity = (HomeScreenActivity)getActivity();
            homeScreenActivity.startApplication(app.getApplicationPackageName());
        }
    }

}
