package com.bruno.desafiomuxi.webrequest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.internal.runner.ClassPathScanner;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import com.bruno.desafiomuxi.R;
import com.bruno.desafiomuxi.core.FruitDetailsActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by Bruno on 20/08/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class WebRequesterTest {
    private static final int    FRUIT_REQUEST_ID = 0, FRUIT_INVALID_REQUEST_ID = 1;

    @Before
    public void runBeforeTestMethods() throws Exception {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        WebRequester.init(appContext);
    }

    // This test is design to verify if the webrequester can properly get the json file
    // and parse it to an array of Fruit objects.
    // The json URI is considered to be fixed because the tests are designed to run in a test
    // enviroment, so in case the json test file changes, this test will be updated to match
    // the new version of the json file
    @Test
    public void fruitsGetRequestValidUri() throws Exception {
        WebRequester webRequester = new WebRequester();

        // fruitsGetRequest is an asyncronous method, so we need to sync the test thread
        // and waits until the asyncronous method finish
        final CountDownLatch signal = new CountDownLatch(1);

        webRequester.fruitsGetRequest(new WebRequestListener() {
                @Override
                public void fruitsReceived(Fruit[] fruits, int requestId) {
                    // assert this is the answer of the correct request
                    assertEquals("Wrong request received", requestId, 0);

                    // assert that the json file was obtained through the web request and
                    // the parsing to an array of Fruit succeed.
                    assertArrayEquals("Result array of Fruit doesn't match.", fruits, new Fruit[]{
                            new Fruit("Apple", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Red_Apple.jpg/265px-Red_Apple.jpg", 35),
                            new Fruit("Banana", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Bananas_white_background_DS.jpg/320px-Bananas_white_background_DS.jpg", 12),
                            new Fruit("Grapes", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Table_grapes_on_white.jpg/320px-Table_grapes_on_white.jpg", 45),
                            new Fruit("Pineapple", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Pineapple_and_cross_section.jpg/286px-Pineapple_and_cross_section.jpg", 200),
                            new Fruit("cherry", "http://www.desicomments.com/wp-content/uploads/2017/05/Cherry-Image-600x570.jpg", 13),
                            new Fruit("clementine", "http://www.icecreamnation.org/wp-content/uploads/2013/04/Clementine_orange.jpg", 12.4),
                            new Fruit("olive", "https://www.homenaturalcures.com/wp-content/uploads/olive.jpg", 9.5),
                            new Fruit("tomato", "http://cdn2.stylecraze.com/wp-content/uploads/2013/05/tomato-hair-benefits1.jpg", 8.75),
                            new Fruit("huckleberry", "http://farm3.static.flickr.com/2131/2082287810_47339fc93e.jpg", 11.75),
                            new Fruit("papaya", "http://media.mercola.com/assets/images/foodfacts/papaya-nutrition-facts.jpg", 2.75),
                            new Fruit("lime", "https://www.florihana.com/images/stories/virtuemart/product/FLE019%20-%20LIME.jpg", 5.75),
                            new Fruit("pear", "https://www.organicfacts.net/wp-content/uploads/pear.jpg", 4.75)});

                    signal.countDown();
                }

                @Override
                public void imageReceived(int requestId) {
                    // Wrong request answer, a json file was expecetd
                    fail("Expected fruits list. Image received");

                    signal.countDown();
                }

                @Override
                public void requestError(String errorMessage, int requestId) {
                    // The request failed. Not necessarily the WebRequester failed
                    // Verifiy the network status
                    fail("Request failed.");

                    signal.countDown();
                }
            }, FRUIT_REQUEST_ID);

        // Waiting for the asyncronous method finish
        signal.await();
    }

    // In this test case the request must fail in order to verify is the request error detection is
    // working properly
    @Test
    public void fruitsGetRequestInvalidUri() throws Exception {
        WebRequester webRequester = new WebRequester();

        final CountDownLatch signal = new CountDownLatch(1);

        // An invalid URI is put in the fruitsJsonUrl in order to test if an request error is
        // properly handled
        WebRequester.fruitsJsonUrl = "https://raw.githubusercontent.com/muxidev/desafio-android/master/seedz.json";

        webRequester.fruitsGetRequest(new WebRequestListener() {
                @Override
                public void fruitsReceived(Fruit[] fruits, int requestId) {
                    // If a Fruit array was received, something went wrong, the request
                    // was expected to fail
                    fail("A request error was expected, but a fruits list was received instead.");

                    signal.countDown();
                }

                @Override
                public void imageReceived(int requestId) {
                    // Wrong request answer, a json file was expecetd
                    fail("Expected fruits list. Image received");

                    signal.countDown();
                }

                @Override
                public void requestError(String errorMessage, int requestId) {
                    // Assert that the correct request has failed
                    assertEquals("Wrong request received.", requestId, FRUIT_INVALID_REQUEST_ID);

                    signal.countDown();
                }
            }, FRUIT_INVALID_REQUEST_ID);

        signal.await();
    }
}