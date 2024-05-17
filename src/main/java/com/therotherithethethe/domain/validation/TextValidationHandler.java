package com.therotherithethethe.domain.validation;

/**
 * Abstract base class for text validation handlers in a chain of responsibility pattern.
 */
public abstract class TextValidationHandler {

    private TextValidationHandler next;

    /**
     * Links a chain of text validation handlers.
     *
     * @param first the first handler in the chain
     * @param chain the subsequent handlers in the chain
     * @return the first handler in the chain
     */
    public static TextValidationHandler link(
        TextValidationHandler first, TextValidationHandler... chain) {
        TextValidationHandler head = first;
        for (TextValidationHandler nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    /**
     * Checks if the text passes the validation.
     *
     * @param text the text to check
     * @return true if the text passes the validation, false otherwise
     */
    public abstract boolean check(String text);

    /**
     * Checks the next handler in the chain.
     *
     * @param text the text to check
     * @return true if the next handler passes the validation, false otherwise
     */
    protected boolean checkNext(String text) {
        if (next == null) {
            return true;
        }
        return next.check(text);
    }
}
