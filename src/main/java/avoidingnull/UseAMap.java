package avoidingnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class UseAMap {
  public static String getFirstName() {
    return "Fred";
  }

//  public static String[] getFromMap(String key, Map<String, String> map) {
  public static List<String> getFromMap(String key, Map<String, String> map) {
    String res = map.get(key);
    List<String> rv = new ArrayList<>();
    if (res != null) {
      rv.add(res);
    }
    return rv;
  }

//  public static List<String> changeItem(
// "the map of a Functor"
  public static List<String> map(
      List<String> ls, Function<String, String> op) {
    List<String> results = new ArrayList<>();
    for (String s : ls) {
      String t = op.apply(s);
      // patch the Functor to the null-based system
      // drives mathematicians and FP zealots NUTS!!!!
      if (t != null) {

        results.add(t);
      }
    }
    return results;
  }

  public static void main(String[] args) {
    Map<String, String> names = Map.of("Fred", "Jones");

    String firstName = getFirstName();
    String lastName = names.get(firstName);
    if (lastName != null) {
      String message = "Dear " + lastName.toUpperCase() + ", " + firstName;
      System.out.println(message);
    }

    System.out.println("----------------");
//    String[] lastNames = getFromMap(firstName, names);
    List<String> lastNames = getFromMap(firstName, names);
    for (String lastName2 : lastNames) {
      String message = "Dear " + lastName2.toUpperCase() + ", " + firstName;
      System.out.println(message);
    }

    System.out.println("----------------");
//    String[] lastNames = getFromMap(firstName, names);
    // still have lastNames...
//    List<String> lastNames = getFromMap(firstName, names);

//    List<String> messages =
//        map(lastNames, s -> "Dear " + s.toUpperCase() + ", " + firstName);
//
//    for (String m : messages) {
//      System.out.println(m);
//    }
    map(lastNames, s -> "Dear " + s.toUpperCase() + ", " + firstName)
        .forEach(s -> System.out.println(s));

    System.out.println("------------------");
    Optional<Map<String, String>> opt = Optional.of(names);
    opt.map(m -> m.get(firstName))
        .map(s -> s.toUpperCase())
        .map(s -> "Dear " + s + ", " + firstName)
        .ifPresent(System.out::println);
  }
}
