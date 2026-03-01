package praktikum;

import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(MockitoJUnitRunner.class)
public class BurgerTest {

    @Mock
    private Bun bun;

    @Mock
    private Ingredient ingredient1;

    @Mock
    private Ingredient ingredient2;

    @Mock
    private Ingredient ingredient3;

    @Test
    public void setBunsSetsBun() {
        Burger burger = new Burger();

        burger.setBuns(bun);

        assertSame(bun, burger.bun);
    }

    @Test
    public void addIngredientAddsIngredientToList() {
        Burger burger = new Burger();

        burger.addIngredient(ingredient1);

        assertEquals(1, burger.ingredients.size());
        assertSame(ingredient1, burger.ingredients.get(0));
    }

    @Test
    public void removeIngredientRemovesByIndex() {
        Burger burger = new Burger();
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
        assertSame(ingredient2, burger.ingredients.get(0));
    }

    @Test
    public void moveIngredientMovesIngredientToNewIndex() {
        Burger burger = new Burger();
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        burger.moveIngredient(0, 2);

        assertSame(ingredient2, burger.ingredients.get(0));
        assertSame(ingredient3, burger.ingredients.get(1));
        assertSame(ingredient1, burger.ingredients.get(2));
    }

    @Test
    public void getPriceReturnsBunPriceTimesTwoPlusIngredientsSum() {
        Burger burger = new Burger();

        when(bun.getPrice()).thenReturn(50.0f);
        burger.setBuns(bun);

        when(ingredient1.getPrice()).thenReturn(10.0f);
        when(ingredient2.getPrice()).thenReturn(15.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        float price = burger.getPrice();

        assertEquals(125.0f, price, 0.0001f);

        verify(bun, times(1)).getPrice();
        verify(ingredient1, times(1)).getPrice();
        verify(ingredient2, times(1)).getPrice();
    }

    @Test
    public void getReceiptContainsBunNameIngredientsAndPrice() {
        Burger burger = new Burger();

        when(bun.getName()).thenReturn("Black bun");
        when(bun.getPrice()).thenReturn(40.0f);
        burger.setBuns(bun);

        when(ingredient1.getType()).thenReturn(IngredientType.SAUCE);
        when(ingredient1.getName()).thenReturn("Space sauce");
        when(ingredient1.getPrice()).thenReturn(10.0f);

        when(ingredient2.getType()).thenReturn(IngredientType.FILLING);
        when(ingredient2.getName()).thenReturn("Meteor meat");
        when(ingredient2.getPrice()).thenReturn(20.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        String receipt = burger.getReceipt();

        assertThat(receipt, containsString("(==== Black bun ====)"));
        assertThat(receipt, containsString("= sauce Space sauce ="));
        assertThat(receipt, containsString("= filling Meteor meat ="));

        // Цена: 40*2 + 10 + 20 = 110 (в чеке формат float обычно с .000000)
        assertThat(receipt, containsString("Price: 110"));
    }
}