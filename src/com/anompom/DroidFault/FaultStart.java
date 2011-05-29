package com.anompom.DroidFault;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FaultStart extends Activity {
	
	private static final String TAG = "FaultStart";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Loading main layout");
        setContentView(R.layout.main);
        Log.d(TAG, "Main layout loaded");


        Button compiler_button = (Button) findViewById(R.id.run_button);
        compiler_button.setOnClickListener(compileListener);
        
    }
    
    private OnClickListener compileListener = new OnClickListener() {

		public void onClick(View v) {
	        EditText et = (EditText) findViewById(R.id.interpreter_input);
	        String input = et.getText().toString();
			new CompileTask().execute(input);
			
		}
    	
    };
    
    private class CompileTask extends AsyncTask<String, CharSequence, Void> {
    	
    	private String results = "";
    	private AlertDialog dialog;
    	private CompileTask task = this;
    	TextView text;
    	
    	private DialogInterface.OnClickListener interpreterDialogListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if(which == AlertDialog.BUTTON_NEGATIVE){
					Log.i(TAG, "Interpreter cancelled");
					task.cancel(true);
				}
				dialog.dismiss();
			}
    		
    	};
    	
    	protected void onPreExecute(){
    		LayoutInflater inflater = LayoutInflater.from(FaultStart.this);
    		View layout = inflater.inflate(R.layout.interpreter_dialog,
    		                               (ViewGroup) findViewById(R.id.layout_root));

    		AlertDialog.Builder builder = new AlertDialog.Builder(FaultStart.this);
    		builder.setView(layout);
    		builder.setPositiveButton("Done", interpreterDialogListener);
    		builder.setNegativeButton("Cancel", interpreterDialogListener);
    		dialog = builder.create();


    		text = (TextView) layout.findViewById(R.id.interpreter_dialog_text);
    		dialog.show();
    		Button done_button = dialog.getButton(AlertDialog.BUTTON1);
    		done_button.setEnabled(false);

    	}

		@Override
		protected Void doInBackground(String... params) {
			DroidFaultInterpreter interpreter = new DroidFaultInterpreter(params[0]);
			try{
				Character c = interpreter.interpretUntilPrint();
				while(c != null){
					publishProgress(Character.toString(c));
					c = interpreter.interpretUntilPrint();
				}
			}catch(Exception e){
				text.setText(e.getMessage());
			}
			return null;
		}
		
		protected void onProgressUpdate(CharSequence... params){
			results  = results + params[0];
			text.setText(results);
			
		}
		
		protected void onPostExecute(Void v){
			Log.d(TAG, "Done compiling!");
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
			dialog.findViewById(R.id.interpreter_progress).setVisibility(View.INVISIBLE);
		}
		
		protected void onCancelled(){
			return;
		}
	
    	
    }
}

