package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;

public class ViewQuestionsNumbersKeyboardCreator {

    //получаем

    // получить остаток от деления на 5 от questionMap.lastOf()  и передать его в createButtonArray()
    //получить результат от деления JHW(size - lo/5) / 5
    // если JHW== 0, || ( JHW==1 &&(lo%5==0) то кнопка вперёд не нужна
    // если (JHW==1 &&(lo%5!=0) то нужна кнопка далее
    // если (JHW>1 &&(lo%5!=0) то нужны обе кнопки
    //доп проверка на последнюю клаву - как понять что больше не будет апдэйтов  - счётчик?
    //(JHW>1 &&(lo%5 ==0)



    private InlineKeyboardButton[] createButtonArrow(int count) {
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[count];
        for (int i = 0; i < count; i++) {
            buttons[i] = new InlineKeyboardButton(String.valueOf(i + 1));
        }
        return buttons;
    }

    public void createButtons(int count) {
        InlineKeyboardButton[] buttons = createButtonArrow(count);
    }


}
