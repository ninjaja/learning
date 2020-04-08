package custom.orm;

/**
 * Enumeration to select default mapping strategy (in lower- or uppercase) for fields without @Column annotation.
 * Initially is set in properties bundle.
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
