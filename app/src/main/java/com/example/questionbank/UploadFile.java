package com.example.questionbank;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;

public class UploadFile extends AppCompatActivity {
    private static final int PICK_FILE_REQ=1;
    private Uri fileUri;
    public static boolean UPLOAD_RESULT;
    static final String[] TYPE={"application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UPLOAD_RESULT=false;
        fileChooser();
    }

    private void fileChooser(){
        Intent intent=new Intent();
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES,TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_FILE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_FILE_REQ && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                fileUri=data.getData();
                setContentView(R.layout.upload_file_progress);
                /*String path;
                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                String uniqueName=UUID.randomUUID().toString();

                    path="users/"+uid+"/documents/"+uniqueName+"."+getFileExtension(fileUri);
                uploadToFirebase(path);*/

            try {
                InputStream in =  getContentResolver().openInputStream(fileUri);
                XSSFWorkbook workbook = new XSSFWorkbook(in);
                XSSFSheet sheet = workbook.getSheetAt(0);

                try {

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("questions");
                        Iterator rowIter = sheet.rowIterator();
                        while (rowIter.hasNext()) {      //no of customers to call=50
                            XSSFRow myRow = (XSSFRow) rowIter.next();
                            Iterator cellIter = myRow.cellIterator();

                            String question = cellIter.next().toString();
                            ref.child(question).setValue("1");
                    }
                }catch (NoSuchElementException e){
                    Toast.makeText(this,"Excel table format is incorrect",Toast.LENGTH_SHORT).show();
                }
                UPLOAD_RESULT=true;
                Toast.makeText(this,"Upload Successful",Toast.LENGTH_SHORT).show();
                finish();

            }catch (FileNotFoundException e) {

                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        else{
            finish();
        }
    }

    /*private String getFileExtension(Uri uri){
        ContentResolver resolver=getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }*/

    /*private void uploadToFirebase(String path){
        UPLOAD_RESULT=false;
        StorageReference ref=FirebaseStorage.getInstance().getReference(path);
        ref.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    UPLOAD_RESULT=true;
                    finish();
                }
            }
        });
    }*/
}
