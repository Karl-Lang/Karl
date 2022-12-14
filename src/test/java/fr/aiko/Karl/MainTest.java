package fr.aiko.Karl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    @Test
    @DisplayName("Show function")
    void testMain() {
        String[] args = {"src/test/resources/tests/Show.karl"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Main.main(args);
        assertEquals("Hello World!", outContent.toString().split("\n")[0]);
    }

    @Test
    @DisplayName("If statements")
    void testIf() {
        String[] args = {"src/test/resources/tests/If.karl"};
        String[] expected = {"Test #1 success", "Test #2 success", "Test #3 success", "Test #4 success", "Test #5 success", "Test #6 success", "Test #7 success", "Test #8 success", "Test #9 success", "Test #10 success", "Test réussit !! ^U^", "true", "Yay!", "true", "Yay!2", "true", "Yay!3", "Yay!4"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Main.main(args);
        String[] output = Arrays.copyOf(outContent.toString().split("\n"), outContent.toString().split("\n").length - 1);
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Functions")
    void testFunctions() {
        String[] args = {"src/test/resources/tests/Functions.karl"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String[] expected = {"What is your name?", "Hello, Karl!", "Hello, World!", "11", "8", "1"};
        System.setOut(new PrintStream(outContent));
        Main.main(args);
        String[] output = Arrays.copyOf(outContent.toString().split("\n"), outContent.toString().split("\n").length - 1);
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Else statements")
    void testElse() {
        String[] args = {"src/test/resources/tests/Else.karl"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String[] expected = {"Test #1 success", "Test #2 success", "Test #3 success", "Test #4 success"};
        System.setOut(new PrintStream(outContent));
        Main.main(args);
        String[] output = Arrays.copyOf(outContent.toString().split("\n"), outContent.toString().split("\n").length - 1);
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Mathematical operations")
    void mathTest() {
        String[] args = {"src/test/resources/tests/Math.karl"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String[] expected = {"17", "14", "22", "14", "36", "36", "36", "7.3333335", "6", "7.3333335"};
        System.setOut(new PrintStream(outContent));
        Main.main(args);
        String[] output = Arrays.copyOf(outContent.toString().split("\n"), outContent.toString().split("\n").length - 1);
        assertArrayEquals(expected, output);
    }
}