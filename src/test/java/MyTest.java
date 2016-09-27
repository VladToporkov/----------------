import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vlad on 28.09.2016.
 */
public class MyTest {

    private WebDriver driver;

    // Урл начальной страницы
    private String url = "http://www.1gl.ru/";

    // Креды пользователя
    private String user = "noauthuser";
    private String password = "12345";
    private Integer timeout = 4000;

    // Позиция Москвы
    private String id = "2";


    // Инициализация драйвера перед тестом
    @Before
    public void openBrowser(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
    }

    // Закрытие драйвера после теста
    @After
    public void closeBrowser() {
        driver.quit();
    }


    // Тест
    @org.junit.Test
    public void Test() throws Exception {


        // 1) Зайти под пользователем noauthuser / 12345

        // Нажимаем на кнопку Входа
        clickOn(By.cssSelector("#user-enter a"));

        //Переключаемся на табу входа
        Thread.sleep(timeout);
        driver.findElement(By.xpath("//*[@id=\"wf-enter\"]")).click();

        // Вводим логин и пароль
        sendKeysTo("#email",user);
        sendKeysTo("#password",password);

        // Нажимаем на кнопку войти
        clickOn(By.cssSelector("#customer-enter"));

        // 2) Перейти в раздел правовая база (закрыть окошко автооткрытия)
        clickOn(By.cssSelector(".list_type_nav .list__item:nth-child(2) a"));
        Thread.sleep(timeout);
        clickOn(By.cssSelector("#search-extended-wrapper .ico_content_close"));

        // 3) Поиск по реквезитам

        // Нажимаем на поиск по реквезитам
        Thread.sleep(timeout);
        clickOn(By.xpath("//*[@id=\"search-button-extended\"]"));

        // 4) Выбрать http://i.imgur.com/cul7a21.png

        // Для усложнения задания и усилия вариативности теста сделаем так, чтобы выбираемый регион можно было выбрать по позиции. Позиция москвы 2 - заявлено в начале в параметрах
        Thread.sleep(timeout);
        clickOn(By.cssSelector("#search-extended-wrapper #regionlist li:nth-child(2)"));

        // 5) Ввести запрос в поискову строку - "Налог"

        // Вводим значение в поле

        sendKeysTo("#search-text", "Налог");

        // Отправление поиска

        clickOn(By.cssSelector("#search-extended-wrapper #button-search-extended"));

        // 6) Проверить заголовок h1
        Thread.sleep(timeout);
        checkTitle("h1", "Результаты поиска по реквизитам: «налог»");

        // 7) Проверить регион (Москва) .search-result div:nth-child(3)

        checkTitle(".search-result div:nth-child(3) span", "«Москва»");

        // 8) Проверить кол-во найденых документов

        checkTitle(".search-result div:nth-child(4)", "Найдено более 50 документов. Попробуйте уточнить запрос.");

        // 9) Проверить наличие блока с поисковой выдачей (результатов)

        assert (isElementPresent(By.cssSelector("#searchResultsSection")));

        // 9.а)  Проверить то что в блоке показано только 50 новостей
        List<WebElement> arcticles = driver.findElements(By.cssSelector("h3.widget-header"));
        assert (arcticles.size() == 50);

        // 10) Проверить наличие блока фильтров

        assert (isElementPresent(By.cssSelector(".menu_type_filter")));

    }


    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    private void checkTitle(String elemCss, String title){

        WebElement element = driver.findElement(By.cssSelector(elemCss));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        String text = element.getText();
        Assert.assertEquals(text, title);

    }

    // Функция для ввода данных в поля
    private void clickOn(By by) {

        WebElement element = driver.findElement(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();

    }

    // Функция для ввода данных в поля
    private void sendKeysTo(String elemCss, String text) {

        WebElement element = driver.findElement(By.cssSelector(elemCss));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.sendKeys(text);
    }


}