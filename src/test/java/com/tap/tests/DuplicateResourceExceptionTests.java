package com.tap.tests;

import com.tap.exceptions.DuplicateResourceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DuplicateResourceExceptionTests {
    @Test
    void testExceptionMessage() {
        // Arrange
        String message = "Resource already exists";

        // Act
        DuplicateResourceException exception = new DuplicateResourceException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        // Act
        DuplicateResourceException exception = new DuplicateResourceException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

}
