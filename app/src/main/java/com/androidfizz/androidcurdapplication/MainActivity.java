package com.androidfizz.androidcurdapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidfizz.adapter.AdapterPerson;
import com.androidfizz.db.DbController;
import com.androidfizz.model.ModelPerson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private List<ModelPerson> mPersonList;
    private AdapterPerson mAdapter;
    private DbController mDbController;
    private SwipeRefreshLayout mSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        context = this;
        mPersonList = new ArrayList<>();
        RecyclerView mList = findViewById(R.id.mList);
        mSwipe = findViewById(R.id.mSwipe);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllPersons();
            }
        });

        mList.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new AdapterPerson(context, mPersonList);
        mList.setAdapter(mAdapter);
        mList.addItemDecoration(new SingleItemDecoration(context));

        //CLICK LISTNER FOR SINGLE RECORD
        mAdapter.setOnCustomClickListner(new AdapterPerson.OnClickListner() {
            @Override
            public void onClick(int position, int type) {
                ModelPerson single = mPersonList.get(position);
                switch (type) {
                    case AdapterPerson.TYPE_DELETE:
                        boolean result = mDbController.delete(single.getPersonID());
                        if (result) {
                            Toast.makeText(context, single.getName() + "'s record deleted successfully", Toast.LENGTH_SHORT).show();
                            mPersonList.remove(position);
                            mAdapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(context, "Operations failed! please try again", Toast.LENGTH_SHORT).show();
                        break;

                    case AdapterPerson.TYPE_UPDATE:

                        updatePersonDialog(single);
                        break;
                }
                //END OF CLICK LISTNER


            }
        });

        mDbController = new DbController(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllPersons();
    }

    private void getAllPersons() {
        List<ModelPerson> mTempPersonList = mDbController.getAllPerson();
        if (mTempPersonList != null && mTempPersonList.size() > 0) {
            mPersonList.clear();
            mPersonList.addAll(mTempPersonList);
            mAdapter.notifyDataSetChanged();
        }

        if (mSwipe != null && mSwipe.isRefreshing())
            mSwipe.setRefreshing(false);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mAddPerson:
                addPersonDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Dialog mDialog = null;
    private EditText etName, etEmail, etAge;
    private Button btnAdd;

    private void addPersonDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_add_person, null);
        mBuilder.setView(view);
        mDialog = mBuilder.create();
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etAge = view.findViewById(R.id.etAge);


        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdd.setEnabled(false);

                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                boolean isSuccess = mDbController.addPerson(new ModelPerson(name, email, age));
                if (isSuccess) {
                    Toast.makeText(context, R.string.inserted_successfully, Toast.LENGTH_SHORT).show();
                    getAllPersons();
                } else
                    Toast.makeText(context, R.string.operation_failed, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                btnAdd.setEnabled(true);
            }
        });
        mDialog.show();
    }

    Dialog mDialogUpdate = null;

    private void updatePersonDialog(final ModelPerson single) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_add_person, null);
        mBuilder.setView(view);
        mDialogUpdate = mBuilder.create();
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogUpdate.dismiss();
            }
        });
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etAge = view.findViewById(R.id.etAge);

        etName.setText(single.getName());
        etEmail.setText(single.getEmail());
        etAge.setText(single.getAge());


        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setText(R.string.update);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdd.setEnabled(false);

                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                boolean isSuccess = mDbController.updatePersonRecord(new ModelPerson(single.getPersonID(), name, email, age));

                if (isSuccess) {
                    Toast.makeText(context, R.string.update_success, Toast.LENGTH_SHORT).show();
                    getAllPersons();
                } else {
                    Toast.makeText(context, R.string.operation_failed, Toast.LENGTH_SHORT).show();
                }
                mDialogUpdate.dismiss();
                btnAdd.setEnabled(true);
            }
        });
        mDialogUpdate.show();
    }

    private class SingleItemDecoration extends RecyclerView.ItemDecoration {
        private  Drawable mDivider;
        private SingleItemDecoration(Context context) {
             mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);

            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount()-1;
            for (int i = 0; i < childCount; i++) {

                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }

        }
    }
}
