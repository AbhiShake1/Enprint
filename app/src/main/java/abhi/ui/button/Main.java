package abhi.ui.button;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public abstract class Main extends AppCompatButton {
    protected Main(@NonNull Context context) {
        super(context);
        init();
    }

    protected Main(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected Main(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init(){
        setOnClickListener((c)->onClick());
    }

    protected abstract void onClick();
}
