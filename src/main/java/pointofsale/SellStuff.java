package pointofsale;

import java.io.IOException;
import java.net.Socket;

class ModemDidNotConnectException extends Exception {
  // should have a bunch of constructors
}

class ModemSupport {
  public static void dialNumber(int number) throws ModemDidNotConnectException {
    // should probably retry?
    // beware of retrying non-idempotent operations!!!
    // also beware of automatic retries in security-like situations
    // that might cause account lockout!
  }
}

/*
 * "could you just"...
 * "better" design if it "limits the consequences of change"
 * encapsulation... split two things apart, design an *interface*
 * to allow them to communicate / get the job done, and bring them
 * together to do that.
 * interface should be the boundary of the effect of change
 *
 * 1) how you fail IS PART OF YOUR INTERFACE
 * 2) allowing implementation specific errors to escape is probably bad!
 */
public class SellStuff {
  private static boolean useModem = false;

  static void chargeCreditCard(int price, int cardNumber)
      throws ModemDidNotConnectException, IOException {
    if (useModem) {
      ModemSupport.dialNumber(12345);
    } else {
      Socket s = new Socket("127.0.0.1", 1234);
    }
  }

  static void sellStuff(String item, int price) {
    try {
      // if credit card
      chargeCreditCard(1000, 12345);
      // ask for alternate means of payment -- here we have more
      // "resources" (e.g. human-interaction) or opportunities
    } catch (ModemDidNotConnectException mdnce) {

    }
  }
}
