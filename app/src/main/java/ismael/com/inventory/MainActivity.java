package ismael.com.inventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ismael.com.inventory.fragments.ListProductFragment;

public class MainActivity extends AppCompatActivity implements ListProductFragment.ListProductListener{

    /*Se inicializa el fragment que permite añadir un product*/
    @Override
    public void onAddProductListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
