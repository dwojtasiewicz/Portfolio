package com.example.mikol.portfolio;
import android.app.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ipaulpro.afilechooser.utils.FileUtils;

import static com.example.mikol.portfolio.ProjectTable.DB_NAME;
import static com.example.mikol.portfolio.ProjectTable.DB_VERSION;

public class ActivityAdd extends Activity {
    private static final int REQUEST_CODE = 6384;
    private String pathToCode="";
    TextView newtext;
    EditText editNameProject;
    EditText editDescriptionProjects;
    String name;
    String description;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        newtext= (TextView) findViewById(R.id.pathToCode);
        editNameProject= (EditText) findViewById(R.id.editNameProject) ;
        editDescriptionProjects= (EditText) findViewById(R.id.editDescriptionProjects);


    }

    public void showChooser(View view) {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser( target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {// If the file selection was successful
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // Get the URI of the selected file
                    final Uri uri = data.getData();
                    Log.i("Activity", "Uri = " + uri.toString());
                    try {
                        // Get the file path from the URI
                        final String path = FileUtils.getPath(this, uri);
                        Toast.makeText(ActivityAdd.this,
                                "File Selected: " + path, Toast.LENGTH_LONG).show();
                        pathToCode=path;
                        newtext.setText(pathToCode);
                        System.out.print(path);
                    } catch (Exception e) {
                        Log.e("Activity", "File select error", e);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addProject(View view)
    {
        name =editNameProject.getText().toString();
        description=editDescriptionProjects.getText().toString();

        if((!name.matches(""))&&(!description.matches(""))&&(!pathToCode.matches("")))
        {
            OpenHelper openHelper = new OpenHelper(getApplicationContext(), DB_NAME, null, DB_VERSION);
            ProjectDao projectDao = new ProjectDao(openHelper.getWritableDatabase());
            Project project=new Project(0,name,description,pathToCode);
            projectDao.save(project);
            finish();

        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),"Check if all fields are filled",Toast.LENGTH_SHORT);
            toast.show();

        }

    }
}