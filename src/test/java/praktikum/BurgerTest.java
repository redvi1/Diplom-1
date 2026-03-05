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
    private Ingredient sauceIngredient;

    @Mock
    private Ingredient fillingIngredient;

    @Mock
    private Ingredient extraIngredient;

    @Test
    public void setBun() {
        Burger burger = new Burger();

        burger.setBuns(bun);

        assertSame(bun, burger.bun);
    }

    @Test
    public void addIngredientAddsItem() {
        Burger burger = new Burger();

        burger.addIngredient(sauceIngredient);

        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void addIngredientSavesCorrectItem() {
        Burger burger = new Burger();
        burger.addIngredient(sauceIngredient);

        assertSame(sauceIngredient, burger.ingredients.get(0));
    }

    @Test
    public void removeIngredientRemovesItem() {
        Burger burger = new Burger();
        burger.addIngredient(sauceIngredient);
        burger.addIngredient(fillingIngredient);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void removeIngredientShiftsList() {
        Burger burger = new Burger();
        burger.addIngredient(sauceIngredient);
        burger.addIngredient(fillingIngredient);

        burger.removeIngredient(0);

        assertSame(fillingIngredient, burger.ingredients.get(0));
    }

    @Test
    public void moveIngredientChangesPosition() {
        Burger burger = new Burger();
        burger.addIngredient(sauceIngredient);
        burger.addIngredient(fillingIngredient);
        burger.addIngredient(extraIngredient);

        burger.moveIngredient(0, 2);

        assertSame(sauceIngredient, burger.ingredients.get(2));
    }

    @Test
    public void moveIngredientKeepsOthers() {
        Burger burger = new Burger();
        burger.addIngredient(sauceIngredient);
        burger.addIngredient(fillingIngredient);
        burger.addIngredient(extraIngredient);

        burger.moveIngredient(0, 2);

        assertSame(fillingIngredient, burger.ingredients.get(0));
    }

    @Test
    public void getPriceReturnsCorrectValue() {
        Burger burger = new Burger();

        when(bun.getPrice()).thenReturn(50.0f);
        burger.setBuns(bun);

        when(sauceIngredient.getPrice()).thenReturn(10.0f);
        when(fillingIngredient.getPrice()).thenReturn(15.0f);

        burger.addIngredient(sauceIngredient);
        burger.addIngredient(fillingIngredient);

        float price = burger.getPrice();

        assertEquals(125.0f, price, 0.0001f);
    }

    @Test
    public void getPriceCallsBun() {
        Burger burger = new Burger();

        when(bun.getPrice()).thenReturn(50.0f);
        burger.setBuns(bun);

        when(sauceIngredient.getPrice()).thenReturn(10.0f);
        burger.addIngredient(sauceIngredient);

        burger.getPrice();

        verify(bun, times(1)).getPrice();
    }

    @Test
    public void getPriceCallsIngredient() {
        Burger burger = new Burger();

        when(bun.getPrice()).thenReturn(50.0f);
        burger.setBuns(bun);

        when(sauceIngredient.getPrice()).thenReturn(10.0f);
        burger.addIngredient(sauceIngredient);

        burger.getPrice();

        verify(sauceIngredient, times(1)).getPrice();
    }

    private Burger makeBurgerForReceipt() {
        Burger burger = new Burger();

        when(bun.getName()).thenReturn("Black bun");
        when(bun.getPrice()).thenReturn(40.0f);
        burger.setBuns(bun);

        when(sauceIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(sauceIngredient.getName()).thenReturn("Space sauce");
        when(sauceIngredient.getPrice()).thenReturn(10.0f);

        when(fillingIngredient.getType()).thenReturn(IngredientType.FILLING);
        when(fillingIngredient.getName()).thenReturn("Meteor meat");
        when(fillingIngredient.getPrice()).thenReturn(20.0f);

        burger.addIngredient(sauceIngredient);
        burger.addIngredient(fillingIngredient);

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

        String expectedReceipt =
                "(==== Black bun ====)\r\n" +
                        "= sauce Space sauce =\r\n" +
                        "= filling Meteor meat =\r\n" +
                        "(==== Black bun ====)\r\n" +
                        "\r\n" +
                        "Price: 110,000000\r\n";

        assertEquals(expectedReceipt, receipt);
    }

    @Test
    public void receiptContainsPrice() {
        Burger burger = makeBurgerForReceipt();

        String receipt = burger.getReceipt();

        // 40*2 + 10 + 20 = 110. Не привязываемся к разделителю дробной части, тк ругался из-за ошибки unmappable character (0x98) for encoding windows-1251
        assertThat(receipt, containsString("Price: 110"));
    }
}