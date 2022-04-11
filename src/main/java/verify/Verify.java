package verify;

sealed interface X permits Verify {}
public final class Verify implements X {
  public static void main(String[] args) {
    System.out.println("Hello Java 17 World!");
  }
}
