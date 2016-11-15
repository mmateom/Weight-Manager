package mikel.poseidon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GetWeight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_weight);

        //Create weight data database
        final DBManager manager = new DBManager(this);

        Button ok_button = (Button) findViewById(R.id.ok_button);

        ok_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText inputWeight = (EditText) findViewById(R.id.editTextWeight);
                Double weight = Double.parseDouble(String.valueOf(inputWeight.getText()));

                /*Intent intent = new Intent(GetWeight.this, ViewSummary.class);
                intent.putExtra("Weight", weight);
                startActivity(intent);*/
                manager.insert(weight);

            }

            });


    }
}
