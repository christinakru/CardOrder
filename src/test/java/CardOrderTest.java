import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.*;

public class CardOrderTest {
    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999");
    }

    @Test
    void sendCorrectForm() {
        $("[data-test-id=name] input").setValue("Иван Иванов");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $("[type=button]").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    @Test
    void sendIncorrectNameWithDigit() {
        $("[data-test-id=name] input").setValue("Иван Иванов 1");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $("[type=button]").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void sendIncorrectNameWithEng() {
        $("[data-test-id=name] input").setValue("Ivan Ivanov");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $("[type=button]").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void sendIncorrectPhoneWithoutPlus() {
        $("[data-test-id=name] input").setValue("Иван Иванов");
        $("[data-test-id=phone] input").setValue("89012345678");
        $("[data-test-id=agreement]").click();
        $("[type=button]").click();
        $$("[data-test-id=phone].input_invalid .input__sub").last().shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void sendIncorrectPhoneLessDigit() {
        $("[data-test-id=name] input").setValue("Иван Иванов");
        $("[data-test-id=phone] input").setValue("+7901234567");
        $("[data-test-id=agreement]").click();
        $("[type=button]").click();
        $$("[data-test-id=phone].input_invalid .input__sub").last().shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void sendIncorrectPhoneMoreDigit() {
        $("[data-test-id=name] input").setValue("Иван Иванов");
        $("[data-test-id=phone] input").setValue("+790123456787");
        $("[data-test-id=agreement]").click();
        $("[type=button]").click();
        $$("[data-test-id=phone].input_invalid .input__sub").last().shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void sendWithoutAgreement() {
        $("[data-test-id=name] input").setValue("Иван Иванов");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[type=button]").click();
        $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй"));
    }

    @Test
    void sendEmpty() {
        $("[data-test-id=agreement]").click();
        $("[type='button']").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }
}
