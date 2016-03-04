package com.li.xroads.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by nagnath_p on 3/4/2016.
 */
public abstract class TextValidator implements TextWatcher {
    private final EditText editText;

    public TextValidator(EditText textView) {
        this.editText = textView;
    }



    public abstract void validate(EditText textView, String text);

    @Override
    final public void afterTextChanged(Editable s) {
        String text = editText.getText().toString();
        validate(editText, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}