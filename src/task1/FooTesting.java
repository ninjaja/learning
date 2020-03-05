package task1;

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
        if (bar != null) {
            final Foo appendMe = new Foo();
            getLastElementOf(bar).setLink(appendMe);
        } else {
            System.out.println("LOGGER: called append method on null");
        }
    }

    private Foo getLastElementOf(Foo initialElement) {
        int count = 0;
        Foo iteratedElement = initialElement;
        while (hasNextElement(iteratedElement)) {
            iteratedElement = iteratedElement.getLink();
            count++;
        }
        System.out.println("Number of nodes iterated: " + count);
        return iteratedElement;
    }

    private boolean hasNextElement(final Foo bar) {
        return bar.getLink() != null;
    }
}
