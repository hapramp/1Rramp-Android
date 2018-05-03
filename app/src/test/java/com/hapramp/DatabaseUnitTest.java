package com.hapramp;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = LOLLIPOP, packageName = "com.hapramp")
public class DatabaseUnitTest {

    public void p(String msg) {
        System.out.println(msg);
    }
}
