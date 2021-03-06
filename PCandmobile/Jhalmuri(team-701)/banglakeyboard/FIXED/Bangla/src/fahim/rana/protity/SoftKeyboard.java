/*
 * Copyright (C) 2008-2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package fahim.rana.protity;

import android.R.color;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.MetaKeyKeyListener;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;












/**
 * Example of writing an input method for a soft keyboard.  This code is
 * focused on simplicity over completeness, so it should in no way be considered
 * to be a complete soft keyboard implementation.  Its purpose is to provide
 * a basic example for how you would get started writing an input method, to
 * be fleshed out as appropriate.
 */
public class SoftKeyboard extends InputMethodService 
        implements KeyboardView.OnKeyboardActionListener {
    static final boolean DEBUG = false;
    
    ArrayList<String> list = new ArrayList<String>();
    

    /**
     * This boolean indicates the optional example code for performing
     * processing of hard keys in addition to regular text generation
     * from on-screen interaction.  It would be used for input methods that
     * perform language translations (such as converting text entered on 
     * a QWERTY keyboard to Chinese), but may not be used for input methods
     * that are primarily intended to be used for on-screen text entry.
     */
    static final boolean PROCESS_HARD_KEYS = true;
    public String c;

  
    public HashMap<String, String> keyCodeMap = new HashMap<String, String>();
    private KeyboardView mInputView;
    private static final int POS_SETTINGS = 0;
    private static final int POS_METHOD = 1;
    private AlertDialog mOptionsDialog;
    private CandidateView mCandidateView;
    private CompletionInfo[] mCompletions;
    private LinearLayout mCandidateAndConfigLayout;
    private BanglaPreviewView mBanglaPreviewView;
    private StringBuilder mComposing = new StringBuilder();
    private boolean mPredictionOn;
    private boolean mCompletionOn;
    private int mLastDisplayWidth;
    private boolean mCapsLock;
    private long mLastShiftTime;
    private long mMetaState;

    private LatinKeyboard mSymbolsKeyboard;
   
    private LatinKeyboard mQwertyKeyboard;
    private LatinKeyboard juktoborno;
    private LatinKeyboard juktoborno2;
    
    private LatinKeyboard mCurKeyboard;
    
    private String mWordSeparators;
    
    /**
     * Main initialization of the input method component.  Be sure to call
     * to super class.
     */
    @Override public void onCreate() {
    	 super.onCreate();
    	
    	 
    	 keyCodeMap.put("500", "্");
         keyCodeMap.put("501", "ৎ");         
         keyCodeMap.put("502", "ঁ");
         keyCodeMap.put("503", "ং");
         keyCodeMap.put("504", "ঃ");
         keyCodeMap.put("505", "ক্ষ");
         keyCodeMap.put("601", "এ");
         keyCodeMap.put("602", "ে");
         keyCodeMap.put("603", "ঐ");
         keyCodeMap.put("604", "ৈ");
         keyCodeMap.put("605", "র");
         keyCodeMap.put("606", "ড়");
         keyCodeMap.put("607", "ঢ়");
         keyCodeMap.put("608", "ৃ");
         keyCodeMap.put("609", "্র");
         keyCodeMap.put("610", "ঋ");
         keyCodeMap.put("611", "র্");
         keyCodeMap.put("615", "ত");
         keyCodeMap.put("616", "ট");
         keyCodeMap.put("617", "থ");
         keyCodeMap.put("618", "ঠ");
         keyCodeMap.put("619", "য়");
         keyCodeMap.put("620", "উ");
         keyCodeMap.put("621", "ু");
         keyCodeMap.put("622", "ঊ");
         keyCodeMap.put("623", "ূ");         
         keyCodeMap.put("624", "ই");
         keyCodeMap.put("625", "ি");
         keyCodeMap.put("626", "ঈ");
         keyCodeMap.put("627", "ী");
         keyCodeMap.put("628", "অ");
         keyCodeMap.put("629", "ো");
         keyCodeMap.put("630", "ও");
         keyCodeMap.put("631", "ঔ");
         keyCodeMap.put("632", "ৌ");
         keyCodeMap.put("633", "প");
        
         keyCodeMap.put("701", "আ");
         keyCodeMap.put("702", "া");
         keyCodeMap.put("703", "স");
         keyCodeMap.put("704", "শ");
         keyCodeMap.put("705", "ষ");         
         keyCodeMap.put("706", "দ");
         keyCodeMap.put("707", "ধ");
         keyCodeMap.put("708", "ড");
         keyCodeMap.put("709", "ঢ");
         keyCodeMap.put("710", "ফ");
         keyCodeMap.put("711", "গ");
         keyCodeMap.put("712", "ঘ");
         keyCodeMap.put("713", "হ");
         keyCodeMap.put("715", "জ");
         keyCodeMap.put("635", "ঝ");
         keyCodeMap.put("716", "ক");
         keyCodeMap.put("717", "খ");
         keyCodeMap.put("718", "ল");
         
         keyCodeMap.put("719", "য");
         keyCodeMap.put("720", "‍্য");
         keyCodeMap.put("721", "ঙ");
         keyCodeMap.put("722", "ঞ");
         keyCodeMap.put("723", "চ");
         keyCodeMap.put("724", "ছ");
         keyCodeMap.put("725", "ভ");
         keyCodeMap.put("726", "ব");
         keyCodeMap.put("728", "ন");
         keyCodeMap.put("729", "ণ");
         keyCodeMap.put("730", "ম");
         
         
         
         
         keyCodeMap.put("949", "১");
         keyCodeMap.put("950", "২");
         keyCodeMap.put("951", "৩");
         keyCodeMap.put("952", "৪");
         keyCodeMap.put("953", "৫");
         keyCodeMap.put("954", "৬");
         keyCodeMap.put("955", "৭");
         keyCodeMap.put("956", "৮");
         keyCodeMap.put("957", "৯");
         keyCodeMap.put("958", "০");
         
         keyCodeMap.put("1049", "1");
         keyCodeMap.put("1050", "2");
         keyCodeMap.put("1051", "3");
         keyCodeMap.put("1052", "4");
         keyCodeMap.put("1053", "5");
         keyCodeMap.put("1054", "6");
         keyCodeMap.put("1055", "7");
         keyCodeMap.put("1056", "8");
         keyCodeMap.put("1057", "9");
         keyCodeMap.put("1058", "0");
         
         
        keyCodeMap.put("801","ক্ট");
		keyCodeMap.put("802","ক্স");
	
		keyCodeMap.put("803","শ্ব");
		keyCodeMap.put("804","শ্ন");
		keyCodeMap.put("805","শ্ত");
	
		keyCodeMap.put("806","ষ্ক");
		keyCodeMap.put("807","ষ্প");
		keyCodeMap.put("808","ষ্ঠ");
	
		keyCodeMap.put("809","ষ্ট");
		keyCodeMap.put("810","স্খ");
		keyCodeMap.put("811","স্ট");
		
		keyCodeMap.put("812","ত্ম");
		keyCodeMap.put("813","ত্ন");
		keyCodeMap.put("814","ত্ব");
	
		keyCodeMap.put("815","");
		keyCodeMap.put("816","ট্ট");
		keyCodeMap.put("817","ত্ত্ব");
		keyCodeMap.put("818","ন্দ্ব");
		keyCodeMap.put("819","ন্ড");
		keyCodeMap.put("820","ন্স");
		keyCodeMap.put("821","ণ্ট");
		keyCodeMap.put("822","ণ্ঠ");
		keyCodeMap.put("823","ণ্ন");
		keyCodeMap.put("824","ল্ড");
		keyCodeMap.put("825","ল্গ");
		keyCodeMap.put("826","প্ত");
		keyCodeMap.put("827","প্প");
		keyCodeMap.put("828","প্স");
		keyCodeMap.put("829","স্ফ");	
		keyCodeMap.put("830","স্ম");
		keyCodeMap.put("831","স্ন");
		keyCodeMap.put("832","স্ক");
		keyCodeMap.put("833","স্প");	
		keyCodeMap.put("834","স্ত");
		keyCodeMap.put("835","দ্ব");
		keyCodeMap.put("836","দ্ধ");
		keyCodeMap.put("837","ফ্ল");
		keyCodeMap.put("838","জ্ঞ");	
		keyCodeMap.put("839","গ্ধ");
		keyCodeMap.put("840","গ্ব");	
		keyCodeMap.put("841","হ্ন");
		keyCodeMap.put("842","হ্ণ");	
		keyCodeMap.put("843","জ্জ");	
		keyCodeMap.put("844","জ্জ্ব");
		keyCodeMap.put("845","ক্ষ");
		keyCodeMap.put("846","ক্ক");
		keyCodeMap.put("920","ক্ত");
		keyCodeMap.put("847","ল্ক");
		keyCodeMap.put("848","ল্প");
		keyCodeMap.put("849","ধ্ব");
		keyCodeMap.put("850","ড্ড");	
		keyCodeMap.put("851","ঙ্গ");
		keyCodeMap.put("852","ঙ্ক্ষ");
		keyCodeMap.put("853","চ্চ");
		keyCodeMap.put("854","ভ্ল");
		keyCodeMap.put("855","ব্ব");
		keyCodeMap.put("856","ব্দ");
		keyCodeMap.put("857","ন্ন");
		keyCodeMap.put("858","ন্থ");
		keyCodeMap.put("859","ন্ত");
		keyCodeMap.put("860","ম্ম");
		keyCodeMap.put("861","ম্প");
		keyCodeMap.put("862","ম্ভ");
		keyCodeMap.put("863","ম্ব");
		keyCodeMap.put("864","ক্ল");
		keyCodeMap.put("865","ক্র");
		keyCodeMap.put("866","শ্চ");
		keyCodeMap.put("867","শ্ছ");
		keyCodeMap.put("921","শ্ম");	
		keyCodeMap.put("868","ষ্ণ");
		keyCodeMap.put("869","ষ্ম");	
		keyCodeMap.put("870","ষ্ক্র");
		keyCodeMap.put("871","শ্র");
		keyCodeMap.put("872","ত্ত");
		keyCodeMap.put("873","ত্র");
		keyCodeMap.put("874","ত্থ");
		keyCodeMap.put("875","থ্ব");
		keyCodeMap.put("876","ন্ত্ব");
		keyCodeMap.put("877","ণ্ব");
		keyCodeMap.put("878","ন্ব");
		keyCodeMap.put("879","ঞ্চ");
		keyCodeMap.put("880","ঞ্ছ");
		keyCodeMap.put("881","ঞ্জ");
		keyCodeMap.put("882","ল্ম");
		keyCodeMap.put("883","ল্ব");
		keyCodeMap.put("884","প্ন");
		keyCodeMap.put("885","প্ট");
		keyCodeMap.put("886","স্ল");
		keyCodeMap.put("887","স্ত্ব");
		keyCodeMap.put("888","স্থ");
		keyCodeMap.put("889","স্ব");
		keyCodeMap.put("890","দ্দ");
		keyCodeMap.put("891","দ্ম");
		keyCodeMap.put("892","ঘ্ন");
		keyCodeMap.put("893","গ্ম");
		keyCodeMap.put("894","গ্ন");
		keyCodeMap.put("895","হ্ব");
		keyCodeMap.put("896","হ্ম");
		keyCodeMap.put("897","জ্ব");
		keyCodeMap.put("898","ক্ষ্ম");
		keyCodeMap.put("899","ক্ম");	
		keyCodeMap.put("900","ক্ব");
		keyCodeMap.put("901","ল্ল");
		keyCodeMap.put("902","ল্ট");
		keyCodeMap.put("903","দ্ভ");	
		keyCodeMap.put("904","ঙ্ক");
		keyCodeMap.put("905","ঙ্ঘ");
		keyCodeMap.put("906","চ্ছ");
		keyCodeMap.put("907","ভ্র");
		keyCodeMap.put("908","ব্ধ");
		keyCodeMap.put("909","ব্জ");
		keyCodeMap.put("910","ন্দ");
		keyCodeMap.put("911","ন্ধ");
		keyCodeMap.put("912","ন্ট");
		keyCodeMap.put("913","ম্ন");
		keyCodeMap.put("914","ম্ফ");
		keyCodeMap.put("915","ম্ল");
		keyCodeMap.put("960","।");
		keyCodeMap.put("961",",");
		keyCodeMap.put("962"," ");	
		keyCodeMap.put("963","!");
		keyCodeMap.put("964","\"");
		keyCodeMap.put("965","\'");
		keyCodeMap.put("966",":");
		keyCodeMap.put("967",";");
		keyCodeMap.put("968","@");
		keyCodeMap.put("969","?");
       
    
       
        mWordSeparators = getResources().getString(R.string.word_separators);
    }
    
    /**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    @Override public void onInitializeInterface() {
    	
        if (mQwertyKeyboard != null) {
            // Configuration changes can happen after the keyboard gets recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth) return;
            mLastDisplayWidth = displayWidth;
        }
        mQwertyKeyboard = new LatinKeyboard(this, R.xml.qwerty);
        juktoborno = new LatinKeyboard(this, R.xml.kbd_popup_template_jukto1);
        juktoborno2 = new LatinKeyboard(this, R.xml.kbd_popup_template_jukto2);
        mSymbolsKeyboard = new LatinKeyboard(this, R.xml.symbols);
       
    }
    
    /**
     * Called by the framework when your view for creating input needs to
     * be generated.  This will be called the first time your input method
     * is displayed, and every time it needs to be re-created such as due to
     * a configuration change.
     */
    @Override public View onCreateInputView() {
    	
        mInputView = (KeyboardView) getLayoutInflater().inflate(
                R.layout.input, null);
        mInputView.setOnKeyboardActionListener(this);
        mInputView.setKeyboard(mQwertyKeyboard);
        return mInputView;
    }

    /**
     * Called by the framework when your view for showing candidates needs to
     * be generated, like {@link #onCreateInputView}.
     */
    @Override public View onCreateCandidatesView() {
    	mCandidateAndConfigLayout = new LinearLayout(this);
 	   // mCandidateAndConfigLayout = new LinearLayout(this);
 	    mCandidateAndConfigLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
 	    mCandidateAndConfigLayout.setOrientation(LinearLayout.VERTICAL);
 	      // CandidateView candidateView = new CandidateView(this);
 	   //("H","here");
 	    mBanglaPreviewView = new BanglaPreviewView(this);
  
 	    mBanglaPreviewView.setService(this);
 	
 	    
 	
 	        setCandidatesViewShown(true);
 	     //   updateCandidateText();
 	       mCandidateView = new CandidateView(this);
 	        mCandidateView.setService(this);
 	   	mCandidateAndConfigLayout.addView(mBanglaPreviewView);
 	       mCandidateAndConfigLayout.addView(mCandidateView);
 	        
 	  
         return mCandidateAndConfigLayout;
    	
    
    }

    /**
     * This is the main point where we do our initialization of the input method
     * to begin operating on an application.  At this point we have been
     * bound to the client, and are now receiving all of the detailed information
     * about the target of our edits.
     */
    @Override public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        
        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        mComposing.setLength(0);
        updateCandidates();
        
        if (!restarting) {
            // Clear shift states.
            mMetaState = 0;
        }
        
        mPredictionOn = false;
        mCompletionOn = false;
        mCompletions = null;
        
        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType&EditorInfo.TYPE_MASK_CLASS) {
            case EditorInfo.TYPE_CLASS_NUMBER:
            case EditorInfo.TYPE_CLASS_DATETIME:
                // Numbers and dates default to the symbols keyboard, with
                // no extra features.
                mCurKeyboard = mSymbolsKeyboard;
                break;
                
            case EditorInfo.TYPE_CLASS_PHONE:
                // Phones will also default to the symbols keyboard, though
                // often you will want to have a dedicated phone keyboard.
                mCurKeyboard = mSymbolsKeyboard;
                break;
                
            case EditorInfo.TYPE_CLASS_TEXT:
                // This is general text editing.  We will default to the
                // normal alphabetic keyboard, and assume that we should
                // be doing predictive text (showing candidates as the
                // user types).
                mCurKeyboard = mQwertyKeyboard;
                mPredictionOn = true;
                
                // We now look for a few special variations of text that will
                // modify our behavior.
                int variation = attribute.inputType &  EditorInfo.TYPE_MASK_VARIATION;
                if (variation == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD ||
                        variation == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Do not display predictions / what the user is typing
                    // when they are entering a password.
                    mPredictionOn = false;
                }
                
                if (variation == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS 
                        || variation == EditorInfo.TYPE_TEXT_VARIATION_URI
                        || variation == EditorInfo.TYPE_TEXT_VARIATION_FILTER) {
                    // Our predictions are not useful for e-mail addresses
                    // or URIs.
                    mPredictionOn = false;
                }
                
                if ((attribute.inputType&EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    // If this is an auto-complete text view, then our predictions
                    // will not be shown and instead we will allow the editor
                    // to supply their own.  We only show the editor's
                    // candidates when in fullscreen mode, otherwise relying
                    // own it displaying its own UI.
                    mPredictionOn = false;
                    mCompletionOn = true;
                }
                
                // We also want to look at the current state of the editor
                // to decide whether our alphabetic keyboard should start out
                // shifted.
                updateShiftKeyState(attribute);
                break;
                
            default:
                // For all unknown input types, default to the alphabetic
                // keyboard with no special features.
                mCurKeyboard = mQwertyKeyboard;
                updateShiftKeyState(attribute);
        }
        
        // Update the label on the enter key, depending on what the application
        // says it will do.
        mCurKeyboard.setImeOptions(getResources(), attribute.imeOptions);
    }

    /**
     * This is called when the user is done editing a field.  We can use
     * this to reset our state.
     */
    @Override public void onFinishInput() {
        super.onFinishInput();
        
        // Clear current composing text and candidates.
        mComposing.setLength(0);
        updateCandidates();
        
        // We only hide the candidates window when finishing input on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);
        
        mCurKeyboard = mQwertyKeyboard;
        if (mInputView != null) {
            mInputView.closing();
        }
    }
    
    @Override public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        // Apply the selected keyboard to the input view.
        mInputView.setKeyboard(mCurKeyboard);
        mInputView.closing();
    }
    
    /**
     * Deal with the editor reporting movement of its cursor.
     */
    @Override public void onUpdateSelection(int oldSelStart, int oldSelEnd,
            int newSelStart, int newSelEnd,
            int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);
        
        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (mComposing.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            mComposing.setLength(0);
            updateCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    /**
     * This tells us about completions that the editor has determined based
     * on the current text in it.  We want to use this in fullscreen mode
     * to show the completions ourself, since the editor can not be seen
     * in that situation.
     */
    @Override public void onDisplayCompletions(CompletionInfo[] completions) {
        if (mCompletionOn) {
            mCompletions = completions;
            if (completions == null) {
                setSuggestions(null, false, false);
                return;
            }
            
         /*   List<String> stringList = new ArrayList<String>();
            for (int i=0; i<(completions != null ? completions.length : 0); i++) {
                CompletionInfo ci = completions[i];
                if (ci != null) stringList.add(ci.getText().toString());
            }
            setSuggestions(stringList, true, true);*/
        }
    }
    
    /**
     * This translates incoming hard key events in to edit operations on an
     * InputConnection.  It is only needed when using the
     * PROCESS_HARD_KEYS option.
     */
    private boolean translateKeyDown(int keyCode, KeyEvent event) {
        mMetaState = MetaKeyKeyListener.handleKeyDown(mMetaState,
                keyCode, event);
        int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(mMetaState));
        mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(mMetaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }
        
        boolean dead = false;

        if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
            dead = true;
            c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
        }
        
        if (mComposing.length() > 0) {
            char accent = mComposing.charAt(mComposing.length() -1 );
            int composed = KeyEvent.getDeadChar(accent, c);

            if (composed != 0) {
                c = composed;
                mComposing.setLength(mComposing.length()-1);
            }
        }
        
        onKey(c, null);
        
        return true;
    }
    
    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // The InputMethodService already takes care of the back
                // key for us, to dismiss the input method if it is shown.
                // However, our keyboard could be showing a pop-up window
                // that back should dismiss, so we first allow it to do that.
                if (event.getRepeatCount() == 0 && mInputView != null) {
                    if (mInputView.handleBack()) {
                        return true;
                    }
                }
                break;
                
            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;
                
            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;
                
            default:
                // For all other keys, if we want to do transformations on
                // text being entered with a hard keyboard, we need to process
                // it and do the appropriate action.
                if (PROCESS_HARD_KEYS) {
                    if (keyCode == KeyEvent.KEYCODE_SPACE
                            && (event.getMetaState()&KeyEvent.META_ALT_ON) != 0) {
                        // A silly example: in our input method, Alt+Space
                        // is a shortcut for 'android' in lower case.
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // First, tell the editor that it is no longer in the
                            // shift state, since we are consuming this.
                            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                            keyDownUp(KeyEvent.KEYCODE_A);
                            keyDownUp(KeyEvent.KEYCODE_N);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            keyDownUp(KeyEvent.KEYCODE_R);
                            keyDownUp(KeyEvent.KEYCODE_O);
                            keyDownUp(KeyEvent.KEYCODE_I);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            // And we consume this event.
                            return true;
                        }
                    }
                    if (mPredictionOn && translateKeyDown(keyCode, event)) {
                        return true;
                    }
                }
        }
        
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override public boolean onKeyUp(int keyCode, KeyEvent event) {
        // If we want to do transformations on text being entered with a hard
        // keyboard, we need to process the up events to update the meta key
        // state we are tracking.
        if (PROCESS_HARD_KEYS) {
            if (mPredictionOn) {
                mMetaState = MetaKeyKeyListener.handleKeyUp(mMetaState,
                        keyCode, event);
            }
        }
        
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Helper function to commit any text being composed in to the editor.
     */
    private void commitTyped(InputConnection inputConnection) {
        if (mComposing.length() > 0) {
            inputConnection.commitText(mComposing, mComposing.length());
            mComposing.setLength(0);
            updateCandidates();
          
        }
    }

    /**
     * Helper to update the shift state of our keyboard based on the initial
     * editor state.
     */
    private void updateShiftKeyState(EditorInfo attr) {
        if (attr != null 
                && mInputView != null && mQwertyKeyboard == mInputView.getKeyboard()) {
            int caps = 0;
            EditorInfo ei = getCurrentInputEditorInfo();
            if (ei != null && ei.inputType != EditorInfo.TYPE_NULL) {
                caps = getCurrentInputConnection().getCursorCapsMode(attr.inputType);
            }
            mInputView.setShifted(mCapsLock || caps != 0);
        }
    }
    
    /**
     * Helper to determine if a given character code is alphabetic.
     */
    private boolean isAlphabet(int code) {
        if (Character.isLetter(code)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Helper to send a key down / key up pair to the current editor.
     */
    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }
    
    /**
     * Helper to send a character to the editor as raw key events.
     */
    private void sendKey(int keyCode) {
        switch (keyCode) {
            case '\n':
                keyDownUp(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                if (keyCode >= '0' && keyCode <= '9') {
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                }
                break;
        }
    }

    // Implementation of KeyboardViewListener

    public void onKey(int primaryCode, int[] keyCodes) {
    	 if (primaryCode == Keyboard.KEYCODE_DELETE) {
             handleBackspace();
             updateCandidates();
         } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
             handleShift();
         } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
             handleClose();
             return;
         } else if (primaryCode == LatinKeyboardView.KEYCODE_OPTIONS) {
             // Show a menu or somethin'
         } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE
                 && mInputView != null) {
             Keyboard current = mInputView.getKeyboard();
             if (current == mSymbolsKeyboard ||current ==juktoborno||current ==juktoborno2) {
                 current = mQwertyKeyboard;
             } else if( current == mQwertyKeyboard) {
                 current = mSymbolsKeyboard;
             }
             mInputView.setKeyboard(current);
             if (current == mSymbolsKeyboard) {
                 current.setShifted(false);
             }
         }   	
         
        else if(primaryCode==10)
        	keyDownUp(KeyEvent.KEYCODE_ENTER);

        else if(BanglaPreviewView.sptype.contains(keyCodeMap.get(""+primaryCode))) {
            // Handle separator
        	
        	c = keyCodeMap.get(""+primaryCode);
        	Log.d("rana",c);
            if (mComposing.length() > 0) {
                commitTyped(getCurrentInputConnection());
            }
            getCurrentInputConnection().commitText(c,1);
            updateShiftKeyState(getCurrentInputEditorInfo());
            updateCandidates();
        }else {
            handleCharacter(primaryCode, keyCodes);
        }
    }

    public void onText(CharSequence text) {
    	//("xxx","RANA");
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.beginBatchEdit();
        if (mComposing.length() > 0) {
            commitTyped(ic);
        }
        ic.commitText(text, 0);
        ic.endBatchEdit();
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    private void updateCandidates() {
//        mTamilPreviewView.update("RANA");//
    	
    	
    	//ami eikhane bangla preview update korsi
    	
    	String strbeforeCursor="";
        String strafterCursor ="";
        try{
        strbeforeCursor = getCurrentInputConnection().getTextBeforeCursor(1000000000, 0).toString();
        strafterCursor = getCurrentInputConnection().getTextAfterCursor(1000000000, 0).toString();
        String str = strbeforeCursor +""+strafterCursor;
        if(mBanglaPreviewView != null && str!=null)
        	mBanglaPreviewView.update(str);
        }
        catch (Exception e) {
        	
			// TODO: handle exception
		}
        
    	
    	//ami eikhane bangla preview update korsi
    	setSuggestions(null, false, false);
    
            if (mComposing.length() > 0) {
            	   //mBanglaPreviewView.update(mComposing.toString());
            	list.clear();
       
                list.add(mComposing.toString());
                SQLiteDatabase db;     
                try{
            	    db = SQLiteDatabase.openDatabase("data/data/fahim.rana.protity/databases/dictionary", null,0);
            		//("opendb","EXIST");
            	    String q = "SELECT * FROM LIST where( wlist like '"+mComposing.toString()+"%')"+"ORDER BY wlist"; //"%')"+
                    Cursor c = db.rawQuery(q, null);
                    if(c.getCount() == 0)
                    {
                  	  setSuggestions(list, true, true);
                    }
                    else  {
                    	int i=0;
                  	  c.moveToFirst();
                        do {
                      	  String cm = c.getString(c.getColumnIndex("wlist")); 
                       	if(!list.contains(cm))
                      		  list.add(cm);
                      		  i++;
                      	  } while (c.moveToNext() && i<30);              
                    	
                  c.close();
                  db.close();   
              	
                  setSuggestions(list, true, true);
              } 
            		}
            		catch(SQLiteException e){
            			//("opendb","NOT EXIST");
            	
            		db = openOrCreateDatabase("dictionary", MODE_PRIVATE, null);
                	      db.execSQL("CREATE TABLE IF NOT EXISTS LIST(wlist varchar);");
                	             
                	        db.execSQL("INSERT INTO LIST VALUES('খবর');");
                	        db.execSQL("INSERT INTO LIST VALUES('বক');");
                	        db.execSQL("INSERT INTO LIST VALUES('কবর');"); 
                	        db.execSQL("INSERT INTO LIST VALUES('কখন');");
                	        db.execSQL("INSERT INTO LIST VALUES('গল্প');"); 
                	        db.execSQL("INSERT INTO LIST VALUES('গাধা');");
                	        db.execSQL("INSERT INTO LIST VALUES('গবর');");
                	      //  db.execSQL("INSERT INTO LIST VALUES('ক','কবর');");
                	        String q = "SELECT * FROM LIST where( wlist like '"+mComposing.toString()+"%')"+"ORDER BY wlist"; //"%')"+
                            Cursor c = db.rawQuery(q, null);
                            if(c.getCount() == 0)
                            {
                          	  setSuggestions(list, true, true);
                            }
                            else  {
                            	 int i=0;
                          	  c.moveToFirst();
                          	 
							do {
                              	  String cm = c.getString(c.getColumnIndex("wlist")); 
                              	if(!list.contains(cm))
                              		  list.add(cm);
                              		  i++;
                              	  } while (c.moveToNext() && i<30);                
                
                          c.close();
                          db.close();   
                      	
                          setSuggestions(list, true, true);
                      }                 	 
            		}

                      if( db.isOpen() )
                      {
                    	  db.close();
                      }
            
            	 
            	 
              
            }
        
        
    }
    
    public void setSuggestions(List<String> suggestions, boolean completions,
            boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        if (mCandidateView != null) {
            mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
        }
    }
    
    private void handleBackspace() {
        final int length = mComposing.length();
        if (length > 1) {
            mComposing.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            updateCandidates();
        } else if (length > 0) {
            mComposing.setLength(0);
            getCurrentInputConnection().commitText("", 0);
            mBanglaPreviewView.update("");
            updateCandidates();
        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
        updateCandidates();
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    private void handleShift() {
  	  if (mInputView == null) {
            return;
        }
        
        Keyboard currentKeyboard = mInputView.getKeyboard();
        if (mQwertyKeyboard == currentKeyboard) {
      	  mInputView.setKeyboard(juktoborno);
         //   juktoborno.setShifted(true);
          
        } 
        else if (juktoborno == currentKeyboard) {
      	  mInputView.setKeyboard(juktoborno2);
      	  juktoborno2.setShifted(true);
          
        } 
        else if (juktoborno2 == currentKeyboard) {
      	  mInputView.setKeyboard(juktoborno);
            juktoborno.setShifted(false);
          
        } 



    
  }
    private void handleCharacter(int primaryCode, int[] keyCodes) {
     
    	if(primaryCode == 1500)
    	{
    	    SQLiteDatabase db;     
            try{

        	    db = SQLiteDatabase.openDatabase("data/data/fahim.rana.protity/databases/dictionary", null,0);
        		//("opendb","EXIST");
        	     String q = "SELECT * FROM LIST where( wlist == '"+mComposing.toString()+"')";
        	     Cursor cc = db.rawQuery(q, null);
        	     if(cc.getCount() == 0)
                 {
                 	 db.execSQL("INSERT INTO LIST VALUES('"+mComposing.toString()+"');");	
                 }
           //      else
                 //	getCurrentInputConnection().commitText("আছে", 1);
                 cc.close();
                 db.close();
          	
              setSuggestions(list, true, true);
          } 
        		
        		catch(SQLiteException e){
        			//("opendb","NOT EXIST");
        	
        		db = openOrCreateDatabase("dictionary", MODE_PRIVATE, null);
            	      db.execSQL("CREATE TABLE IF NOT EXISTS LIST(wlist varchar);");
            	             
            	        db.execSQL("INSERT INTO LIST VALUES('খবর');");
            	        db.execSQL("INSERT INTO LIST VALUES('বক');");
            	        db.execSQL("INSERT INTO LIST VALUES('কবর');"); 
            	        db.execSQL("INSERT INTO LIST VALUES('কখন');");
            	        db.execSQL("INSERT INTO LIST VALUES('গল্প');"); 
            	        db.execSQL("INSERT INTO LIST VALUES('গাধা');");
            	        db.execSQL("INSERT INTO LIST VALUES('গবর');");
            
                        db.close();   
        		}

                  if( db.isOpen() )
                  {
                	  db.close();
                  }
    	}
    	else if(primaryCode>=500)// == -20 ||primaryCode == -21 ||primaryCode == -22 ||primaryCode == -23 ||primaryCode == -28 ||primaryCode == -29 ||primaryCode == -30 ||primaryCode == -15||primaryCode == 900 )
        { 
    		c = keyCodeMap.get(""+primaryCode);
        	mComposing.append(c);
        getCurrentInputConnection().setComposingText(mComposing, 1);
        updateShiftKeyState(getCurrentInputEditorInfo());
        //editing here
    
   
       updateCandidates();
     //   mBanglaPreviewView.update("ি কিকি ককি কিক ক্র ক্রি ক্রিক ক্রিকি কি িিিিি");
     //       getCurrentInputConnection().commitText(c,1);                   
        }
    }

    private void handleClose() {
        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        mInputView.closing();
    }

    private void checkToggleCapsLock() {
        long now = System.currentTimeMillis();
        if (mLastShiftTime + 800 > now) {
            mCapsLock = !mCapsLock;
            mLastShiftTime = 0;
        } else {
            mLastShiftTime = now;
        }
    }
    
    private String getWordSeparators() {
        return mWordSeparators;
    }
    
    public boolean isWordSeparator(int code) {
        String separators = getWordSeparators();
        return separators.contains(String.valueOf((char)code));
    }

    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }
    
    public void pickSuggestionManually(int index) {
    	//&& mCompletions != null
        if (index >= 0) {
          //  CompletionInfo ci = list.get(index);
            getCurrentInputConnection().commitText(list.get(index), 1);
          // getCurrentInputConnection().commitCompletion(null);
       //     getCurrentInputConnection().commitText(" ",1);
        //	getCurrentInputConnection().commitText(p.substring(0, 2), 1);
            if (mCandidateView != null) {
                mCandidateView.clear();
            }
            updateShiftKeyState(getCurrentInputEditorInfo());
        } /*else if (mComposing.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
            commitTyped(getCurrentInputConnection());
        }*/
        updateCandidates();
    }
    
    
    public void swipeRight() {
        if (mCompletionOn) {
            pickDefaultCandidate();
        }
    }
    
    public void swipeLeft() {
        handleBackspace();
    }

    public void swipeDown() {
        handleClose();
    }

    public void swipeUp() {
    }
    
    public void onPress(int primaryCode) {
    }
    
    public void onRelease(int primaryCode) {
    }
    
    
   
}
