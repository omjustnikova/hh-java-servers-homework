import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;
import org.asynchttpclient.cookie.CookieStore;
import org.asynchttpclient.uri.Uri;
import org.awaitility.Awaitility;
import org.hamcrest.core.IsEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JerseyAppTest {

  private static final String HOST = "http://localhost:8081";
  private final static ObjectMapper objectMapper = new ObjectMapper();
  private static AsyncHttpClient client;
  private static boolean isServerUp;

  @BeforeEach
  public void init() {
    client.getConfig().getCookieStore().clear();

    assertTrue(isServerUp, "Server is down or not responding to /status");
  }

  @BeforeAll
  public static void staticInit() {
    client = Dsl.asyncHttpClient();
    runAndWaitServer();
  }

  @Test
  public void counterWorksTest() {
    getCounterValue();
  }

  @Test
  public void testCounterIncreaseWorks() throws Exception {
    int initCounterValue = getCounterValue();

    Response response = increaseCounter().get();
    assertTrue(isStatusCodeOk(response), "Server response is not ok");

    int counterValue = getCounterValue();
    assertEquals(initCounterValue + 1, counterValue, "Counter value didnt change");
  }

  @Test
  public void testCounterDecreaseWorks() throws Exception {
    int initCounterValue = getCounterValue();

    increaseCounter().get();
    increaseCounter().get();
    increaseCounter().get();

    Response response = decreaseCounter(2).get();
    assertTrue(isStatusCodeOk(response), "Server response is not ok");

    int counterValue = getCounterValue();
    assertEquals(initCounterValue + 3 - 2, counterValue, "Subtraction is not works properly");
  }

  @Test
  public void testClearWorksCorrect() throws Exception {
    increaseCounter().get();
    increaseCounter().get();
    increaseCounter().get();

    Response response = clearCounter("somestringmorethan10").get();
    assertTrue(isStatusCodeOk(response));

    int counterValue = getCounterValue();
    assertEquals(0, counterValue, "Counter is not cleared");
  }

  @Test
  public void testClearAuthWorksCorrect() throws Exception {
    increaseCounter().get();

    Response response = clearCounter("some").get();
    int statusCode = response.getStatusCode();
    assertTrue(statusCode >= 400 && statusCode < 500);

    int counterValue = getCounterValue();
    assertNotEquals(0, counterValue, "Counter is cleared, but should not");
  }

  @Test
  public void testMultithreadingIncreaseWorksCorrect() throws Exception {
    int increaseTo = 20_000;
    int initValue = getCounterValue();
    List<CompletableFuture<Response>> futures = new ArrayList<>();

    for (int i = 0; i < increaseTo; i++) {
      futures.add(increaseCounter());
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    int result = getCounterValue();

    assertEquals(initValue + increaseTo, result, "Counter is not thread safe");
  }

  @Test
  public void testMultithreadingDecreaseWorksCorrect() throws Exception {
    int decreaseTo = 20_000;
    int increaseTo = 40_000;
    int initValue = getCounterValue();

    // increase
    for (int i = 0; i < increaseTo; i++) {
      increaseCounter().get();
    }

    // decrease async
    List<CompletableFuture<Response>> futures = new ArrayList<>();

    for (int i = 0; i < decreaseTo; i++) {
      futures.add(decreaseCounter(1));
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

    int result = getCounterValue();
    assertEquals(initValue + increaseTo - decreaseTo, result, "Counter is not thread safe");
  }

  private CompletableFuture<Response> clearCounter(String authCookieValue) {
    CookieStore cookieStore = client.getConfig().getCookieStore();
    String url = HOST + "/counter/clear";
    Uri uri = Uri.create(url);

    cookieStore.add(uri, new DefaultCookie("hh-auth", authCookieValue));
    return client.preparePost(url)
        .execute()
        .toCompletableFuture();
  }

  private boolean isStatusCodeOk(Response httpResponse) {
    int statusCode = httpResponse.getStatusCode();
    return statusCode >= 200 && statusCode < 300;
  }

  private CompletableFuture<Response> increaseCounter() {
    return client.preparePost(HOST + "/counter")
        .execute()
        .toCompletableFuture();
  }

  private CompletableFuture<Response> decreaseCounter(int value) {
    return client.prepareDelete(HOST + "/counter")
        .addHeader("Subtraction-Value", String.valueOf(value))
        .execute()
        .toCompletableFuture();
  }

  private int getCounterValue() {
    try {
      Response response = client.prepareGet(HOST + "/counter")
          .execute()
          .get(5L, TimeUnit.SECONDS);

      assertTrue(isStatusCodeOk(response));

      JsonNode jsonNode = objectMapper.readTree(response.getResponseBody());

      JsonNode counterValue = jsonNode.path("value");
      assertFalse(counterValue.isMissingNode(), "Counter value field wasn't found");

      JsonNode counterDate = jsonNode.path("date");
      assertFalse(counterDate.isMissingNode(), "Counter date field wasn't found");

      assertTrue(isIsoDateFormat(counterDate.textValue()), "Date is not in iso format");

      return counterValue.intValue();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private boolean isIsoDateFormat(String date) {
    try {
      Instant.from(DateTimeFormatter.ISO_INSTANT.parse(date));
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  private static Thread runAndWaitServer() {
    Thread thread = new Thread(() -> {
      try {
        JerseyApplication.main(new String[]{});
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    thread.start();

    waitUntilServerUp();
    isServerUp = true;
    return thread;
  }

  private static void waitUntilServerUp() {
    Awaitility.await()
        .atMost(10, TimeUnit.SECONDS)
        .pollInterval(500, TimeUnit.MILLISECONDS)
        .until(JerseyAppTest::isServerUp, IsEqual.equalTo(true));
  }

  private static boolean isServerUp() {
    try {
      Response response = client.prepareGet(HOST + "/status").execute().get();
      return response.getStatusCode() == 200;
    } catch (Exception e) {
      return false;
    }
  }
}
