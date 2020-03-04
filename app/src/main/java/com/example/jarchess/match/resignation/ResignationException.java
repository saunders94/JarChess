package com.example.jarchess.match.resignation;

/**
 * A resignation exception is thrown when a waiting thread needs to stop waiting as a result of a resignation being detected.
 */
public class ResignationException extends Exception {

    /**
     * Creates a resignation exception
     *
     * @param resignationEvent the resignation event that triggered this exception
     */
    public ResignationException(ResignationEvent resignationEvent) {
        super(resignationEvent.getColorOfResigningParticipant() + " resigned");
    }
}
