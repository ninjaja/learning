package task1;

import java.util.Objects;

/**
 * Demo of two ways of assigning value to the last (or the deepest) entry of a recursive variable:
 * in {@code while} cycle and recursion
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class FooTesting {

    public static void main(String[] args) {
        FooTesting fooTesting = new FooTesting();
        final Foo bar = new Foo() {{
            setData(new Object());
            setLink(new Foo() {{
                setData(new Object());
                setLink(new Foo() {{
                    setLink(new Foo() {{
                        setData(new Object());
                        setLink(new Foo() {{
                            setLink(new Foo() {{
                                setData(new Object());
                                setLink(new Foo() {{
                                    setLink(new Foo() {{
                                        setData(new Object());
                                        setLink(new Foo() {{
                                            setLink(new Foo() {{
                                                setData(new Object());
                                                setLink(new Foo() {{
                                                    setLink(new Foo() {{
                                                        setData(new Object());
                                                        setLink(new Foo() {{
                                                            setLink(new Foo() {{
                                                                setData(new Object());
                                                                setLink(new Foo() {{
                                                                    setLink(new Foo() {{
                                                                        setData(new Object());
                                                                        setLink(new Foo() {{
                                                                            setLink(new Foo() {{
                                                                                setData(new Object());
                                                                                setLink(new Foo() {{
                                                                                    setLink(new Foo() {{
                                                                                        setData(new Object());
                                                                                        setLink(new Foo() {{
                                                                                            setLink(new Foo() {{
                                                                                                setData(new Object());
                                                                                                setLink(new Foo() {{
                                                                                                    setLink(new Foo() {{
                                                                                                        setData(new Object());
                                                                                                        setLink(new Foo() {{
                                                                                                            setLink(new Foo() {{
                                                                                                                setData(new Object());
                                                                                                                setLink(new Foo() {{
                                                                                                                    setLink(new Foo() {{
                                                                                                                        setData(new Object());
                                                                                                                        setLink(new Foo() {{
                                                                                                                            setLink(new Foo() {{
                                                                                                                                setData(new Object());
                                                                                                                                setLink(new Foo() {{
                                                                                                                                    setData(new Object());
                                                                                                                                }});
                                                                                                                            }});
                                                                                                                        }});
                                                                                                                    }});
                                                                                                                }});
                                                                                                            }});
                                                                                                        }});
                                                                                                    }});
                                                                                                }});
                                                                                            }});
                                                                                        }});
                                                                                    }});
                                                                                }});
                                                                            }});
                                                                        }});
                                                                    }});
                                                                }});
                                                            }});
                                                        }});
                                                    }});
                                                }});
                                            }});
                                        }});
                                    }});
                                }});
                            }});
                        }});
                    }});
                }});
            }});
        }};

        final Foo appendMe = new Foo();
        fooTesting.append(bar);
        fooTesting.findLastByRecursion(bar).setLink(appendMe);
    }

    /**
     * Method to assign value to recursive variable
     * @param bar recursive variable
     */
    public void append(final Foo bar) {
        final Foo appendMe = new Foo();
        getLastElementOf(bar).setLink(appendMe);
    }

    private Foo findLastByRecursion(final Foo bar) {
        if (Objects.nonNull(bar.getLink())) {
            return findLastByRecursion(bar.getLink());
        }
        return bar;
    }

    private Foo getLastElementOf(final Foo initialElement) {
        Foo currentElement = initialElement;
        while (Objects.nonNull(currentElement.getLink())) {
            currentElement = currentElement.getLink();
        }
        return currentElement;
    }
}
