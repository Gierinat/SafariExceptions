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

class NoMoneyException extends Exception {}
class StolenCardException extends Exception {}
// might simply use IOException for "Infrastructure Failure"
class InfrastructureException extends Exception {
  public InfrastructureException() {
  }

  public InfrastructureException(String message) {
    super(message);
  }

  public InfrastructureException(String message, Throwable cause) {
    super(message, cause);
  }

  public InfrastructureException(Throwable cause) {
    super(cause);
  }

  public InfrastructureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
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
//      throws ModemDidNotConnectException, IOException {
  throws InfrastructureException, NoMoneyException, StolenCardException {
    try {
      if (useModem) {
        ModemSupport.dialNumber(12345);
      } else {
        Socket s = new Socket("127.0.0.1", 1234);
      }
//    } catch (ModemDidNotConnectException m) { // these two are duplicate
//      throw new InfrastructureException(m);
//    } catch (IOException i) {
//      throw new InfrastructureException(i);
//    } catch (Exception e) { BAD BAD BAD
    } catch (ModemDidNotConnectException | IOException e) {
      // e has type "nearest common ancestor"
//      Exception f = e;
//      throw f; // BAD, requires "throws Exception"

//      throw e; // ok... even though this is type Exception, compiler
//      // works out that it can only be MDNCE, or IOE, but ONLY
//      // if e is final or effectively final

      throw new InfrastructureException(e);
    }
  }

  static void sellStuff(String item, int price) {
    try {
      // if credit card
      chargeCreditCard(1000, 12345);
      // ask for alternate means of payment -- here we have more
      // "resources" (e.g. human-interaction) or opportunities
    } catch (InfrastructureException mdnce) {

    } catch (StolenCardException sce) {}
    catch (NoMoneyException nme) {}
  }
}
