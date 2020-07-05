import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Example {

    public String getAgentName() {
        return "Bond";
    }

    public int meaningOfLife() {
        return 42;
    }

    @Test
    public void testAgent() {
        assertEquals(42, meaningOfLife());
        assertEquals("Smith", getAgentName());
    }
}
