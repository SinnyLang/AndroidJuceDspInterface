package xyz.sl.mylibrary;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import xyz.sl.dsp.juce.JuceNativeInterface;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("xyz.sl.mylibrary.test", appContext.getPackageName());
    }

    @Test
    public void loadLibrary(){
        JuceNativeInterface juceNativeInterface = new JuceNativeInterface();
        String version = juceNativeInterface.getVersion();
        assertNotNull("加载JuceNativeInterface动态库测试失败", version);
    }
}