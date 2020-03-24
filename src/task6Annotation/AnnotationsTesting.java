package task6Annotation;

/**
 * Demo of applying and accessing a custom annotation
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class AnnotationsTesting {

    public static void main(String[] args) throws Exception {
        int times = AnnotationsTesting.class.getMethod("doSomething").getAnnotation(CustomAnnotation.class).value();
        for (int i = 0; i < times; i++) {
            doSomething();
        }
    }

    @CustomAnnotation(3)
    public static void doSomething() {
        System.out.println("toss a coin");
    }

}
