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
    public void setBun() {
        Burger burger = new Burger();

        burger.setBuns(bun);

        assertSame(bun, burger.bun);
    }

    @Test
    public void addIngredientAddsItem() {
        Burger burger = new Burger();

        burger.addIngredient(ingredient1);

        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void addIngredientSavesCorrectItem() {
        Burger burger = new Burger();
        burger.addIngredient(ingredient1);

        assertSame(ingredient1, burger.ingredients.get(0));
    }

    @Test
    public void removeIngredientRemovesItem() {
        Burger burger = new Burger();
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void removeIngredientShiftsList() {
        Burger burger = new Burger();
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);

        assertSame(ingredient2, burger.ingredients.get(0));
    }

    @Test
    public void moveIngredientChangesPosition() {
        Burger burger = new Burger();
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        burger.moveIngredient(0, 2);

        assertSame(ingredient1, burger.ingredients.get(2));
    }

    @Test
    public void moveIngredientKeepsOthers() {
        Burger burger = new Burger();
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);

        burger.moveIngredient(0, 2);

        assertSame(ingredient2, burger.ingredients.get(0));
    }

    @Test
    public void getPriceReturnsCorrectValue() {
        Burger burger = new Burger();

        when(bun.getPrice()).thenReturn(50.0f);
        burger.setBuns(bun);

        when(ingredient1.getPrice()).thenReturn(10.0f);
        when(ingredient2.getPrice()).thenReturn(15.0f);

        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        float price = burger.getPrice();

        assertEquals(125.0f, price, 0.0001f);
    }

    @Test
    public void getPriceCallsBun() {
        Burger burger = new Burger();

        when(bun.getPrice()).thenReturn(50.0f);
        burger.setBuns(bun);

        when(ingredient1.getPrice()).thenReturn(10.0f);
        burger.addIngredient(ingredient1);

        burger.getPrice();

        verify(bun, times(1)).getPrice();
    }

    @Test
    public void getPriceCallsIngredient() {
        Burger burger = new Burger();

        when(bun.getPrice()).thenReturn(50.0f);
        burger.setBuns(bun);

        when(ingredient1.getPrice()).thenReturn(10.0f);
        burger.addIngredient(ingredient1);

        burger.getPrice();

        verify(ingredient1, times(1)).getPrice();
    }

    private Burger makeBurgerForReceipt() {
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

        return burger;
    }

    @Test
    public void receiptContainsBun() {
        Burger burger = makeBurgerForReceipt();

        String receipt = burger.getReceipt();

        assertThat(receipt, containsString("(==== Black bun ====)"));
    }

    @Test
    public void receiptContainsSauce() {
        Burger burger = makeBurgerForReceipt();

        String receipt = burger.getReceipt();

        assertThat(receipt, containsString("= sauce Space sauce ="));
    }

    @Test
    public void receiptContainsFilling() {
        Burger burger = makeBurgerForReceipt();

        String receipt = burger.getReceipt();

        assertThat(receipt, containsString("= filling Meteor meat ="));
    }

    @Test
    public void receiptContainsPrice() {
        Burger burger = makeBurgerForReceipt();

        String receipt = burger.getReceipt();

        // 40*2 + 10 + 20 = 110. Не привязываемся к разделителю дробной части.
        assertThat(receipt, containsString("Price: 110"));
    }
}