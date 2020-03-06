package task1;

import java.util.Objects;

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
        fooTesting.append(bar);
    }

    public void append(final Foo bar) {
        final Foo appendMe = new Foo();
        getLastElementOf(bar).setLink(appendMe);
    }

    private Foo getLastElementOf(final Foo initialElement) {
        Foo currentElement = initialElement;
        while (Objects.nonNull(currentElement.getLink())) {
            currentElement = currentElement.getLink();
        }
        return currentElement;
    }
}
