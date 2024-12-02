import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CardGameTest {

    private CardGame game;

    @BeforeEach
    public void setUp() {
        game = CardGame.getInstance();
    }

    @AfterEach
    public void tearDown() {
        game = null;
    }


    // TODO: SINGLETON TESTS GO HERE
    // WAIT UNTIL ALL OTHER TESTS ARE COMPLETED

    @Test
    
}