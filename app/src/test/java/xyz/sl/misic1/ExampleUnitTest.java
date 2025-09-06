package xyz.sl.misic1;

import org.junit.Test;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testArray(){
        V a = new V(1);
        V b;
        b = a;
        a.setA(2);
        assertEquals(2, b.a);

    }
    class V {
        int a;

        V(int a){
            this.a = a;
        }

        public void setA(int a) {
            this.a = a;
        }
    }

    @Test
    public void testBuffer(){
        ByteBuffer buffer = ByteBuffer.allocate(10);
        ShortBuffer sb = buffer.asShortBuffer().put((short) 1);
        sb.flip();
        assertEquals(1, sb.get());
    }
}