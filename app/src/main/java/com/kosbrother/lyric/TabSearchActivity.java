package com.kosbrother.lyric;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TabSearchActivity extends Activity {
	
	private EditText mEditText;
	private ImageView mImageButton;
	private RadioGroup mRadioGroup;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_search);
        
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth(); // deprecated
        int height = display.getHeight(); // deprecated

        if (width > 480) {
        	   setContentView(R.layout.layout_search);
        } else {
        	   setContentView(R.layout.layout_search_small);
        }
        
        mEditText = (EditText) findViewById (R.id.edittext_search);
        mImageButton = (ImageView) findViewById (R.id.imageview_search);
        mRadioGroup = (RadioGroup) findViewById (R.id.radiogroup_search);
        
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
//        mEditText.requestFocus();
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            	try{
	                if (actionId == EditorInfo.IME_ACTION_SEARCH ) {
//	                	int type = mRadioGroup.getCheckedRadioButtonId();
	                	int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
	                    View radioButton = mRadioGroup.findViewById(radioButtonID);
	                    int idx = mRadioGroup.indexOfChild(radioButton);
	                	
	                    Bundle bundle = new Bundle();
	                    bundle.putString("SearchKeyword", v.getText().toString());
	                    bundle.putInt("SearchTypeId", idx);
	                    Intent intent = new Intent();
	                    intent.setClass(TabSearchActivity.this, SearchActivity.class);
	                    intent.putExtras(bundle);
	                    startActivity(intent);
	                    return true;
	                }
            	}catch(Exception e){
            		Toast.makeText(TabSearchActivity.this, "got problem", Toast.LENGTH_SHORT).show();
            	}
                return false;
            }
        });
        
        mImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // if edittext != "", go to SearchActivity
            	// else toast enter searh key
            	if(mEditText.getText().toString().equals("") || mEditText.getText().toString().equals(0) ){
            		Toast.makeText(TabSearchActivity.this, "請輸入搜索文字", Toast.LENGTH_SHORT).show();
            	}else{
            		int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
                    View radioButton = mRadioGroup.findViewById(radioButtonID);
                    int idx = mRadioGroup.indexOfChild(radioButton);
                    
            		Bundle bundle = new Bundle();
                    bundle.putString("SearchKeyword", mEditText.getText().toString());
                    bundle.putInt("SearchTypeId", idx);
                    Intent intent = new Intent();
                    intent.setClass(TabSearchActivity.this, SearchActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
            	}
            }
        });
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		this.getParent().onMenuItemSelected(featureId, item);
        return true;
    }
    
    @Override
    public void onBackPressed() {
		this.getParent().onBackPressed(); 
    }
    
    @Override
    public void onStart() {
      super.onStart();
    }

    @Override
    public void onStop() {
      super.onStop();
    }
}
