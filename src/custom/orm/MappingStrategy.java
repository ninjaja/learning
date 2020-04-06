package custom.orm;

/**
 * Enumeration to select default mapping strategy for fields without @Column annotation. Depends on properties bundle.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public enum MappingStrategy {

    LOWERCASE {
        @Override
        String defineCase(String s) {
            return s.toLowerCase();
        }
    },

    UPPERCASE {
        @Override
        String defineCase(String s) {
            return s.toUpperCase();
        }
    };

    abstract String defineCase(String s);
}
