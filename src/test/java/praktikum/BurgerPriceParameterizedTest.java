package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BurgerPriceParameterizedTest {

    private final float bunPrice;
    private final float[] ingredientPrices;
    private final float expectedPrice;

    public BurgerPriceParameterizedTest(float bunPrice, float[] ingredientPrices, float expectedPrice) {
        this.bunPrice = bunPrice;
        this.ingredientPrices = ingredientPrices;
        this.expectedPrice = expectedPrice;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{50.0f, new float[]{}, 100.0f},
                new Object[]{30.0f, new float[]{10.0f}, 70.0f},
                new Object[]{40.0f, new float[]{10.0f, 15.0f}, 105.0f}
        );
    }

    @Test
    public void getPriceWorksForDifferentIngredientSets() {
        Burger burger = new Burger();
        burger.setBuns(new Bun("Any bun", bunPrice));

        for (float p : ingredientPrices) {
            burger.addIngredient(new Ingredient(IngredientType.FILLING, "Any", p));
        }

        assertEquals(expectedPrice, burger.getPrice(), 0.0001f);
    }
}