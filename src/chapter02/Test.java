package chapter02;

public class Test {
  public Test() {
    System.out.println("111");
  }

  public void Func() {
    System.out.println("222");
  }

  public static void main(String[] args) {
    Test t = new Test();
    t.Func();
  }
}
