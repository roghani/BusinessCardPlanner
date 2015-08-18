package sam.businesscardplanner.BusinessCard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

import sam.businesscardplanner.DatabaseHandler.DatabaseHandler;
import sam.businesscardplanner.R;

/**
 * Created by Administrator on 7/16/2015.
 */
public class BusinessCardsFragment extends Fragment implements SearchView.OnQueryTextListener {
    List list = null;
    RowViewAdapter adapter;
    private final int ADD = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_cards, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    //set up the listview
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        final RowViewAdapter adapter =  new RowViewAdapter(getActivity().getApplicationContext(),
                generateData());
        ListView listView = (ListView) getActivity().findViewById(R.id.business_card_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemID = adapter.getListItemId(position);
                Intent intent = new Intent(BusinessCardsFragment.this.getActivity(),
                        BusinessCardProfile.class);
                intent.putExtra("ITEM ID", itemID);
                startActivity(intent);
            }
        });

        ((SearchView) getActivity().findViewById(R.id.search_view)).setOnQueryTextListener(this);

    }

    public void onResume(){
        super.onResume();
        final RowViewAdapter adapter = new RowViewAdapter(getActivity().getApplicationContext(),
                generateData());
        final ListView listView = (ListView) getActivity().findViewById(R.id.business_card_list);
        listView.setAdapter(adapter);
    }

    private List<BusinessCard> generateData(){
        DatabaseHandler db1 = new DatabaseHandler(getActivity().getApplicationContext());
        list = db1.getAllBusinessCard();
        return list;
    }

    //setup the menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_all_card, menu);
    }

    //setup the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this.getActivity(),AddNewCardActivity.class);
                intent.putExtra("ADD OR EDIT", ADD);
                this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onQueryTextChange(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(adapter!=null) {
            adapter.getFilter().filter(newText.toString());
        }
        return false;
    }
}
