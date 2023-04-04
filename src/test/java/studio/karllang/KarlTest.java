package studio.karllang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import studio.karllang.karl.Karl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KarlTest {
    @Test
    @DisplayName("Show function")
    void testMain() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, false, StandardCharsets.UTF_8));
        new Karl().run("src/test/resources/tests/Show.karl", null);
        assertEquals("Hello World!", outContent.toString().trim());
    }

    @Test
    @DisplayName("If statements")
    void testIf() {
        String[] expected = {"Test #1 success", "Test #2 success", "Test #3 success", "Test #4 success", "Test #5 success", "Test #6 success", "Test #7 success", "Test #8 success", "Test #9 success", "Test #10 success", "Test réussit !! ^U^", "true", "Yay!", "true", "Yay!2", "true", "Yay!3", "Yay!4"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, false, StandardCharsets.UTF_8));
        new Karl().run("src/test/resources/tests/If.karl", null);
        String[] output = outContent.toString().trim().strip().replaceAll("\r\n", "\n").split("\n");
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Functions")
    void testFunctions() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String[] expected = {"What is your name?", "Hello, Karl!", "Hello, World!", "11", "8", "1"};
        System.setOut(new PrintStream(outContent, false, StandardCharsets.UTF_8));
        new Karl().run("src/test/resources/tests/Functions.karl", null);
        String[] output = outContent.toString().trim().strip().replaceAll("\r\n", "\n").split("\n");
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Else statements")
    void testElse() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, false, StandardCharsets.UTF_8));
        String[] expected = {"Test #1 success", "Test #2 success", "Test #3 success", "Test #4 success"};
        new Karl().run("src/test/resources/tests/Else.karl", null);
        String[] output = outContent.toString().trim().strip().replaceAll("\r\n", "\n").split("\n");
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Mathematical operations")
    void mathTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String[] expected = {"17", "14", "22", "14", "36", "36", "36", "7.3333335", "6", "7.3333335"};
        System.setOut(new PrintStream(outContent, false, StandardCharsets.UTF_8));
        new Karl().run("src/test/resources/tests/Math.karl", null);
        String[] output = outContent.toString().trim().strip().replaceAll("\r\n", "\n").split("\n");
        assertArrayEquals(expected, output);
    }
}