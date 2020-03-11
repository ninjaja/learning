package task1;

/**
 * Bean holding a recursively declared variable of its own type
 */

public class Foo {
    Object data;
    Foo link;

    public Object getData() {
        return data;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public Foo getLink() {
        return link;
    }

    public void setLink(final Foo link) {
        this.link = link;
    }
}
