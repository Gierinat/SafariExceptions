package usingeither;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

interface ExFunction<A, R> {
  R apply(A a) throws Throwable;

  static <A, R> Function<A, Either<R>> wrap(ExFunction<A, R> op) {
    return a -> {
      try {
        return Either.success(op.apply(a));
      } catch (Throwable t) {
        // generics have no meaning at runtime,
        // so we must catch Throwable...
        return Either.failure(t);
      }
    };
  }
}

// Look in libraries like VAVR for tools like Either
//class Either<L, R>
class Either<R> {
  public R right;
  public Throwable left;

  private Either(R right, Throwable left) {
    this.right = right;
    this.left = left;
  }

  public static <R> Either<R> success(R success) {
    return new Either(success, null);
  }

  public static <R> Either<R> failure(Throwable t) {
    return new Either(null, t);
  }

  public boolean isSuccess() {
    return left == null;
  }

  public boolean isFailure() {
    return left != null;
  }

  public Throwable getLeft() {
    if (isSuccess()) throw new IllegalStateException("getLeft on a success");
    return left;
  }

  public R getSuccess() {
    if (isFailure()) throw new IllegalStateException("getSuccess on a failure");
    return right;
  }

  public Either<R> map(Function<R, Either<R>> op) {
    if (isSuccess()) return op.apply(right);
    return this;
  }

  public Either<R> recover(Function<Throwable, Either<R>> op) {
    if (isFailure()) return op.apply(left);
    return this;
  }
}

public class UseEither {
  public static Either<Stream<String>> recovery(Throwable t) {
    ExFunction<String, Stream<String>> getter =
        fn -> Files.lines(Path.of(fn));
    return ExFunction.wrap(getter).apply("d.txt");
  }

  public static void processError(Either<?> e) {
    if (e.isFailure()) {
      System.err.println("Uh oh: " + e.getLeft());
    }
  }
  public static void main(String[] args) {
    Stream.of("a.txt", "b.txt", "c.txt")

        .map(ExFunction.wrap(fn -> Files.lines(Path.of(fn))))
        .peek(e -> UseEither.processError(e))

        .map(e -> e.recover(f -> UseEither.recovery(f)))

        .filter(e -> e.isSuccess())
        .flatMap(e -> e.getSuccess())

        .forEach(System.out::println);
  }
}
