package betterthanexceptions;

import java.util.stream.Stream;

public class UseAStream {
  public static void main(String[] args) {
    Stream.of("a.txt", "b.txt", "c.txt")
        // convert filename to stream of data?
        // process data?
        .forEach(System.out::println);
  }
}
