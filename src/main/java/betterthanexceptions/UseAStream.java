package betterthanexceptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

interface ExFunction<A, R> {
  R apply(A a) throws Throwable;

  static <A, R> Function<A, Optional<R>> wrap(ExFunction<A, R> op) {
    return a -> {
      try {
        return Optional.of(op.apply(a));
      } catch (Throwable t) {
        // generics have no meaning at runtime,
        // so we must catch Throwable...
        return Optional.empty();
      }
    };
  }
}

public class UseAStream {
//  public static Stream<String> getStreamFromFile(String fn) {
//    try {
//      return Files.lines(Path.of(fn));
//    } catch (IOException ioe){
//      throw new RuntimeException(ioe);
//    }
//  }


  public static Optional<Stream<String>> getStreamFromFile(String fn) {
    try {
      return Optional.of(Files.lines(Path.of(fn)));
    } catch (IOException ioe){
      System.err.println("it broke: " + ioe.getMessage());
      return Optional.empty();
    }
  }

  public static void processError(Optional<?> opt) {
    if (opt.isEmpty()) {
      System.err.println("Uh oh, something broke");
    }
  }
  public static void main(String[] args) {
    Stream.of("a.txt", "b.txt", "c.txt")
        // convert filename to stream of data?

        // fails, throws checked exception
//        .flatMap(fn -> Files.lines(Path.of(fn)))

//        .flatMap(fn -> UseAStream.getStreamFromFile(fn))

        .map(fn -> UseAStream.getStreamFromFile(fn))

        .peek(opt -> UseAStream.processError(opt))
        .filter(opt -> opt.isPresent())
        .flatMap(opt -> opt.get())

        // process data?
        .forEach(System.out::println);
  }
}
