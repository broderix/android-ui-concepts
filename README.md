android-ui-concepts
===================

[Youtube video with demo](http://www.youtube.com/watch?v=IGMLwgUDb4o) 


How to use
==========

* import /library to Eclipse as library;
* add 
`<include layout="@layout/login_ui"/>`
to layout 
* add buttons in activity code like that

``` java

public class AndroidUIConceptsDemos extends Activity {
    private LoginUI mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogin = new LoginUI(this);
        mLogin.addButton("facebook", R.drawable.f_logo, new Handler(){
            @Override public void handleMessage(Message msg) {
                Toast.makeText(AndroidUIConceptsDemos.this, "FACEBOOK", Toast.LENGTH_SHORT).show();
            }
        });
        mLogin.draw();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mLogin.shouldProcessBack()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
```
